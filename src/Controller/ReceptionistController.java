/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.poi.xwpf.usermodel.*;

import Model.AppointmentData;
import Model.PatientData;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.util.Units;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.awt.Checkbox;
import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import com.microsoft.schemas.vml.CTTextbox;

import Alert.AlertMessage;
import Controller.DoctorMainFormController.DashBoardAppointmentData;
import Controller.DoctorMainFormController.DoctorAppointmentData;
import Controller.DoctorMainFormController.FormatterUtils;
import DAO.Database;
import Enum.AppointmentStatus;
import Enum.Gender;
import Enum.UrgencyLevel;
import Model.AppointmentData;
import Model.AppointmentSuggester;
import Model.Data;
import Model.DoctorData;
import Model.PatientData;
import Model.ReceptionistData;
import Model.RevenueService;
import Model.ServiceData;
import Model.ReceptionistData;
import Model.DrugData;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author WINDOWS 10
 */
public class ReceptionistController implements Initializable {

	/* =====================CRUD DRUG======================================== */

	@FXML
	private TableView<DrugData> drug_tableView;
	@FXML
	private TableColumn<DrugData, String> drug_col_id;
	@FXML
	private TableColumn<DrugData, String> drug_col_name;
	@FXML
	private TableColumn<DrugData, String> drug_col_manufacturer;
	@FXML
	private TableColumn<DrugData, String> drug_col_expiry;
	@FXML
	private TableColumn<DrugData, String> drug_col_unit;
	@FXML
	private TableColumn<DrugData, String> drug_col_price;
	@FXML
	private TableColumn<DrugData, String> drug_col_stock;
	@FXML
	private TableColumn<DrugData, String> drug_col_create;
	@FXML
	private TableColumn<DrugData, String> drug_col_update;
	@FXML
	private TableColumn<DrugData, Void> drug_col_action;

	@FXML
	private TextField txtDrugSearch;
	@FXML
	private ComboBox<String> cmbDrugSearchBy;
	@FXML
	private ComboBox<String> cmbDrugExpiryFilter;
	@FXML
	private ComboBox<String> cmbDrugStockFilter;
	@FXML
	private ComboBox<String> cmbDrugPriceSort;

	/* =====================CRUD PATIENT======================================== */

	@FXML
	private TableView<PatientData> patients_tableView;
	@FXML
	private TableColumn<PatientData, String> patients_col_patientID;
	@FXML
	private TableColumn<PatientData, String> patients_col_name;
	@FXML
	private TableColumn<PatientData, String> patients_col_email;
	@FXML
	private TableColumn<PatientData, String> patients_col_gender;
	@FXML
	private TableColumn<PatientData, String> patients_col_phone;
	@FXML
	private TableColumn<PatientData, String> patients_col_address;
	@FXML
	private TableColumn<PatientData, String> patients_col_diagnosis;
	@FXML
	private TableColumn<PatientData, BigDecimal> patients_col_height;
	@FXML
	private TableColumn<PatientData, BigDecimal> patients_col_weight;
	@FXML
	private TableColumn<PatientData, Timestamp> patients_col_create;
	@FXML
	private TableColumn<PatientData, Timestamp> patients_col_update;
	@FXML
	private TableColumn<PatientData, Void> patients_col_action;

	@FXML
	private ComboBox<String> cmbPatientSearchBy;
	@FXML
	private TextField txtPatientSearch;
	@FXML
	private ComboBox<String> cmbPatientGenderFilter;

	@FXML
	private AnchorPane main_form;

	@FXML
	private Circle top_profile;

	@FXML
	private Label top_username;

	@FXML
	private Label date_time;

	@FXML
	private Label current_form;

	@FXML
	private Button logout_btn;

	@FXML
	private Button dashboard_btn;

	@FXML
	private Button patients_btn;

	@FXML
	private Button drugs_btn;

	@FXML
	private Button appointments_btn;
	@FXML
	private Button appointments_manage_btn;
	@FXML
	private Button profile_btn;

	@FXML
	private AnchorPane home_form;

	@FXML
	private Circle home_doctor_circle;

	@FXML
	private Label home_doctor_name;

	@FXML
	private Label home_doctor_specialization;

	@FXML
	private Label home_doctor_email;

	@FXML
	private Label home_doctor_mobileNumber;

	@FXML
	private TableView<AppointmentData> home_appointment_tableView;

	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_appointmenID;

	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_description;

	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_diagnosis;

	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_treatment;

	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_doctor;

	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_schedule;

	@FXML
	private AnchorPane patients_form;

	@FXML
	private ScrollPane doctors_scrollPane;

	@FXML
	private GridPane doctors_gridPane;

	@FXML
	private AnchorPane appointments_form;
	@FXML
	private AnchorPane choose_service_form;
	@FXML
	private VBox vboxContainer;
	@FXML
	private TextArea appointment_d_description;

	@FXML
	private ComboBox<DoctorData> cb_doctor;
	@FXML
	private ComboBox<ServiceData> cb_service;
	@FXML
	private ComboBox<PatientData> cb_patient;
	@FXML
	private ComboBox<UrgencyLevel> cb_urgency;
	@FXML
	private DatePicker select_time;
	@FXML
	private CheckBox checkbox_followUp;
	@FXML
	private Label lb_patient_name;
	@FXML
	private Label lb_patient_gender;
	@FXML
	private Label lb_patient_age;
	@FXML
	private Label lb_patient_address;
	@FXML
	private Label lb_price_service, lb_check;
	@FXML
	private TextArea txt_suggest;
	@FXML
	private TextArea txt_details;
	@FXML
	private Pane btn_pane;
	@FXML
	private Button btn_remove;
	@FXML
	private Button btn_add;
	@FXML
	private Button btn_create;
	@FXML
	private Button bnt_suggest;
	@FXML
	private Button btn_check;

	@SuppressWarnings("rawtypes")
	@FXML
	private Spinner<Integer> spHour, spMinute, spHour1, spMinute1;
	@FXML
	private Button btn_clear_suggest;

	// appointment manage
	@FXML
	private AnchorPane appointments_manage_form;
	@FXML
	private TableView<AppointmentData> appointments_tableView;
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_service;
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_time;
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_status;
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_name, appointments_col_doctor;
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_contactNumber, appointments_col_prescription;
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_reason;
	ObservableList<AppointmentData> appoinmentListData = FXCollections.observableArrayList();

	@FXML
	private TextField appointment_serviceName;
	@FXML
	private TextField appointment_mobileNumber;
	@FXML
	private ComboBox<PatientData> appointment_patient;
	@FXML
	private ComboBox<DoctorData> appointment_doctor;
	@FXML
	private ComboBox<String> appointment_status;
	@FXML
	private TextArea appointment_cancelReason;
	@FXML
	private DatePicker appointment_date;
	@FXML
	private TextField appointment_time;
	@FXML
	private Label appointment_createdDate;
	@FXML
	private Label appointment_updatedDate;

	@FXML
	private Button appointment_insertBtn;

	@FXML
	private Button appointment_updateBtn;

	@FXML
	private Button appointment_clearBtn;

	@FXML
	private Button appointment_deleteBtn;

	@FXML
	private AnchorPane drugs_form;

	@FXML
	private AnchorPane profile_form;

	@FXML
	private Circle profile_circle;

	@FXML
	private Button profile_importBtn;

	// Left panel
	@FXML
	private Label name_receptDB, username_receptDB;

	@FXML
	private Label name_recept, username_recept, email_recept, phone_recept, gender_recept, createdDate_recept;

	@FXML
	private TextField txt_name_recept, txt_username_recept, txt_email_recept, txt_phone_recept;

	@FXML
	private ComboBox<String> gender_cb;

	@FXML
	private Button profile_updateBtn;

	@FXML
	private TextArea txt_address_recept;

	private AlertMessage alert = new AlertMessage();

	private Image image;

	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	private Statement statement;

	// load data receptionist
	private String username;

	public void setUsername(String username) {
		this.username = username;
		loadReceptionistProfile();
		profileDisplayImages();
	}

	// =======================CRUD Drug==================================
	private ObservableList<DrugData> drugMasterList = FXCollections.observableArrayList();

	private void initializeDrugFilters() {
		cmbDrugSearchBy.setItems(FXCollections.observableArrayList("Name", "Manufacturer", "Unit"));
		cmbDrugSearchBy.setValue("Name");

		cmbDrugExpiryFilter.setItems(FXCollections.observableArrayList("All", "Valid", "Expired"));
		cmbDrugExpiryFilter.setValue("All");

		cmbDrugStockFilter.setItems(FXCollections.observableArrayList("All", "In Stock", "Out of Stock"));
		cmbDrugStockFilter.setValue("All");

		cmbDrugPriceSort.setItems(FXCollections.observableArrayList("None", "Low to High", "High to Low"));
		cmbDrugPriceSort.setValue("None");

		txtDrugSearch.clear();
		txtDrugSearch.setPromptText("Enter keyword to search");

		// Gắn listener để tự động lọc khi người dùng thay đổi
		txtDrugSearch.textProperty().addListener((obs, oldVal, newVal) -> applyAdvancedDrugFilter());
		cmbDrugSearchBy.valueProperty().addListener((obs, o, n) -> applyAdvancedDrugFilter());
		cmbDrugExpiryFilter.valueProperty().addListener((obs, o, n) -> applyAdvancedDrugFilter());
		cmbDrugStockFilter.valueProperty().addListener((obs, o, n) -> applyAdvancedDrugFilter());
		cmbDrugPriceSort.valueProperty().addListener((obs, o, n) -> applyAdvancedDrugFilter());
	}

