/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xwpf.usermodel.*;

import Model.AppointmentData;
import Model.PatientData;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

import Alert.AlertMessage;
import Controller.DoctorMainFormController.FormatterUtils;
import DAO.Database;
import Enum.AppointmentStatus;
import Enum.Gender;
import Enum.UrgencyLevel;
import Model.AppointmentSuggester;
import Model.Data;
import Model.DoctorData;
import Model.ServiceData;
import Model.DrugData;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
	// Các thành phần giao diện cho bảng thuốc
	@FXML
	private TableView<DrugData> drug_tableView; // Bảng hiển thị danh sách thuốc
	@FXML
	private TableColumn<DrugData, String> drug_col_id; // Cột ID thuốc
	@FXML
	private TableColumn<DrugData, String> drug_col_name; // Cột tên thuốc
	@FXML
	private TableColumn<DrugData, String> drug_col_manufacturer; // Cột nhà sản xuất
	@FXML
	private TableColumn<DrugData, String> drug_col_expiry; // Cột ngày hết hạn
	@FXML
	private TableColumn<DrugData, String> drug_col_unit; // Cột đơn vị
	@FXML
	private TableColumn<DrugData, String> drug_col_price; // Cột giá
	@FXML
	private TableColumn<DrugData, String> drug_col_stock; // Cột số lượng tồn kho
	@FXML
	private TableColumn<DrugData, String> drug_col_create; // Cột ngày tạo
	@FXML
	private TableColumn<DrugData, String> drug_col_update; // Cột ngày cập nhật
	@FXML
	private TableColumn<DrugData, Void> drug_col_action; // Cột hành động (sửa/xóa)

	// Các thành phần giao diện cho tìm kiếm và lọc thuốc
	@FXML
	private TextField txtDrugSearch; // Ô tìm kiếm thuốc
	@FXML
	private ComboBox<String> cmbDrugSearchBy; // Lựa chọn tiêu chí tìm kiếm
	@FXML
	private ComboBox<String> cmbDrugExpiryFilter; // Lọc theo ngày hết hạn
	@FXML
	private ComboBox<String> cmbDrugStockFilter; // Lọc theo trạng thái tồn kho
	@FXML
	private ComboBox<String> cmbDrugPriceSort; // Sắp xếp theo giá

	/* =====================CRUD PATIENT======================================== */
	// Các thành phần giao diện cho bảng bệnh nhân
	@FXML
	private TableView<PatientData> patients_tableView; // Bảng hiển thị danh sách bệnh nhân
	@FXML
	private TableColumn<PatientData, String> patients_col_patientID; // Cột ID bệnh nhân
	@FXML
	private TableColumn<PatientData, String> patients_col_name; // Cột tên bệnh nhân
	@FXML
	private TableColumn<PatientData, String> patients_col_email; // Cột email
	@FXML
	private TableColumn<PatientData, String> patients_col_gender; // Cột giới tính
	@FXML
	private TableColumn<PatientData, String> patients_col_phone; // Cột số điện thoại
	@FXML
	private TableColumn<PatientData, String> patients_col_address; // Cột địa chỉ
	@FXML
	private TableColumn<PatientData, String> patients_col_diagnosis; // Cột chẩn đoán
	@FXML
	private TableColumn<PatientData, BigDecimal> patients_col_height; // Cột chiều cao
	@FXML
	private TableColumn<PatientData, BigDecimal> patients_col_weight; // Cột cân nặng
	@FXML
	private TableColumn<PatientData, Timestamp> patients_col_create; // Cột ngày tạo
	@FXML
	private TableColumn<PatientData, Timestamp> patients_col_update; // Cột ngày cập nhật
	@FXML
	private TableColumn<PatientData, Void> patients_col_action; // Cột hành động (sửa/xóa/chi tiết)

	// Các thành phần giao diện cho tìm kiếm và lọc bệnh nhân
	@FXML
	private ComboBox<String> cmbPatientSearchBy; // Lựa chọn tiêu chí tìm kiếm
	@FXML
	private TextField txtPatientSearch; // Ô tìm kiếm bệnh nhân
	@FXML
	private ComboBox<String> cmbPatientGenderFilter; // Lọc theo giới tính

	// Các thành phần giao diện chính
	@FXML
	private AnchorPane main_form; // Form chính của giao diện

	@FXML
	private Circle top_profile; // Hình ảnh hồ sơ ở thanh trên cùng

	@FXML
	private Label top_username; // Tên người dùng ở thanh trên cùng

	@FXML
	private Label date_time; // Hiển thị ngày giờ hiện tại

	@FXML
	private Label current_form; // Hiển thị tên form hiện tại

	// Các nút điều hướng
	@FXML
	private Button logout_btn; // Nút đăng xuất
	@FXML
	private Button dashboard_btn; // Nút chuyển đến dashboard
	@FXML
	private Button patients_btn; // Nút chuyển đến form bệnh nhân
	@FXML
	private Button drugs_btn; // Nút chuyển đến form thuốc
	@FXML
	private Button appointments_btn; // Nút chuyển đến form lịch hẹn
	@FXML
	private Button appointments_manage_btn; // Nút chuyển đến form quản lý lịch hẹn
	@FXML
	private Button profile_btn; // Nút chuyển đến form hồ sơ

	// Các form giao diện
	@FXML
	private AnchorPane home_form; // Form dashboard
	@FXML
	private AreaChart home_chart_drugs, home_chart_patients; // Biểu đồ thuốc và bệnh nhân
	@FXML
	private BarChart home_chart_appointments; // Biểu đồ lịch hẹn
	@FXML
	private AnchorPane panel_total_appointments, panel_active_patients, panel_total_patients, panel_total_drugs; // Các panel hiển thị số liệu
	@FXML
	private Label label_total_appointments, label_active_patients, label_total_patients, label_total_drugs; // Nhãn hiển thị số liệu

	// Bảng hiển thị lịch hẹn ở dashboard
	@FXML
	private TableView<AppointmentData> home_appointment_tableView; // Bảng lịch hẹn
	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_patient; // Cột tên bệnh nhân
	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_date; // Cột ngày hẹn
	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_status; // Cột trạng thái
	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_appointmenID; // Cột ID lịch hẹn
	@FXML
	private TableColumn<AppointmentData, String> home_appointment_col_doctor; // Cột tên bác sĩ

	@FXML
	private AnchorPane patients_form; // Form quản lý bệnh nhân
	@FXML
	private AnchorPane appointments_form; // Form tạo lịch hẹn
	@FXML
	private AnchorPane choose_service_form; // Form chọn dịch vụ
	@FXML
	private VBox vboxContainer; // Container chứa các form dịch vụ

	// Các thành phần giao diện tạo lịch hẹn
	@FXML
	private ComboBox<DoctorData> cb_doctor; // ComboBox chọn bác sĩ
	@FXML
	private ComboBox<ServiceData> cb_service; // ComboBox chọn dịch vụ
	@FXML
	private ComboBox<PatientData> cb_patient; // ComboBox chọn bệnh nhân
	@FXML
	private ComboBox<UrgencyLevel> cb_urgency; // ComboBox chọn mức độ khẩn cấp
	@FXML
	private DatePicker select_time; // Lựa chọn ngày hẹn
	@FXML
	private CheckBox checkbox_followUp; // Checkbox kiểm tra tái khám
	@FXML
	private Label lb_patient_name; // Nhãn tên bệnh nhân
	@FXML
	private Label lb_patient_gender; // Nhãn giới tính bệnh nhân
	@FXML
	private Label lb_patient_age; // Nhãn tuổi bệnh nhân
	@FXML
	private Label lb_patient_address; // Nhãn địa chỉ bệnh nhân
	@FXML
	private Label lb_price_service, lb_check; // Nhãn giá dịch vụ và trạng thái kiểm tra
	@FXML
	private TextArea txt_suggest; // Khu vực gợi ý lịch hẹn
	@FXML
	private TextArea txt_details; // Khu vực chi tiết lịch hẹn
	@FXML
	private Pane btn_pane; // Panel chứa các nút
	@FXML
	private Button btn_remove; // Nút xóa form dịch vụ
	@FXML
	private Button btn_add; // Nút thêm form dịch vụ
	@FXML
	private Button btn_create; // Nút tạo lịch hẹn
	@FXML
	private Button bnt_suggest, bnt_reset; // Nút gợi ý và reset
	@FXML
	private Button btn_check; // Nút kiểm tra lịch hẹn

	@SuppressWarnings("rawtypes")
	@FXML
	private Spinner<Integer> spHour, spMinute, spHour1, spMinute1; // Các spinner chọn giờ và phút
	@FXML
	private Button btn_clear_suggest; // Nút xóa gợi ý

	// Form quản lý lịch hẹn
	@FXML
	private AnchorPane appointments_manage_form; // Form quản lý lịch hẹn
	@FXML
	private TableView<AppointmentData> appointments_tableView; // Bảng danh sách lịch hẹn
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_service; // Cột dịch vụ
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_time; // Cột thời gian
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_status; // Cột trạng thái
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_name, appointments_col_doctor; // Cột tên bệnh nhân và bác sĩ
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_contactNumber, appointments_col_prescription; // Cột số liên lạc và trạng thái đơn thuốc
	@FXML
	private TableColumn<AppointmentData, String> appointments_col_reason; // Cột lý do hủy
	ObservableList<AppointmentData> appoinmentListData = FXCollections.observableArrayList(); // Danh sách dữ liệu lịch hẹn
	@FXML
	private ComboBox<String> appointments_searchBy; // ComboBox chọn tiêu chí tìm kiếm
	@FXML
	private TextField appointments_searchQuery; // Ô tìm kiếm lịch hẹn

	// Các thành phần giao diện cập nhật lịch hẹn
	@FXML
	private TextField appointment_serviceName; // Ô tên dịch vụ
	@FXML
	private TextField appointment_mobileNumber; // Ô số điện thoại
	@FXML
	private ComboBox<PatientData> appointment_patient; // ComboBox chọn bệnh nhân
	@FXML
	private ComboBox<DoctorData> appointment_doctor; // ComboBox chọn bác sĩ
	@FXML
	private ComboBox<String> appointment_status; // ComboBox chọn trạng thái
	@FXML
	private TextArea appointment_cancelReason; // Khu vực lý do hủy
	@FXML
	private DatePicker appointment_date; // Lựa chọn ngày hẹn
	@FXML
	private TextField appointment_time; // Ô thời gian
	@FXML
	private Label appointment_createdDate; // Nhãn ngày tạo
	@FXML
	private Label appointment_updatedDate; // Nhãn ngày cập nhật

	// Các nút điều khiển lịch hẹn
	@FXML
	private Button appointment_insertBtn; // Nút thêm lịch hẹn
	@FXML
	private Button appointment_updateBtn; // Nút cập nhật lịch hẹn
	@FXML
	private Button appointment_clearBtn; // Nút xóa form
	@FXML
	private Button appointment_deleteBtn; // Nút xóa lịch hẹn

	@FXML
	private AnchorPane drugs_form; // Form quản lý thuốc
	@FXML
	private AnchorPane profile_form; // Form hồ sơ

	// Các thành phần giao diện hồ sơ
	@FXML
	private Circle profile_circle; // Hình ảnh hồ sơ
	@FXML
	private Button profile_importBtn; // Nút nhập ảnh hồ sơ

	// Các nhãn hiển thị thông tin lễ tân
	@FXML
	private Label name_receptDB, username_receptDB; // Nhãn tên và tên đăng nhập
	@FXML
	private Label name_recept, username_recept, email_recept, phone_recept, gender_recept, createdDate_recept; // Các nhãn thông tin lễ tân
	@FXML
	private TextField txt_name_recept, txt_username_recept, txt_email_recept, txt_phone_recept; // Các ô nhập thông tin lễ tân
	@FXML
	private ComboBox<String> gender_cb; // ComboBox chọn giới tính
	@FXML
	private Button profile_updateBtn; // Nút cập nhật hồ sơ
	@FXML
	private TextArea txt_address_recept; // Khu vực nhập địa chỉ

	private AlertMessage alert = new AlertMessage(); // Đối tượng hiển thị thông báo
	private Image image; // Đối tượng hình ảnh
	private Connection connect; // Kết nối cơ sở dữ liệu
	private PreparedStatement prepare; // Đối tượng thực thi truy vấn SQL
	private ResultSet result; // Kết quả truy vấn
	private Statement statement; // Đối tượng thực thi câu lệnh SQL

	// Biến lưu tên người dùng
	private String username;

	// Thiết lập tên người dùng và tải thông tin hồ sơ
	public void setUsername(String username) {
		this.username = username; // Gán tên người dùng
		loadReceptionistProfile(); // Tải thông tin hồ sơ lễ tân
		profileDisplayImages(); // Hiển thị ảnh hồ sơ
	}

	private ObservableList<DrugData> drugMasterList = FXCollections.observableArrayList(); // Danh sách thuốc chính

	// Khởi tạo các bộ lọc cho bảng thuốc
	private void initializeDrugFilters() {
		cmbDrugSearchBy.setItems(FXCollections.observableArrayList("Name", "Manufacturer", "Unit")); // Thiết lập tiêu chí tìm kiếm
		cmbDrugSearchBy.setValue("Name"); // Mặc định tìm kiếm theo tên

		cmbDrugExpiryFilter.setItems(FXCollections.observableArrayList("All", "Valid", "Expired")); // Thiết lập bộ lọc ngày hết hạn
		cmbDrugExpiryFilter.setValue("All"); // Mặc định hiển thị tất cả

		cmbDrugStockFilter.setItems(FXCollections.observableArrayList("All", "In Stock", "Out of Stock")); // Thiết lập bộ lọc tồn kho
		cmbDrugStockFilter.setValue("All"); // Mặc định hiển thị tất cả

		cmbDrugPriceSort.setItems(FXCollections.observableArrayList("None", "Low to High", "High to Low")); // Thiết lập sắp xếp giá
		cmbDrugPriceSort.setValue("None"); // Mặc định không sắp xếp

		txtDrugSearch.clear(); // Xóa ô tìm kiếm
		txtDrugSearch.setPromptText("Enter keyword to search"); // Đặt gợi ý cho ô tìm kiếm

		// Gắn listener để tự động lọc khi thay đổi giá trị
		txtDrugSearch.textProperty().addListener((obs, oldVal, newVal) -> applyAdvancedDrugFilter());
		cmbDrugSearchBy.valueProperty().addListener((obs, o, n) -> applyAdvancedDrugFilter());
		cmbDrugExpiryFilter.valueProperty().addListener((obs, o, n) -> applyAdvancedDrugFilter());
		cmbDrugStockFilter.valueProperty().addListener((obs, o, n) -> applyAdvancedDrugFilter());
		cmbDrugPriceSort.valueProperty().addListener((obs, o, n) -> applyAdvancedDrugFilter());
	}

	// Tải dữ liệu thuốc vào bảng
	private void loadDrugTable() {
		drugMasterList.clear(); // Xóa danh sách hiện tại
		try {
			Connection conn = Database.connectDB(); // Kết nối cơ sở dữ liệu
			String sql = "SELECT Id, Name, Manufacturer, Expiry_date, Unit, Price, Stock, Create_date, Update_date "
					+ "FROM DRUG "; // Truy vấn lấy danh sách thuốc

			// Thực thi truy vấn
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			// Thêm dữ liệu vào danh sách
			while (rs.next()) {
				drugMasterList.add(new DrugData(rs.getString("Id"), rs.getString("Name"), rs.getString("Manufacturer"),
						rs.getString("Unit"), rs.getBigDecimal("Price"), rs.getInt("Stock"),
						rs.getDate("Expiry_date").toLocalDate(), rs.getTimestamp("Create_date"),
						rs.getTimestamp("Update_date")));
			}

			// Gán dữ liệu cho các cột
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

			// Thiết lập cột hành động với các nút sửa và xóa
			drug_col_action.setCellFactory(col -> new TableCell<>() {
				private final Button editBtn = new Button("Update"); // Nút sửa
				private final Button deleteBtn = new Button("Delete"); // Nút xóa
				private final HBox hbox = new HBox(5, editBtn, deleteBtn); // HBox chứa các nút
				{
					editBtn.setOnAction(e -> {
						DrugData drug = getTableView().getItems().get(getIndex()); // Lấy thông tin thuốc
						openEditDrugForm(drug); // Mở form sửa thuốc
					});

					deleteBtn.setOnAction(e -> {
						DrugData drug = getTableView().getItems().get(getIndex()); // Lấy thông tin thuốc
						deleteDrug(drug.getDrugId()); // Xóa thuốc
					});
				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					setGraphic(empty ? null : hbox); // Hiển thị hoặc ẩn các nút
				}
			});

			drug_tableView.setItems(drugMasterList); // Gán danh sách thuốc vào bảng
			initializeDrugFilters(); // Khởi tạo bộ lọc

			conn.close(); // Đóng kết nối
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Error loading drug list!"); // Hiển thị lỗi nếu có
			alert.showAndWait();
		}
	}

	// Áp dụng bộ lọc nâng cao cho thuốc
	private void applyAdvancedDrugFilter() {
		String keyword = txtDrugSearch.getText().toLowerCase(); // Lấy từ khóa tìm kiếm
		String searchBy = cmbDrugSearchBy.getValue(); // Lấy tiêu chí tìm kiếm
		String expiryFilter = cmbDrugExpiryFilter.getValue(); // Lấy bộ lọc ngày hết hạn
		String stockFilter = cmbDrugStockFilter.getValue(); // Lấy bộ lọc tồn kho
		String priceSort = cmbDrugPriceSort.getValue(); // Lấy tiêu chí sắp xếp giá

		ObservableList<DrugData> filtered = FXCollections.observableArrayList(); // Danh sách thuốc đã lọc

		// Lọc thuốc theo các tiêu chí
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

			// Lọc theo ngày hết hạn
			boolean matchesExpiry = expiryFilter.equals("All")
					|| (expiryFilter.equals("Valid") && drug.getExpiryDate().isAfter(LocalDate.now()))
					|| (expiryFilter.equals("Expired") && !drug.getExpiryDate().isAfter(LocalDate.now()));

			// Lọc theo tồn kho
			boolean matchesStock = stockFilter.equals("All") || (stockFilter.equals("In Stock") && drug.getStock() > 0)
					|| (stockFilter.equals("Out of Stock") && drug.getStock() <= 0);

			if (matchesKeyword && matchesExpiry && matchesStock) {
				filtered.add(drug); // Thêm thuốc vào danh sách đã lọc
			}
		}

		// Sắp xếp theo giá
		if (priceSort.equals("Low to High")) {
			FXCollections.sort(filtered, Comparator.comparing(DrugData::getPrice));
		} else if (priceSort.equals("High to Low")) {
			FXCollections.sort(filtered, Comparator.comparing(DrugData::getPrice).reversed());
		}

		drug_tableView.setItems(filtered); // Cập nhật bảng với danh sách đã lọc
	}

	// Xóa thuốc khỏi cơ sở dữ liệu
	private void deleteDrug(String drugId) {
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Delete Confirmation");
	    alert.setHeaderText("Are you sure you want to delete this drug?");
	    alert.setContentText("This action cannot be undone.");

	    Optional<ButtonType> result = alert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        Connection conn = null;
	        try {
	            conn = Database.connectDB();
	            conn.setAutoCommit(false); // Start transaction

	            // Check if drug is in any prescription details
	            String checkPrescriptionSQL = "SELECT COUNT(*) FROM PRESCRIPTION_DETAILS WHERE Drug_id = ?";
	            PreparedStatement psCheck = conn.prepareStatement(checkPrescriptionSQL);
	            psCheck.setString(1, drugId);
	            ResultSet rs = psCheck.executeQuery();
	            rs.next();
	            int prescriptionCount = rs.getInt(1);

	            if (prescriptionCount > 0) {
	                // Show warning about prescriptions
	                Alert warning = new Alert(Alert.AlertType.WARNING);
	                warning.setTitle("Drug in Use");
	                warning.setHeaderText("This drug is part of " + prescriptionCount + " prescription(s).");
	                warning.setContentText("Deleting this drug will remove it from all associated prescriptions and reset the Prescription_Status of affected appointments to 'Created'. Do you want to proceed?");
	                ButtonType proceedButton = new ButtonType("Proceed");
	                ButtonType cancelButton = new ButtonType("Cancel");
	                warning.getButtonTypes().setAll(proceedButton, cancelButton);

	                Optional<ButtonType> warningResult = warning.showAndWait();
	                if (warningResult.isPresent() && warningResult.get() != proceedButton) {
	                    conn.rollback();
	                    conn.close();
	                    return; // User canceled
	                }

	                // Get affected appointment IDs
	                String getAppointmentIdsSQL = "SELECT DISTINCT p.Appointment_id " +
	                                             "FROM PRESCRIPTION p " +
	                                             "JOIN PRESCRIPTION_DETAILS pd ON p.Id = pd.Prescription_id " +
	                                             "WHERE pd.Drug_id = ?";
	                PreparedStatement psGetAppointments = conn.prepareStatement(getAppointmentIdsSQL);
	                psGetAppointments.setString(1, drugId);
	                ResultSet rsAppointments = psGetAppointments.executeQuery();
	                List<String> appointmentIds = new ArrayList<>();
	                while (rsAppointments.next()) {
	                    appointmentIds.add(rsAppointments.getString("Appointment_id"));
	                }

	                // Delete drug from prescription details
	                String deletePrescriptionDetailsSQL = "DELETE FROM PRESCRIPTION_DETAILS WHERE Drug_id = ?";
	                PreparedStatement psDeleteDetails = conn.prepareStatement(deletePrescriptionDetailsSQL);
	                psDeleteDetails.setString(1, drugId);
	                psDeleteDetails.executeUpdate();

	                // Reset Prescription_Status to 'Created' for affected appointments
	                if (!appointmentIds.isEmpty()) {
	                    String updatePrescriptionStatusSQL = "UPDATE APPOINTMENT SET Prescription_Status = 'Created' WHERE id = ?";
	                    PreparedStatement psUpdateStatus = conn.prepareStatement(updatePrescriptionStatusSQL);
	                    for (String appointmentId : appointmentIds) {
	                        psUpdateStatus.setString(1, appointmentId);
	                        psUpdateStatus.executeUpdate();
	                    }
	                }
	            }

	            // Delete the drug
	            String deleteDrugSQL = "DELETE FROM DRUG WHERE Id = ?";
	            PreparedStatement psDeleteDrug = conn.prepareStatement(deleteDrugSQL);
	            psDeleteDrug.setString(1, drugId);
	            int rowsAffected = psDeleteDrug.executeUpdate();

	            if (rowsAffected > 0) {
	                conn.commit(); // Commit transaction
	                loadDrugTable(); // Refresh table
	                Alert success = new Alert(Alert.AlertType.INFORMATION);
	                success.setContentText("Drug deleted successfully.");
	                success.showAndWait();
	            } else {
	                conn.rollback();
	                Alert error = new Alert(Alert.AlertType.ERROR);
	                error.setContentText("Drug not found.");
	                error.showAndWait();
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            try {
	                if (conn != null) conn.rollback();
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	            Alert error = new Alert(Alert.AlertType.ERROR);
	            error.setContentText("Error deleting drug: " + e.getMessage());
	            error.showAndWait();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (conn != null) conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	// Mở form chỉnh sửa thuốc
	private void openEditDrugForm(DrugData drug) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditDrugForm.fxml"));
			Parent root = loader.load();

			EditDrugFormController controller = loader.getController();
			controller.setDrugData(drug); // Truyền dữ liệu thuốc sang form chỉnh sửa

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

	// Mở form thêm thuốc mới
	@FXML
	private void openAddDrugForm() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddDrugForm.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Add New Drug");
			stage.setScene(new Scene(root));

			stage.setOnHidden(e -> loadDrugTable()); // Tải lại bảng khi thêm thuốc
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// =======================CRUD Patient==================================
	private ObservableList<PatientData> patientMasterList = FXCollections.observableArrayList(); // Danh sách bệnh nhân chính

	// Khởi tạo các bộ lọc cho bảng bệnh nhân
	private void initializePatientFilters() {
		cmbPatientSearchBy.setItems(FXCollections.observableArrayList("Name", "Email", "Phone", "Address", "Diagnosis",
				"Height", "Weight")); // Thiết lập tiêu chí tìm kiếm
		cmbPatientSearchBy.setValue("Name"); // Mặc định tìm kiếm theo tên

		txtPatientSearch.clear(); // Xóa ô tìm kiếm

		cmbPatientGenderFilter.setItems(FXCollections.observableArrayList("All", "Male", "Female", "Other")); // Thiết lập bộ lọc giới tính
		cmbPatientGenderFilter.setValue("All"); // Mặc định hiển thị tất cả

		// Gắn listener để tự động lọc khi thay đổi giá trị
		txtPatientSearch.textProperty().addListener((obs, oldVal, newVal) -> applyPatientFilters());
		cmbPatientSearchBy.valueProperty().addListener((obs, o, n) -> applyPatientFilters());
		cmbPatientGenderFilter.valueProperty().addListener((obs, o, n) -> applyPatientFilters());
	}

	// Áp dụng bộ lọc cho bệnh nhân
	private void applyPatientFilters() {
		String keyword = txtPatientSearch.getText().toLowerCase(); // Lấy từ khóa tìm kiếm
		String searchBy = cmbPatientSearchBy.getValue(); // Lấy tiêu chí tìm kiếm
		String selectedGender = cmbPatientGenderFilter.getValue(); // Lấy giới tính được chọn

		ObservableList<PatientData> filtered = FXCollections.observableArrayList(); // Danh sách bệnh nhân đã lọc

		// Lọc bệnh nhân theo các tiêu chí
		for (PatientData p : patientMasterList) {
			// Lấy giá trị trường tương ứng
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
				filtered.add(p); // Thêm bệnh nhân vào danh sách đã lọc
			}
		}

		patients_tableView.setItems(filtered); // Cập nhật bảng với danh sách đã lọc
	}

	// Tải dữ liệu bệnh nhân vào bảng
	public void loadPatientTable() {
		patientMasterList.clear(); // Xóa danh sách hiện tại

		String sql = "SELECT Patient_id, Name, Email, Gender, Phone, Address, Diagnosis, Height, Weight, Create_date, Update_date FROM PATIENT"; // Truy vấn lấy danh sách bệnh nhân

		try {
			Connection conn = Database.connectDB(); // Kết nối cơ sở dữ liệu
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			// Thêm dữ liệu vào danh sách
			while (rs.next()) {
				PatientData patient = new PatientData(rs.getString("Patient_Id"), rs.getString("Name"),
						rs.getString("Email"), rs.getString("Gender"), rs.getString("Phone"), rs.getString("Address"),
						rs.getString("Diagnosis"), rs.getBigDecimal("Height"), rs.getBigDecimal("Weight"),
						rs.getTimestamp("Create_date"), rs.getTimestamp("Update_date"));
				patientMasterList.add(patient);
			}

			// Gán dữ liệu cho các cột
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

			// Thiết lập cột hành động với các nút sửa, xóa, chi tiết
			patients_col_action.setCellFactory(col -> new TableCell<>() {
				private final Button editBtn = new Button("Update"); // Nút sửa
				private final Button deleteBtn = new Button("Delete"); // Nút xóa
				private final Button detailBtn = new Button("Detail"); // Nút xem chi tiết
				private final HBox hbox = new HBox(5, editBtn, deleteBtn, detailBtn); // HBox chứa các nút
				{
					editBtn.setOnAction(e -> {
						PatientData patient = getTableView().getItems().get(getIndex()); // Lấy thông tin bệnh nhân
						openEditPatientForm(patient); // Mở form sửa bệnh nhân
					});

					deleteBtn.setOnAction(e -> {
						PatientData patient = getTableView().getItems().get(getIndex()); // Lấy thông tin bệnh nhân
						deletePatient(patient.getPatientId()); // Xóa bệnh nhân
					});
					detailBtn.setOnAction(e -> {
						PatientData patient = getTableView().getItems().get(getIndex()); // Lấy thông tin bệnh nhân
						handleViewPatientDetail(patient.getPatientId()); // Xem chi tiết bệnh nhân
					});
				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					setGraphic(empty ? null : hbox); // Hiển thị hoặc ẩn các nút
				}
			});

			patients_tableView.setItems(patientMasterList); // Gán danh sách bệnh nhân vào bảng
			conn.close(); // Đóng kết nối

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Error loading patient list!"); // Hiển thị lỗi nếu có
			alert.showAndWait();
		}
	}

	// Xem chi tiết bệnh nhân
	private void handleViewPatientDetail(String patientId) {
		try (Connection conn = Database.connectDB()) {
			String sql = "SELECT * FROM PATIENT WHERE Patient_id = ?"; // Truy vấn thông tin bệnh nhân
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
				controller.setPatientData(patient); // Truyền dữ liệu bệnh nhân vào controller

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

	// Xóa bệnh nhân khỏi cơ sở dữ liệu
	private void deletePatient(String patientId) {
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Delete Confirmation");
	    alert.setHeaderText("Are you sure you want to delete this patient?");
	    alert.setContentText("This action cannot be undone.");

	    Optional<ButtonType> result = alert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        Connection conn = null;
	        try {
	            conn = Database.connectDB();
	            conn.setAutoCommit(false); // Start transaction

	            // Check if patient has any appointments
	            String checkAppointmentSQL = "SELECT COUNT(*) FROM APPOINTMENT WHERE Patient_id = ?";
	            PreparedStatement psCheck = conn.prepareStatement(checkAppointmentSQL);
	            psCheck.setString(1, patientId);
	            ResultSet rs = psCheck.executeQuery();
	            rs.next();
	            int appointmentCount = rs.getInt(1);

	            if (appointmentCount > 0) {
	                // Show warning about appointments and related data
	                Alert warning = new Alert(Alert.AlertType.WARNING);
	                warning.setTitle("Patient Has Appointments");
	                warning.setHeaderText("This patient is associated with " + appointmentCount + " appointment(s).");
	                warning.setContentText("Deleting this patient will also delete all associated appointments, their services, prescriptions, and prescription details. Do you want to proceed?");
	                ButtonType proceedButton = new ButtonType("Proceed");
	                ButtonType cancelButton = new ButtonType("Cancel");
	                warning.getButtonTypes().setAll(proceedButton, cancelButton);

	                Optional<ButtonType> warningResult = warning.showAndWait();
	                if (warningResult.isPresent() && warningResult.get() != proceedButton) {
	                    conn.rollback();
	                    conn.close();
	                    return; // User canceled
	                }

	                // Delete associated prescription details
	                String deletePrescriptionDetailsSQL = "DELETE FROM PRESCRIPTION_DETAILS WHERE Prescription_id IN " +
	                                                     "(SELECT Id FROM PRESCRIPTION WHERE Appointment_id IN " +
	                                                     "(SELECT id FROM APPOINTMENT WHERE Patient_id = ?))";
	                PreparedStatement psDeletePrescriptionDetails = conn.prepareStatement(deletePrescriptionDetailsSQL);
	                psDeletePrescriptionDetails.setString(1, patientId);
	                psDeletePrescriptionDetails.executeUpdate();

	                // Delete associated prescriptions
	                String deletePrescriptionsSQL = "DELETE FROM PRESCRIPTION WHERE Appointment_id IN " +
	                                               "(SELECT id FROM APPOINTMENT WHERE Patient_id = ?)";
	                PreparedStatement psDeletePrescriptions = conn.prepareStatement(deletePrescriptionsSQL);
	                psDeletePrescriptions.setString(1, patientId);
	                psDeletePrescriptions.executeUpdate();

	                // Delete associated appointment_service entries
	                String deleteAppointmentServiceSQL = "DELETE FROM APPOINTMENT_SERVICE WHERE Appointment_id IN " +
	                                                    "(SELECT id FROM APPOINTMENT WHERE Patient_id = ?)";
	                PreparedStatement psDeleteAppointmentService = conn.prepareStatement(deleteAppointmentServiceSQL);
	                psDeleteAppointmentService.setString(1, patientId);
	                psDeleteAppointmentService.executeUpdate();

	                // Delete associated appointments
	                String deleteAppointmentsSQL = "DELETE FROM APPOINTMENT WHERE Patient_id = ?";
	                PreparedStatement psDeleteAppointments = conn.prepareStatement(deleteAppointmentsSQL);
	                psDeleteAppointments.setString(1, patientId);
	                psDeleteAppointments.executeUpdate();
	            }

	            // Delete the patient
	            String deletePatientSQL = "DELETE FROM PATIENT WHERE Patient_Id = ?";
	            PreparedStatement psDeletePatient = conn.prepareStatement(deletePatientSQL);
	            psDeletePatient.setString(1, patientId);
	            int rowsAffected = psDeletePatient.executeUpdate();

	            if (rowsAffected > 0) {
	                conn.commit(); // Commit transaction
	                loadPatientTable(); // Refresh table
	                Alert success = new Alert(Alert.AlertType.INFORMATION);
	                success.setContentText("Patient deleted successfully.");
	                success.showAndWait();
	            } else {
	                conn.rollback();
	                Alert error = new Alert(Alert.AlertType.ERROR);
	                error.setContentText("Patient not found.");
	                error.showAndWait();
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            try {
	                if (conn != null) conn.rollback();
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	            Alert error = new Alert(Alert.AlertType.ERROR);
	            error.setContentText("Error deleting patient: " + e.getMessage());
	            error.showAndWait();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (conn != null) conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	// Mở form chỉnh sửa bệnh nhân
	private void openEditPatientForm(PatientData patient) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditPatientForm.fxml"));
			Parent root = loader.load();

			EditPatientFormController controller = loader.getController();
			controller.setPatientData(patient); // Truyền dữ liệu bệnh nhân sang form chỉnh sửa

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

	// Mở form thêm bệnh nhân mới
	@FXML
	private void openAddPatientForm() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddPatientForm.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Add New Patient");
			stage.setScene(new Scene(root));

			stage.setOnHidden(e -> loadPatientTable()); // Tải lại bảng khi thêm bệnh nhân
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* =====================LOAD PROFILE======================================== */
	// Tải thông tin hồ sơ lễ tân
	private void loadReceptionistProfile() {
		String checkUserSQL = "SELECT ua.name, ua.username, ua.email, ua.gender, ua.Create_date, r.phone, r.address "
				+ "FROM user_account ua " + "JOIN receptionist r ON ua.id = r.receptionist_id "
				+ "WHERE ua.username = ?"; // Truy vấn thông tin lễ tân

		Connection connect = Database.connectDB();

		try {
			PreparedStatement prepare = connect.prepareStatement(checkUserSQL);
			prepare.setString(1, username);

			ResultSet result = prepare.executeQuery();

			if (!result.next()) {
				alert.errorMessage("Username does not match data."); // Hiển thị lỗi nếu không tìm thấy
				return;
			}
			String name = result.getString("name");
			String username = result.getString("username");
			String email = result.getString("email");
			String phone = result.getString("phone");
			String address = result.getString("address");
			String gender = result.getString("gender");
			String createdAt = result.getString("Create_date");

			// Gán thông tin cho các nhãn và ô nhập
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
			txt_phone_recept.setText(phone != null ? phone : "");
			gender_cb.setValue(gender != null ? gender : "");
			txt_address_recept.setText(address != null ? address : "");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Hiển thị ảnh hồ sơ
	public void profileDisplayImages() {
		String sql = "SELECT Avatar FROM user_account WHERE username = ?"; // Truy vấn ảnh đại diện
		connect = Database.connectDB();

		try {
			prepare = connect.prepareStatement(sql);
			prepare.setString(1, username);
			result = prepare.executeQuery();
			if (result.next()) {
				InputStream inputStream = result.getBinaryStream("Avatar");

				if (inputStream != null) {
					// Đọc dữ liệu ảnh từ cơ sở dữ liệu
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					byte[] data = new byte[1024];
					int nRead;
					while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
						buffer.write(data, 0, nRead);
					}
					buffer.flush();
					byte[] imageBytes = buffer.toByteArray();
					inputStream.close();

					// Hiển thị ảnh lên giao diện
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
	// Cập nhật thông tin hồ sơ
	public void profileUpdateBtn() {
		String name = txt_name_recept.getText();
		String phone = txt_phone_recept.getText();
		String usernameEdit = txt_username_recept.getText();
		String address = txt_address_recept.getText();
		String email = email_recept.getText();
		String gender = (String) gender_cb.getSelectionModel().getSelectedItem();

		// Kiểm tra dữ liệu đầu vào
		if (usernameEdit.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
			alert.errorMessage("Please fill in all the fields.");
			return;
		}
		if (gender == null || gender.isEmpty()) {
			alert.errorMessage("Please select a gender.");
			return;
		}

		String checkUsernameSQL = "SELECT * FROM user_account WHERE username = ?"; // Kiểm tra username tồn tại
		String updateUserSQL = "UPDATE user_account SET name = ?, username = ?, gender = ? WHERE email = ?"; // Cập nhật thông tin người dùng
		String updateReceptionistSQL = "UPDATE receptionist SET phone = ?, address = ? WHERE receptionist_id = (SELECT id FROM user_account WHERE email = ?)"; // Cập nhật thông tin lễ tân

		connect = Database.connectDB();

		try {
			// Kiểm tra username đã tồn tại
			if (!username.equals(usernameEdit)) {
				prepare = connect.prepareStatement(checkUsernameSQL);
				prepare.setString(1, usernameEdit);
				result = prepare.executeQuery();

				if (result.next()) {
					alert.errorMessage("Username \"" + usernameEdit + "\" already exists!");
					return;
				}
			}

			// Cập nhật thông tin người dùng
			prepare = connect.prepareStatement(updateUserSQL);
			prepare.setString(1, name);
			prepare.setString(2, usernameEdit);
			prepare.setString(3, gender);
			prepare.setString(4, email);
			System.out.println(name + " " + usernameEdit + " " + gender + " " + email);
			int rowsUserUpdated = prepare.executeUpdate();

			// Cập nhật thông tin lễ tân
			prepare = connect.prepareStatement(updateReceptionistSQL);
			prepare.setString(1, phone);
			prepare.setString(2, address);
			prepare.setString(3, email);
			int rowsReceptionistUpdated = prepare.executeUpdate();

			if (rowsUserUpdated > 0 || rowsReceptionistUpdated > 0) {
				alert.successMessage("Profile updated successfully.");
				this.username = usernameEdit;
				loadReceptionistProfile(); // Tải lại hồ sơ
			} else {
				alert.errorMessage("No user found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			alert.errorMessage("Error updating profile: " + e.getMessage());
		}
	}

	// Nhập ảnh hồ sơ
	@FXML
	private void profileImportBtn(ActionEvent event) {
		FileChooser open = new FileChooser();
		open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*jpg", "*jpeg", "*png")); // Bộ lọc định dạng ảnh

		File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());

		if (file != null) {
			Data.path = file.getAbsolutePath();

			// Hiển thị ảnh lên giao diện
			image = new Image(file.toURI().toString(), 0, 0, false, true);
			profile_circle.setFill(new ImagePattern(image));

			// Lưu ảnh vào cơ sở dữ liệu
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
				profileDisplayImages(); // Tải lại ảnh hồ sơ
			} catch (Exception e) {
				e.printStackTrace();
				alert.errorMessage("Error uploading avatar: " + e.getMessage());
			}
		}
	}
	/*
	 * =====================CREATE APPOINTMENT========================================
	 */

	// Tải danh sách bệnh nhân vào ComboBox
	private void loadComboBoxPatient() {
		ObservableList<PatientData> patientList = FXCollections.observableArrayList();
		String sql = "SELECT * FROM patient"; // Truy vấn danh sách bệnh nhân

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
			appointment_patient.setItems(patientList); // Gán danh sách vào ComboBox
			cb_patient.setItems(patientList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Tải danh sách dịch vụ vào ComboBox
	private void loadComboBoxService() {
		ObservableList<ServiceData> serviceList = FXCollections.observableArrayList();

		String sql = "SELECT * FROM service"; // Truy vấn danh sách dịch vụ

		try {
			connect = Database.connectDB();
			prepare = connect.prepareStatement(sql);
			ResultSet rs = prepare.executeQuery();

			while (rs.next()) {
				serviceList.add(new ServiceData(rs.getString("id"), rs.getString("name"), rs.getBigDecimal("price"),
						rs.getString("type")));
			}

			cb_service.setItems(serviceList); // Gán danh sách vào ComboBox

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Tải danh sách bác sĩ theo dịch vụ
	public void loadComboBoxDoctor(String serviceId, ComboBox<DoctorData> cbDoctor) {
		ObservableList<DoctorData> doctorList = FXCollections.observableArrayList();

		String sql = "SELECT d.Doctor_id, d.Phone, d.Service_id, d.Address, d.Is_confirmed, "
				+ "u.Username, u.Password, u.Email, u.Name, u.Gender, u.Is_active, " + "s.Name AS ServiceName "
				+ "FROM DOCTOR d " + "JOIN USER_ACCOUNT u ON d.Doctor_id = u.Id "
				+ "LEFT JOIN SERVICE s ON d.Service_id = s.Id " + "WHERE d.Service_id = ?"; // Truy vấn danh sách bác sĩ

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

			cbDoctor.setItems(doctorList); // Gán danh sách vào ComboBox

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Tải danh sách mức độ khẩn cấp
	private void loadUrgencyLevels() {
		cb_urgency.getItems().clear();
		cb_urgency.getItems().addAll(UrgencyLevel.values()); // Thêm tất cả mức độ khẩn cấp vào ComboBox
	}

	// Chọn bệnh nhân
	@FXML
	private void choosePatient(ActionEvent event) throws IOException {
		selectedPatient = cb_patient.getSelectionModel().getSelectedItem(); // Lấy bệnh nhân được chọn

		if (selectedPatient != null) {
			String sql = "SELECT * FROM patient WHERE patient_id = ?"; // Truy vấn thông tin bệnh nhân
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
			lb_patient_name.setText(""); // Xóa nhãn nếu không chọn bệnh nhân
		}
	}

	// Chọn dịch vụ
	@FXML
	private void chooseService(ActionEvent event) throws IOException {
		ComboBox<ServiceData> cbService = (ComboBox<ServiceData>) event.getSource();
		AnchorPane currentPane = (AnchorPane) cbService.getParent();
		Label lbPrice = (Label) currentPane.lookup("#lb_price_service");
		ComboBox<DoctorData> cbDoctor = (ComboBox<DoctorData>) currentPane.lookup("#cb_doctor");

		ServiceData selectedService = cb_service.getSelectionModel().getSelectedItem(); // Lấy dịch vụ được chọn

		if (selectedService != null) {
			String sql = "SELECT * FROM service WHERE id = ?"; // Truy vấn thông tin dịch vụ
			try (Connection connect = Database.connectDB(); PreparedStatement ps = connect.prepareStatement(sql)) {

				ps.setString(1, selectedService.getServiceId());
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					lbPrice.setText(formatCurrencyVND(rs.getBigDecimal("price"))); // Hiển thị giá dịch vụ
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			loadComboBoxDoctor(selectedService.getServiceId(), cbDoctor); // Tải danh sách bác sĩ theo dịch vụ
		} else {
			lbPrice.setText(""); // Xóa nhãn giá nếu không chọn dịch vụ
		}
	}

	// Reset form tạo lịch hẹn
	@FXML
	private void resetForm(ActionEvent event) {
		List<Node> toRemove = new ArrayList<>();
		int limit = vboxContainer.getChildren().size() - 1; // Lấy số lượng node trừ nút cuối

		// Xóa các form dịch vụ
		for (int i = 0; i < limit; i++) {
			Node node = vboxContainer.getChildren().get(i);
			if (node instanceof AnchorPane) {
				AnchorPane pane = (AnchorPane) node;
				ComboBox<ServiceData> cbService = (ComboBox<ServiceData>) pane.lookup("#cb_service");

				if (cbService != null) {
					toRemove.add(pane);
				}
			}
		}
		vboxContainer.getChildren().removeAll(toRemove); // Xóa các form dịch vụ
		lb_patient_name.setText("");
		lb_patient_gender.setText("");
		lb_patient_address.setText("");
		lb_patient_age.setText("");
		txt_suggest.setText("");
		txt_details.setText("");
		lb_check.setText("Please check all the required fields carefully before creating appointments");
		cb_patient.getSelectionModel().clearSelection();
		cb_urgency.getSelectionModel().clearSelection();
		checkbox_followUp.setSelected(false);
	}

	// Thêm form dịch vụ mới
	@FXML
	private void addNewForm(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ChooseServiceForm.fxml"));
			AnchorPane newForm = loader.load();

			int insertPos = vboxContainer.getChildren().size() - 2; // Vị trí chèn form mới
			if (insertPos < 0) {
				insertPos = 0;
			}

			vboxContainer.getChildren().add(insertPos, newForm); // Thêm form vào container
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Xóa form dịch vụ
	@FXML
	private void removeForm(ActionEvent event) {
		Button btn = (Button) event.getSource();
		AnchorPane pane = (AnchorPane) btn.getParent();
		VBox vboxContainer = (VBox) pane.getParent();

		vboxContainer.getChildren().remove(pane); // Xóa form khỏi container
	}

	// Lấy danh sách ID dịch vụ đã chọn
	private List<String> getSelectedServiceIdsFromVBox() {
		List<String> serviceIds = new ArrayList<>();
		for (Node node : vboxContainer.getChildren()) {
			if (node instanceof AnchorPane) {
				AnchorPane pane = (AnchorPane) node;
				ComboBox<ServiceData> cbService = (ComboBox<ServiceData>) pane.lookup("#cb_service");
				if (cbService != null) {
					ServiceData selected = cbService.getSelectionModel().getSelectedItem();
					if (selected != null) {
						serviceIds.add(selected.getServiceId()); // Thêm ID dịch vụ
					}
				}
			}
		}
		return serviceIds;
	}

	// Tạo gợi ý lịch hẹn
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
		if (getSelectedServiceIdsFromVBox().isEmpty()) {
			alert.errorMessage("⚠ Please select at least one service.");
			return;
		}
		
		String patientId = selectedPatient.getPatientId();
		List<String> serviceIds = getSelectedServiceIdsFromVBox();
		int urgency = cb_urgency.getValue().getScore();
		boolean isFollowup = checkbox_followUp.isSelected();
		AppointmentSuggester suggester = new AppointmentSuggester(txt_suggest);
		suggester.createSuggest(patientId, serviceIds, urgency, isFollowup); // Tạo gợi ý lịch hẹn
	}

	// Biến kiểm tra lịch hẹn
	boolean isAvailableAppointment = true;
	boolean isCheck = false;

	// Danh sách thông tin dịch vụ và bác sĩ được chọn
	List<String> selectedServiceNames = new ArrayList<>();
	List<Map<String, String>> selectedDoctorInfos = new ArrayList<>();
	List<String> selectedSlotTimes = new ArrayList<>();
	List<LocalDate> selectTimes = new ArrayList<>();
	List<LocalTime> slotTimes = new ArrayList<>();

	PatientData selectedPatient = new PatientData();
	int urgency;
	boolean isFollowup;

	// Kiểm tra trùng lịch hẹn
	public boolean hasDuplicateSlots() {
		Set<String> bookedSlots = new HashSet<>();

		for (int i = 0; i < selectTimes.size(); i++) {
			LocalDate date = selectTimes.get(i);
			LocalTime time = slotTimes.get(i);

			String key = date.toString() + "|" + time.toString();

			if (bookedSlots.contains(key)) {
				return true; // Có lịch trùng
			} else {
				bookedSlots.add(key);
			}
		}

		return false; // Không có lịch trùng
	}

	// Kiểm tra lịch hẹn
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

		// Kiểm tra từng form dịch vụ
		for (Node node : vboxContainer.getChildren()) {
			if (node instanceof AnchorPane anchorPane) {
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

					// Kiểm tra lịch bác sĩ
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

						// Kiểm tra lịch bệnh nhân
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
					System.out.println(serviceName);
					Map<String, String> doctorInfo = new HashMap<>();
					doctorInfo.put("id", doctorId);
					doctorInfo.put("name", doctorName);

					selectedDoctorInfos.add(doctorInfo);
					selectedSlotTimes.add(selectTimeStr);
					selectTimes.add(date);
					slotTimes.add(time);

				} else {
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

		urgency = cb_urgency.getValue() != null ? cb_urgency.getValue().getScore() : 1;
		isFollowup = checkbox_followUp.isSelected();

		// Hiển thị chi tiết lịch hẹn
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

	// Cấu hình logger
	@FXML
	private static final Logger LOGGER = Logger.getLogger(ReceptionistController.class.getName());
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	static {
		LOGGER.setLevel(Level.FINE);
		for (Handler handler : LOGGER.getParent().getHandlers()) {
			handler.setLevel(Level.FINE);
		}
	}

	// Lấy tên lễ tân
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

	// Thay thế các placeholder trong tài liệu
	private void replacePlaceholders(XWPFDocument doc, Map<String, String> placeholders) {
		if (placeholders == null) {
			LOGGER.warning("Placeholders map is null, skipping replacement.");
			return;
		}

		// Xử lý các đoạn văn
		for (XWPFParagraph para : doc.getParagraphs()) {
			replaceInParagraph(para, placeholders);
		}

		// Xử lý các bảng
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

		// Xử lý header và footer
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

	// Thay thế placeholder trong đoạn văn
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
	
	// Hàm thay thế các placeholder trong bảng của tài liệu Word
	private void replaceTablePlaceholders(XWPFDocument doc, List<String> serviceNames, List<BigDecimal> servicePrices,
				List<Map<String, Object>> drugs, BigDecimal totalPrice, String formattedDate, String receptionistName,
				String doctorName, String diagnose, String advice) {
			// Lấy danh sách tất cả các bảng trong tài liệu
			List<XWPFTable> tables = doc.getTables();
			// Kiểm tra nếu không có bảng nào trong tài liệu
			if (tables.isEmpty()) {
				LOGGER.warning("No tables found in document.");
				return;
			}

			// Tạo một HashMap để lưu các placeholder và giá trị thay thế
			Map<String, String> tablePlaceholders = new HashMap<>();
			// Gán giá trị cho các placeholder liên quan đến thông tin bệnh nhân
			tablePlaceholders.put("patientId", selectedPatient != null ? selectedPatient.getPatientId() : "");
			tablePlaceholders.put("patientName", selectedPatient != null ? selectedPatient.getName() : "");
			// Tính tuổi bệnh nhân dựa trên ngày sinh
			tablePlaceholders.put("age",
					selectedPatient != null && selectedPatient.getBirthDate() != null
							? String.valueOf(Period.between(selectedPatient.getBirthDate(), LocalDate.now()).getYears())
							: "0");
			tablePlaceholders.put("gender", selectedPatient != null ? selectedPatient.getGender() : "");
			tablePlaceholders.put("address", selectedPatient != null ? selectedPatient.getAddress() : "");
			tablePlaceholders.put("diagnose", diagnose != null ? diagnose : "");
			tablePlaceholders.put("advice", advice != null ? advice : "");
			// Định dạng tổng giá tiền sang định dạng tiền tệ VNĐ
			tablePlaceholders.put("totalPrice", totalPrice != null ? formatCurrencyVND(totalPrice) : "0 VNĐ");
			tablePlaceholders.put("date", formattedDate != null ? formattedDate : "");
			tablePlaceholders.put("receptionistName", receptionistName != null ? receptionistName : "");
			tablePlaceholders.put("doctorName", doctorName != null ? doctorName : "");

			// Duyệt qua từng bảng trong tài liệu
			for (XWPFTable table : tables) {
				// Khởi tạo biến để xác định hàng tiêu đề
				int headerRowIndex = -1;
				// Cờ để xác định bảng là bảng dịch vụ hay bảng thuốc
				boolean isServiceTable = false;
				boolean isDrugTable = false;

				// Duyệt qua các hàng trong bảng để tìm hàng tiêu đề
				for (int i = 0; i < table.getRows().size(); i++) {
					XWPFTableRow row = table.getRow(i);
					// Lấy nội dung ô đầu tiên trong hàng và chuyển về chữ thường
					String cellText = row.getCell(0) != null ? row.getCell(0).getText().toLowerCase() : "";
					// Kiểm tra nếu ô chứa từ "service" thì đây là bảng dịch vụ
					if (cellText.contains("service")) {
						isServiceTable = true;
						headerRowIndex = i;
						break;
					// Kiểm tra nếu ô chứa từ "medicine" thì đây là bảng thuốc
					} else if (cellText.contains("medicine")) {
						isDrugTable = true;
						headerRowIndex = i;
						break;
					}
				}

				// Nếu không tìm thấy hàng tiêu đề, mặc định là hàng đầu tiên
				if (headerRowIndex == -1)
					headerRowIndex = 0;

				// Xóa các hàng dư thừa, chỉ giữ lại hàng tiêu đề
				while (table.getRows().size() > headerRowIndex + 1) {
					table.removeRow(table.getRows().size() - 1);
				}

				// Nếu là bảng dịch vụ và danh sách dịch vụ, giá không null
				if (isServiceTable && serviceNames != null && servicePrices != null) {
					// Duyệt qua danh sách tên dịch vụ
					for (int i = 0; i < serviceNames.size(); i++) {
						// Tạo một hàng mới trong bảng
						XWPFTableRow row = table.createRow();
						// Đảm bảo hàng có ít nhất 2 ô
						while (row.getTableCells().size() < 2) {
							row.addNewTableCell();
						}
						// Lấy ô tên dịch vụ và ô giá
						XWPFTableCell nameCell = row.getCell(0);
						XWPFTableCell priceCell = row.getCell(1);
						// Thêm đoạn văn nếu ô chưa có
						if (nameCell.getParagraphs().isEmpty())
							nameCell.addParagraph();
						if (priceCell.getParagraphs().isEmpty())
							priceCell.addParagraph();
						// Thay thế các placeholder trong đoạn văn
						replaceInParagraph(nameCell.getParagraphs().get(0), tablePlaceholders);
						replaceInParagraph(priceCell.getParagraphs().get(0), tablePlaceholders);
						// Tạo hoặc lấy run để đặt nội dung tên dịch vụ
						XWPFRun nameRun = nameCell.getParagraphs().get(0).getRuns().isEmpty()
								? nameCell.getParagraphs().get(0).createRun()
								: nameCell.getParagraphs().get(0).getRuns().get(0);
						// Tạo hoặc lấy run để đặt nội dung giá
						XWPFRun priceRun = priceCell.getParagraphs().get(0).getRuns().isEmpty()
								? priceCell.getParagraphs().get(0).createRun()
								: priceCell.getParagraphs().get(0).getRuns().get(0);
						// Định dạng font chữ và kích thước
						nameRun.setFontFamily("Times New Roman");
						nameRun.setFontSize(12);
						// Gán tên dịch vụ, nếu null thì hiển thị "N/A"
						nameRun.setText(serviceNames.get(i) != null ? serviceNames.get(i) : "N/A");
						priceRun.setFontFamily("Times New Roman");
						priceRun.setFontSize(12);
						// Gán giá dịch vụ, định dạng VNĐ
						priceRun.setText(servicePrices.get(i) != null ? formatCurrencyVND(servicePrices.get(i)) : "0 VNĐ");
					}
				}

				// Nếu là bảng thuốc và danh sách thuốc không null
				if (isDrugTable && drugs != null) {
					// Duyệt qua danh sách thuốc
					for (int i = 0; i < drugs.size(); i++) {
						// Tạo một hàng mới trong bảng
						XWPFTableRow row = table.createRow();
						// Đảm bảo hàng có ít nhất 4 ô
						while (row.getTableCells().size() < 4) {
							row.addNewTableCell();
						}
						// Lấy thông tin thuốc từ danh sách
						Map<String, Object> drug = drugs.get(i);
						String drugName = (String) drug.get("name");
						String instructions = (String) drug.get("instructions");
						Object quantityObj = drug.get("quantity");
						String unit = (String) drug.get("unit");
						BigDecimal price = (BigDecimal) drug.get("price");
						// Chuyển đổi số lượng thành số nguyên
						int quantity = quantityObj instanceof Integer ? (Integer) quantityObj : 0;

						// Lấy các ô trong hàng
						XWPFTableCell nameCell = row.getCell(0);
						XWPFTableCell xCell = row.getCell(1);
						XWPFTableCell qtyCell = row.getCell(2);
						XWPFTableCell priceCell = row.getCell(3);

						// Thêm đoạn văn nếu ô chưa có
						if (nameCell.getParagraphs().isEmpty())
							nameCell.addParagraph();
						if (xCell.getParagraphs().isEmpty())
							xCell.addParagraph();
						if (qtyCell.getParagraphs().isEmpty())
							qtyCell.addParagraph();
						if (priceCell.getParagraphs().isEmpty())
							priceCell.addParagraph();
						// Thay thế các placeholder trong đoạn văn
						replaceInParagraph(nameCell.getParagraphs().get(0), tablePlaceholders);
						replaceInParagraph(xCell.getParagraphs().get(0), tablePlaceholders);
						replaceInParagraph(qtyCell.getParagraphs().get(0), tablePlaceholders);
						replaceInParagraph(priceCell.getParagraphs().get(0), tablePlaceholders);

						// Tạo hoặc lấy run để đặt nội dung
						XWPFRun nameRun = nameCell.getParagraphs().get(0).getRuns().isEmpty()
								? nameCell.getParagraphs().get(0).createRun()
								: nameCell.getParagraphs().get(0).getRuns().get(0);
						XWPFRun xRun = xCell.getParagraphs().get(0).getRuns().isEmpty()
								? xCell.getParagraphs().get(0).createRun()
								: xCell.getParagraphs().get(0).getRuns().get(0);
						XWPFRun qtyRun = qtyCell.getParagraphs().get(0).getRuns().isEmpty()
								? qtyCell.getParagraphs().get(0).createRun()
								: qtyCell.getParagraphs().get(0).getRuns().get(0);
						XWPFRun priceRun = priceCell.getParagraphs().get(0).getRuns().isEmpty()
								? priceCell.getParagraphs().get(0).createRun()
								: priceCell.getParagraphs().get(0).getRuns().get(0);

						// Định dạng font chữ và kích thước
						nameRun.setFontFamily("Times New Roman");
						nameRun.setFontSize(12);
						// Gán tên thuốc, nếu null thì hiển thị "N/A"
						nameRun.setText(drugName != null ? drugName : "N/A");
						// Nếu có hướng dẫn sử dụng, thêm vào dưới tên thuốc
						if (instructions != null) {
							nameRun.addBreak();
							XWPFRun instrRun = nameCell.getParagraphs().get(0).createRun();
							instrRun.setFontFamily("Times New Roman");
							instrRun.setFontSize(12);
							instrRun.setItalic(true);
							instrRun.setText(instructions);
						}
						// Gán ký hiệu "x" cho cột thứ hai
						xRun.setText("x");
						// Gán số lượng và đơn vị
						qtyRun.setText(quantityObj != null ? quantity + (unit != null ? " " + unit : "") : "0");
						// Gán giá tiền, tính tổng giá (giá * số lượng)
						priceRun.setText(
								price != null ? formatCurrencyVND(price.multiply(BigDecimal.valueOf(quantity))) : "0 VNĐ");
					}
				}
			}
		}

	@FXML
	public void createAppointment(ActionEvent event) {
	    // Kiểm tra xem lịch hẹn đã được kiểm tra chưa
	    if (!isCheck) {
	        alert.errorMessage("Please check the schedule before creating appointments!");
	        return;
	    }
	    // Kiểm tra xem lịch hẹn có hợp lệ không
	    if (!isAvailableAppointment) {
	        alert.errorMessage("⚠ Cannot create appointment due to schedule conflicts!");
	        return;
	    }
	    // Kiểm tra xem bệnh nhân đã được chọn chưa
	    if (selectedPatient == null) {
	        alert.errorMessage("Please select a patient!");
	        return;
	    }

	    try {
	        // Tạo danh sách các dịch vụ được chọn và loại bỏ trùng lặp
	        List<ServiceData> selectedServices = new ArrayList<>();
	        Set<String> serviceIds = new HashSet<>(); // Để kiểm tra trùng lặp Service_id
	        for (Node node : vboxContainer.getChildren()) {
	            if (node instanceof AnchorPane) {
	                ComboBox<ServiceData> cbService = (ComboBox<ServiceData>) ((AnchorPane) node).lookup("#cb_service");
	                if (cbService != null && cbService.getValue() != null) {
	                    ServiceData service = cbService.getValue();
	                    // Kiểm tra trùng lặp Service_id
	                    if (!serviceIds.add(service.getServiceId())) {
	                        alert.errorMessage("Service: '" + service.getName() + "' has been selected multiple times. Please select service one time only!");
	                        return;
	                    }
	                    selectedServices.add(service);
	                }
	            }
	        }
	        // Kiểm tra xem có chọn dịch vụ nào chưa
	        if (selectedServices.isEmpty()) {
	            alert.errorMessage("Please select at least one service!");
	            return;
	        }

	        // Kết nối cơ sở dữ liệu
	        connect = Database.connectDB();
	        // Tắt chế độ tự động commit để thực hiện giao dịch
	        connect.setAutoCommit(false);

	        String insertAppointmentSQL = "INSERT INTO appointment (id, time, status, cancel_reason, Doctor_id, Patient_id, Urgency_level, Is_followup, Priority_score, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        String insertAppointmentServiceSQL = "INSERT INTO appointment_service (Appointment_id, Service_id) VALUES (?, ?)";
	        String checkDuplicateServiceSQL = "SELECT COUNT(*) FROM appointment_service WHERE Appointment_id = ? AND Service_id = ?";
	        String updateSlotSQL = "UPDATE AVAILABLE_SLOT SET Doctor_id = ?, Is_booked = ?, Appointment_id = ? WHERE Id = ?";
	        String findSlotSQL = "SELECT Id FROM AVAILABLE_SLOT WHERE Doctor_id = ? AND Slot_date = ? AND Slot_time = ? AND Is_booked = FALSE LIMIT 1";

	        // Chuẩn bị các câu lệnh SQL
	        PreparedStatement psFindSlot = connect.prepareStatement(findSlotSQL);
	        PreparedStatement psInsertAppointment = connect.prepareStatement(insertAppointmentSQL);
	        PreparedStatement psUpdateSlot = connect.prepareStatement(updateSlotSQL);
	        PreparedStatement psCheckDuplicateService = connect.prepareStatement(checkDuplicateServiceSQL);

	        // Duyệt qua danh sách thời gian và bác sĩ
	        for (int i = 0; i < selectedServiceNames.size(); i++) {
	            // Tạo ID duy nhất cho lịch hẹn
	            String appointmentId = UUID.randomUUID().toString();
	            String doctorId = selectedDoctorInfos.get(i).get("id");
	            String doctorName = selectedDoctorInfos.get(i).get("name");
	            LocalDate date = selectTimes.get(i);
	            LocalTime time = slotTimes.get(i);
	            LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);

	            // 1. Kiểm tra slot trống
	            psFindSlot.setString(1, doctorId);
	            psFindSlot.setDate(2, java.sql.Date.valueOf(date));
	            psFindSlot.setTime(3, java.sql.Time.valueOf(time));
	            ResultSet rs = psFindSlot.executeQuery();

	            if (rs.next()) {
	                // Lấy ID của slot
	                String slotId = rs.getString("Id");

	                // 2. Thêm lịch hẹn vào cơ sở dữ liệu
	                psInsertAppointment.setString(1, appointmentId);
	                psInsertAppointment.setTimestamp(2, Timestamp.valueOf(appointmentDateTime));
	                psInsertAppointment.setString(3, "Coming");
	                psInsertAppointment.setString(4, "");
	                psInsertAppointment.setString(5, doctorId);
	                psInsertAppointment.setString(6, selectedPatient.getPatientId());
	                psInsertAppointment.setInt(7, urgency);
	                psInsertAppointment.setBoolean(8, isFollowup);
	                psInsertAppointment.setInt(9, 0); // priority_score
	                psInsertAppointment.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
	                psInsertAppointment.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
	                psInsertAppointment.executeUpdate();

	                // 3. Cập nhật trạng thái slot đã đặt
	                psUpdateSlot.setString(1, doctorId);
	                psUpdateSlot.setBoolean(2, true);
	                psUpdateSlot.setString(3, appointmentId);
	                psUpdateSlot.setString(4, slotId);
	                psUpdateSlot.executeUpdate();

	                // 4. Thêm các dịch vụ tương ứng với lịch hẹn
	                for (ServiceData serviceData : selectedServices) {
	                    // Kiểm tra xem dịch vụ đã được thêm cho lịch hẹn này chưa
	                    psCheckDuplicateService.setString(1, appointmentId);
	                    psCheckDuplicateService.setString(2, serviceData.getServiceId());
	                    ResultSet checkRs = psCheckDuplicateService.executeQuery();
	                    checkRs.next();
	                    if (checkRs.getInt(1) > 0) {
	                        connect.rollback();
	                        alert.errorMessage("Service: '" + serviceData.getName() + "' has been added to this appointment!");
	                        return;
	                    }

	                    PreparedStatement psAppointmentService = connect.prepareStatement(insertAppointmentServiceSQL);
	                    psAppointmentService.setString(1, appointmentId);
	                    psAppointmentService.setString(2, serviceData.getServiceId());
	                    psAppointmentService.executeUpdate();
	                }
	            } else {
	                // Nếu không tìm thấy slot trống, hiển thị thông báo lỗi
	                connect.rollback();
	                alert.errorMessage("⚠ No empty slot for " + doctorName + " at " + time + " on " + date);
	                return;
	            }
	        }

	        // Commit giao dịch
	        connect.commit();

	        // In hóa đơn
	        List<String> serviceNames = selectedServices.stream().map(ServiceData::getName).collect(Collectors.toList());
	        List<BigDecimal> servicePrices = selectedServices.stream().map(ServiceData::getPrice).collect(Collectors.toList());
	        try {
	            // Gọi hàm xuất hóa đơn
	            generateInvoiceDocx(serviceNames, servicePrices, getReceptionistName(connect));
	        } catch (IOException e) {
	            e.printStackTrace();
	            alert.errorMessage("Error generating invoice: " + e.getMessage());
	        }

	        // Hiển thị thông báo thành công
	        alert.successMessage("✔ Appointment(s) created successfully!");

	        // Xóa các danh sách tạm
	        selectedServiceNames.clear();
	        selectedDoctorInfos.clear();
	        selectedSlotTimes.clear();
	        selectTimes.clear();
	        slotTimes.clear();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        alert.errorMessage("❌ Error creating appointment: " + e.getMessage());
	        try {
	            if (connect != null) {
	                connect.rollback();
	            }
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	    } finally {
	        try {
	            if (connect != null) {
	                connect.setAutoCommit(true);
	                connect.close();
	            }
	        } catch (SQLException closeEx) {
	            closeEx.printStackTrace();
	        }
	    }
	}
		// Hàm tạo và xuất hóa đơn dưới dạng tài liệu Word
		private void generateInvoiceDocx(List<String> serviceNames, List<BigDecimal> servicePrices, String receptionistName)
				throws IOException {
			// Ghi log bắt đầu quá trình xuất hóa đơn
			LOGGER.info("Starting invoice generation. Service count: " + (serviceNames != null ? serviceNames.size() : 0));
			// Kiểm tra xem bệnh nhân đã được chọn chưa
			if (selectedPatient == null) {
				LOGGER.severe("Selected patient is null.");
				throw new IllegalStateException("Selected patient cannot be null.");
			}
			// Kiểm tra tính hợp lệ của danh sách dịch vụ và giá
			if (serviceNames == null || servicePrices == null || serviceNames.size() != servicePrices.size()) {
				LOGGER.severe(
						"Mismatch in service data: serviceNames=" + serviceNames + ", servicePrices=" + servicePrices);
				throw new IllegalStateException("Invalid service names or prices.");
			}

			// Tạo thư mục lưu trữ nếu chưa tồn tại
			Files.createDirectories(Paths.get("Word"));
			// Tạo tên file hóa đơn với ID duy nhất
			String destFileName = "Word/invoice_details_" + UUID.randomUUID().toString() + ".docx";
			// Đường dẫn file mẫu hóa đơn
			File sourceFile = new File("Word/INVOICE.docx");
			Path destPath = Paths.get(destFileName);

			// Kiểm tra xem file mẫu có tồn tại không
			if (!sourceFile.exists()) {
				LOGGER.severe("Source file Word/INVOICE.docx not found.");
				throw new IOException("Source file not found.");
			}

			// Xóa file đích nếu đã tồn tại
			if (Files.exists(destPath)) {
				Files.delete(destPath);
			}
			// Sao chép file mẫu sang file đích
			Files.copy(sourceFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

			// Mở tài liệu Word để chỉnh sửa
			try (XWPFDocument doc = new XWPFDocument(new FileInputStream(destFileName))) {
				// Tính tuổi bệnh nhân
				int age = selectedPatient != null && selectedPatient.getBirthDate() != null
						? Period.between(selectedPatient.getBirthDate(), LocalDate.now()).getYears()
						: 0;
				// Tính tổng giá tiền
				BigDecimal totalPrice = BigDecimal.ZERO;
				for (BigDecimal price : servicePrices) {
					totalPrice = totalPrice.add(price != null ? price : BigDecimal.ZERO);
				}

				ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
				String formattedDate = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

				// Tạo các placeholder và giá trị thay thế
				Map<String, String> placeholders = new HashMap<>();
				placeholders.put("patientId", selectedPatient.getPatientId());
				placeholders.put("patientName", selectedPatient.getName());
				placeholders.put("age", String.valueOf(age));
				placeholders.put("gender", selectedPatient.getGender());
				placeholders.put("address", selectedPatient.getAddress());
				placeholders.put("totalPrice", formatCurrencyVND(totalPrice));
				placeholders.put("date", formattedDate);
				placeholders.put("receptionistName", receptionistName);

				// Thay thế các placeholder trong tài liệu
				replacePlaceholders(doc, placeholders);
				// Thay thế các placeholder trong bảng
				replaceTablePlaceholders(doc, serviceNames, servicePrices, null, totalPrice, formattedDate,
						receptionistName, null, null, null);

				// Ghi tài liệu đã chỉnh sửa vào file đích
				try (FileOutputStream fos = new FileOutputStream(destFileName)) {
					doc.write(fos);
					LOGGER.info("Invoice document successfully written to " + destFileName);
				}

				// Mở tài liệu hóa đơn nếu hệ thống hỗ trợ
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().open(new File(destFileName));
					} catch (IOException e) {
						LOGGER.severe("Error opening invoice document: " + destFileName + ": " + e.getMessage());
						alert.errorMessage("Failed to open invoice file: " + e.getMessage());
					}
				}

				// Hiển thị thông báo thành công
				alert.successMessage("Invoice successfully exported to " + destFileName);
			} catch (IOException e) {
				// Xử lý lỗi IO
				LOGGER.severe("IO Exception during invoice document generation: " + e.getMessage());
				alert.errorMessage("Error exporting invoice: " + e.getMessage());
				throw e;
			}
		}

		// Hàm xóa nội dung gợi ý lịch hẹn
		public void clearSuggestion(ActionEvent event) {
			txt_suggest.clear();
		}
		/*
		 * =====================QUẢN LÝ LỊCH HẸN VÀ IN HÓA ĐƠN THUỐC
		 *========================================
		 */

		// Hàm tải dữ liệu lịch hẹn từ cơ sở dữ liệu
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

			// Kết nối cơ sở dữ liệu
			connect = Database.connectDB();
			try {
				// Chuẩn bị và thực thi câu lệnh SQL
				prepare = connect.prepareStatement(sql);
				result = prepare.executeQuery();
				// Xóa danh sách lịch hẹn hiện tại
				appoinmentListData.clear();

				// Duyệt qua kết quả truy vấn và thêm vào danh sách
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
					String prescriptionStatus = result.getString("Prescription_Status"); // Trạng thái đơn thuốc
					Timestamp createdDate = result.getTimestamp("create_date");
					Timestamp lastModifiedDate = result.getTimestamp("update_date");

					// Tạo đối tượng AppointmentData và thêm vào danh sách
					appoinmentListData.add(new AppointmentData(id, time, status, reason, doctorId, patientId, serviceId,
							serviceName, prescriptionStatus, createdDate, lastModifiedDate, patientName, doctorName,
							contactNumber));
				}
			} catch (SQLException e) {
				// Xử lý lỗi SQL
				e.printStackTrace();
			} finally {
				// Đóng kết nối
				try {
					if (connect != null)
						connect.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// Hàm cập nhật thông tin lịch hẹn
		public void appointmentUpdateBtn() {
	        // Get selected appointment
	        AppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();
	        if (selectedAppointment == null) {
	            alert.errorMessage("Please select an appointment to update!");
	            return;
	        }

	        // Get current appointment details
	        String appointmentId = selectedAppointment.getId();
	        String currentStatus = selectedAppointment.getStatus();
	        String currentPrescriptionStatus = selectedAppointment.getPrescriptionStatus();
	        String currentPatientId = selectedAppointment.getPatientId();
	        String currentDoctorId = selectedAppointment.getDoctorId();

	        // Get updated values from input fields
	        String newServiceName = appointment_serviceName.getText() != null ? appointment_serviceName.getText().trim() : "";
	        PatientData newPatient = appointment_patient.getValue();
	        DoctorData newDoctor = appointment_doctor.getValue();
	        String newStatus = appointment_status.getValue();
	        String newCancelReason = appointment_cancelReason.getText() != null ? appointment_cancelReason.getText().trim() : "";
	        LocalDate newDate = appointment_date.getValue();
	        Integer newHour = spHour1.getValue();
	        Integer newMinute = spMinute1.getValue();

	        // Validate inputs
	        if (newServiceName.isEmpty() || newPatient == null || newDoctor == null ||
	                newStatus == null || newDate == null || newHour == null || newMinute == null) {
	            alert.errorMessage("Please fill all required fields!");
	            return;
	        }

	        // Combine date and time
	        LocalDateTime newDateTime;
	        newDateTime = LocalDateTime.of(newDate, LocalTime.of(newHour, newMinute));
//	        try {
//	            newDateTime = LocalDateTime.of(newDate, LocalTime.of(newHour, newMinute));
//	            if (newDateTime.isBefore(LocalDateTime.now())) {
//	                alert.errorMessage("Appointment date and time cannot be in the past!");
//	                return;
//	            }
//	        } catch (Exception e) {
//	            alert.errorMessage("Invalid date or time!");
//	            return;
//	        }

	        // Format time for SQL
	        String sqlTimeStr = newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

	        // Prevent status change if Prescription_status is Paid or Created
	        if ((currentPrescriptionStatus.equals("Paid") || currentPrescriptionStatus.equals("Created")) &&
	                !newStatus.equals(currentStatus)) {
	            alert.errorMessage("Cannot change appointment status when Prescription_status is 'Paid' or 'Created'!");
	            return;
	        }

	        // Check if patient or doctor has changed
	        boolean patientChanged = !newPatient.getPatientId().equals(currentPatientId);
	        boolean doctorChanged = !newDoctor.getId().equals(currentDoctorId);

	        Connection conn = null;
	        try {
	            conn = Database.connectDB();
	            conn.setAutoCommit(false);

	            // Get Service_id from service name
	            String serviceId = null;
	            String getServiceIdSQL = "SELECT Id FROM SERVICE WHERE Name = ?";
	            PreparedStatement psService = conn.prepareStatement(getServiceIdSQL);
	            psService.setString(1, newServiceName);
	            ResultSet rsService = psService.executeQuery();
	            if (rsService.next()) {
	                serviceId = rsService.getString("Id");
	            } else {
	                alert.errorMessage("Invalid service name!");
	                conn.rollback();
	                return;
	            }

	            // Check if prescription exists
	            String checkPrescriptionSQL = "SELECT Id, status FROM PRESCRIPTION WHERE Appointment_id = ?";
	            PreparedStatement psCheck = conn.prepareStatement(checkPrescriptionSQL);
	            psCheck.setString(1, appointmentId);
	            ResultSet rs = psCheck.executeQuery();
	            boolean prescriptionExists = rs.next();
	            String prescriptionId = prescriptionExists ? rs.getString("Id") : null;
	            String prescriptionStatus = prescriptionExists ? rs.getString("status") : null;

	            // If prescription is Paid and patient/doctor changed, warn user
	            if (prescriptionExists && prescriptionStatus.equals("Paid") && (patientChanged || doctorChanged)) {
	                Alert warning = new Alert(Alert.AlertType.WARNING);
	                warning.setTitle("Prescription Already Paid");
	                warning.setHeaderText("The prescription for this appointment is already paid.");
	                warning.setContentText("Updating patient or doctor will reset the Prescription_status to 'Created' and require a new invoice. Proceed?");
	                warning.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
	                if (warning.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
	                    conn.rollback();
	                    return;
	                }
	            }

	            // Check doctor availability
	            String sqlDoctor = "SELECT COUNT(*) FROM AVAILABLE_SLOT WHERE Doctor_id = ? AND Slot_time = ? AND Slot_date = ? AND Is_booked = TRUE AND Appointment_id != ?";
	            PreparedStatement psDoctor = conn.prepareStatement(sqlDoctor);
	            psDoctor.setString(1, newDoctor.getId());
	            psDoctor.setString(2, newDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	            psDoctor.setString(3, newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	            psDoctor.setString(4, appointmentId);
	            ResultSet rsDoctor = psDoctor.executeQuery();
	            if (rsDoctor.next() && rsDoctor.getInt(1) > 0) {
	                alert.errorMessage("Doctor " + newDoctor.getName() + " is already booked at this time!");
	                conn.rollback();
	                return;
	            }

	            // Check patient availability
	            String sqlPatient = "SELECT COUNT(*) FROM APPOINTMENT WHERE Patient_id = ? AND Time = ? AND Id != ?";
	            PreparedStatement psPatient = conn.prepareStatement(sqlPatient);
	            psPatient.setString(1, newPatient.getPatientId());
	            psPatient.setString(2, sqlTimeStr);
	            psPatient.setString(3, appointmentId);
	            ResultSet rsPatient = psPatient.executeQuery();
	            if (rsPatient.next() && rsPatient.getInt(1) > 0) {
	                alert.errorMessage("Patient already has an appointment at this time!");
	                conn.rollback();
	                return;
	            }

	            // Update appointment
	            String updateAppointmentSQL = "UPDATE APPOINTMENT SET Patient_id = ?, Doctor_id = ?, Status = ?, Cancel_reason = ?, Time = ?, Update_date = ?, Prescription_status = ? WHERE Id = ?";
	            PreparedStatement psUpdate = conn.prepareStatement(updateAppointmentSQL);
	            psUpdate.setString(1, newPatient.getPatientId());
	            psUpdate.setString(2, newDoctor.getId());
	            psUpdate.setString(3, newStatus);
	            psUpdate.setString(4, newCancelReason.isEmpty() ? null : newCancelReason);
	            psUpdate.setString(5, sqlTimeStr);
	            psUpdate.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
	            psUpdate.setString(7, (prescriptionExists && prescriptionStatus.equals("Paid") && (patientChanged || doctorChanged)) ? "Created" : currentPrescriptionStatus);
	            psUpdate.setString(8, appointmentId);
	            int rowsAffected = psUpdate.executeUpdate();

	            // Update APPOINTMENT_SERVICE
	            String deleteServiceSQL = "DELETE FROM APPOINTMENT_SERVICE WHERE Appointment_id = ?";
	            PreparedStatement psDeleteService = conn.prepareStatement(deleteServiceSQL);
	            psDeleteService.setString(1, appointmentId);
	            psDeleteService.executeUpdate();

	            String insertServiceSQL = "INSERT INTO APPOINTMENT_SERVICE (Appointment_id, Service_id) VALUES (?, ?)";
	            PreparedStatement psInsertService = conn.prepareStatement(insertServiceSQL);
	            psInsertService.setString(1, appointmentId);
	            psInsertService.setString(2, serviceId);
	            psInsertService.executeUpdate();

	            // Update prescription if it exists and patient or doctor changed
	            if (prescriptionExists && (patientChanged || doctorChanged)) {
	                String updatePrescriptionSQL = "UPDATE PRESCRIPTION SET Patient_id = ?, Doctor_id = ?, Update_date = ? WHERE Id = ?";
	                PreparedStatement psUpdatePrescription = conn.prepareStatement(updatePrescriptionSQL);
	                psUpdatePrescription.setString(1, newPatient.getPatientId());
	                psUpdatePrescription.setString(2, newDoctor.getId());
	                psUpdatePrescription.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
	                psUpdatePrescription.setString(4, prescriptionId);
	                psUpdatePrescription.executeUpdate();
	            }

	            // Update AVAILABLE_SLOT
	            String updateSlotSQL = "UPDATE AVAILABLE_SLOT SET Is_booked = FALSE, Appointment_id = NULL WHERE Appointment_id = ?";
	            PreparedStatement psUpdateSlot = conn.prepareStatement(updateSlotSQL);
	            psUpdateSlot.setString(1, appointmentId);
	            psUpdateSlot.executeUpdate();

	            String insertSlotSQL = "INSERT INTO AVAILABLE_SLOT (Id, Doctor_id, Slot_time, Slot_date, Duration_minutes, Is_booked, Appointment_id) " +
	                                  "VALUES (UUID(), ?, ?, ?, 15, TRUE, ?) ON DUPLICATE KEY UPDATE Is_booked = TRUE, Appointment_id = ?";
	            PreparedStatement psInsertSlot = conn.prepareStatement(insertSlotSQL);
	            psInsertSlot.setString(1, newDoctor.getId());
	            psInsertSlot.setString(2, newDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	            psInsertSlot.setString(3, newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	            psInsertSlot.setString(4, appointmentId);
	            psInsertSlot.setString(5, appointmentId);
	            psInsertSlot.executeUpdate();

	            if (rowsAffected > 0) {
	                conn.commit();
	                alert.successMessage("Appointment updated successfully!");
	                loadAppointmentData();
	                appointmentClearBtn();
	            } else {
	                conn.rollback();
	                alert.errorMessage("Failed to update appointment!");
	            }
	        } catch (SQLException e) {
	            try {
	                if (conn != null) conn.rollback();
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	            e.printStackTrace();
	            alert.errorMessage("Error updating appointment: " + e.getMessage());
	        } finally {
	            try {
	                if (conn != null) conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

		// Hàm xuất đơn thuốc dưới dạng tài liệu Word
		@FXML
		public void appointmentPrescriptionBtn() {
	        // Lấy lịch hẹn được chọn
	        AppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();

	        // Kiểm tra xem đã chọn lịch hẹn chưa
	        if (selectedAppointment == null) {
	            alert.errorMessage("Please select an appointment first.");
	            return;
	        }

	        // Kiểm tra trạng thái lịch hẹn
	        if (!selectedAppointment.getStatus().equals(AppointmentStatus.Finish.toString())) {
	            alert.errorMessage("This appointment has not finished yet.");
	            return;
	        }

	        try {
	            // Kết nối cơ sở dữ liệu
	            connect = Database.connectDB();
	            if (connect == null) {
	                throw new SQLException("Failed to establish database connection.");
	            }
	            connect.setAutoCommit(false); // Start transaction

	            // Câu lệnh SQL lấy thông tin đơn thuốc
	            String prescriptionSQL = "SELECT p.Id, p.Diagnose, p.Advice, p.Doctor_id, p.Patient_id, "
	                    + "ua.Name AS Doctor_name, pt.Name AS Patient_name, pt.Gender, pt.Address, pt.Date_of_birth, pt.Patient_id "
	                    + "FROM PRESCRIPTION p "
	                    + "JOIN DOCTOR d ON p.Doctor_id = d.Doctor_id "
	                    + "JOIN USER_ACCOUNT ua ON ua.Id = d.Doctor_id "
	                    + "JOIN PATIENT pt ON p.Patient_id = pt.Patient_id "
	                    + "WHERE p.Appointment_id = ?";
	            // Câu lệnh SQL lấy chi tiết đơn thuốc
	            String prescriptionDetailsSQL = "SELECT pd.Drug_id, pd.Quantity, pd.Instructions, dr.Name AS Drug_name, dr.Unit, dr.Price "
	                    + "FROM PRESCRIPTION_DETAILS pd "
	                    + "JOIN DRUG dr ON pd.Drug_id = dr.Id "
	                    + "WHERE pd.Prescription_id = ?";
	            // Câu lệnh SQL cập nhật thông tin đơn thuốc
	            String updatePrescriptionSQL = "UPDATE PRESCRIPTION SET Doctor_id = ?, Patient_id = ? WHERE Id = ?";
	            // Câu lệnh SQL cập nhật trạng thái đơn thuốc
	            String updatePrescriptionStatusSQL = "UPDATE APPOINTMENT SET Prescription_Status = ? WHERE id = ?";

	            // Thực thi truy vấn đơn thuốc
	            PreparedStatement psPrescription = connect.prepareStatement(prescriptionSQL);
	            psPrescription.setString(1, selectedAppointment.getId());
	            ResultSet rsPrescription = psPrescription.executeQuery();

	            // Kiểm tra xem có đơn thuốc không
	            if (!rsPrescription.next()) {
	                alert.errorMessage("No prescription found!");
	                connect.rollback();
	                connect.close();
	                return;
	            }

	            // Lấy thông tin từ đơn thuốc
	            String prescriptionId = rsPrescription.getString("Id");
	            String currentDoctorId = rsPrescription.getString("Doctor_id");
	            String currentPatientId = rsPrescription.getString("Patient_id");
	            String patientName = rsPrescription.getString("Patient_name");
	            String doctorName = rsPrescription.getString("Doctor_name");
	            String diagnose = rsPrescription.getString("Diagnose");
	            String advice = rsPrescription.getString("Advice");
	            String gender = rsPrescription.getString("Gender");
	            String address = rsPrescription.getString("Address");
	            String patientId = rsPrescription.getString("Patient_id");
	            java.sql.Date dob = rsPrescription.getDate("Date_of_birth");
	            // Tính tuổi bệnh nhân
	            int age = (dob != null) ? Period.between(dob.toLocalDate(), LocalDate.now()).getYears() : 0;

	            // Kiểm tra xem Doctor_id hoặc Patient_id có thay đổi không
	            boolean needsUpdate = !currentDoctorId.equals(selectedAppointment.getDoctorId()) ||
	                                 !currentPatientId.equals(selectedAppointment.getPatientId());

	            if (needsUpdate) {
	                // Cập nhật Doctor_id và Patient_id trong PRESCRIPTION
	                PreparedStatement psUpdatePrescription = connect.prepareStatement(updatePrescriptionSQL);
	                psUpdatePrescription.setString(1, selectedAppointment.getDoctorId());
	                psUpdatePrescription.setString(2, selectedAppointment.getPatientId());
	                psUpdatePrescription.setString(3, prescriptionId);
	                psUpdatePrescription.executeUpdate();

	                // Cập nhật thông tin hiển thị
	                String sqlFetchUpdatedInfo = "SELECT ua.Name AS Doctor_name, pt.Name AS Patient_name, pt.Gender, pt.Address, pt.Date_of_birth "
	                        + "FROM USER_ACCOUNT ua, PATIENT pt "
	                        + "WHERE ua.Id = ? AND pt.Patient_id = ?";
	                PreparedStatement psFetchUpdatedInfo = connect.prepareStatement(sqlFetchUpdatedInfo);
	                psFetchUpdatedInfo.setString(1, selectedAppointment.getDoctorId());
	                psFetchUpdatedInfo.setString(2, selectedAppointment.getPatientId());
	                ResultSet rsUpdatedInfo = psFetchUpdatedInfo.executeQuery();
	                if (rsUpdatedInfo.next()) {
	                    doctorName = rsUpdatedInfo.getString("Doctor_name");
	                    patientName = rsUpdatedInfo.getString("Patient_name");
	                    gender = rsUpdatedInfo.getString("Gender");
	                    address = rsUpdatedInfo.getString("Address");
	                    dob = rsUpdatedInfo.getDate("Date_of_birth");
	                    age = (dob != null) ? Period.between(dob.toLocalDate(), LocalDate.now()).getYears() : 0;
	                }
	            }

	            // Thực thi truy vấn chi tiết đơn thuốc
	            PreparedStatement psDetails = connect.prepareStatement(prescriptionDetailsSQL);
	            psDetails.setString(1, prescriptionId);
	            ResultSet rsDetails = psDetails.executeQuery();

	            // Tạo danh sách thuốc
	            List<Map<String, Object>> drugs = new ArrayList<>();
	            BigDecimal totalPrice = BigDecimal.ZERO;
	            while (rsDetails.next()) {
	                // Thêm thông tin thuốc vào danh sách
	                Map<String, Object> drug = new HashMap<>();
	                drug.put("name", rsDetails.getString("Drug_name"));
	                drug.put("instructions", rsDetails.getString("Instructions"));
	                drug.put("quantity", rsDetails.getInt("Quantity"));
	                drug.put("unit", rsDetails.getString("Unit"));
	                drug.put("price", rsDetails.getBigDecimal("Price"));
	                drugs.add(drug);
	                // Tính tổng giá tiền
	                totalPrice = totalPrice.add(
	                        rsDetails.getBigDecimal("Price").multiply(BigDecimal.valueOf(rsDetails.getInt("Quantity"))));
	            }

	            // Kiểm tra trạng thái đơn thuốc
	            if ("Paid".equals(selectedAppointment.getPrescriptionStatus()) && !needsUpdate) {
	                // Nếu đơn thuốc đã thanh toán và không cần cập nhật, mở file đơn thuốc
	                String filePath = "Word/prescription_" + selectedAppointment.getId() + ".docx";
	                File file = new File(filePath);
	                if (file.exists()) {
	                    try {
	                        alert.successMessage("Opening prescription file: " + filePath);
	                        LOGGER.info("Opening prescription file: " + filePath);
	                        Desktop.getDesktop().open(file);
	                        connect.commit();
	                        connect.close();
	                        return;
	                    } catch (IOException e) {
	                        LOGGER.severe("Failed to open prescription file: " + e.getMessage());
	                        alert.errorMessage("Failed to open prescription file: " + e.getMessage());
	                        connect.rollback();
	                        connect.close();
	                        return;
	                    }
	                } else {
	                    LOGGER.warning("Prescription file not found: " + filePath);
	                    // Tiếp tục tạo mới file nếu không tìm thấy
	                }
	            }

	            // Tạo thư mục lưu trữ nếu chưa tồn tại
	            Files.createDirectories(Paths.get("Word"));
	            // Tạo tên file đơn thuốc
	            String destFileName = "Word/prescription_" + selectedAppointment.getId() + ".docx";
	            // Đường dẫn file mẫu đơn thuốc
	            File srcFile = new File("Word/PRESCRIPTION.docx");
	            Path destPath = Paths.get(destFileName);

	            // Kiểm tra xem file mẫu có tồn tại không
	            if (!srcFile.exists()) {
	                connect.rollback();
	                connect.close();
	                throw new IOException("Source file not found.");
	            }

	            // Xóa file đích nếu đã tồn tại
	            if (Files.exists(destPath)) {
	                Files.delete(destPath);
	            }
	            // Sao chép file mẫu sang file đích
	            Files.copy(srcFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

	            // Mở tài liệu Word để chỉnh sửa
	            try (XWPFDocument doc = new XWPFDocument(new FileInputStream(destFileName))) {
	                String formattedDate = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
	                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

	                // Tạo các placeholder và giá trị thay thế
	                Map<String, String> placeholders = new HashMap<>();
	                placeholders.put("patientId", selectedAppointment.getPatientId());
	                placeholders.put("patientName", patientName);
	                placeholders.put("age", String.valueOf(age));
	                placeholders.put("gender", gender);
	                placeholders.put("address", address);
	                placeholders.put("diagnose", diagnose);
	                placeholders.put("advice", advice);
	                placeholders.put("totalPrice", formatCurrencyVND(totalPrice));
	                placeholders.put("date", formattedDate);
	                placeholders.put("doctorName", doctorName);

	                // Thay thế các placeholder trong tài liệu
	                replacePlaceholders(doc, placeholders);
	                // Thay thế các placeholder trong bảng
	                replaceTablePlaceholders(doc, null, null, drugs, totalPrice, formattedDate, null, doctorName, diagnose,
	                        advice);

	                // Ghi tài liệu đã chỉnh sửa vào file đích
	                try (FileOutputStream fos = new FileOutputStream(destFileName)) {
	                    doc.write(fos);
	                }

	                // Mở tài liệu đơn thuốc nếu hệ thống hỗ trợ
	                if (Desktop.isDesktopSupported()) {
	                    Desktop.getDesktop().open(new File(destFileName));
	                }

	                // Hiển thị thông báo thành công
	                alert.successMessage("Prescription document successfully exported to " + destFileName);

	                // Cập nhật trạng thái đơn thuốc
	                PreparedStatement psUpdate = connect.prepareStatement(updatePrescriptionStatusSQL);
	                psUpdate.setString(1, "Paid");
	                psUpdate.setString(2, selectedAppointment.getId());
	                psUpdate.executeUpdate();
	                // Cập nhật trạng thái trong đối tượng và làm mới bảng
	                selectedAppointment.setPrescriptionStatus("Paid");
	                appointments_tableView.refresh();

	                connect.commit(); // Commit transaction
	            }

	        } catch (SQLException e) {
	            // Xử lý lỗi SQL
	            e.printStackTrace();
	            alert.errorMessage("❌ Database error: " + e.getMessage());
	            try {
	                if (connect != null) connect.rollback();
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	        } catch (IOException e) {
	            // Xử lý lỗi IO
	            e.printStackTrace();
	            alert.errorMessage("❌ File error: " + e.getMessage());
	            try {
	                if (connect != null) connect.rollback();
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	        } finally {
	            // Đóng kết nối
	            if (connect != null) {
	                try {
	                    connect.setAutoCommit(true);
	                    connect.close();
	                } catch (SQLException e) {
	                    LOGGER.severe("Error closing connection: " + e.getMessage());
	                }
	            }
	        }
	    }

		// Hàm xử lý khi chọn một lịch hẹn từ bảng
		public void appointmentSelect() {
			// Lấy lịch hẹn được chọn
			AppointmentData appointmentData = appointments_tableView.getSelectionModel().getSelectedItem();
			int index = appointments_tableView.getSelectionModel().getSelectedIndex();
			// Kiểm tra xem có chọn lịch hẹn nào không
			if (index <= -1 || appointmentData == null) {
				System.out.println("No appointment selected");
				return;
			}

			// Tải danh sách bác sĩ theo dịch vụ
			loadComboBoxDoctor(appointmentData.getServiceId(), appointment_doctor);
			// Chọn bác sĩ tương ứng
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

			// Chọn bệnh nhân tương ứng
			for (PatientData patient : appointment_patient.getItems()) {
				if (patient.getPatientId().equals(appointmentData.getPatientId())) {
					appointment_patient.getSelectionModel().select(patient);
					break;
				}
			}

			appointment_date.setValue(appointmentData.getLocalDate());
			// Gán giờ và phút từ lịch hẹn
			spHour1.getValueFactory().setValue(appointmentData.getLocalTime().getHour());
			spMinute1.getValueFactory().setValue(appointmentData.getLocalTime().getMinute());
			appointment_createdDate.setText(FormatterUtils.formatTime(appointmentData.getCreateDate().toString()));
			appointment_updatedDate.setText(FormatterUtils.formatTime(appointmentData.getUpdateDate().toString()));
		}

		// Hàm xóa lịch hẹn
		public void appointmentDeleteBtn() {
		    // Lấy lịch hẹn được chọn
			AlertMessage alert1 = new AlertMessage();
		    AppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();
		    String appointmentID = "";
		    if (selectedAppointment != null) {
		        appointmentID = selectedAppointment.getId();
		    } else {
		        // Hiển thị thông báo lỗi nếu chưa chọn lịch hẹn
		        alert.errorMessage("Please select an appointment to delete.");
		        return;
		    }
		    		  
		    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		    alert.setTitle("Delete Confirmation");
		    alert.setHeaderText("Are you sure you want to delete this appointment?");
		    alert.setContentText("This action cannot be undone.");

		    Optional<ButtonType> result = alert.showAndWait();
		    if (result.isPresent() && result.get() == ButtonType.OK) {
		        Connection conn = null;
		        try {
		            conn = Database.connectDB();
		            conn.setAutoCommit(false); // Start transaction

		            // Check appointment status
		            String checkStatusSQL = "SELECT Status FROM APPOINTMENT WHERE id = ?";
		            PreparedStatement psCheck = conn.prepareStatement(checkStatusSQL);
		            psCheck.setString(1, appointmentID);
		            ResultSet rs = psCheck.executeQuery();
		            boolean isFinished = false;
		            if (rs.next()) {
		                isFinished = "Finish".equals(rs.getString("Status"));
		            } else {
		                conn.rollback();
		                alert1.errorMessage("Appointment not found.");
		                conn.close();
		                return;
		            }

		            if (isFinished) {
		                // Show warning for finished appointment
		                Alert warning = new Alert(Alert.AlertType.WARNING);
		                warning.setTitle("Finished Appointment");
		                warning.setHeaderText("This appointment is marked as 'Finish'.");
		                warning.setContentText("Deleting this appointment will also delete all associated services, prescriptions, and prescription details. Do you want to proceed?");
		                ButtonType proceedButton = new ButtonType("Proceed");
		                ButtonType cancelButton = new ButtonType("Cancel");
		                warning.getButtonTypes().setAll(proceedButton, cancelButton);

		                Optional<ButtonType> warningResult = warning.showAndWait();
		                if (warningResult.isEmpty() || warningResult.get() != proceedButton) {
		                    conn.rollback();
		                    conn.close();
		                    return; // User canceled
		                }

		                // Delete associated prescription details
		                String deletePrescriptionDetailsSQL = "DELETE FROM PRESCRIPTION_DETAILS WHERE Prescription_id IN " +
		                                                     "(SELECT Id FROM PRESCRIPTION WHERE Appointment_id = ?)";
		                PreparedStatement psDeletePrescriptionDetails = conn.prepareStatement(deletePrescriptionDetailsSQL);
		                psDeletePrescriptionDetails.setString(1, appointmentID);
		                psDeletePrescriptionDetails.executeUpdate();

		                // Delete associated prescriptions
		                String deletePrescriptionSQL = "DELETE FROM PRESCRIPTION WHERE Appointment_id = ?";
		                PreparedStatement psDeletePrescriptions = conn.prepareStatement(deletePrescriptionSQL);
		                psDeletePrescriptions.setString(1, appointmentID);
		                psDeletePrescriptions.executeUpdate();
		            }

		            // Delete associated appointment_service entries (for both Finish and non-Finish)
		            String deleteAppointmentServiceSQL = "DELETE FROM APPOINTMENT_SERVICE WHERE Appointment_id = ?";
		            PreparedStatement psDeleteAppointmentService = conn.prepareStatement(deleteAppointmentServiceSQL);
		            psDeleteAppointmentService.setString(1, appointmentID);
		            psDeleteAppointmentService.executeUpdate();

		            // Reset associated slot in AVAILABLE_SLOT
		            String updateSlotSQL = "UPDATE AVAILABLE_SLOT SET Is_booked = FALSE, Appointment_id = NULL WHERE Appointment_id = ?";
		            PreparedStatement psUpdateSlot = conn.prepareStatement(updateSlotSQL);
		            psUpdateSlot.setString(1, appointmentID);
		            psUpdateSlot.executeUpdate();

		            // Delete the appointment
		            String deleteAppointmentSQL = "DELETE FROM APPOINTMENT WHERE id = ?";
		            PreparedStatement psDeleteAppointment = conn.prepareStatement(deleteAppointmentSQL);
		            psDeleteAppointment.setString(1, appointmentID);
		            int rowsDeleted = psDeleteAppointment.executeUpdate();

		            if (rowsDeleted > 0) {
		                conn.commit(); // Commit transaction
		                appointmentClearBtn(); // Clear form
		                loadAppointmentData(); // Refresh table
		                alert1.successMessage("Appointment deleted successfully.");
		            } else {
		                conn.rollback();
		                alert1.errorMessage("No appointment found with ID: " + appointmentID);
		            }

		        } catch (SQLException e) {
		            e.printStackTrace();
		            try {
		                if (conn != null) conn.rollback();
		            } catch (SQLException rollbackEx) {
		                rollbackEx.printStackTrace();
		            }
		            AlertMessage alert2 = new AlertMessage();
		            alert2.errorMessage("Error deleting appointment: " + e.getMessage());
		        } finally {
		            try {
		                if (conn != null) conn.close();
		            } catch (SQLException e) {
		                e.printStackTrace();
		            }
		        }
		    }
		}

		// Hàm xóa nội dung form quản lý lịch hẹn
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
		 * =====================ĐỊNH DẠNG VÀ KHỞI TẠO
		 *========================================
		 */
		// Hàm tải dữ liệu cho dashboard
		private void loadDashboardData() {
			Connection connect = null;
			PreparedStatement prepare = null;
			ResultSet result = null;

			try {
				connect = Database.connectDB();
				// Đếm tổng số lịch hẹn
				String sqlAppointments = "SELECT COUNT(*) FROM appointment";
				prepare = connect.prepareStatement(sqlAppointments);
				result = prepare.executeQuery();
				int totalAppointments = result.next() ? result.getInt(1) : 0;
				// Hiển thị tổng số lịch hẹn
				label_total_appointments.setText(String.valueOf(totalAppointments));

				// Đếm số bệnh nhân đang có lịch hẹn sắp tới
				String sqlActivePatients = "SELECT COUNT(DISTINCT Patient_id) FROM appointment WHERE status = 'Coming'";
				prepare = connect.prepareStatement(sqlActivePatients);
				result = prepare.executeQuery();
				int activePatients = result.next() ? result.getInt(1) : 0;
				// Hiển thị số bệnh nhân hoạt động
				label_active_patients.setText(String.valueOf(activePatients));

				// Đếm tổng số bệnh nhân
				String sqlTotalPatients = "SELECT COUNT(*) FROM patient";
				prepare = connect.prepareStatement(sqlTotalPatients);
				result = prepare.executeQuery();
				int totalPatients = result.next() ? result.getInt(1) : 0;
				// Hiển thị tổng số bệnh nhân
				label_total_patients.setText(String.valueOf(totalPatients));

				// Đếm tổng số loại thuốc
				String sqlTotalDrugs = "SELECT COUNT(*) FROM drug";
				prepare = connect.prepareStatement(sqlTotalDrugs);
				result = prepare.executeQuery();
				int totalDrugs = result.next() ? result.getInt(1) : 0;
				// Hiển thị tổng số loại thuốc
				label_total_drugs.setText(String.valueOf(totalDrugs));

				// Tải dữ liệu cho bảng lịch hẹn trên dashboard
				String sqlRecentAppointments = "SELECT a.Id AS appointment_id, a.Time AS appointment_date, a.Status AS appointment_status, "
						+ "p.Name AS patient_name, ua.Name AS doctor_name, a.Create_date, a.Update_date "
						+ "FROM appointment a "
						+ "JOIN patient p ON a.Patient_id = p.Patient_id "
						+ "JOIN doctor d ON a.Doctor_id = d.Doctor_id "
						+ "JOIN user_account ua ON d.Doctor_id = ua.Id ";
				prepare = connect.prepareStatement(sqlRecentAppointments);
				result = prepare.executeQuery();

				// Tạo danh sách lịch hẹn
				ObservableList<AppointmentData> appointmentList = FXCollections.observableArrayList();
				while (result.next()) {
					// Tạo đối tượng AppointmentData
					AppointmentData appointment = new AppointmentData(
							result.getString("appointment_id"),
							result.getTimestamp("appointment_date"),
							result.getString("appointment_status"),
							"", // cancel_reason
							"", // doctor_id
							"", // patient_id
							"", // service_id
							"", // service_name
							"", // prescription_status
							result.getTimestamp("create_date"),
							result.getTimestamp("update_date"),
							result.getString("patient_name"),
							result.getString("doctor_name"),
							"" // contact_number
					);
					appointmentList.add(appointment);
				}

				// Cấu hình các cột cho bảng lịch hẹn
				home_appointment_col_appointmenID.setCellValueFactory(new PropertyValueFactory<>("id"));
				home_appointment_col_patient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
				home_appointment_col_date.setCellValueFactory(new PropertyValueFactory<>("time"));
				home_appointment_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
				home_appointment_col_doctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));

				// Gán danh sách lịch hẹn vào bảng
				home_appointment_tableView.setItems(appointmentList);

				// Tải dữ liệu cho biểu đồ thuốc (tổng số thuốc tích lũy trong 6 tháng gần nhất)
				String sqlDrugs = "SELECT DATE_FORMAT(dates.month_start, '%c/%Y') AS month, "
						+ "COALESCE((SELECT COUNT(*) FROM drug d2 WHERE d2.Create_date <= dates.month_end), 0) AS drug_count "
						+ "FROM (SELECT DATE_ADD(DATE_SUB(CURDATE(), INTERVAL (MONTH(CURDATE()) - 1) MONTH), INTERVAL n MONTH) AS month_start, "
						+ "             LAST_DAY(DATE_ADD(DATE_SUB(CURDATE(), INTERVAL (MONTH(CURDATE()) - 1) MONTH), INTERVAL n MONTH)) AS month_end "
						+ "      FROM (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) numbers "
						+ "      WHERE DATE_ADD(DATE_SUB(CURDATE(), INTERVAL (MONTH(CURDATE()) - 1) MONTH), INTERVAL n MONTH) <= CURDATE()) dates "
						+ "ORDER BY dates.month_start ASC";
				prepare = connect.prepareStatement(sqlDrugs);
				result = prepare.executeQuery();
				XYChart.Series<String, Number> drugsSeries = new XYChart.Series<>();
				drugsSeries.setName("Total Drugs");
				while (result.next()) {
					// Thêm dữ liệu vào biểu đồ thuốc
					drugsSeries.getData().add(new XYChart.Data<>(result.getString("month"), result.getInt("drug_count")));
				}
				home_chart_drugs.getData().clear();
				home_chart_drugs.getData().add(drugsSeries);

				// Tải dữ liệu cho biểu đồ bệnh nhân (tổng số bệnh nhân tích lũy trong 6 tháng gần nhất)
				String sqlPatients = "SELECT DATE_FORMAT(dates.month_start, '%c/%Y') AS month, "
						+ "COALESCE((SELECT COUNT(*) FROM patient p2 WHERE p2.Create_date <= dates.month_end), 0) AS patient_count "
						+ "FROM (SELECT DATE_ADD(DATE_SUB(CURDATE(), INTERVAL (MONTH(CURDATE()) - 1) MONTH), INTERVAL n MONTH) AS month_start, "
						+ "             LAST_DAY(DATE_ADD(DATE_SUB(CURDATE(), INTERVAL (MONTH(CURDATE()) - 1) MONTH), INTERVAL n MONTH)) AS month_end "
						+ "      FROM (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) numbers "
						+ "      WHERE DATE_ADD(DATE_SUB(CURDATE(), INTERVAL (MONTH(CURDATE()) - 1) MONTH), INTERVAL n MONTH) <= CURDATE()) dates "
						+ "ORDER BY dates.month_start ASC";
				prepare = connect.prepareStatement(sqlPatients);
				result = prepare.executeQuery();
				XYChart.Series<String, Number> patientsSeries = new XYChart.Series<>();
				patientsSeries.setName("Total Patients");
				while (result.next()) {
					// Thêm dữ liệu vào biểu đồ bệnh nhân
					patientsSeries.getData()
							.add(new XYChart.Data<>(result.getString("month"), result.getInt("patient_count")));
				}
				home_chart_patients.getData().clear();
				home_chart_patients.getData().add(patientsSeries);

				// Tải dữ liệu cho biểu đồ lịch hẹn (số lượng theo trạng thái)
				String sqlAppointmentsChart = "SELECT Status, COUNT(*) AS appointment_count "
						+ "FROM appointment GROUP BY Status";
				prepare = connect.prepareStatement(sqlAppointmentsChart);
				result = prepare.executeQuery();
				XYChart.Series<String, Number> appointmentsSeries = new XYChart.Series<>();
				appointmentsSeries.setName("Appointments by Status");
				while (result.next()) {
					// Thêm dữ liệu vào biểu đồ lịch hẹn
					appointmentsSeries.getData()
							.add(new XYChart.Data<>(result.getString("Status"), result.getInt("appointment_count")));
				}
				home_chart_appointments.getData().clear();
				home_chart_appointments.getData().add(appointmentsSeries);

			} catch (SQLException e) {
				// Xử lý lỗi SQL
				e.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Error loading dashboard data!");
				alert.showAndWait();
			} finally {
				// Đóng các tài nguyên
				try {
					if (result != null)
						result.close();
					if (prepare != null)
						prepare.close();
					if (connect != null)
						connect.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// Hàm chuyển đổi giữa các form giao diện
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

		// Hàm hiển thị form cụ thể
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
					loadDashboardData();
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

		// Hàm tải danh sách giới tính vào ComboBox
		public void loadComboBox() {
			// Gán danh sách giới tính từ enum Gender
			gender_cb.setItems(FXCollections
					.observableArrayList(Arrays.stream(Gender.values()).map(Enum::name).collect(Collectors.toList())));
		}

		// Hàm cập nhật thời gian thực
		public void runTime() {
			// Tạo một thread mới để cập nhật thời gian
			new Thread() {
				public void run() {
					// Định dạng thời gian
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
					while (true) {
						try {
							Thread.sleep(1000); 
						} catch (Exception e) {
							e.printStackTrace();
						}
						// Cập nhật thời gian trên giao diện
						Platform.runLater(() -> {
							date_time.setText(format.format(new Date()));
						});
					}
				}
			}.start();
		}

		// Hàm đăng xuất
		@FXML
		void logoutBtn(ActionEvent event) {

			try {
				if (alert.confirmationMessage("Are you sure you want to logout?")) {
					Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
					Stage stage = new Stage();
					stage.setScene(new Scene(root));
					stage.setTitle("Login");
					stage.show();

					// Đóng cửa sổ hiện tại
					logout_btn.getScene().getWindow().hide();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Hàm khởi tạo giao diện
		@SuppressWarnings("unchecked")
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// Bắt đầu cập nhật thời gian thực
			runTime();
			// Tải danh sách giới tính
			loadComboBox();
			// Tải danh sách bệnh nhân
			loadComboBoxPatient();
			// Tải danh sách dịch vụ
			loadComboBoxService();
			// Tải danh sách mức độ khẩn cấp
			loadUrgencyLevels();
			// Tải dữ liệu lịch hẹn
			loadAppointmentData();
			// Cấu hình spinner giờ
			SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 17, 8);
			spHour.setValueFactory(hourFactory);
			// Cấu hình spinner phút
			SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, 0, 15);
			spMinute.setValueFactory(minuteFactory);
			// Cấu hình spinner giờ cho form quản lý lịch hẹn
			SpinnerValueFactory<Integer> hourFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 17, 8);
			spHour1.setValueFactory(hourFactory1);
			// Cấu hình spinner phút cho form quản lý lịch hẹn
			SpinnerValueFactory<Integer> minuteFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, 0, 15);
			spMinute1.setValueFactory(minuteFactory1);
			// Khởi tạo bộ lọc cho bảng thuốc
			initializeDrugFilters();
			// Khởi tạo bộ lọc cho bảng bệnh nhân
			initializePatientFilters();
			// Hiển thị form dashboard mặc định
			showForm("dashboard");

			// Cấu hình các cột cho bảng lịch hẹn
			appointments_col_service.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
			appointments_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
			appointments_col_name.setCellValueFactory(new PropertyValueFactory<>("patientName"));
			appointments_col_doctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
			appointments_col_contactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
			appointments_col_reason.setCellValueFactory(new PropertyValueFactory<>("cancelReason"));
			appointments_col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
			// Cấu hình cột trạng thái đơn thuốc
			appointments_col_prescription.setCellValueFactory(cellData -> {
				String status = cellData.getValue().getPrescriptionStatus();
				return new SimpleStringProperty(status != null ? status : "");
			});
			// Gán danh sách lịch hẹn vào bảng
			appointments_tableView.setItems(appoinmentListData);
			// Gán danh sách trạng thái lịch hẹn vào ComboBox
			appointment_status.setItems(FXCollections.observableArrayList(
					Arrays.stream(AppointmentStatus.values()).map(Enum::name).collect(Collectors.toList())));
			// Cấu hình tiêu chí tìm kiếm lịch hẹn
			appointments_searchBy
					.setItems(FXCollections.observableArrayList("Patient Name", "Doctor Name", "Cancel Reason", "Contact"));
			appointments_searchBy.getSelectionModel().selectFirst();
			// Thêm listener cho tìm kiếm
			appointments_searchBy.valueProperty().addListener((observable, oldValue, newValue) -> {
				onSearchChanged();
			});
			appointments_searchQuery.textProperty().addListener((observable, oldValue, newValue) -> {
				onSearchChanged();
			});
		}

		// Hàm xử lý thay đổi tìm kiếm lịch hẹn
		private void onSearchChanged() {
			// Lấy từ khóa tìm kiếm
			String query = appointments_searchQuery.getText().toLowerCase();
			// Lấy tiêu chí tìm kiếm
			String searchBy = appointments_searchBy.getValue();
			// Nếu không có từ khóa hoặc tiêu chí, hiển thị toàn bộ danh sách
			if (query.isEmpty() || searchBy == null || searchBy.isEmpty()) {
				appointments_tableView.setItems(appoinmentListData);
				return;
			}

			// Lọc danh sách lịch hẹn theo tiêu chí
			ObservableList<AppointmentData> filtered = appoinmentListData.filtered(appointment -> {
				switch (searchBy) {
					case "Patient Name":
						return appointment.getPatientName() != null
								&& appointment.getPatientName().toLowerCase().contains(query);
					case "Doctor Name":
						return appointment.getDoctorName() != null
								&& appointment.getDoctorName().toLowerCase().contains(query);
					case "Cancel Reason":
						return appointment.getCancelReason() != null
								&& appointment.getCancelReason().toLowerCase().contains(query);
					case "Contact":
						return appointment.getContactNumber() != null
								&& appointment.getContactNumber().toLowerCase().contains(query);
					default:
						return false;
				}
			});
			// Gán danh sách đã lọc vào bảng
			appointments_tableView.setItems(filtered);
		}

		// Hàm định dạng tiền tệ VNĐ
		public static String formatCurrencyVND(BigDecimal amount) {
			if (amount == null)
				return "0 VNĐ";
			NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
			return formatter.format(amount) + " VNĐ";
		}
	}