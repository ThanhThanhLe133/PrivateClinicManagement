package Controller;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Alert.AlertMessage;
import DAO.Database;
import Enum.Gender;
import Enum.User;
import Model.Data;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;

// MainFormController

import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;
import Model.DoctorData;
import Model.DrugData;
import Model.PrescriptionDetailsData;
import Model.ReceptionistData;
import Model.RevenueDrug;
import Model.RevenueService;
import Model.ServiceData;

public class AdminMainFormController {

	/* =====================CRUD DOCTOR======================================== */

	@FXML
	private TableView<DoctorData> doctors_tableView;
	@FXML
	private TableColumn<DoctorData, String> doctors_col_doctorID;
	@FXML
	private TableColumn<DoctorData, String> doctors_col_name;
	@FXML
	private TableColumn<DoctorData, String> doctors_col_gender;
	@FXML
	private TableColumn<DoctorData, String> doctors_col_contactNumber;
	@FXML
	private TableColumn<DoctorData, String> doctors_col_email;
	@FXML
	private TableColumn<DoctorData, String> doctors_col_specialization;
	@FXML
	private TableColumn<DoctorData, String> doctors_col_address;
	@FXML
	private TableColumn<DoctorData, String> doctors_col_confirm;
	@FXML
	private TableColumn<DoctorData, Void> doctors_col_action;

	/*
	 * =====================CRUD
	 * RECEPTIONIST========================================
	 */

	@FXML
	private TableView<ReceptionistData> receptionist_tableView;
	@FXML
	private TableColumn<ReceptionistData, String> receptionist_col_id;
	@FXML
	private TableColumn<ReceptionistData, String> receptionist_col_name;
	@FXML
	private TableColumn<ReceptionistData, String> receptionist_col_gender;
	@FXML
	private TableColumn<ReceptionistData, String> receptionist_col_phone;
	@FXML
	private TableColumn<ReceptionistData, String> receptionist_col_email;
	@FXML
	private TableColumn<ReceptionistData, String> receptionist_col_address;
	@FXML
	private TableColumn<ReceptionistData, Void> receptionist_col_action;

	/* =====================CRUD SERVICE======================================== */
	@FXML
	private TableView<ServiceData> service_tableView;
	@FXML
	private TableColumn<ServiceData, String> service_col_name;
	@FXML
	private TableColumn<ServiceData, String> service_col_type;
	@FXML
	private TableColumn<ServiceData, String> service_col_price;
	@FXML
	private TableColumn<ServiceData, Void> service_col_action;

	/*
	 * =====================CRUD REVENUE REPORT===============================
	 */
	@FXML
	private AnchorPane report_form;
	@FXML
	private DatePicker revenue_startDate, revenue_endDate;
	@FXML
	private Label revenue_totalRevenue, revenue_examineFees, revenue_medicationFees, revenue_labTestFees;

	@FXML
	private TableView<RevenueDrug> revenue_drug;
	@FXML
	private TableColumn<RevenueDrug, String> revenue_col_drugName;
	@FXML
	private TableColumn<RevenueDrug, Integer> revenue_col_drugQuantity;
	@FXML
	private TableColumn<RevenueDrug, String> revenue_col_drugPrice;
	@FXML
	private TableColumn<RevenueDrug, String> revenue_col_drugRevenue;

	@FXML
	private TableView<RevenueService> revenue_service;
	@FXML
	private TableColumn<RevenueService, String> revenue_col_serviceName;
	@FXML
	private TableColumn<RevenueService, String> revenue_col_servicePrice;
	@FXML
	private TableColumn<RevenueService, String> revenue_col_serviceType;
	@FXML
	private TableColumn<RevenueService, String> revenue_col_serviceQuantity;
	@FXML
	private TableColumn<RevenueService, String> revenue_col_serviceRevenue;

	@FXML
	private AnchorPane main_form;
	private String currentUserId; // Lưu ID của user đang đăng nhập
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
	// Top panel
	@FXML
	private Label top_username, current_form, date_time;

	@FXML
	private Circle top_profile;

	@FXML
	private Button logout_btn, revenue_resetBtn, revenue_exportBtn, revenue_filterBtn;

	// Left panel
	@FXML
	private Label name_adminDB, username_adminDB;

	@FXML
	private Button dashboard_btn, doctors_btn, receptionist_btn, service_btn, report_btn, profile_btn;

	// Forms
	@FXML
	private AnchorPane dashboard_form, doctors_form, receptionist_form, service_form, profile_form;

	@FXML
	private Circle profile_circle;
	@FXML
	private TextField txt_name_admin, txt_username_admin, txt_email_admin;