	private void loadDrugTable() {
		drugMasterList.clear();
		try {
			Connection conn = Database.connectDB();
			String sql = "SELECT Id, Name, Manufacturer, Expiry_date, Unit, Price, Stock, Create_date, Update_date "
					+ "FROM DRUG ";

			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				drugMasterList.add(new DrugData(rs.getString("Id"), rs.getString("Name"), rs.getString("Manufacturer"),
						rs.getString("Unit"), rs.getBigDecimal("Price"), rs.getInt("Stock"),
						rs.getDate("Expiry_date").toLocalDate(), rs.getTimestamp("Create_date"),
						rs.getTimestamp("Update_date")));
			}

			// Cột dữ liệu
			drug_col_id.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDrugId()));
			drug_col_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
			drug_col_manufacturer
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getManufacturer()));
			drug_col_expiry
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getExpiryDate().toString()));
			drug_col_unit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnit()));
			drug_col_price.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrice().toString()));
			drug_col_stock
					.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStock())));
			drug_col_create
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreateDate().toString()));
			drug_col_update
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUpdateDate().toString()));

			// Cột hành động
			drug_col_action.setCellFactory(col -> new TableCell<>() {
				private final Button editBtn = new Button("Update");
				private final Button deleteBtn = new Button("Delete");
				private final HBox hbox = new HBox(5, editBtn, deleteBtn);
				{
					editBtn.setOnAction(e -> {
						DrugData drug = getTableView().getItems().get(getIndex());
						openEditDrugForm(drug);
					});

					deleteBtn.setOnAction(e -> {
						DrugData drug = getTableView().getItems().get(getIndex());
						deleteDrug(drug.getDrugId());
					});
				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					setGraphic(empty ? null : hbox);
				}
			});

			drug_tableView.setItems(drugMasterList);
			initializeDrugFilters();

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Error loading drug list!");
			alert.showAndWait();
		}
	}

	private void applyAdvancedDrugFilter() {
		String keyword = txtDrugSearch.getText().toLowerCase();
		String searchBy = cmbDrugSearchBy.getValue();
		String expiryFilter = cmbDrugExpiryFilter.getValue();
		String stockFilter = cmbDrugStockFilter.getValue();
		String priceSort = cmbDrugPriceSort.getValue();

		ObservableList<DrugData> filtered = FXCollections.observableArrayList();

		for (DrugData drug : drugMasterList) {
			boolean matchesKeyword = true;

			switch (searchBy) {
			case "Name":
				matchesKeyword = drug.getName().toLowerCase().contains(keyword);
				break;
			case "Manufacturer":
				matchesKeyword = drug.getManufacturer().toLowerCase().contains(keyword);
				break;
			case "Unit":
				matchesKeyword = drug.getUnit().toLowerCase().contains(keyword);
				break;
			}

			boolean matchesExpiry = expiryFilter.equals("All")
					|| (expiryFilter.equals("Valid") && drug.getExpiryDate().isAfter(LocalDate.now()))
					|| (expiryFilter.equals("Expired") && !drug.getExpiryDate().isAfter(LocalDate.now()));

			boolean matchesStock = stockFilter.equals("All") || (stockFilter.equals("In Stock") && drug.getStock() > 0)
					|| (stockFilter.equals("Out of Stock") && drug.getStock() <= 0);

			if (matchesKeyword && matchesExpiry && matchesStock) {
				filtered.add(drug);
			}
		}

		// Sort theo giá
		if (priceSort.equals("Low to High")) {
			FXCollections.sort(filtered, Comparator.comparing(DrugData::getPrice));
		} else if (priceSort.equals("High to Low")) {
			FXCollections.sort(filtered, Comparator.comparing(DrugData::getPrice).reversed());
		}

		drug_tableView.setItems(filtered);
	}

	private void deleteDrug(String drugId) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText("Are you sure you want to delete this drug?");
		alert.setContentText("This action cannot be undone.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				Connection conn = Database.connectDB();

				String sql = "DELETE FROM DRUG WHERE Id = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, drugId);
				ps.executeUpdate();

				conn.close();
				loadDrugTable(); // Refresh lại bảng sau khi xoá
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void openEditDrugForm(DrugData drug) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditDrugForm.fxml"));
			Parent root = loader.load();

			EditDrugFormController controller = loader.getController();
			controller.setDrugData(drug); // Truyền dữ liệu sang form chỉnh sửa

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Update Drug");
			stage.setScene(new Scene(root));

			stage.setOnHidden(e -> loadDrugTable()); // Tải lại bảng khi form đóng
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("ERROR : " + e.getMessage());
		}
	}

	@FXML
	private void openAddDrugForm() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddDrugForm.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Add New Drug");
			stage.setScene(new Scene(root));

			stage.setOnHidden(e -> loadDrugTable()); // Refresh lại bảng khi thêm thuốc
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// =======================CRUD Patient==================================
	private ObservableList<PatientData> patientMasterList = FXCollections.observableArrayList();

	private void initializePatientFilters() {
		cmbPatientSearchBy.setItems(FXCollections.observableArrayList("Name", "Email", "Phone", "Address", "Diagnosis",
				"Height", "Weight"));
		cmbPatientSearchBy.setValue("Name");

		txtPatientSearch.clear();

		cmbPatientGenderFilter.setItems(FXCollections.observableArrayList("All", "Male", "Female", "Other"));
		cmbPatientGenderFilter.setValue("All");

		// Gắn listener
		txtPatientSearch.textProperty().addListener((obs, oldVal, newVal) -> applyPatientFilters());
		cmbPatientSearchBy.valueProperty().addListener((obs, o, n) -> applyPatientFilters());
		cmbPatientGenderFilter.valueProperty().addListener((obs, o, n) -> applyPatientFilters());
	}

	private void applyPatientFilters() {
		String keyword = txtPatientSearch.getText().toLowerCase();
		String searchBy = cmbPatientSearchBy.getValue();
		String selectedGender = cmbPatientGenderFilter.getValue();

		ObservableList<PatientData> filtered = FXCollections.observableArrayList();

		for (PatientData p : patientMasterList) {
			// 1. Tìm kiếm theo trường
			String fieldValue = switch (searchBy) {
			case "Name" -> p.getName();
			case "Email" -> p.getEmail();
			case "Phone" -> p.getPhone();
			case "Address" -> p.getAddress();
			case "Diagnosis" -> p.getDiagnosis();
			case "Height" -> p.getHeight().toPlainString();
			case "Weight" -> p.getWeight().toPlainString();
			default -> "";
			};

			boolean matchesKeyword = fieldValue != null && fieldValue.toLowerCase().contains(keyword);
			boolean matchesGender = selectedGender.equals("All") || p.getGender().equalsIgnoreCase(selectedGender);

			if (matchesKeyword && matchesGender) {
				filtered.add(p);
			}
		}

		patients_tableView.setItems(filtered);
	}

	public void loadPatientTable() {
		patientMasterList.clear();

		String sql = "SELECT Patient_id, Name, Email, Gender, Phone, Address, Diagnosis, Height, Weight, Create_date, Update_date FROM PATIENT";

		try {
			Connection conn = Database.connectDB();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PatientData patient = new PatientData(rs.getString("Patient_Id"), rs.getString("Name"),
						rs.getString("Email"), rs.getString("Gender"), rs.getString("Phone"), rs.getString("Address"),
						rs.getString("Diagnosis"), rs.getBigDecimal("Height"), rs.getBigDecimal("Weight"),
						rs.getTimestamp("Create_date"), rs.getTimestamp("Update_date"));
				patientMasterList.add(patient);
			}

			// Gán dữ liệu cho TableView
			patients_col_patientID.setCellValueFactory(new PropertyValueFactory<>("patientId"));
			patients_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
			patients_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
			patients_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
			patients_col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
			patients_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
			patients_col_diagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
			patients_col_height.setCellValueFactory(new PropertyValueFactory<>("height"));
			patients_col_weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
			patients_col_create.setCellValueFactory(new PropertyValueFactory<>("createDate"));
			patients_col_update.setCellValueFactory(new PropertyValueFactory<>("updateDate"));

			// Cột hành động
			patients_col_action.setCellFactory(col -> new TableCell<>() {
				private final Button editBtn = new Button("Update");
				private final Button deleteBtn = new Button("Delete");
				private final Button detailBtn = new Button("Detail");
				private final HBox hbox = new HBox(5, editBtn, deleteBtn, detailBtn);
				{
					editBtn.setOnAction(e -> {
						PatientData patient = getTableView().getItems().get(getIndex());
						openEditPatientForm(patient);
					});

					deleteBtn.setOnAction(e -> {
						PatientData patient = getTableView().getItems().get(getIndex());
						deletePatient(patient.getPatientId());
					});
					detailBtn.setOnAction(e -> {
						PatientData patient = getTableView().getItems().get(getIndex());
						handleViewPatientDetail(patient.getPatientId());
					});
				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					setGraphic(empty ? null : hbox);
				}
			});

			patients_tableView.setItems(patientMasterList);
			conn.close();

//	        // Thêm nút cập nhật và xóa vào mỗi dòng
//	        patients_col_action.setCellFactory(col -> new TableCell<>() {
//	            private final Button btnEdit = new Button("Edit");
//	            private final Button btnDelete = new Button("Delete");
//	            private final HBox hbox = new HBox(5, btnEdit, btnDelete);
//
//	            {
//	                btnEdit.getStyleClass().add("btn-2");
//	                btnDelete.getStyleClass().add("btn-danger");
//
//	                btnEdit.setOnAction(e -> {
//	                    PatientData patient = getTableView().getItems().get(getIndex());
//	                    //openEditPatientForm(patient); // Mở form sửa
//	                });
//
//	                btnDelete.setOnAction(e -> {
//	                    PatientData patient = getTableView().getItems().get(getIndex());
//	                    //deletePatient(patient); // Gọi hàm xóa
//	                });
//	            }
//
//	            @Override
//	            protected void updateItem(Void item, boolean empty) {
//	                super.updateItem(item, empty);
//	                if (empty) {
//	                    setGraphic(null);
//	                } else {
//	                    setGraphic(hbox);
//	                }
//	            }
//	        });

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Error loading patient list!");
			alert.showAndWait();
		}
	}

	private void handleViewPatientDetail(String patientId) {
		try (Connection conn = Database.connectDB()) {
			String sql = "SELECT * FROM PATIENT WHERE Patient_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, patientId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				PatientData patient = new PatientData(rs.getString("Patient_id"), rs.getString("Name"),
						rs.getString("Email"), rs.getString("Gender"), rs.getString("Phone"), rs.getString("Address"),
						rs.getString("Diagnosis"), rs.getBigDecimal("Height"), rs.getBigDecimal("Weight"),
						rs.getTimestamp("Create_date"), rs.getTimestamp("Update_date"));

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PatientDetailView.fxml"));
				Parent root = loader.load();

				PatientDetailController controller = loader.getController();
				controller.setPatientData(patient); // truyền dữ liệu vào controller

				Stage stage = new Stage();
				stage.setTitle("Patient Detail");
				stage.setScene(new Scene(root));
				stage.show();
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Patient not found.");
				alert.showAndWait();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading patient detail: " + e.getMessage());
			alert.showAndWait();
		}
	}

	private void deletePatient(String patientId) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText("Are you sure you want to delete this patient?");
		alert.setContentText("This action cannot be undone.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				Connection conn = Database.connectDB();

				String sql = "DELETE FROM PATIENT WHERE Patient_Id = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, patientId);
				ps.executeUpdate();

				conn.close();
				loadPatientTable(); // Refresh lại bảng sau khi xoá
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void openEditPatientForm(PatientData patient) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditPatientForm.fxml"));
			Parent root = loader.load();

			EditPatientFormController controller = loader.getController();
			controller.setPatientData(patient); // Truyền dữ liệu sang form chỉnh sửa

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Update Patient");
			stage.setScene(new Scene(root));

			stage.setOnHidden(e -> loadPatientTable()); // Tải lại bảng khi form đóng
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("ERROR : " + e.getMessage());
		}
	}

	@FXML
	private void openAddPatientForm() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddPatientForm.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Add New Patient");
			stage.setScene(new Scene(root));

			stage.setOnHidden(e -> loadPatientTable()); // Refresh lại bảng khi thêm thuốc
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* =====================LOAD PROFILE======================================== */
	private void loadReceptionistProfile() {
		String checkUserSQL = "SELECT ua.name, ua.username, ua.email, ua.gender, ua.Create_date, r.phone, r.address "
				+ "FROM user_account ua " + "JOIN receptionist r ON ua.id = r.receptionist_id "
				+ "WHERE ua.username = ?";

		Connection connect = Database.connectDB();

		try {
			PreparedStatement prepare = connect.prepareStatement(checkUserSQL);
			prepare.setString(1, username);

			ResultSet result = prepare.executeQuery();

//			if (!result.next() || result.getInt(1) <= 0) {
//				alert.errorMessage("Username does not match data.");
//				return;
//			}
			if (!result.next()) {
				alert.errorMessage("Username does not match data.");
				return;
			}
			String name = result.getString("name");
			String username = result.getString("username");
			String email = result.getString("email");
			String phone = result.getString("phone");
			String address = result.getString("address");
			String gender = result.getString("gender");
			String createdAt = result.getString("Create_date");

			// Gán cho các Label
			name_receptDB.setText(name != null ? name : "UNKNOWN");
			username_receptDB.setText(username != null ? username : "");
			name_recept.setText(name != null ? name : "UNKNOWN");
			username_recept.setText(username != null ? username : "");
			gender_recept.setText(gender != null ? gender : "");
			phone_recept.setText(phone != null ? phone : "");
			email_recept.setText(email != null ? email : "");
			createdDate_recept.setText(createdAt != null ? createdAt : "");

			top_username.setText(name != null ? name : "UNKNOWN");

			txt_name_recept.setText(name != null ? name : "");
			txt_username_recept.setText(username != null ? username : "");
			txt_email_recept.setText(email != null ? email : "");
			txt_phone_recept.setText(phone != null ? phone : "");
			gender_cb.setValue(gender != null ? gender : "");
			txt_address_recept.setText(address != null ? address : "");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void profileDisplayImages() {

		String sql = "SELECT Avatar FROM user_account WHERE username = ?";
		connect = Database.connectDB();

		try {
			prepare = connect.prepareStatement(sql);
			prepare.setString(1, username);
			result = prepare.executeQuery();
			if (result.next()) {
				InputStream inputStream = result.getBinaryStream("Avatar");

				if (inputStream != null) {
					// Đọc toàn bộ dữ liệu từ inputStream vào byte[]
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					byte[] data = new byte[1024];
					int nRead;
					while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
						buffer.write(data, 0, nRead);
					}
					buffer.flush();
					byte[] imageBytes = buffer.toByteArray();
					inputStream.close();

					// Tạo nhiều InputStream từ cùng một mảng byte
					InputStream imgStream1 = new ByteArrayInputStream(imageBytes);
					InputStream imgStream2 = new ByteArrayInputStream(imageBytes);

					Image img1 = new Image(imgStream1, 0, 0, true, true);
					profile_circle.setFill(new ImagePattern(img1));

					Image img2 = new Image(imgStream2, 0, 0, true, true);

					top_profile.setFill(new ImagePattern(img2));
				} else {
					System.out.println("Ảnh trong DB bị null.");
				}
			}

		} catch (Exception e) {
			System.out.println("Lỗi khi xử lý dữ liệu hình ảnh: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/* =====================EDIT PROFILE======================================== */
	public void profileUpdateBtn() {
		String name = txt_name_recept.getText();
		String phone = txt_phone_recept.getText();
		String usernameEdit = txt_username_recept.getText();
		String address = txt_address_recept.getText();
		String email = txt_email_recept.getText();
		String gender = (String) gender_cb.getSelectionModel().getSelectedItem();

		if (usernameEdit.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
			alert.errorMessage("Please fill in all the fields.");
			return;
		}
		if (gender == null || gender.isEmpty()) {
			alert.errorMessage("Please select a gender.");
			return;
		}
		String checkUsernameSQL = "SELECT * FROM user_account WHERE username = ?";
		String updateUserSQL = "UPDATE user_account SET name = ?, username = ?, gender = ? WHERE email = ?";
		String updateReceptionistSQL = "UPDATE receptionist SET phone = ?, address = ? WHERE receptionist_id = (SELECT id FROM user_account WHERE email = ?)";

		connect = Database.connectDB();

		try {
			// Kiểm tra username đã tồn tại (trừ chính mình)
			if (!username.equals(usernameEdit)) {
				prepare = connect.prepareStatement(checkUsernameSQL);
				prepare.setString(1, usernameEdit);
				result = prepare.executeQuery();

				if (result.next()) {
					alert.errorMessage("Username \"" + usernameEdit + "\" already exists!");
					return;
				}
			}

			// Cập nhật user_account
			prepare = connect.prepareStatement(updateUserSQL);
			prepare.setString(1, name);
			prepare.setString(2, usernameEdit);
			prepare.setString(3, gender);
			prepare.setString(4, email);
			int rowsUserUpdated = prepare.executeUpdate();

			// Cập nhật receptionist
			prepare = connect.prepareStatement(updateReceptionistSQL);
			prepare.setString(1, phone);
			prepare.setString(2, address);
			prepare.setString(3, email);
			int rowsReceptionistUpdated = prepare.executeUpdate();

			if (rowsUserUpdated > 0 || rowsReceptionistUpdated > 0) {
				alert.successMessage("Profile updated successfully.");
				loadReceptionistProfile();
			} else {
				alert.errorMessage("No user found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			alert.errorMessage("Error updating profile: " + e.getMessage());
		}
	}

	@FXML
	private void profileImportBtn(ActionEvent event) {
		FileChooser open = new FileChooser();
		open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*jpg", "*jpeg", "*png"));

		File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());

		if (file != null) {
			Data.path = file.getAbsolutePath();

			// Hiển thị ảnh lên UI
			image = new Image(file.toURI().toString(), 0, 0, false, true);
			profile_circle.setFill(new ImagePattern(image));

			// Lưu ảnh vào DB
			try {
				connect = Database.connectDB();
				String updateAvatarSQL = "UPDATE user_account SET avatar = ? WHERE email = ?";

				FileInputStream input = new FileInputStream(file);
				prepare = connect.prepareStatement(updateAvatarSQL);
				prepare.setBinaryStream(1, input, (int) file.length());
				prepare.setString(2, email_recept.getText());

				int rows = prepare.executeUpdate();
				if (rows > 0) {
					alert.successMessage("Avatar updated successfully.");
				} else {
					alert.errorMessage("Failed to update avatar.");
				}
				profileDisplayImages();
			} catch (Exception e) {
				e.printStackTrace();
				alert.errorMessage("Error uploading avatar: " + e.getMessage());
			}
		}
	}
	/*
	 * =====================CREATE
	 * APPOINTMENT========================================
	 */

	private void loadComboBoxPatient() {
		ObservableList<PatientData> patientList = FXCollections.observableArrayList();
		String sql = "SELECT * FROM patient";

		try {
			connect = Database.connectDB();
			prepare = connect.prepareStatement(sql);
			ResultSet rs = prepare.executeQuery();

			while (rs.next()) {
				LocalDate birthDate = rs.getDate("date_of_birth").toLocalDate();

				patientList.add(new PatientData(rs.getString("patient_id"), rs.getString("name"), rs.getString("email"),
						rs.getString("gender"), rs.getString("phone"), rs.getString("address"),
						rs.getString("diagnosis"), rs.getBigDecimal("height"), rs.getBigDecimal("weight"), birthDate));
			}
			appointment_patient.setItems(patientList);

			cb_patient.setItems(patientList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadComboBoxService() {
		ObservableList<ServiceData> serviceList = FXCollections.observableArrayList();

		String sql = "SELECT * FROM service";

		try {
			connect = Database.connectDB();
			prepare = connect.prepareStatement(sql);
			ResultSet rs = prepare.executeQuery();

			while (rs.next()) {
				serviceList.add(new ServiceData(rs.getString("id"), rs.getString("name"), rs.getBigDecimal("price"),
						rs.getString("type")));
			}

			cb_service.setItems(serviceList);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void loadComboBoxDoctor(String serviceId, ComboBox<DoctorData> cbDoctor) {
		ObservableList<DoctorData> doctorList = FXCollections.observableArrayList();

		String sql = "SELECT d.Doctor_id, d.Phone, d.Service_id, d.Address, d.Is_confirmed, "
				+ "u.Username, u.Password, u.Email, u.Name, u.Gender, u.Is_active, " + "s.Name AS ServiceName "
				+ "FROM DOCTOR d " + "JOIN USER_ACCOUNT u ON d.Doctor_id = u.Id "
				+ "LEFT JOIN SERVICE s ON d.Service_id = s.Id " + "WHERE d.Service_id = ?";

		try {
			connect = Database.connectDB();
			prepare = connect.prepareStatement(sql);
			prepare.setString(1, serviceId);
			ResultSet rs = prepare.executeQuery();

			while (rs.next()) {
				doctorList.add(new DoctorData(rs.getString("Doctor_id"), rs.getString("Username"),
						rs.getString("Password"), rs.getString("Name"), rs.getString("Email"), rs.getString("Gender"),
						rs.getBoolean("Is_active"), rs.getString("Phone"), rs.getString("ServiceName"),
						rs.getString("Address"), rs.getBoolean("Is_confirmed")));
			}

			cbDoctor.setItems(doctorList);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadUrgencyLevels() {
		cb_urgency.getItems().clear();
		cb_urgency.getItems().addAll(UrgencyLevel.values());
	}

	@FXML
	private void choosePatient(ActionEvent event) throws IOException {
		selectedPatient = cb_patient.getSelectionModel().getSelectedItem();

		if (selectedPatient != null) {
			String sql = "SELECT * FROM patient WHERE patient_id = ?";
			try (Connection connect = Database.connectDB(); PreparedStatement ps = connect.prepareStatement(sql)) {

				ps.setString(1, selectedPatient.getPatientId());
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					lb_patient_name.setText(rs.getString("name"));
					lb_patient_gender.setText(rs.getString("gender"));
					lb_patient_address.setText(rs.getString("gender"));
					int age = Period.between(selectedPatient.getBirthDate(), LocalDate.now()).getYears();
					lb_patient_age.setText(String.valueOf(age));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			lb_patient_name.setText("");
		}

	}

	@FXML
	private void chooseService(ActionEvent event) throws IOException {
		ComboBox<ServiceData> cbService = (ComboBox<ServiceData>) event.getSource();
		AnchorPane currentPane = (AnchorPane) cbService.getParent();
		Label lbPrice = (Label) currentPane.lookup("#lb_price_service");

		ComboBox<DoctorData> cbDoctor = (ComboBox<DoctorData>) currentPane.lookup("#cb_doctor");

		ServiceData selectedService = cb_service.getSelectionModel().getSelectedItem();

		if (selectedService != null) {
			String sql = "SELECT * FROM service WHERE id = ?";
			try (Connection connect = Database.connectDB(); PreparedStatement ps = connect.prepareStatement(sql)) {

				ps.setString(1, selectedService.getServiceId());
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					lbPrice.setText(formatCurrencyVND(rs.getBigDecimal("price")));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			loadComboBoxDoctor(selectedService.getServiceId(), cbDoctor);
		} else {
			lbPrice.setText("");
		}

	}

	@FXML
	private void addNewForm(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChooseServiceForm.fxml"));
			AnchorPane newForm = loader.load();

			int insertPos = vboxContainer.getChildren().size() - 2;
			if (insertPos < 0) {
				insertPos = 0;
			}

			vboxContainer.getChildren().add(insertPos, newForm);
//	        updateRemoveButtons();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void removeForm(ActionEvent event) {
		Button btn = (Button) event.getSource();
		AnchorPane pane = (AnchorPane) btn.getParent();

		VBox vboxContainer = (VBox) pane.getParent();

//		if (vboxContainer.getChildren().size() <= 1) {
//			return;
//		}

		vboxContainer.getChildren().remove(pane);
//		updateRemoveButtons();
	}

//	private void updateRemoveButtons() {
//		ObservableList<Node> children = vboxContainer.getChildren();
//
//		for (int i = 0; i < children.size(); i++) {
//			Node node = children.get(i);
//			if (node instanceof AnchorPane) {
//				AnchorPane pane = (AnchorPane) node;
//				Button btnRemove = (Button) pane.lookup("#btn_remove");
//				if (btnRemove != null) {
//					btnRemove.setVisible(i != 0);
//				}
//			}
//		}
//	}

	// lấy danh sách các serviceId
	private List<String> getSelectedServiceIdsFromVBox() {
		List<String> serviceIds = new ArrayList<>();
		for (Node node : vboxContainer.getChildren()) {
			if (node instanceof AnchorPane) {
				AnchorPane pane = (AnchorPane) node;
				ComboBox<ServiceData> cbService = (ComboBox<ServiceData>) pane.lookup("#cb_service");
				if (cbService != null) {
					ServiceData selected = cbService.getSelectionModel().getSelectedItem();
					if (selected != null) {
						serviceIds.add(selected.getServiceId());
					}
				}
			}
		}
		return serviceIds;
	}

	// tạo suggest
	@FXML
	public void createSuggest(ActionEvent event) throws IOException, InterruptedException {

		if (selectedPatient == null) {
			alert.errorMessage("⚠ Please select patient.");
			return;
		}

		if (cb_urgency.getValue() == null) {
			alert.errorMessage("⚠ Please select urgency level.");
			return;
		}
		String patientId = selectedPatient.getPatientId();
		List<String> serviceIds = getSelectedServiceIdsFromVBox();
		int urgency = cb_urgency.getValue().getScore();
		boolean isFollowup = checkbox_followUp.isSelected();
		AppointmentSuggester suggester = new AppointmentSuggester(txt_suggest);
		suggester.createSuggest(patientId, serviceIds, urgency, isFollowup);
	}

	boolean isAvailableAppointment = true;
	boolean isCheck = false;

	// Lấy danh sách dịch vụ đã chọn
	List<String> selectedServiceNames = new ArrayList<>();
	List<Map<String, String>> selectedDoctorInfos = new ArrayList<>();
	List<String> selectedSlotTimes = new ArrayList<>();
	List<LocalDate> selectTimes = new ArrayList<>();
	List<LocalTime> slotTimes = new ArrayList<>();

	PatientData selectedPatient = new PatientData();
	int urgency;
	boolean isFollowup;

	// kiểm tra trùng lịch khám
	public boolean hasDuplicateSlots() {
		Set<String> bookedSlots = new HashSet<>();

		for (int i = 0; i < selectTimes.size(); i++) {
			LocalDate date = selectTimes.get(i);
			LocalTime time = slotTimes.get(i);

			String key = date.toString() + "|" + time.toString();

			if (bookedSlots.contains(key)) {
				return true;
			} else {
				bookedSlots.add(key);
			}
		}

		return false;
	}

	// check lịch hẹn
	@FXML
	public void checkSchedule(ActionEvent event) {
		isAvailableAppointment = true;
		isCheck = true;
		txt_details.clear();
		selectedServiceNames.clear();
		selectedDoctorInfos.clear();
		selectedSlotTimes.clear();
		selectTimes.clear();
		slotTimes.clear();
		if (selectedPatient == null) {
			alert.errorMessage("⚠ Please select patient!");
			isAvailableAppointment = false;
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("🔹 Patient: ").append(selectedPatient.getName()).append("\n\n");

		for (Node node : vboxContainer.getChildren()) {

			if (node instanceof AnchorPane anchorPane) {
				// Lấy thông tin dịch vụ
				ComboBox<ServiceData> cbService = (ComboBox<ServiceData>) anchorPane.lookup("#cb_service");
				ComboBox<DoctorData> cbDoctor = (ComboBox<DoctorData>) anchorPane.lookup("#cb_doctor");
				DatePicker selectTime = (DatePicker) anchorPane.lookup("#select_time");
				Spinner<Integer> selectHour = (Spinner<Integer>) anchorPane.lookup("#spHour");
				Spinner<Integer> selectMinute = (Spinner<Integer>) anchorPane.lookup("#spMinute");

				String serviceName = (cbService != null && cbService.getValue() != null)
						? cbService.getValue().getName()
						: "Haven't chosen service";

				if (serviceName == null) {
					alert.errorMessage("⚠ Please fill all the banks.");
					isAvailableAppointment = false;
					return;
				}

				String doctorName = (cbDoctor != null && cbDoctor.getValue() != null) ? cbDoctor.getValue().getName()
						: "Haven't chosen doctor";
				String doctorId = (cbDoctor != null && cbDoctor.getValue() != null) ? cbDoctor.getValue().getId()
						: null;

				if (doctorId == null) {
					alert.errorMessage("⚠ Please select doctor for service " + serviceName);
					isAvailableAppointment = false;
					return;
				}

				String slotTimeStr;
				String selectTimeStr;
				if (selectTime != null && selectTime.getValue() != null) {
					Integer hour = selectHour != null && selectHour.getValue() != null ? selectHour.getValue() : 0;
					Integer minute = selectMinute != null && selectMinute.getValue() != null ? selectMinute.getValue()
							: 0;

					LocalDate date = selectTime.getValue();
					LocalTime time = LocalTime.of(hour, minute);
					LocalDateTime dateTime = LocalDateTime.of(date, time);

					slotTimeStr = time.format(DateTimeFormatter.ofPattern("HH:mm"));
					selectTimeStr = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

					String sqlDoctor = "SELECT COUNT(*) FROM AVAILABLE_SLOT WHERE Doctor_id = ? AND TIME(Slot_time) = ? AND Is_booked = TRUE";
					String sqlPatient = "SELECT COUNT(*) FROM APPOINTMENT" + " WHERE Patient_id = ? AND TIME = ?";
					try {
						connect = Database.connectDB();
						prepare = connect.prepareStatement(sqlDoctor);
						prepare.setString(1, doctorId);
						prepare.setString(2, slotTimeStr);
						ResultSet rs = prepare.executeQuery();
						if (rs.next()) {
							int count = rs.getInt(1);
							if (count > 0) {
								lb_check.setText("⚠ Bác sĩ " + cbDoctor.getValue().getName()
										+ " đã có lịch hẹn vào lúc " + slotTimeStr);
								isAvailableAppointment = false;
								return;
							}
						}
						DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
						DateTimeFormatter sqlFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
						String sqlTimeStr = LocalDateTime.parse(selectTimeStr, inputFormat).format(sqlFormat);

						prepare = connect.prepareStatement(sqlPatient);
						prepare.setString(1, selectedPatient.getPatientId());
						prepare.setString(2, sqlTimeStr);
						rs = prepare.executeQuery();
						if (rs.next()) {
							int count = rs.getInt(1);
							if (count > 0) {
								lb_check.setText("⚠ " + serviceName
										+ " is coincided. This patient has already have appointment at " + slotTimeStr);
								isAvailableAppointment = false;
								return;
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
						lb_check.setText("❌ Error while checking appointments!");
					}
					selectedServiceNames.add(serviceName);

					Map<String, String> doctorInfo = new HashMap<>();
					doctorInfo.put("id", doctorId);
					doctorInfo.put("name", doctorName);

					selectedDoctorInfos.add(doctorInfo);

					selectedSlotTimes.add(selectTimeStr);
					selectTimes.add(date);
					slotTimes.add(time);

				} else {
					// Chưa chọn thời gian
					lb_check.setText("You haven't choose time for service " + serviceName);
					isAvailableAppointment = false;
				}

			}
		}

		if (selectedServiceNames.isEmpty()) {
			alert.errorMessage("⚠ Please select at lease 1 service.");
			isAvailableAppointment = false;
			return;
		}

		if (hasDuplicateSlots()) {
			alert.errorMessage("⚠ There are duplicated appointments among your chosen doctors!.");
			isAvailableAppointment = false;
			return;
		}

		// Lấy thông tin khác
		urgency = cb_urgency.getValue() != null ? cb_urgency.getValue().getScore() : 1;
		isFollowup = checkbox_followUp.isSelected();

		// Ghi thông tin vào txt_details
		for (int i = 0; i < selectedServiceNames.size(); i++) {
			sb.append("📌 Service: ").append(selectedServiceNames.get(i)).append("\n");
			sb.append("👨‍⚕️ Doctor: ").append(selectedDoctorInfos.get(i).get("name")).append("\n");
			sb.append("⏰ Time: ").append(selectedSlotTimes.get(i)).append("\n");
			sb.append("----------------------------------------\n");
		}

		sb.append("\n🔻 Emergency: ").append(urgency);
		sb.append("\n🔁 Follow Up: ").append(isFollowup ? "Yes" : "No");
		txt_details.setText(sb.toString());

		if (isAvailableAppointment) {
			lb_check.setText("✔ These appointments are valid!");
		}

	}

	private static final Logger LOGGER = Logger.getLogger(ReceptionistController.class.getName());
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    static {
        LOGGER.setLevel(Level.FINE);
        for (Handler handler : LOGGER.getParent().getHandlers()) {
            handler.setLevel(Level.FINE);
        }
    }

 // Helper method to get receptionist name
    private String getReceptionistName(Connection connect) {
        String sql = "SELECT ua.Name FROM USER_ACCOUNT ua JOIN RECEPTIONIST r ON ua.Id = r.Receptionist_id WHERE ua.Username = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Name");
            }
        } catch (SQLException e) {
            LOGGER.severe("Error fetching receptionist name: " + e.getMessage());
        }
        return "Unknown";
    }

    // Method to replace placeholders while preserving formatting
    private void replacePlaceholders(XWPFDocument doc, Map<String, String> placeholders) {
        if (placeholders == null) {
            LOGGER.warning("Placeholders map is null, skipping replacement.");
            return;
        }

        // Process paragraphs
        for (XWPFParagraph para : doc.getParagraphs()) {
            replaceInParagraph(para, placeholders);
        }

        // Process tables
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph para : cell.getParagraphs()) {
                        replaceInParagraph(para, placeholders);
                    }
                    if (cell.getParagraphs().isEmpty()) {
                        XWPFParagraph newPara = cell.addParagraph();
                        replaceInParagraph(newPara, placeholders);
                    }
                }
            }
        }

        // Process headers and footers
        XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
        if (headerFooterPolicy != null) {
            XWPFHeader defaultHeader = headerFooterPolicy.getDefaultHeader();
            if (defaultHeader != null) {
                for (XWPFParagraph para : defaultHeader.getParagraphs()) {
                    replaceInParagraph(para, placeholders);
                }
            }
            XWPFFooter defaultFooter = headerFooterPolicy.getDefaultFooter();
            if (defaultFooter != null) {
                for (XWPFParagraph para : defaultFooter.getParagraphs()) {
                    replaceInParagraph(para, placeholders);
                }
            }
        }
    }

    private void replaceInParagraph(XWPFParagraph para, Map<String, String> placeholders) {
        if (para == null || placeholders == null) {
            LOGGER.warning("Paragraph or placeholders is null: Para=" + para);
            return;
        }
        List<XWPFRun> runs = para.getRuns();
        if (runs == null || runs.isEmpty()) {
            XWPFRun newRun = para.createRun();
            newRun.setFontFamily("Times New Roman");
            newRun.setFontSize(12);
            runs = para.getRuns();
        }

        StringBuilder fullText = new StringBuilder();
        for (XWPFRun run : runs) {
            String text = run.getText(0);
            if (text != null) {
                fullText.append(text);
            }
        }

        String originalText = fullText.toString();
        String replacedText = originalText;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String replacement = entry.getValue() != null ? entry.getValue() : "";
            replacedText = replacedText.replace(placeholder, replacement);
            if (originalText.contains("$" + entry.getKey())) {
                replacedText = replacedText.replace("$" + entry.getKey(), replacement);
            }
        }

        if (!originalText.equals(replacedText)) {
            for (int i = runs.size() - 1; i >= 0; i--) {
                para.removeRun(i);
            }
            boolean isTotal = originalText.contains("Total:");
            boolean isAdvice = originalText.contains("Advice:");
            if (isTotal) {
                XWPFRun totalRun = para.createRun();
                totalRun.setFontFamily("Times New Roman");
                totalRun.setFontSize(12);
                totalRun.setBold(true);
                totalRun.setText("Total: ");
                XWPFRun valueRun = para.createRun();
                valueRun.setFontFamily("Times New Roman");
                valueRun.setFontSize(12);
                valueRun.setText(replacedText.replace("Total: ", ""));
            } else if (isAdvice) {
                XWPFRun adviceRun = para.createRun();
                adviceRun.setFontFamily("Times New Roman");
                adviceRun.setFontSize(12);
                adviceRun.setBold(true);
                adviceRun.setText("Advice: ");
                XWPFRun valueRun = para.createRun();
                valueRun.setFontFamily("Times New Roman");
                valueRun.setFontSize(12);
                valueRun.setText(replacedText.replace("Advice: ", ""));
            } else {
                XWPFRun newRun = para.createRun();
                newRun.setFontFamily("Times New Roman");
                newRun.setFontSize(12);
                newRun.setText(replacedText);
            }
            LOGGER.fine("Replaced placeholders in paragraph: " + originalText + " -> " + replacedText);
        }
    }

    private void replaceTablePlaceholders(XWPFDocument doc, List<String> serviceNames, List<BigDecimal> servicePrices,
            List<Map<String, Object>> drugs, BigDecimal totalPrice, String formattedDate,
            String receptionistName, String doctorName, String diagnose, String advice) {
        List<XWPFTable> tables = doc.getTables();
        if (tables.isEmpty()) {
            LOGGER.warning("No tables found in document.");
            return;
        }

        Map<String, String> tablePlaceholders = new HashMap<>();
        tablePlaceholders.put("patientId", selectedPatient != null ? selectedPatient.getPatientId() : "");
        tablePlaceholders.put("patientName", selectedPatient != null ? selectedPatient.getName() : "");
        tablePlaceholders.put("age", selectedPatient != null && selectedPatient.getBirthDate() != null ?
                String.valueOf(Period.between(selectedPatient.getBirthDate(), LocalDate.now()).getYears()) : "0");
        tablePlaceholders.put("gender", selectedPatient != null ? selectedPatient.getGender() : "");
        tablePlaceholders.put("address", selectedPatient != null ? selectedPatient.getAddress() : "");
        tablePlaceholders.put("diagnose", diagnose != null ? diagnose : "");
        tablePlaceholders.put("advice", advice != null ? advice : "");
        tablePlaceholders.put("totalPrice", totalPrice != null ? formatCurrencyVND(totalPrice) : "0 VNĐ");
        tablePlaceholders.put("date", formattedDate != null ? formattedDate : "");
        tablePlaceholders.put("receptionistName", receptionistName != null ? receptionistName : "");
        tablePlaceholders.put("doctorName", doctorName != null ? doctorName : "");

        for (XWPFTable table : tables) {
            int headerRowIndex = -1;
            boolean isServiceTable = false;
            boolean isDrugTable = false;

            for (int i = 0; i < table.getRows().size(); i++) {
                XWPFTableRow row = table.getRow(i);
                String cellText = row.getCell(0) != null ? row.getCell(0).getText().toLowerCase() : "";
                if (cellText.contains("service")) {
                    isServiceTable = true;
                    headerRowIndex = i;
                    break;
                } else if (cellText.contains("medicine")) {
                    isDrugTable = true;
                    headerRowIndex = i;
                    break;
                }
            }

            if (headerRowIndex == -1) headerRowIndex = 0;

            while (table.getRows().size() > headerRowIndex + 1) {
                table.removeRow(table.getRows().size() - 1);
            }

            if (isServiceTable && serviceNames != null && servicePrices != null) {
                for (int i = 0; i < serviceNames.size(); i++) {
                    XWPFTableRow row = table.createRow();
                    while (row.getTableCells().size() < 2) {
                        row.addNewTableCell();
                    }
                    XWPFTableCell nameCell = row.getCell(0);
                    XWPFTableCell priceCell = row.getCell(1);
                    if (nameCell.getParagraphs().isEmpty()) nameCell.addParagraph();
                    if (priceCell.getParagraphs().isEmpty()) priceCell.addParagraph();
                    replaceInParagraph(nameCell.getParagraphs().get(0), tablePlaceholders);
                    replaceInParagraph(priceCell.getParagraphs().get(0), tablePlaceholders);
                    XWPFRun nameRun = nameCell.getParagraphs().get(0).getRuns().isEmpty() ? nameCell.getParagraphs().get(0).createRun() : nameCell.getParagraphs().get(0).getRuns().get(0);
                    XWPFRun priceRun = priceCell.getParagraphs().get(0).getRuns().isEmpty() ? priceCell.getParagraphs().get(0).createRun() : priceCell.getParagraphs().get(0).getRuns().get(0);
                    nameRun.setFontFamily("Times New Roman");
                    nameRun.setFontSize(12);
                    nameRun.setText(serviceNames.get(i) != null ? serviceNames.get(i) : "N/A");
                    priceRun.setFontFamily("Times New Roman");
                    priceRun.setFontSize(12);
                    priceRun.setText(servicePrices.get(i) != null ? formatCurrencyVND(servicePrices.get(i)) : "0 VNĐ");
                }
            }

            if (isDrugTable && drugs != null) {
                for (int i = 0; i < drugs.size(); i++) {
                    XWPFTableRow row = table.createRow();
                    while (row.getTableCells().size() < 4) {
                        row.addNewTableCell();
                    }
                    Map<String, Object> drug = drugs.get(i);
                    String drugName = (String) drug.get("name");
                    String instructions = (String) drug.get("instructions");
                    Object quantityObj = drug.get("quantity");
                    String unit = (String) drug.get("unit");
                    BigDecimal price = (BigDecimal) drug.get("price");
                    int quantity = quantityObj instanceof Integer ? (Integer) quantityObj : 0;

                    XWPFTableCell nameCell = row.getCell(0);
                    XWPFTableCell xCell = row.getCell(1);
                    XWPFTableCell qtyCell = row.getCell(2);
                    XWPFTableCell priceCell = row.getCell(3);

                    if (nameCell.getParagraphs().isEmpty()) nameCell.addParagraph();
                    if (xCell.getParagraphs().isEmpty()) xCell.addParagraph();
                    if (qtyCell.getParagraphs().isEmpty()) qtyCell.addParagraph();
                    if (priceCell.getParagraphs().isEmpty()) priceCell.addParagraph();
                    replaceInParagraph(nameCell.getParagraphs().get(0), tablePlaceholders);
                    replaceInParagraph(xCell.getParagraphs().get(0), tablePlaceholders);
                    replaceInParagraph(qtyCell.getParagraphs().get(0), tablePlaceholders);
                    replaceInParagraph(priceCell.getParagraphs().get(0), tablePlaceholders);

                    XWPFRun nameRun = nameCell.getParagraphs().get(0).getRuns().isEmpty() ? nameCell.getParagraphs().get(0).createRun() : nameCell.getParagraphs().get(0).getRuns().get(0);
                    XWPFRun xRun = xCell.getParagraphs().get(0).getRuns().isEmpty() ? xCell.getParagraphs().get(0).createRun() : xCell.getParagraphs().get(0).getRuns().get(0);
                    XWPFRun qtyRun = qtyCell.getParagraphs().get(0).getRuns().isEmpty() ? qtyCell.getParagraphs().get(0).createRun() : qtyCell.getParagraphs().get(0).getRuns().get(0);
                    XWPFRun priceRun = priceCell.getParagraphs().get(0).getRuns().isEmpty() ? priceCell.getParagraphs().get(0).createRun() : priceCell.getParagraphs().get(0).getRuns().get(0);

                    nameRun.setFontFamily("Times New Roman");
                    nameRun.setFontSize(12);
                    nameRun.setText(drugName != null ? drugName : "N/A");
                    if (instructions != null) {
                        nameRun.addBreak();
                        XWPFRun instrRun = nameCell.getParagraphs().get(0).createRun();
                        instrRun.setFontFamily("Times New Roman");
                        instrRun.setFontSize(12);
                        instrRun.setItalic(true);
                        instrRun.setText(instructions);
                    }
                    xRun.setText("x");
                    qtyRun.setText(quantityObj != null ? quantity + (unit != null ? " " + unit : "") : "0");
                    priceRun.setText(price != null ? formatCurrencyVND(price.multiply(BigDecimal.valueOf(quantity))) : "0 VNĐ");
                }
            }
        }
    }
    
    
 // Method to create appointments and generate invoice
    @FXML
    public void createAppointment(ActionEvent event) {
        if (!isCheck) {
            alert.errorMessage("Please check the schedule before creating appointments!");
            return;
        }
        if (!isAvailableAppointment) {
            alert.errorMessage("Cannot create appointment due to schedule conflicts!");
            return;
        }
        if (selectedPatient == null) {
            alert.errorMessage("Please select a patient!");
            return;
        }

        List<ServiceData> selectedServices = new ArrayList<>();
        for (Node node : vboxContainer.getChildren()) {
            if (node instanceof AnchorPane) {
                ComboBox<ServiceData> cbService = (ComboBox<ServiceData>) ((AnchorPane) node).lookup("#cb_service");
                if (cbService != null && cbService.getValue() != null) {
                    selectedServices.add(cbService.getValue());
                }
            }
        }

        if (selectedServices.isEmpty()) {
            alert.errorMessage("Please select at least one service!");
            return;
        }

        try {
            connect = Database.connectDB();
            connect.setAutoCommit(false);

            String insertAppointmentSQL = "INSERT INTO appointment (id, time, status, cancel_reason, Doctor_id, Patient_id, Urgency_level, Is_followup, Priority_score, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String insertAvailableSlotSQL = "INSERT INTO available_slot (id, Doctor_id, Slot_time, Slot_date, Is_booked) VALUES (?, ?, ?, ?, ?)";
            String insertAppointmentServiceSQL = "INSERT INTO appointment_service (Appointment_id, Service_id) VALUES (?, ?)";

            for (int i = 0; i < selectedServiceNames.size(); i++) {
                String appointmentId = UUID.randomUUID().toString();
                String doctorId = selectedDoctorInfos.get(i).get("id");
                LocalDateTime dateTime = LocalDateTime.parse(selectedSlotTimes.get(i), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                String sqlTimeStr = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String slotTimeStr = dateTime.toLocalTime().toString();
                String slotDateStr = dateTime.toLocalDate().toString();
                String status = "Scheduled";
                String cancelReason = "";
                int priorityScore = urgency;

                PreparedStatement psAppointment = connect.prepareStatement(insertAppointmentSQL);
                psAppointment.setString(1, appointmentId);
                psAppointment.setString(2, sqlTimeStr);
                psAppointment.setString(3, status);
                psAppointment.setString(4, cancelReason);
                psAppointment.setString(5, doctorId);
                psAppointment.setString(6, selectedPatient.getPatientId());
                psAppointment.setInt(7, urgency);
                psAppointment.setBoolean(8, isFollowup);
                psAppointment.setInt(9, priorityScore);
                psAppointment.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                psAppointment.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
                psAppointment.executeUpdate();

                String slotId = UUID.randomUUID().toString();
                PreparedStatement psSlot = connect.prepareStatement(insertAvailableSlotSQL);
                psSlot.setString(1, slotId);
                psSlot.setString(2, doctorId);
                psSlot.setString(3, slotTimeStr);
                psSlot.setString(4, slotDateStr);
                psSlot.setBoolean(5, true);
                psSlot.executeUpdate();

                for (ServiceData serviceData : selectedServices) {
                    PreparedStatement psAppointmentService = connect.prepareStatement(insertAppointmentServiceSQL);
                    psAppointmentService.setString(1, appointmentId);
                    psAppointmentService.setString(2, serviceData.getServiceId());
                    psAppointmentService.executeUpdate();
                }
            }

            connect.commit();

            List<String> serviceNames = selectedServices.stream().map(ServiceData::getName).collect(Collectors.toList());
            List<BigDecimal> servicePrices = selectedServices.stream().map(ServiceData::getPrice).collect(Collectors.toList());
            generateInvoiceDocx(serviceNames, servicePrices, getReceptionistName(connect));

            alert.successMessage("Appointment(s) created successfully.");

        } catch (SQLException e) {
            LOGGER.severe("Database error during appointment creation: " + e.getMessage());
            try {
                if (connect != null) {
                    connect.rollback();
                }
            } catch (SQLException rollbackEx) {
                LOGGER.severe("Rollback failed: " + rollbackEx.getMessage());
            }
            alert.errorMessage("Database error: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.severe("IO Exception during invoice generation: " + e.getMessage());
            alert.errorMessage("Error exporting invoice: " + e.getMessage());
        } finally {
            try {
                if (connect != null) {
                    connect.setAutoCommit(true);
                    connect.close();
                }
            } catch (SQLException e) {
                LOGGER.severe("Error closing connection: " + e.getMessage());
            }
        }
    }

    // Fixed generateInvoiceDocx method
    private void generateInvoiceDocx(List<String> serviceNames, List<BigDecimal> servicePrices, String receptionistName) throws IOException {
        LOGGER.info("Starting invoice generation. Service count: " + (serviceNames != null ? serviceNames.size() : 0));
        if (selectedPatient == null) {
            LOGGER.severe("Selected patient is null.");
            throw new IllegalStateException("Selected patient cannot be null.");
        }
        if (serviceNames == null || servicePrices == null || serviceNames.size() != servicePrices.size()) {
            LOGGER.severe("Mismatch in service data: serviceNames=" + serviceNames + ", servicePrices=" + servicePrices);
            throw new IllegalStateException("Invalid service names or prices.");
        }

        Files.createDirectories(Paths.get("Word"));
        String destFileName = "Word/invoice_details_" + UUID.randomUUID().toString() + ".docx";
        File sourceFile = new File("Word/INVOICE.docx");
        Path destPath = Paths.get(destFileName);

        if (!sourceFile.exists()) {
            LOGGER.severe("Source file Word/INVOICE.docx not found.");
            throw new IOException("Source file not found.");
        }

        if (Files.exists(destPath)) {
            Files.delete(destPath);
        }
        Files.copy(sourceFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(destFileName))) {
            int age = selectedPatient != null && selectedPatient.getBirthDate() != null ?
                    Period.between(selectedPatient.getBirthDate(), LocalDate.now()).getYears() : 0;
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (BigDecimal price : servicePrices) {
                totalPrice = totalPrice.add(price != null ? price : BigDecimal.ZERO);
            }

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            String formattedDate = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("patientId", selectedPatient.getPatientId());
            placeholders.put("patientName", selectedPatient.getName());
            placeholders.put("age", String.valueOf(age));
            placeholders.put("gender", selectedPatient.getGender());
            placeholders.put("address", selectedPatient.getAddress());
            placeholders.put("totalPrice", formatCurrencyVND(totalPrice));
            placeholders.put("date", formattedDate);
            placeholders.put("receptionistName", receptionistName);

            replacePlaceholders(doc, placeholders);
            replaceTablePlaceholders(doc, serviceNames, servicePrices, null, totalPrice, formattedDate, receptionistName, null, null, null);

            try (FileOutputStream fos = new FileOutputStream(destFileName)) {
                doc.write(fos);
                LOGGER.info("Invoice document successfully written to " + destFileName);
            }

            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(new File(destFileName));
                } catch (IOException e) {
                    LOGGER.severe("Error opening invoice document: " + destFileName + ": " + e.getMessage());
                    alert.errorMessage("Failed to open invoice file: " + e.getMessage());
                }
            }

            alert.successMessage("Invoice successfully exported to " + destFileName);
        } catch (IOException e) {
            LOGGER.severe("IO Exception during invoice document generation: " + e.getMessage());
            alert.errorMessage("Error exporting invoice: " + e.getMessage());
            throw e;
        }
    }
    
	public void clearSuggestion(ActionEvent event) {
		txt_suggest.clear();
	}
	/*
	 * =====================MANGE APPOINTMENT AND PRINT DRUG
	 * BILL========================================
	 */

	private void loadAppointmentData() {
	    String sql = """
	        SELECT
	            a.id,
	            a.time,
	            a.status AS appointment_status,
	            a.Doctor_id,
	            ua.name AS doctor_name,
	            a.Patient_id AS patient_id,
	            pt.name AS patient_name,
	            pt.phone AS contact_number,
	            s.id AS service_id,
	            s.name AS service_name,
	            a.cancel_reason,
	            a.Prescription_Status,
	            a.create_date,
	            a.update_date
	        FROM appointment a
	                 JOIN doctor d ON a.Doctor_id = d.Doctor_id
	        JOIN user_account ua ON ua.id = a.Doctor_id
	        JOIN patient pt ON pt.Patient_id = a.Patient_id
	        JOIN service s ON d.service_id = s.id
	        LEFT JOIN prescription p ON p.Appointment_id = a.id
	    """;

	    connect = Database.connectDB();
	    try {
	        prepare = connect.prepareStatement(sql);
	        result = prepare.executeQuery();
	        appoinmentListData.clear();
	        while (result.next()) {
	            String id = result.getString("id");
	            Timestamp time = result.getTimestamp("time");
	            String status = result.getString("appointment_status");
	            String doctorId = result.getString("Doctor_id");
	            String doctorName = result.getString("doctor_name");
	            String patientId = result.getString("patient_id");
	            String patientName = result.getString("patient_name");
	            String contactNumber = result.getString("contact_number");
	            String serviceId = result.getString("service_id");
	            String serviceName = result.getString("service_name");
	            String reason = result.getString("cancel_reason");
	            String prescriptionStatus = result.getString("Prescription_Status"); // New field
	            Timestamp createdDate = result.getTimestamp("create_date");
	            Timestamp lastModifiedDate = result.getTimestamp("update_date");

	            appoinmentListData.add(new AppointmentData(id, time, status, reason, doctorId, patientId, serviceId,
	                    serviceName, prescriptionStatus, createdDate, lastModifiedDate, patientName, doctorName,
	                    contactNumber));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (connect != null) connect.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public void appointmentUpdateBtn() {
		AppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();
		String appointmentID = "";
		if (selectedAppointment != null) {
			appointmentID = selectedAppointment.getId();
		} else {
			alert.errorMessage("Please select appointment to update");
		}

		Integer hour = spHour1 != null && spHour1.getValue() != null ? spHour1.getValue() : 0;
		Integer minute = spMinute1 != null && spMinute1.getValue() != null ? spMinute1.getValue() : 0;

		LocalDate dateSet = appointment_date.getValue();
		LocalTime timeSet = LocalTime.of(hour, minute);
		LocalDateTime dateTime = LocalDateTime.of(dateSet, timeSet);

		String slotTimeStr = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")); // cho DB TIME
		String selectTimeStr = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")); // cho hiển thị
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		DateTimeFormatter sqlFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String sqlTimeStr = LocalDateTime.parse(selectTimeStr, inputFormat).format(sqlFormat);

		String status = appointment_status.getSelectionModel().getSelectedItem();
		String cancelReason = appointment_cancelReason.getText();
		String patientID = appointment_patient.getSelectionModel().getSelectedItem().getPatientId();
		String doctorId = appointment_doctor.getSelectionModel().getSelectedItem().getId();
		String doctorName = appointment_doctor.getSelectionModel().getSelectedItem().getName();
		String serviceName = appointment_serviceName.getText();

		if (appointmentID.isEmpty() || sqlTimeStr.isEmpty() || status.isEmpty() || patientID.isEmpty()) {
			alert.errorMessage("Please fill all blank fields");
			System.out.println(sqlTimeStr);
			return;
		}

		// Chuẩn bị các câu truy vấn kiểm tra
		String sqlDoctor = "SELECT COUNT(*) FROM AVAILABLE_SLOT WHERE Doctor_id = ? AND TIME(Slot_time) = ? AND Is_booked = TRUE";
		String sqlPatient = "SELECT COUNT(*) FROM APPOINTMENT WHERE Patient_id = ? AND time = ?";
		String sqlUpdate = "UPDATE appointment SET time = ?, status = ?, cancel_reason = ?, patient_id = ?, doctor_id = ? WHERE id = ?";

		try {
			connect = Database.connectDB();

			// 1. Check lịch của bác sĩ
			prepare = connect.prepareStatement(sqlDoctor);
			prepare.setString(1, doctorId);
			prepare.setString(2, slotTimeStr); // slotTimeStr định dạng 'HH:mm:ss' hoặc tương tự
			ResultSet rs = prepare.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					lb_check.setText("⚠ Doctor " + doctorName + " has already had appointment " + slotTimeStr);
					isAvailableAppointment = false;
					return;
				}
			}

			// 2. Check lịch của bệnh nhân

			prepare = connect.prepareStatement(sqlPatient);
			prepare.setString(1, selectedPatient.getPatientId());
			prepare.setString(2, sqlTimeStr);
			rs = prepare.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					lb_check.setText("⚠ " + serviceName + " is coincided. This patient already has an appointment at "
							+ slotTimeStr);
					isAvailableAppointment = false;
					return;
				}
			}

			prepare = connect.prepareStatement(sqlUpdate);
			prepare.setString(1, sqlTimeStr);
			prepare.setString(2, status);
			prepare.setString(3, cancelReason);
			prepare.setString(4, patientID);
			prepare.setString(5, doctorId);
			prepare.setString(6, appointmentID);

			int rowsUpdated = prepare.executeUpdate();
			if (rowsUpdated > 0) {
				alert.successMessage("Appointment updated successfully.");
				appointmentClearBtn();
				loadAppointmentData();
			} else {
				alert.errorMessage("No appointment found with ID: " + appointmentID);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			alert.errorMessage("Error updating appointment: " + e.getMessage());
		}

	}
	
	// Method to generate prescription document
	@FXML
    public void appointmentPrescriptionBtn() {
        AppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            alert.errorMessage("Please select an appointment first!");
            return;
        }
        if (!selectedAppointment.getStatus().equals(AppointmentStatus.Finish.toString())) {
            alert.errorMessage("This appointment is not finished yet!");
            return;
        }
                
        if ("Paid".equals(selectedAppointment.getPrescriptionStatus())) {
            String filePath = "Word/prescription_" + selectedAppointment.getId() + ".docx";
            File file = new File(filePath);
            if (file.exists()) {
                try {
                	alert.successMessage("Opening prescription file: " + filePath);
                    LOGGER.info("Opening prescription file: " + filePath);
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    LOGGER.severe("Failed to open prescription file: " + e.getMessage());
                    alert.errorMessage("Failed to open prescription file: " + e.getMessage());
                }
                return;
            } else {
                LOGGER.warning("Prescription file not found: " + filePath);
                alert.errorMessage("Prescription file not found for this appointment!");
                return;
            }
        }
        
        try {
            connect = Database.connectDB();
            if (connect == null) {
                throw new SQLException("Failed to establish database connection.");
            }

            String prescriptionSQL = "SELECT p.Id, p.Diagnose, p.Advice, ua.Name AS Doctor_name, pt.Name AS Patient_name, " +
                    "pt.Gender, pt.Address, pt.Date_of_birth, pt.Patient_id " +
                    "FROM PRESCRIPTION p " +
                    "JOIN DOCTOR d ON p.Doctor_id = d.Doctor_id " +
                    "JOIN USER_ACCOUNT ua ON ua.Id = d.Doctor_id " +
                    "JOIN PATIENT pt ON p.Patient_id = pt.Patient_id " +
                    "WHERE p.Appointment_id = ?";
            String prescriptionDetailsSQL = "SELECT pd.Drug_id, pd.Quantity, pd.Instructions, dr.Name AS Drug_name, dr.Unit, dr.Price " +
                    "FROM PRESCRIPTION_DETAILS pd " +
                    "JOIN DRUG dr ON pd.Drug_id = dr.Id " +
                    "WHERE pd.Prescription_id = ?";
            String updatePrescriptionStatusSQL = "UPDATE appointment SET Prescription_Status = ? WHERE id = ?";

            PreparedStatement psPrescription = connect.prepareStatement(prescriptionSQL);
            psPrescription.setString(1, selectedAppointment.getId());
            ResultSet rsPrescription = psPrescription.executeQuery();

            if (!rsPrescription.next()) {
                alert.errorMessage("No prescription found for this appointment!");
                return;
            }

            String patientName = rsPrescription.getString("Patient_name");
            String doctorName = rsPrescription.getString("Doctor_name");
            String diagnose = rsPrescription.getString("Diagnose");
            String advice = rsPrescription.getString("Advice");
            String gender = rsPrescription.getString("Gender");
            String address = rsPrescription.getString("Address");
            String patientId = rsPrescription.getString("Patient_id");
            java.sql.Date dob = rsPrescription.getDate("Date_of_birth");
            int age = 0;
            if (dob != null) {
                age = Period.between(dob.toLocalDate(), LocalDate.now()).getYears();
                LOGGER.info("Calculated age: " + age);
            } else {
                LOGGER.warning("Date_of_birth is null for patient ID: " + patientId);
            }
            String prescriptionId = rsPrescription.getString("Id");

            PreparedStatement psDetails = connect.prepareStatement(prescriptionDetailsSQL);
            psDetails.setString(1, prescriptionId);
            ResultSet rsDetails = psDetails.executeQuery();

            List<Map<String, Object>> drugs = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.ZERO;
            while (rsDetails.next()) {
                Map<String, Object> drug = new HashMap<>();
                drug.put("name", rsDetails.getString("Drug_name"));
                drug.put("instructions", rsDetails.getString("Instructions"));
                drug.put("quantity", rsDetails.getInt("Quantity"));
                drug.put("unit", rsDetails.getString("Unit"));
                drug.put("price", rsDetails.getBigDecimal("Price"));
                drugs.add(drug);
                totalPrice = totalPrice.add(rsDetails.getBigDecimal("Price").multiply(BigDecimal.valueOf(rsDetails.getInt("Quantity"))));
            }

            Files.createDirectories(Paths.get("Word"));
            String destFileName = "Word/prescription_" + selectedAppointment.getId() + ".docx";
            File srcFile = new File("Word/PRESCRIPTION.docx");
            Path destPath = Paths.get(destFileName);

            if (!srcFile.exists()) {
                LOGGER.severe("Source file does not exist.");
                throw new IOException("Source file not found.");
            }

            if (Files.exists(destPath)) {
                Files.delete(destPath);
            }
            Files.copy(srcFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

            try (XWPFDocument doc = new XWPFDocument(new FileInputStream(destFileName))) {
                ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
                String formattedDate = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("patientId", patientId);
                placeholders.put("patientName", patientName);
                placeholders.put("age", String.valueOf(age));
                placeholders.put("gender", gender);
                placeholders.put("address", address);
                placeholders.put("diagnose", diagnose);
                placeholders.put("advice", advice);
                placeholders.put("totalPrice", formatCurrencyVND(totalPrice));
                placeholders.put("date", formattedDate);
                placeholders.put("doctorName", doctorName);

                replacePlaceholders(doc, placeholders);
                replaceTablePlaceholders(doc, null, null, drugs, totalPrice, formattedDate, null, doctorName, diagnose, advice);

                try (FileOutputStream fos = new FileOutputStream(destFileName)) {
                    doc.write(fos);
                    LOGGER.info("Prescription document successfully written to " + destFileName);
                }

                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(new File(destFileName));
                    } catch (IOException ex) {
                        LOGGER.severe("Error opening prescription document: " + ex.getMessage());
                        alert.errorMessage("Failed to open prescription document: " + ex.getMessage());
                    }
                }
                alert.successMessage("Prescription document successfully exported to " + destFileName);

                // Update Prescription_Status to "Paid" in the database after document creation
                PreparedStatement psUpdate = connect.prepareStatement(updatePrescriptionStatusSQL);
                psUpdate.setString(1, "Paid");
                psUpdate.setString(2, selectedAppointment.getId());
                psUpdate.executeUpdate();

                // Refresh the table to reflect the updated status
                selectedAppointment.setPrescriptionStatus("Paid");
                appointments_tableView.refresh();
            }
        } catch (SQLException ex) {
            LOGGER.severe("Database error: " + ex.getMessage());
            alert.errorMessage("Database error: " + ex.getMessage());
        } catch (IOException ex) {
            LOGGER.severe("Unexpected error: " + ex.getMessage());
            alert.errorMessage("Unexpected error: " + ex.getMessage());
            throw new RuntimeException("Error generating prescription document", ex);
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException ex) {
                LOGGER.severe("Error closing connection: " + ex.getMessage());
            }
        }
    }


	public void appointmentSelect() {
		AppointmentData appointmentData = appointments_tableView.getSelectionModel().getSelectedItem();
		int index = appointments_tableView.getSelectionModel().getSelectedIndex();
		if (index <= -1 || appointmentData == null) {
			System.out.println("No appointment selected");
			return;
		}

		loadComboBoxDoctor(appointmentData.getServiceId(), appointment_doctor);
		for (DoctorData doc : appointment_doctor.getItems()) {
			if (doc.getId().equals(appointmentData.getDoctorId())) {
				appointment_doctor.getSelectionModel().select(doc);
				break;
			}
		}
		appointment_serviceName.setText(appointmentData.getServiceName());
		appointment_mobileNumber.setText(appointmentData.getContactNumber());

		appointment_status.setValue(appointmentData.getStatus());
		appointment_cancelReason.setText(appointmentData.getCancelReason());

		for (PatientData patient : appointment_patient.getItems()) {
			if (patient.getPatientId().equals(appointmentData.getPatientId())) {
				appointment_patient.getSelectionModel().select(patient);
				break;
			}
		}

		appointment_date.setValue(appointmentData.getLocalDate());

		spHour1.getValueFactory().setValue(appointmentData.getLocalTime().getHour());
		spMinute1.getValueFactory().setValue(appointmentData.getLocalTime().getMinute());

		appointment_createdDate.setText(FormatterUtils.formatTime(appointmentData.getCreateDate().toString()));
		appointment_updatedDate.setText(FormatterUtils.formatTime(appointmentData.getUpdateDate().toString()));
	}

	public void appointmentDeleteBtn() {
		AppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();
		String appointmentID = "";
		if (selectedAppointment != null) {
			appointmentID = selectedAppointment.getId();
		} else {
			alert.errorMessage("Please select appointment to update");
		}
		System.out.println("Appointment ID: " + appointmentID);

		if (appointmentID.isEmpty()) {
			alert.errorMessage("Please select an appointment to delete.");
			return;
		}

		String sql = "DELETE FROM appointment WHERE id = ?";
		connect = Database.connectDB();
		try {
			prepare = connect.prepareStatement(sql);
			prepare.setString(1, appointmentID);

			int rowsDeleted = prepare.executeUpdate();
			if (rowsDeleted > 0) {
				alert.successMessage("Appointment deleted successfully.");
				appointmentClearBtn();
				loadAppointmentData();
			} else {
				alert.errorMessage("No appointment found with ID: " + appointmentID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			alert.errorMessage("Error deleting appointment: " + e.getMessage());
		}
	}

	public void appointmentClearBtn() {

		appointment_doctor.getSelectionModel().clearSelection();
		appointment_serviceName.clear();
		appointment_mobileNumber.clear();
		appointment_patient.getSelectionModel().clearSelection();
		appointment_status.getSelectionModel().clearSelection();
		appointment_status.setValue("");
		appointment_cancelReason.clear();
		appointment_date.getEditor().clear();
		spMinute1.getValueFactory().setValue(null); 
		spHour1.getValueFactory().setValue(null); 

		appointment_createdDate.setText("");
		appointment_updatedDate.setText("");
	}

	/*
	 * =====================FORMAT AND
	 * INTIALIZE========================================
	 */
	@FXML
	private void switchForm(ActionEvent event) {

		if (event.getSource() == dashboard_btn) {
			showForm("dashboard");
		} else if (event.getSource() == patients_btn) {
			showForm("patients");
		} else if (event.getSource() == drugs_btn) {
			showForm("drugs");
		} else if (event.getSource() == appointments_btn) {
			showForm("appointments");
		} else if (event.getSource() == appointments_manage_btn) {
			showForm("appointments_manage");
		} else if (event.getSource() == profile_btn) {
			showForm("profile");
		}
	}

	private void showForm(String formName) {
		home_form.setVisible(false);
		drugs_form.setVisible(false);
		patients_form.setVisible(false);
		appointments_form.setVisible(false);
		appointments_manage_form.setVisible(false);
		profile_form.setVisible(false);

		switch (formName) {
		case "dashboard":
			home_form.setVisible(true);
			current_form.setText("Home Form");
			break;
		case "drugs":
			drugs_form.setVisible(true);
			current_form.setText("Drugs Form");
			loadDrugTable();
			break;
		case "patients":
			patients_form.setVisible(true);
			current_form.setText("Patients Form");
			loadPatientTable();
			break;
		case "appointments":
			appointments_form.setVisible(true);
			current_form.setText("Appointments Form");
			break;
		case "appointments_manage":
			loadAppointmentData();
			appointments_manage_form.setVisible(true);
			current_form.setText("Appointments Manage Form");
			break;
		case "profile":
			profile_form.setVisible(true);
			current_form.setText("Profile Form");
			break;
		}
	}

	public void loadComboBox() {
		gender_cb.setItems(FXCollections
				.observableArrayList(Arrays.stream(Gender.values()).map(Enum::name).collect(Collectors.toList())));
	}

	public void runTime() {

		new Thread() {

			public void run() {
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
				while (true) {
					try {

						Thread.sleep(1000); // 1000 ms = 1s

					} catch (Exception e) {
						e.printStackTrace();
					}

					Platform.runLater(() -> {
						date_time.setText(format.format(new Date()));
					});
				}
			}
		}.start();
	}

	@FXML
	void logoutBtn(ActionEvent event) {

		try {
			if (alert.confirmationMessage("Are you sure you want to logout?")) {
				Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
				Stage stage = new Stage();
				stage.setScene(new Scene(root));
				stage.setTitle("Login");
				stage.show();

				logout_btn.getScene().getWindow().hide();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		runTime();
		loadComboBox();
		loadComboBoxPatient();
		loadComboBoxService();
		loadUrgencyLevels();
		loadAppointmentData();
		SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 17, 8);
		spHour.setValueFactory(hourFactory);

		SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, 0, 15);
		spMinute.setValueFactory(minuteFactory);

		SpinnerValueFactory<Integer> hourFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 17, 8);
		spHour1.setValueFactory(hourFactory1);

		SpinnerValueFactory<Integer> minuteFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, 0, 15);
		spMinute1.setValueFactory(minuteFactory1);
		initializeDrugFilters();
		initializePatientFilters();

		showForm("dashboard");

		appointments_col_service.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
		appointments_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
		appointments_col_name.setCellValueFactory(new PropertyValueFactory<>("patientName"));
		appointments_col_doctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
		appointments_col_contactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
		appointments_col_reason.setCellValueFactory(new PropertyValueFactory<>("cancelReason"));
		appointments_col_prescription.setCellValueFactory(new PropertyValueFactory<>("prescriptionStatus"));

		appointments_tableView.setItems(appoinmentListData);
		appointment_status.setItems(FXCollections.observableArrayList(
				Arrays.stream(AppointmentStatus.values()).map(Enum::name).collect(Collectors.toList())));

	}

	public static String formatCurrencyVND(BigDecimal amount) {
		if (amount == null)
			return "0 VNĐ";
		NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
		return formatter.format(amount) + " VNĐ";
	}

}