	@FXML
	private Button add_service_btn;
	@FXML
	private Button profile_importBtn, profile_updateBtn;

	@FXML
	private ComboBox<String> filterTypeComboBox;
	@FXML
	private DatePicker filterDatePicker;
	@FXML
	private Label totalRevenueLabel, totalCostLabel, netProfitLabel;

	@FXML
	private Label name_admin, username_admin, email_admin, gender_admin, createdDate_admin;

	@FXML
	private ComboBox<String> gender_cb;
	// DATABASE TOOLS
	private Connection connect;
	private PreparedStatement prepare;
	private Statement statement;
	private ResultSet result;

	private AlertMessage alert = new AlertMessage();

	private Image image;

	// load data admin
	private String username;

	public void setUsername(String username) {
		this.username = username;
		loadAdminProfile();
		profileDisplayImages();
	}

	// Simple TableRow class nội bộ (không cần model file riêng)
	public static class TableRow {
		private final String id;
		private final String name;
		private final String role;
		private final String totalsalary;
		private final String salary;

		public TableRow(String id, String name, String role, String totalsalary, String salary) {
			this.id = id;
			this.name = name;
			this.role = role;
			this.totalsalary = totalsalary;
			this.salary = salary;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getRole() {
			return role;
		}

		public String getTotalsalary() {
			return totalsalary;
		}

		public String getSalary() {
			return salary;
		}
	}

	@FXML
	private void handleCalculateRevenue() {
		String type = filterTypeComboBox.getValue();
		if (type == null || filterDatePicker.getValue() == null) {
			System.out.println("Please select filter type and date!");
			return;
		}

		double totalRevenue = 12000.0;
		double totalCost = 5000.0;
		double netProfit = totalRevenue - totalCost;

		totalRevenueLabel.setText("$" + totalRevenue);
		totalCostLabel.setText("$" + totalCost);
		netProfitLabel.setText("$" + netProfit);

		System.out.println("Revenue calculated for " + type + ": " + filterDatePicker.getValue());
	}

	// CRUD ADMINFORMCONTROLLER code

	// =======================CRUD Doctor==================================

	private void loadDoctorTable() {
		ObservableList<DoctorData> list = FXCollections.observableArrayList();

		try {
			Connection conn = Database.connectDB();
			String sql = "SELECT u.Id AS doctorId, u.Username, u.Name,u.Is_active, u.Email, u.Gender, u.Password, u.Avatar, "
					+ "d.Phone, d.Address, d.Is_confirmed, s.Name AS ServiceName " + "FROM DOCTOR d "
					+ "JOIN USER_ACCOUNT u ON d.Doctor_id = u.Id " + "JOIN SERVICE s ON s.Id = d.Service_id";

			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				list.add(new DoctorData(rs.getString("doctorId"), rs.getString("Username"), rs.getString("Password"),
						rs.getString("Name"), rs.getString("Email"), rs.getString("Gender"), rs.getBoolean("Is_active"),
						rs.getString("Phone"), rs.getString("ServiceName"), rs.getString("Address"),
						rs.getBoolean("Is_confirmed")));
			}

			// Cột dữ liệu
			doctors_col_doctorID.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
			doctors_col_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
			doctors_col_gender.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));
			doctors_col_contactNumber.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
			doctors_col_email.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
			doctors_col_specialization
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getServiceName()));
			doctors_col_address.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));

			// Cột hành động
			doctors_col_action.setCellFactory(col -> new TableCell<>() {
				private final Button editBtn = new Button("Update");
				private final Button deleteBtn = new Button("Delete");
				private final HBox hbox = new HBox(5, editBtn, deleteBtn);

				{
					editBtn.setOnAction(e -> {
						DoctorData doctor = getTableView().getItems().get(getIndex());
						openEditDoctorForm(doctor);
					});

					deleteBtn.setOnAction(e -> {
						DoctorData doctor = getTableView().getItems().get(getIndex());
						deleteDoctor(doctor.getId());
					});
				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					setGraphic(empty ? null : hbox);
				}
			});

			doctors_tableView.setItems(list);
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteDoctor(String doctorId) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText("Are you sure you want to delete this receptionist?");
		alert.setContentText("This action cannot be undone.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				Connection conn = Database.connectDB();

				// Xoá bác sĩ sẽ tự động xoá trong bảng DOCTOR nhờ ON DELETE CASCADE
				String sql = "DELETE FROM USER_ACCOUNT WHERE Id = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, doctorId);
				ps.executeUpdate();

				conn.close();
				loadDoctorTable();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void openEditDoctorForm(DoctorData doctor) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditDoctorForm.fxml"));
			Parent root = loader.load();

			EditDoctorFormController controller = loader.getController();
			controller.setDoctorData(doctor);

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Update Doctor");
			stage.setScene(new Scene(root));

			stage.setOnHidden(e -> loadDoctorTable());
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("ERROR : " + e.getMessage());
		}
	}

	@FXML
	private void openAddDoctorForm() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddDoctorForm.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Add New Doctor");
			stage.setScene(new Scene(root));

			stage.setOnHidden(e -> loadDoctorTable());
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// =============================CRUD
	// RECEPTIONIST=====================================

	private void loadReceptionistTable() {
		ObservableList<ReceptionistData> list = FXCollections.observableArrayList();

		try (Connection conn = Database.connectDB()) {
			String sql = "SELECT u.Id AS receptionist_id, u.Username, u.Password, u.Name, u.Email, u.Gender, u.Is_active,"
					+ " r.Phone, r.Address, r.Is_confirmed "
					+ "FROM RECEPTIONIST r JOIN USER_ACCOUNT u ON r.Receptionist_id = u.Id";

			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				list.add(new ReceptionistData(rs.getString("receptionist_id"), rs.getString("username"),
						rs.getString("password"), rs.getString("name"), rs.getString("email"), rs.getString("gender"),
						rs.getBoolean("is_active"), rs.getString("phone"), rs.getString("address"),
						rs.getBoolean("is_confirmed")));
			}

			receptionist_col_id.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
			receptionist_col_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
			receptionist_col_gender.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));
			receptionist_col_phone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
			receptionist_col_email.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

			receptionist_col_address
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));

			receptionist_col_action.setCellFactory(col -> new TableCell<>() {
				private final Button editBtn = new Button("Update");
				private final Button deleteBtn = new Button("Delete");
				private final HBox hbox = new HBox(5, editBtn, deleteBtn);

				{
					editBtn.setOnAction(e -> {
						ReceptionistData selected = getTableView().getItems().get(getIndex());
						openEditReceptionistForm(selected);
					});

					deleteBtn.setOnAction(e -> {
						ReceptionistData selected = getTableView().getItems().get(getIndex());
						deleteReceptionist(selected.getId());
						loadReceptionistTable();
					});
				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					setGraphic(empty ? null : hbox);
				}
			});

			receptionist_tableView.setItems(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteReceptionist(String receptionistId) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText("Are you sure you want to delete this receptionist?");
		alert.setContentText("This action cannot be undone.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try (Connection conn = Database.connectDB()) {
				String sql = "DELETE FROM USER_ACCOUNT WHERE Id = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, receptionistId);
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void openEditReceptionistForm(ReceptionistData data) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditReceptionistForm.fxml"));
			Parent root = loader.load();

			// Lấy controller và truyền dữ liệu
			EditReceptionistFormController controller = loader.getController();
			controller.setReceptionistData(data.getId(), data.getName(), data.getEmail(), data.getGender(),
					data.getPhone(), data.getAddress());

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Update Receptionist");
			stage.setScene(new Scene(root));

			// Reload lại bảng khi đóng form
			stage.setOnHidden(e -> loadReceptionistTable());
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openAddReceptionistForm() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddReceptionistForm.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Add Receptionist");
			stage.setScene(new Scene(root));

			// Refresh lại bảng sau khi đóng form
			stage.setOnHidden(e -> loadReceptionistTable());

			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// =======================CRUD Service==================================

	private void loadServiceTable() {
		ObservableList<ServiceData> list = FXCollections.observableArrayList();

		try {
			Connection conn = Database.connectDB();
			String sql = "SELECT Id, Name, Type, Price FROM SERVICE ";

			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				list.add(new ServiceData(rs.getString("Id"), rs.getString("Name"), rs.getBigDecimal("Price"),
						rs.getString("Type")));
			}

			// Cột dữ liệu
			service_col_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
			service_col_type.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
			service_col_price
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedPrice()));

			// Cột hành động
			service_col_action.setCellFactory(col -> new TableCell<>() {
				private final Button editBtn = new Button("Update");
				private final Button deleteBtn = new Button("Delete");
				private final HBox hbox = new HBox(5, editBtn, deleteBtn);
				{
					editBtn.setOnAction(e -> {
						ServiceData service = getTableView().getItems().get(getIndex());
						openEditService(service);
					});

					deleteBtn.setOnAction(e -> {
						ServiceData service = getTableView().getItems().get(getIndex());
						deleteService(service.getServiceId());
					});
				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					setGraphic(empty ? null : hbox);
				}
			});

			service_tableView.setItems(list);
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Error loading drug list!");
			alert.showAndWait();
		}
	}

	private void deleteService(String serviceId) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText("Are you sure you want to delete this service?");
		alert.setContentText("This action cannot be undone.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				Connection conn = Database.connectDB();

				String sql = "DELETE FROM SERVICE WHERE Id = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, serviceId);
				ps.executeUpdate();

				conn.close();
				loadServiceTable(); // Refresh lại bảng sau khi xoá
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void openEditService(ServiceData service) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditServiceForm.fxml"));
			Parent root = loader.load();

			EditServiceFormController controller = loader.getController();
			controller.setServiceData(service); // Truyền dữ liệu sang form chỉnh sửa

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Update Service");
			stage.setScene(new Scene(root));

			stage.setOnHidden(e -> loadServiceTable()); // Tải lại bảng khi form đóng
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("ERROR : " + e.getMessage());
		}
	}

	@FXML
	private void openAddServiceForm() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddServiceForm.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Add Service");
			stage.setScene(new Scene(root));

			stage.setOnHidden(e -> loadServiceTable()); // Refresh lại bảng khi thêm thuốc
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* =====================LOAD REVENUE======================================= */
	BigDecimal totalRevenue;
	BigDecimal examineFees;
	BigDecimal medicationFees;
	BigDecimal labTestFees;
	BigDecimal serviceFees;
	List<Map<String, String>> drugRevenueList = new ArrayList<>();
	List<Map<String, String>> serviceRevenueList = new ArrayList<>();
	List<Map<String, String>> revenueListSortByDate = new ArrayList<>();
	int i;

	@FXML
	private void resetRevenueFilter() {
		totalRevenue = BigDecimal.ZERO;
		examineFees = BigDecimal.ZERO;
		medicationFees = BigDecimal.ZERO;
		labTestFees = BigDecimal.ZERO;
		serviceFees = BigDecimal.ZERO;
		revenue_startDate.setValue(null);
		revenue_endDate.setValue(null);
		setRevenue();
	}

	private void setRevenue() {
		revenue_totalRevenue.setText(FormatterUtils.formatCurrencyVND(totalRevenue));
		revenue_examineFees.setText(FormatterUtils.formatCurrencyVND(examineFees));
		revenue_medicationFees.setText(FormatterUtils.formatCurrencyVND(medicationFees));
		revenue_labTestFees.setText(FormatterUtils.formatCurrencyVND(labTestFees));
	}

	private void checkValidDate(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null) {
			alert.errorMessage("Please select both start date and end date.");
			return;
		}

		else if (startDate.isAfter(endDate)) {
			alert.errorMessage("Start date cannot be after end date.");
			return;
		} else if (endDate.isAfter(LocalDate.now())) {
			alert.errorMessage("End date has not come yet.");
			return;
		}
	}

	@FXML
	private void exportRevenueReport(ActionEvent event) throws IOException {
		totalRevenue = BigDecimal.ZERO;
		examineFees = BigDecimal.ZERO;
		medicationFees = BigDecimal.ZERO;
		labTestFees = BigDecimal.ZERO;
		serviceFees = BigDecimal.ZERO;
		LocalDate startDate = revenue_startDate.getValue();
		LocalDate endDate = revenue_endDate.getValue();
		checkValidDate(startDate, endDate);

		handleRevenueDrugReport(startDate, endDate);
		handleRevenueServiceReport(startDate, endDate);
		totalRevenue = examineFees.add(medicationFees).add(labTestFees);
		serviceFees = examineFees.add(labTestFees);
		handleDateReport(startDate, endDate);

		File sourceFile = new File("Word/REPORT.docx");
		String destFileName = String.format("Word/REPORT_from_%s_to_%s.docx", startDate.format(formatter).toString(),
				endDate.format(formatter));
		Path destPath = Paths.get(destFileName);

		if (Files.exists(destPath)) {
			Files.delete(destPath); // Nếu tệp đích đã tồn tại, xóa
		}
		File destFile = new File(destFileName);

		// Copy file gốc sang file đích
		Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		try (XWPFDocument doc = new XWPFDocument(new FileInputStream(destFile))) {
			for (XWPFParagraph para : doc.getParagraphs()) {
				StringBuilder fullText = new StringBuilder();
				List<XWPFRun> runs = para.getRuns();

				for (XWPFRun run : runs) {
					String text = run.getText(0);
					if (text != null) {
						fullText.append(text);
					}
				}

				String replacedText = fullText.toString().replace("{{Start Date}}", startDate.format(formatter))
						.replace("{{End Date}}", endDate.format(formatter));

				if (!fullText.toString().equals(replacedText)) {
					for (int i = runs.size() - 1; i >= 0; i--) {
						para.removeRun(i);
					}
					XWPFRun newRun = para.createRun();
					newRun.setText(replacedText);
				}
			}
			replaceAllPlaceholders(doc, drugRevenueList, serviceRevenueList, revenueListSortByDate, totalRevenue,
					medicationFees, serviceFees);

			// Ghi lại file đã chỉnh sửa
			try (FileOutputStream fos = new FileOutputStream(destFile)) {
				doc.write(fos);
			}

			// Mở file
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(destFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void replaceTablePlaceholderRows(XWPFTable table, String placeholderKey, List<String> columns,
			List<Map<String, String>> dataList, int rowIndex) {
		if (dataList == null||dataList.isEmpty()) {
			dataList = new ArrayList<>();
			Map<String, String> emptyData = new HashMap<>();
			
			for (String column : columns) {
				emptyData.put(column, "");  
			}
			dataList.add(emptyData); 
		}

		XWPFTableRow row = table.getRow(rowIndex);

		for (XWPFTableCell cell : row.getTableCells()) {
			String text = cell.getText();
			if (text != null && text.contains(placeholderKey)) {
				table.removeRow(rowIndex);
				for (Map<String, String> data : dataList) {
					XWPFTableRow newRow = table.insertNewTableRow(rowIndex++);
					for (String col : columns) {
						String value = data.get(col);
						
						if (value == null || value.trim().isEmpty()) {
							value = "";
						}
						newRow.addNewTableCell().setText(value);
					}
					rowIndex++;
				}
				return;
			}
		}
	}

	public static void replaceAllPlaceholders(XWPFDocument doc, List<Map<String, String>> drugRevenueList,
			List<Map<String, String>> serviceRevenueList, List<Map<String, String>> revenueListSortByDate,
			BigDecimal totalRevenue, BigDecimal medicationFees, BigDecimal serviceFees) throws IOException {
		for (XWPFTable table : doc.getTables()) {
			for (int i = 0; i < table.getRows().size(); i++) {
				XWPFTableRow row = table.getRow(i);

				for (XWPFTableCell cell : row.getTableCells()) {
					try {
						for (XWPFParagraph para : cell.getParagraphs()) {
							String text = para.getText();

							if (text == null)
								continue;
							if (text != null) {
								replaceInParagraph(para, "{{totalDrugRevenue}}",
										FormatterUtils.formatCurrencyVND(medicationFees));
								replaceInParagraph(para, "{{totalServiceRevenue}}",
										FormatterUtils.formatCurrencyVND(serviceFees));
								replaceInParagraph(para, "{{grandTotal}}",
										FormatterUtils.formatCurrencyVND(totalRevenue));
								if (text.contains("{{noDrug}}")) {
									replaceTablePlaceholderRows(table, "{{noDrug}}",
											List.of("noDrug", "drugName", "quantity", "DrugPrice", "totalDrug"),
											drugRevenueList, i);
									break;
								}
								if (text.contains("{{noService}}")) {
									replaceTablePlaceholderRows(table, "{{noService}}",
											List.of("noService", "serviceName", "type", "ServicePrice", "totalService"),
											serviceRevenueList, i);
									break;
								}
								if (text.contains("{{noTotal}}")) {
									replaceTablePlaceholderRows(table, "{{noTotal}}",
											List.of("date", "totalDrug", "totalService", "totalRevenue"),
											revenueListSortByDate, i);
									break;
								}

							}
						}
					} catch (Exception e) {
						System.err.println("⚠️ Lỗi khi đọc nội dung ô: " + e.getMessage());
					}
				}
			}
		}
	}

	private static void replaceInParagraph(XWPFParagraph paragraph, String placeholder, String replacement) {
		for (XWPFRun run : paragraph.getRuns()) {
			String text = run.getText(0);
			if (text != null && text.contains(placeholder)) {
				String replaced = text.replace(placeholder, replacement);
				run.setText(replaced, 0);
				return;
			}
		}
	}

	private void handleDateReport(LocalDate startDate, LocalDate endDate) {

		try (Connection conn = Database.connectDB()) {
			String sql = "SELECT DATE(pd.Create_date) AS CreateDate, " + "SUM(d.Price) AS TotalDrugRevenue, "
					+ "SUM(s.Price) AS TotalServiceRevenue, " + "SUM(d.Price + s.Price) AS GrandTotal "
					+ "FROM PRESCRIPTION_DETAILS pd " + "JOIN APPOINTMENT p ON p.Create_date = pd.Create_date "
					+ "JOIN DRUG d ON d.id=pd.Drug_id " + "JOIN DOCTOR doc ON doc.Doctor_id = p.Doctor_id "
					+ "JOIN SERVICE s ON s.Id = doc.Service_id " + "WHERE DATE(pd.Create_date) BETWEEN ? AND ? "
					+ "GROUP BY DATE(pd.Create_date) " + "ORDER BY DATE(pd.Create_date) DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, java.sql.Date.valueOf(startDate));
			ps.setDate(2, java.sql.Date.valueOf(endDate));

			ResultSet rs = ps.executeQuery();
			i = 1;
			revenueListSortByDate.clear();
			while (rs.next()) {
				String createDate = rs.getString("CreateDate");
				BigDecimal totalDrugRevenue = rs.getBigDecimal("TotalDrugRevenue");
				BigDecimal totalServiceRevenue = rs.getBigDecimal("TotalServiceRevenue");
				BigDecimal grandTotal = rs.getBigDecimal("GrandTotal");

				medicationFees = medicationFees.add(totalDrugRevenue);
				serviceFees = serviceFees.add(totalServiceRevenue);

				revenueListSortByDate.add(Map.of("noTotal", String.valueOf(i), "CreateDate",
						FormatterUtils.formatTimestamp(createDate.toString()), "totalDrugRevenue",
						totalDrugRevenue.toString(), "totalServiceRevenue", totalServiceRevenue.toString(),
						"grandTotal", grandTotal.toString()));

				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Alert error = new Alert(Alert.AlertType.ERROR, "Failed to load revenue report: " + e.getMessage());
			error.showAndWait();
		}
	}

	@FXML
	private void filterRevenueData() {
		totalRevenue = BigDecimal.ZERO;
		examineFees = BigDecimal.ZERO;
		medicationFees = BigDecimal.ZERO;
		labTestFees = BigDecimal.ZERO;
		serviceFees = BigDecimal.ZERO;
		LocalDate startDate = revenue_startDate.getValue();
		LocalDate endDate = revenue_endDate.getValue();

		checkValidDate(startDate, endDate);

		handleRevenueDrugReport(startDate, endDate);
		handleRevenueServiceReport(startDate, endDate);
		totalRevenue = examineFees.add(medicationFees).add(labTestFees);
		setRevenue();
	}

	private void handleRevenueDrugReport(LocalDate startDate, LocalDate endDate) {

		try (Connection conn = Database.connectDB()) {
			String sql = "SELECT d.Name AS DrugName, SUM(pd.Quantity) AS quantity, d.Price, SUM(d.Price) AS TotalRevenue "
					+ "FROM PRESCRIPTION_DETAILS pd " + "JOIN DRUG d ON pd.Drug_id = d.Id "
					+ "WHERE DATE(pd.Create_date) BETWEEN ? AND ? " + "GROUP BY d.Name, d.Price "
					+ "ORDER BY DATE(pd.Create_date) DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, java.sql.Date.valueOf(startDate));
			ps.setDate(2, java.sql.Date.valueOf(endDate));

			ResultSet rs = ps.executeQuery();
			ObservableList<RevenueDrug> dataList = FXCollections.observableArrayList();
			i = 1;
			drugRevenueList.clear();
			while (rs.next()) {
				String drugName = rs.getString("DrugName");
				int quantity = rs.getInt("quantity");
				BigDecimal price = rs.getBigDecimal("Price");
				BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");
				medicationFees = medicationFees.add(totalRevenue);
				dataList.add(new RevenueDrug(drugName, quantity, price, totalRevenue));

				drugRevenueList.add(Map.of("noDrug", String.valueOf(i), "drugName", drugName, "quantity",
						String.valueOf(quantity), "DrugPrice", price.toString(), "totalDrug", totalRevenue.toString()));

				i++;
			}

			revenue_col_drugName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDrugName()));
			revenue_col_drugQuantity
					.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
			revenue_col_drugPrice
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedPrice()));
			revenue_col_drugRevenue
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedTotalRevenue()));

			revenue_drug.setItems(dataList);

		} catch (Exception e) {
			e.printStackTrace();
			Alert error = new Alert(Alert.AlertType.ERROR, "Failed to load revenue report: " + e.getMessage());
			error.showAndWait();
		}
	}

	private void handleRevenueServiceReport(LocalDate startDate, LocalDate endDate) {
		try (Connection conn = Database.connectDB()) {
			String sql = "SELECT s.Name AS ServiceName, COUNT(*) AS quantity, s.Price,s.Type, SUM(s.Price) AS TotalRevenue "
					+ "FROM SERVICE s " + "JOIN DOCTOR d ON d.Service_id = s.Id "
					+ "JOIN APPOINTMENT p ON p.Doctor_id = d.Doctor_id " + "WHERE DATE(p.Create_date) BETWEEN ? AND ? "
					+ "GROUP BY s.Name, s.Price" + " ORDER BY DATE(p.Create_date) DESC";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, java.sql.Date.valueOf(startDate));
			ps.setDate(2, java.sql.Date.valueOf(endDate));

			ResultSet rs = ps.executeQuery();
			ObservableList<RevenueService> dataList = FXCollections.observableArrayList();
			i = 1;
			serviceRevenueList.clear();
			while (rs.next()) {
				String serviceName = rs.getString("ServiceName");
				int quantity = rs.getInt("quantity");
				BigDecimal price = rs.getBigDecimal("Price");
				String type = rs.getString("Type");
				BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");

				switch (type.toLowerCase()) {
				case "examination":
					examineFees = examineFees.add(totalRevenue);
					break;
				case "test":
					labTestFees = labTestFees.add(totalRevenue);
					break;
				}

				dataList.add(new RevenueService(serviceName, type, quantity, price, totalRevenue));

				serviceRevenueList.add(Map.of("noService", String.valueOf(i), "serviceName", serviceName, "type", type,
						"ServicePrice", price.toString(), "totalService", totalRevenue.toString()));

				i++;
			}

			revenue_col_serviceName
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getServiceName()));
			revenue_col_serviceQuantity.setCellValueFactory(
					data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));
			revenue_col_servicePrice
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedPrice()));
			revenue_col_serviceRevenue
					.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedTotalRevenue()));
			revenue_col_serviceType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));

			revenue_service.setItems(dataList);

		} catch (Exception e) {
			e.printStackTrace();
			Alert error = new Alert(Alert.AlertType.ERROR, "Failed to load revenue report: " + e.getMessage());
			error.showAndWait();
		}
	}

	/* =====================LOAD PROFILE======================================== */
	private void loadAdminProfile() {
		String checkUserSQL = "SELECT id, name, email,username, gender, create_date  FROM user_account WHERE username = ?";
		Connection connect = Database.connectDB();

		try {
			PreparedStatement prepare = connect.prepareStatement(checkUserSQL);
			prepare.setString(1, username);

			ResultSet result = prepare.executeQuery();

			if (!result.next()) {
				alert.errorMessage("Username does not match data." + username);
				return;
			}
			this.currentUserId = result.getString("id"); // Lưu id để update sau

			String name = result.getString("name");
			String username = result.getString("username");
			String email = result.getString("email");
			String gender = result.getString("gender");

			String createdAt = result.getString("create_date");

			// Gán cho các Label
			name_adminDB.setText(name != null ? name : "UNKNOWN");
			username_admin.setText(username != null ? username : "");
			name_admin.setText(name != null ? name : "UNKNOWN");
			username_adminDB.setText(username != null ? username : "");
			email_admin.setText(email != null ? email : "");
			gender_admin.setText(gender != null ? gender : "");
			createdDate_admin.setText(createdAt != null ? FormatterUtils.formatTimestamp(createdAt) : "");

			// Gán cho các TextField (nếu có)
			txt_name_admin.setText(name != null ? name : "");
			txt_username_admin.setText(username != null ? username : "");
			txt_email_admin.setText(email != null ? email : "");
			gender_cb.setValue(gender != null ? gender : "");

			top_username.setText(name != null ? name : "UNKNOWN");
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

					Image img1 = new Image(imgStream1, 137, 95, true, true);
					profile_circle.setFill(new ImagePattern(img1));

					Image img2 = new Image(imgStream2, 1012, 22, true, true);

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
	@FXML
	public void profileUpdateBtn(ActionEvent event) {
		String name = txt_name_admin.getText().trim();
		String username = txt_username_admin.getText().trim();
		String gender = gender_cb.getValue();
		String email = txt_email_admin.getText().trim(); // Lấy từ TextField (nếu có)

		if (name.isEmpty() || username.isEmpty()) {
			alert.errorMessage("Please fill in all the fields.");
			return;
		} else if (gender == null || gender.isEmpty()) {
			alert.errorMessage("Please select a gender.");
			return;
		} else if (currentUserId == null || currentUserId.isEmpty()) {
			alert.errorMessage("User ID not found. Please reload the profile.");
			return;
		}

		String checkUsernameSQL = "SELECT * FROM user_account WHERE username = ? AND id <> ?";
		String updateUserSQL = "UPDATE user_account SET name = ?, username = ?, gender = ?, email = ? WHERE id = ?";

		try (Connection connect = Database.connectDB();
				PreparedStatement checkStmt = connect.prepareStatement(checkUsernameSQL);
				PreparedStatement updateStmt = connect.prepareStatement(updateUserSQL)) {

			checkStmt.setString(1, username);
			checkStmt.setString(2, currentUserId);
			ResultSet result = checkStmt.executeQuery();

			if (result.next()) {
				alert.errorMessage("Username '" + username + "' already exists!");
				return;
			}

			updateStmt.setString(1, name);
			updateStmt.setString(2, username);
			updateStmt.setString(3, gender);
			updateStmt.setString(4, email);
			updateStmt.setString(5, currentUserId);

			int rowsUpdated = updateStmt.executeUpdate();

			if (rowsUpdated > 0) {
				alert.successMessage("Profile updated successfully.");
				loadAdminProfile(); // Refresh
			} else {
				alert.errorMessage("No user found to update.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			alert.errorMessage("Error updating profile: " + e.getMessage());
		}
	}

	@FXML
	private void profileInsertImage(ActionEvent event) {
		FileChooser open = new FileChooser();
		open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*jpg", "*jpeg", "*png"));

		File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());

		if (file != null) {
			Data.path = file.getAbsolutePath();

			// Hiển thị ảnh lên UI
			image = new Image(file.toURI().toString(), 137, 95, false, true);
			profile_circle.setFill(new ImagePattern(image));

			// Lưu ảnh vào DB
			try {
				connect = Database.connectDB();
				String updateAvatarSQL = "UPDATE user_account SET Avatar = ? WHERE email = ?";

				FileInputStream input = new FileInputStream(file);
				prepare = connect.prepareStatement(updateAvatarSQL);
				prepare.setBinaryStream(1, input, (int) file.length());
				prepare.setString(2, email_admin.getText()); // email người đăng nhập

				int rows = prepare.executeUpdate();
				if (rows > 0) {
					alert.successMessage("Avatar updated successfully.");
				} else {
					alert.errorMessage("Failed to update Avatar.");
				}
				profileDisplayImages();
			} catch (Exception e) {
				e.printStackTrace();
				alert.errorMessage("Error uploading Avatar: " + e.getMessage());
			}
		}
	}

	/*
	 * =====================FORMAT AND
	 * INTIALIZE========================================
	 */
	public class FormatterUtils {
		public static String formatTimestamp(String createdAt) {
			try {
				SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
				return outputFormat.format(inputFormat.parse(createdAt));
			} catch (Exception e) {
				return "";
			}
		}

		public static String formatCurrencyVND(BigDecimal amount) {
			if (amount == null)
				return "0 VNĐ";
			NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
			return formatter.format(amount) + " VNĐ";
		}
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
	private void logoutBtn() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Login");
			stage.show();

			logout_btn.getScene().getWindow().hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void switchForm(ActionEvent event) {

		if (event.getSource() == dashboard_btn) {
			showForm("dashboard");
		} else if (event.getSource() == doctors_btn) {
			showForm("doctors");
		} else if (event.getSource() == receptionist_btn) {
			showForm("receptionists");
		} else if (event.getSource() == service_btn) {
			showForm("service");
		} else if (event.getSource() == profile_btn) {
			showForm("profile");
		} else if (event.getSource() == report_btn) {
			showForm("report");
		}

	}

	private void showForm(String formName) {
		dashboard_form.setVisible(false);
		doctors_form.setVisible(false);
		receptionist_form.setVisible(false);
		service_form.setVisible(false);
		profile_form.setVisible(false);
		report_form.setVisible(false);

		switch (formName) {
		case "dashboard":
			dashboard_form.setVisible(true);
			current_form.setText("Dashboard Form");
			break;
		case "doctors":
			doctors_form.setVisible(true);
			current_form.setText("Doctors Form");
			loadDoctorTable(); // Add this line
			break;
		case "receptionists":
			receptionist_form.setVisible(true);
			current_form.setText("Receptionists Form");
			loadReceptionistTable();
			break;
		case "service":
			service_form.setVisible(true);
			current_form.setText("Service Form");
			loadServiceTable();
			break;

		case "profile":
			profile_form.setVisible(true);
			current_form.setText("Profile Form");
			break;
		case "report":
			report_form.setVisible(true);
			current_form.setText("Report Form");
			resetRevenueFilter();
			break;

		}
	}

	public void loadComboBox() {
		gender_cb.setItems(FXCollections
				.observableArrayList(Arrays.stream(Gender.values()).map(Enum::name).collect(Collectors.toList())));
	}

	@FXML
	public void initialize() {
		runTime();
		loadComboBox();

		showForm("dashboard");

	}

}
