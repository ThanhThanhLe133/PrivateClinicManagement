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
	private TableColumn<DoctorData, String> doctors_col_address,doctors_col_status;
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
	private TableColumn<ReceptionistData, String> receptionist_col_address,receptionist_col_status;
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
	    // Lấy giá trị loại lọc từ ComboBox
	    String type = filterTypeComboBox.getValue();
	    // Kiểm tra xem loại lọc và ngày có được chọn hay không
	    if (type == null || filterDatePicker.getValue() == null) {
	        System.out.println("Please select filter type and date!");
	        return;
	    }

	    // Giá trị mẫu cho doanh thu, chi phí và lợi nhuận
	    double totalRevenue = 12000.0;
	    double totalCost = 5000.0;
	    double netProfit = totalRevenue - totalCost;

	    // Hiển thị kết quả lên các nhãn
	    totalRevenueLabel.setText("$" + totalRevenue);
	    totalCostLabel.setText("$" + totalCost);
	    netProfitLabel.setText("$" + netProfit);

	    // In thông báo ra console
	    System.out.println("Revenue calculated for " + type + ": " + filterDatePicker.getValue());
	}

	// CRUD ADMINFORMCONTROLLER code

	// =======================CRUD Doctor==================================

	private void loadDoctorTable() {
	    // Tạo danh sách Observable để chứa dữ liệu bác sĩ
	    ObservableList<DoctorData> list = FXCollections.observableArrayList();

	    try {
	        // Kết nối đến cơ sở dữ liệu
	        Connection conn = Database.connectDB();
	        // Câu truy vấn lấy thông tin bác sĩ
	        String sql = "SELECT u.Id AS doctorId, u.Username, u.Name,u.Is_active, u.Email, u.Gender, u.Password, u.Avatar, "
	                + "d.Phone, d.Address, d.Is_confirmed, s.Name AS ServiceName " + "FROM DOCTOR d "
	                + "JOIN USER_ACCOUNT u ON d.Doctor_id = u.Id " + "JOIN SERVICE s ON s.Id = d.Service_id";

	        // Chuẩn bị và thực thi câu truy vấn
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        // Duyệt kết quả và thêm vào danh sách
	        while (rs.next()) {
	            list.add(new DoctorData(rs.getString("doctorId"), rs.getString("Username"), rs.getString("Password"),
	                    rs.getString("Name"), rs.getString("Email"), rs.getString("Gender"), rs.getBoolean("Is_active"),
	                    rs.getString("Phone"), rs.getString("ServiceName"), rs.getString("Address"),
	                    rs.getBoolean("Is_confirmed")));
	        }

	        // Gán giá trị cho các cột trong bảng
	        doctors_col_doctorID.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
	        doctors_col_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
	        doctors_col_gender.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));
	        doctors_col_contactNumber.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
	        doctors_col_email.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
	        doctors_col_specialization
	                .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getServiceName()));
	        doctors_col_address.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));
	        // Hiển thị trạng thái active/inactive
	        doctors_col_status.setCellValueFactory(data -> 
	            new SimpleStringProperty(data.getValue().getStatus() ? "Active" : "Inactive")
	        );

	        // Tạo cột hành động với các nút chỉnh sửa, xóa, xác nhận
	        doctors_col_action.setCellFactory(col -> new TableCell<>() {
	            private final Button editBtn = new Button("Update");
	            private final Button deleteBtn = new Button("Delete");
	            private final Button confirmBtn = new Button("Confirm");
	            private final HBox hbox = new HBox(5, editBtn, deleteBtn);

	            {
	                // Xử lý sự kiện nút chỉnh sửa
	                editBtn.setOnAction(e -> {
	                    DoctorData doctor = getTableView().getItems().get(getIndex());
	                    openEditDoctorForm(doctor);
	                });

	                // Xử lý sự kiện nút xóa
	                deleteBtn.setOnAction(e -> {
	                    DoctorData doctor = getTableView().getItems().get(getIndex());
	                    deleteDoctor(doctor.getId());
	                    loadDoctorTable();
	                });
	                // Xử lý sự kiện nút xác nhận
	                confirmBtn.setOnAction(e -> {
	                    DoctorData doctor = getTableView().getItems().get(getIndex());
	                    confirmDoctor(doctor.getId());
	                    loadDoctorTable();
	                });
	            }

	            // Cập nhật giao diện ô hành động
	            @Override
	            protected void updateItem(Void item, boolean empty) {
	                super.updateItem(item, empty);
	                if (empty) {
	                    setGraphic(null);
	                } else {
	                    DoctorData row = getTableView().getItems().get(getIndex()); 
	                    // Hiển thị nút dựa trên trạng thái xác nhận
	                    if (row.isConfirmed()) { 
	                        hbox.getChildren().setAll(editBtn, deleteBtn);
	                    } else { 
	                        hbox.getChildren().setAll(editBtn, deleteBtn, confirmBtn);
	                    }
	                    setGraphic(hbox);
	                }
	            }
	        });

	        // Gán danh sách vào bảng
	        doctors_tableView.setItems(list);
	        // Đóng kết nối
	        conn.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void confirmDoctor(String doctorId) {
	    // Tạo hộp thoại xác nhận
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Confirm account");
	    alert.setHeaderText("Are you sure you want to confirm this doctor's account?");
	    alert.setContentText("This action will activate this account.");

	    // Chờ người dùng xác nhận
	    Optional<ButtonType> result = alert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        try (Connection conn = Database.connectDB()) {
	            // Cập nhật trạng thái xác nhận
	            String sql = "UPDATE DOCTOR SET Is_confirmed = TRUE WHERE Doctor_id = ?";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, doctorId);
	            ps.executeUpdate();

	            // Thông báo thành công
	            Alert info = new Alert(Alert.AlertType.INFORMATION);
	            info.setHeaderText("Confirmation successful.");
	            info.setContentText("The receptionist's account has been confirmed.");
	            info.showAndWait();

	        } catch (Exception e) {
	            e.printStackTrace();

	            // Thông báo lỗi
	            Alert error = new Alert(Alert.AlertType.ERROR);
	            error.setHeaderText("Confirmation failed.");
	            error.setContentText("An error occurred while confirming.");
	            error.showAndWait();
	        }
	    }
	}

	private void deleteDoctor(String doctorId) {
	    // Initial confirmation alert
	    Alert initialAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    initialAlert.setTitle("Delete Confirmation");
	    initialAlert.setHeaderText("Are you sure you want to delete this doctor?");
	    initialAlert.setContentText("This action cannot be undone.");
	    
	    Optional<ButtonType> initialResult = initialAlert.showAndWait();
	    if (initialResult.isEmpty() || initialResult.get() != ButtonType.OK) {
	        return; // User canceled
	    }

	    Connection conn = null;
	    try {
	        conn = Database.connectDB();
	        // Check for associated appointments
	        String checkAppointmentsSQL = "SELECT COUNT(*) AS count FROM APPOINTMENT WHERE Doctor_id = ?";
	        PreparedStatement psCheck = conn.prepareStatement(checkAppointmentsSQL);
	        psCheck.setString(1, doctorId);
	        ResultSet rs = psCheck.executeQuery();
	        int appointmentCount = 0;
	        if (rs.next()) {
	            appointmentCount = rs.getInt("count");
	        }

	        // If appointments exist, show second warning alert
	        if (appointmentCount > 0) {
	            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
	            warningAlert.setTitle("Warning: Associated Appointments");
	            warningAlert.setHeaderText("This doctor has " + appointmentCount + " appointment(s).");
	            warningAlert.setContentText("Deleting this doctor will also delete all related appointments, appointment services, prescriptions, prescription details, and available slots. Do you want to proceed?");
	            ButtonType proceedButton = new ButtonType("Proceed");
	            ButtonType cancelButton = new ButtonType("Cancel");
	            warningAlert.getButtonTypes().setAll(proceedButton, cancelButton);

	            Optional<ButtonType> warningResult = warningAlert.showAndWait();
	            if (warningResult.isEmpty() || warningResult.get() != proceedButton) {
	                conn.close();
	                return; // User canceled
	            }
	        }

	        // Proceed with deletion
	        conn.setAutoCommit(false); // Start transaction

	        // Delete USER_ACCOUNT record (cascades to DOCTOR, APPOINTMENT, etc.)
	        String deleteUserSQL = "DELETE FROM USER_ACCOUNT WHERE Id = ?";
	        PreparedStatement psDelete = conn.prepareStatement(deleteUserSQL);
	        psDelete.setString(1, doctorId);
	        int rowsDeleted = psDelete.executeUpdate();

	        if (rowsDeleted > 0) {
	            conn.commit(); // Commit transaction
	            loadDoctorTable(); // Refresh table
	            alert.successMessage("Doctor deleted successfully.");
	        } else {
	            conn.rollback();
	            alert.errorMessage("No doctor found with ID: " + doctorId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            if (conn != null) conn.rollback();
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        alert.errorMessage("Error deleting doctor: " + e.getMessage());
	    } finally {
	        try {
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	private void openEditDoctorForm(DoctorData doctor) {
	    try {
	        // Tải giao diện chỉnh sửa bác sĩ
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditDoctorForm.fxml"));
	        Parent root = loader.load();

	        // Truyền dữ liệu bác sĩ sang controller
	        EditDoctorFormController controller = loader.getController();
	        controller.setDoctorData(doctor);

	        // Hiển thị cửa sổ chỉnh sửa
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setTitle("Update Doctor");
	        stage.setScene(new Scene(root));

	        // Tải lại bảng khi đóng form
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
	        // Tải giao diện thêm bác sĩ mới
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddDoctorForm.fxml"));
	        Parent root = loader.load();

	        // Hiển thị cửa sổ thêm bác sĩ
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Add New Doctor");
	        stage.setScene(new Scene(root));

	        // Tải lại bảng khi đóng form
	        stage.setOnHidden(e -> loadDoctorTable());
	        stage.showAndWait();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	// =============================CRUD RECEPTIONIST=====================================

	private void loadReceptionistTable() {
	    // Tạo danh sách Observable để chứa dữ liệu lễ tân
	    ObservableList<ReceptionistData> list = FXCollections.observableArrayList();

	    try (Connection conn = Database.connectDB()) {
	        // Câu truy vấn lấy thông tin lễ tân
	        String sql = "SELECT u.Id AS receptionist_id, u.Username, u.Password, u.Name, u.Email, u.Gender, u.Is_active,"
	                + " r.Phone, r.Address, r.Is_confirmed "
	                + "FROM RECEPTIONIST r JOIN USER_ACCOUNT u ON r.Receptionist_id = u.Id";

	        // Chuẩn bị và thực thi câu truy vấn
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        // Duyệt kết quả và thêm vào danh sách
	        while (rs.next()) {
	            list.add(new ReceptionistData(rs.getString("receptionist_id"), rs.getString("username"),
	                    rs.getString("password"), rs.getString("name"), rs.getString("email"), rs.getString("gender"),
	                    rs.getBoolean("is_active"), rs.getString("phone"), rs.getString("address"),
	                    rs.getBoolean("is_confirmed")));
	        }
	        // Gán giá trị cho các cột trong bảng
	        receptionist_col_id.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
	        receptionist_col_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
	        receptionist_col_gender.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));
	        receptionist_col_phone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
	        receptionist_col_email.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
	        receptionist_col_address
	                .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));
	        // Hiển thị trạng thái active/inactive
	        receptionist_col_status.setCellValueFactory(data -> 
	            new SimpleStringProperty(data.getValue().getStatus() ? "Active" : "Inactive"));
	        // Tạo cột hành động với các nút chỉnh sửa, xóa, xác nhận
	        receptionist_col_action.setCellFactory(col -> new TableCell<>() {
	            private final Button editBtn = new Button("Update");
	            private final Button deleteBtn = new Button("Delete");
	            private final Button confirmBtn = new Button("Confirm");
	            private final HBox hbox = new HBox(5, editBtn, deleteBtn);

	            {
	                // Xử lý sự kiện nút chỉnh sửa
	                editBtn.setOnAction(e -> {
	                    ReceptionistData selected = getTableView().getItems().get(getIndex());
	                    openEditReceptionistForm(selected);
	                });

	                // Xử lý sự kiện nút xóa
	                deleteBtn.setOnAction(e -> {
	                    ReceptionistData selected = getTableView().getItems().get(getIndex());
	                    deleteReceptionist(selected.getId());
	                    loadReceptionistTable();
	                });
	                // Xử lý sự kiện nút xác nhận
	                confirmBtn.setOnAction(e -> {
	                    ReceptionistData selected = getTableView().getItems().get(getIndex());
	                    confirmReceptionist(selected.getId());
	                    loadReceptionistTable();
	                });
	            }

	            // Cập nhật giao diện ô hành động
	            @Override
	            protected void updateItem(Void item, boolean empty) {
	                super.updateItem(item, empty);
	                if (empty) {
	                    setGraphic(null);
	                } else {
	                    ReceptionistData row = getTableView().getItems().get(getIndex()); 
	                    // Hiển thị nút dựa trên trạng thái xác nhận
	                    if (row.isConfirmed()) { 
	                        hbox.getChildren().setAll(editBtn, deleteBtn);
	                    } else { 
	                        hbox.getChildren().setAll(editBtn, deleteBtn, confirmBtn);
	                    }
	                    setGraphic(hbox);
	                }
	            }
	        });

	        // Gán danh sách vào bảng
	        receptionist_tableView.setItems(list);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void confirmReceptionist(String receptionistId) {
	    // Tạo hộp thoại xác nhận
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Confirm account");
	    alert.setHeaderText("Are you sure you want to confirm this receptionist's account?");
	    alert.setContentText("This action will activate this account.");

	    // Chờ người dùng xác nhận
	    Optional<ButtonType> result = alert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        try (Connection conn = Database.connectDB()) {
	            // Cập nhật trạng thái xác nhận
	            String sql = "UPDATE RECEPTIONIST SET Is_confirmed = TRUE WHERE Receptionist_id = ?";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, receptionistId);
	            ps.executeUpdate();

	            // Thông báo thành công
	            Alert info = new Alert(Alert.AlertType.INFORMATION);
	            info.setHeaderText("Confirmation successful.");
	            info.setContentText("The receptionist's account has been confirmed.");
	            info.showAndWait();

	        } catch (Exception e) {
	            e.printStackTrace();

	            // Thông báo lỗi
	            Alert error = new Alert(Alert.AlertType.ERROR);
	            error.setHeaderText("Confirmation failed.");
	            error.setContentText("An error occurred while confirming.");
	            error.showAndWait();
	        }
	    }
	}

	private void deleteReceptionist(String receptionistId) {
	    // Tạo hộp thoại xác nhận xóa
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Delete Confirmation");
	    alert.setHeaderText("Are you sure you want to delete this receptionist?");
	    alert.setContentText("This action cannot be undone.");

	    // Chờ người dùng xác nhận
	    Optional<ButtonType> result = alert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        try (Connection conn = Database.connectDB()) {
	            // Xóa tài khoản lễ tân
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
	        // Tải giao diện chỉnh sửa lễ tân
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditReceptionistForm.fxml"));
	        Parent root = loader.load();

	        // Truyền dữ liệu lễ tân sang controller
	        EditReceptionistFormController controller = loader.getController();
	        controller.setReceptionistData(data.getId(), data.getName(), data.getEmail(), data.getGender(),
	                data.getPhone(), data.getAddress());

	        // Hiển thị cửa sổ chỉnh sửa
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Update Receptionist");
	        stage.setScene(new Scene(root));

	        // Tải lại bảng khi đóng form
	        stage.setOnHidden(e -> loadReceptionistTable());
	        stage.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@FXML
	private void openAddReceptionistForm() {
	    try {
	        // Tải giao diện thêm lễ tân mới
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddReceptionistForm.fxml"));
	        Parent root = loader.load();

	        // Hiển thị cửa sổ thêm lễ tân
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Add Receptionist");
	        stage.setScene(new Scene(root));

	        // Tải lại bảng khi đóng form
	        stage.setOnHidden(e -> loadReceptionistTable());

	        stage.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	// =======================CRUD Service==================================

	private void loadServiceTable() {
	    // Tạo danh sách Observable để chứa dữ liệu dịch vụ
	    ObservableList<ServiceData> list = FXCollections.observableArrayList();

	    try {
	        // Kết nối đến cơ sở dữ liệu
	        Connection conn = Database.connectDB();
	        // Câu truy vấn lấy thông tin dịch vụ
	        String sql = "SELECT Id, Name, Type, Price FROM SERVICE ";

	        // Chuẩn bị và thực thi câu truy vấn
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        // Duyệt kết quả và thêm vào danh sách
	        while (rs.next()) {
	            list.add(new ServiceData(rs.getString("Id"), rs.getString("Name"), rs.getBigDecimal("Price"),
	                    rs.getString("Type")));
	        }

	        // Gán giá trị cho các cột trong bảng
	        service_col_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
	        service_col_type.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
	        service_col_price
	                .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedPrice()));

	        // Tạo cột hành động với các nút chỉnh sửa, xóa
	        service_col_action.setCellFactory(col -> new TableCell<>() {
	            private final Button editBtn = new Button("Update");
	            private final Button deleteBtn = new Button("Delete");
	            private final HBox hbox = new HBox(5, editBtn, deleteBtn);
	            {
	                // Xử lý sự kiện nút chỉnh sửa
	                editBtn.setOnAction(e -> {
	                    ServiceData service = getTableView().getItems().get(getIndex());
	                    openEditService(service);
	                });

	                // Xử lý sự kiện nút xóa
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

	        // Gán danh sách vào bảng
	        service_tableView.setItems(list);
	        // Đóng kết nối
	        conn.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Thông báo lỗi khi tải danh sách
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setContentText("Error loading drug list!");
	        alert.showAndWait();
	    }
	}

	private void deleteService(String serviceId) {
	    // Initial confirmation alert
	    Alert initialAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    initialAlert.setTitle("Delete Confirmation");
	    initialAlert.setHeaderText("Are you sure you want to delete this service?");
	    initialAlert.setContentText("This action cannot be undone.");
	    
	    Optional<ButtonType> initialResult = initialAlert.showAndWait();
	    if (initialResult.isEmpty() || initialResult.get() != ButtonType.OK) {
	        return; // User canceled
	    }

	    Connection conn = null;
	    try {
	        conn = Database.connectDB();
	        // Check for associated doctors
	        String checkDoctorsSQL = "SELECT COUNT(*) AS count FROM DOCTOR WHERE Service_id = ?";
	        PreparedStatement psCheck = conn.prepareStatement(checkDoctorsSQL);
	        psCheck.setString(1, serviceId);
	        ResultSet rs = psCheck.executeQuery();
	        int doctorCount = 0;
	        if (rs.next()) {
	            doctorCount = rs.getInt("count");
	        }

	        // If doctors are associated, show second warning alert
	        if (doctorCount > 0) {
	            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
	            warningAlert.setTitle("Warning: Associated Doctors");
	            warningAlert.setHeaderText("This service is associated with " + doctorCount + " doctor(s).");
	            warningAlert.setContentText("Deleting this service will also delete all related doctors, their user accounts, appointments, appointment services, prescriptions, prescription details, and available slots. Do you want to proceed?");
	            ButtonType proceedButton = new ButtonType("Proceed");
	            ButtonType cancelButton = new ButtonType("Cancel");
	            warningAlert.getButtonTypes().setAll(proceedButton, cancelButton);

	            Optional<ButtonType> warningResult = warningAlert.showAndWait();
	            if (warningResult.isEmpty() || warningResult.get() != proceedButton) {
	                conn.close();
	                return; // User canceled
	            }
	        }

	        // Proceed with deletion
	        conn.setAutoCommit(false); // Start transaction

	        // Delete USER_ACCOUNT records for doctors associated with this service
	        String deleteUserAccountsSQL = "DELETE FROM USER_ACCOUNT WHERE Id IN (SELECT Doctor_id FROM DOCTOR WHERE Service_id = ?)";
	        PreparedStatement psDeleteUsers = conn.prepareStatement(deleteUserAccountsSQL);
	        psDeleteUsers.setString(1, serviceId);
	        psDeleteUsers.executeUpdate();

	        // Delete SERVICE record (cascades to DOCTOR, APPOINTMENT, etc.)
	        String deleteServiceSQL = "DELETE FROM SERVICE WHERE Id = ?";
	        PreparedStatement psDelete = conn.prepareStatement(deleteServiceSQL);
	        psDelete.setString(1, serviceId);
	        int rowsDeleted = psDelete.executeUpdate();

	        if (rowsDeleted > 0) {
	            conn.commit(); // Commit transaction
	            loadServiceTable(); // Refresh table
	            alert.successMessage("Service deleted successfully.");
	        } else {
	            conn.rollback();
	            alert.errorMessage("No service found with ID: " + serviceId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            if (conn != null) conn.rollback();
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        alert.errorMessage("Error deleting service: " + e.getMessage());
	    } finally {
	        try {
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	private void openEditService(ServiceData service) {
	    try {
	        // Tải giao diện chỉnh sửa dịch vụ
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditServiceForm.fxml"));
	        Parent root = loader.load();

	        // Truyền dữ liệu dịch vụ sang controller
	        EditServiceFormController controller = loader.getController();
	        controller.setServiceData(service);

	        // Hiển thị cửa sổ chỉnh sửa
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setTitle("Update Service");
	        stage.setScene(new Scene(root));

	        // Tải lại bảng khi đóng form
	        stage.setOnHidden(e -> loadServiceTable());
	        stage.showAndWait();

	    } catch (IOException e) {
	        e.printStackTrace();
	        System.err.println("ERROR : " + e.getMessage());
	    }
	}

	@FXML
	private void openAddServiceForm() {
	    try {
	        // Tải giao diện thêm dịch vụ mới
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddServiceForm.fxml"));
	        Parent root = loader.load();

	        // Hiển thị cửa sổ thêm dịch vụ
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Add Service");
	        stage.setScene(new Scene(root));

	        // Tải lại bảng khi đóng form
	        stage.setOnHidden(e -> loadServiceTable());
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
	static BigDecimal serviceFees;
	// Danh sách lưu dữ liệu doanh thu
	List<Map<String, String>> drugRevenueList = new ArrayList<>();
	List<Map<String, String>> serviceRevenueList = new ArrayList<>();
	List<Map<String, String>> revenueListSortByDate = new ArrayList<>();
	int i;

	@FXML
	private void resetRevenueFilter() {
	    // Đặt lại các giá trị doanh thu về 0
	    totalRevenue = BigDecimal.ZERO;
	    examineFees = BigDecimal.ZERO;
	    medicationFees = BigDecimal.ZERO;
	    labTestFees = BigDecimal.ZERO;
	    serviceFees = BigDecimal.ZERO;
	    // Xóa giá trị ngày
	    revenue_startDate.setValue(null);
	    revenue_endDate.setValue(null);
	    // Cập nhật hiển thị
	    setRevenue();
	}

	private void setRevenue() {
	    // Cập nhật hiển thị các nhãn doanh thu
	    revenue_totalRevenue.setText(FormatterUtils.formatCurrencyVND(totalRevenue));
	    revenue_examineFees.setText(FormatterUtils.formatCurrencyVND(examineFees));
	    revenue_medicationFees.setText(FormatterUtils.formatCurrencyVND(medicationFees));
	    revenue_labTestFees.setText(FormatterUtils.formatCurrencyVND(labTestFees));
	}

	private void checkValidDate(LocalDate startDate, LocalDate endDate) {
	    // Kiểm tra ngày bắt đầu và kết thúc
	    if (startDate == null || endDate == null) {
	        alert.errorMessage("Please select both start date and end date.");
	        return;
	    } else if (startDate.isAfter(endDate)) {
	        alert.errorMessage("Start date cannot be after end date.");
	        return;
	    } else if (endDate.isAfter(LocalDate.now())) {
	        alert.errorMessage("End date has not come yet.");
	        return;
	    }
	}

	@FXML
	private void exportRevenueReport(ActionEvent event) throws IOException {
	    // Đặt lại các giá trị doanh thu
	    totalRevenue = BigDecimal.ZERO;
	    examineFees = BigDecimal.ZERO;
	    medicationFees = BigDecimal.ZERO;
	    labTestFees = BigDecimal.ZERO;
	    serviceFees = BigDecimal.ZERO;
	    LocalDate startDate = revenue_startDate.getValue();
	    LocalDate endDate = revenue_endDate.getValue();
	    // Kiểm tra ngày hợp lệ
	    checkValidDate(startDate, endDate);

	    // Tính toán doanh thu từ thuốc và dịch vụ
	    handleRevenueDrugReport(startDate, endDate);
	    handleRevenueServiceReport(startDate, endDate);
	    totalRevenue = examineFees.add(medicationFees).add(labTestFees);
	    serviceFees = examineFees.add(labTestFees);
	    handleDateReport(startDate, endDate);

	    // Tạo file báo cáo
	    File sourceFile = new File("Word/REPORT.docx");
	    String destFileName = String.format("Word/REPORT_from_%s_to_%s.docx", startDate.format(formatter).toString(),
	            endDate.format(formatter));
	    Path destPath = Paths.get(destFileName);

	    // Xóa file cũ nếu tồn tại
	    if (Files.exists(destPath)) {
	        try {
	            Files.delete(destPath);
	        } catch (java.nio.file.AccessDeniedException e) {
	            alert.errorMessage("Tệp " + destPath + " đang được sử dụng bởi chương trình khác.");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    File destFile = new File(destFileName);

	    // Sao chép file mẫu
	    Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

	    try (XWPFDocument doc = new XWPFDocument(new FileInputStream(destFile))) {
	        // Thay thế placeholder ngày tháng
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
	        // Thay thế các placeholder khác
	        replaceAllPlaceholders(doc, drugRevenueList, serviceRevenueList, revenueListSortByDate, totalRevenue,
	                medicationFees, serviceFees);

	        // Ghi file
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

	    // Kiểm tra danh sách dữ liệu rỗng
	    if (dataList == null || dataList.isEmpty()) {
	        dataList = new ArrayList<>();
	        Map<String, String> emptyData = new HashMap<>();
	        for (String column : columns) {
	            emptyData.put(column, "");
	        }
	        dataList.add(emptyData);
	    }
	    XWPFTableRow row = table.getRow(rowIndex);
	    boolean found = false;

	    int insertPos = rowIndex;

	    // Kiểm tra placeholder trong ô
	    for (XWPFTableCell cell : row.getTableCells()) {
	        String text = cell.getText();
	        if (text != null && text.contains(placeholderKey)) {
	            table.removeRow(rowIndex);
	            found = true;
	            break;
	        }
	    }

	    // Thêm các hàng mới với dữ liệu
	    if (found) {
	        for (Map<String, String> data : dataList) {
	            XWPFTableRow newRow = table.insertNewTableRow(insertPos++);
	            for (String col : columns) {
	                String value = data.getOrDefault(col, "");
	                if (value == null || value.trim().isEmpty()) {
	                    value = "";
	                }

	                newRow.createCell().setText(value);
	            }
	        }
	    }
	}

	public static void replaceAllPlaceholders(XWPFDocument doc, List<Map<String, String>> drugRevenueList,
	        List<Map<String, String>> serviceRevenueList, List<Map<String, String>> revenueListSortByDate,
	        BigDecimal totalRevenue, BigDecimal medicationFees, BigDecimal serviceFees) throws IOException {
	    // Duyệt qua các bảng trong tài liệu
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

	                            // Thay thế placeholder cho bảng thuốc
	                            if (text.contains("{{noDrug}}")) {
	                                replaceTablePlaceholderRows(table, "{{noDrug}}",
	                                        List.of("noDrug", "drugName", "quantity", "DrugPrice", "totalDrug"),
	                                        drugRevenueList, i);
	                                break;
	                            }
	                            // Thay thế placeholder cho bảng dịch vụ
	                            if (text.contains("{{noService}}")) {
	                                replaceTablePlaceholderRows(table, "{{noService}}",
	                                        List.of("noService", "serviceName", "type","ServiceQuantity", "ServicePrice", "totalService"),
	                                        serviceRevenueList, i);
	                                break;
	                            }
	                            // Thay thế placeholder cho bảng tổng hợp
	                            if (text.contains("{{noTotal}}")) {
	                                replaceTablePlaceholderRows(table, "{{noTotal}}",
	                                        List.of("noTotal", "date", "totalDrug", "totalService", "totalRevenue"),
	                                        revenueListSortByDate, i);
	                                break;
	                            }
	                            // Thay thế các giá trị tổng
	                            replaceInParagraph(para, "{{totalDrugRevenue}}",
	                                    FormatterUtils.formatCurrencyVND(medicationFees));
	                            replaceInParagraph(para, "{{totalServiceRevenue}}",
	                                    FormatterUtils.formatCurrencyVND(serviceFees));
	                            replaceInParagraph(para, "{{grandTotal}}",
	                                    FormatterUtils.formatCurrencyVND(totalRevenue));
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
	    // Thay thế placeholder trong đoạn văn
	    for (XWPFRun run : paragraph.getRuns()) {
	        String text = run.getText(0);
	        if (text != null && text.contains(placeholder)) {
	            String replaced = text.replace(placeholder, replacement);
	            run.setText(replaced, 0);
	            return;
	        }
	    }
	}

	@FXML
	private void filterRevenueData() {
	    // Lọc dữ liệu doanh thu
	    // Đặt lại các giá trị doanh thu
	    totalRevenue = BigDecimal.ZERO;
	    examineFees = BigDecimal.ZERO;
	    medicationFees = BigDecimal.ZERO;
	    labTestFees = BigDecimal.ZERO;
	    serviceFees = BigDecimal.ZERO;
	    LocalDate startDate = revenue_startDate.getValue();
	    LocalDate endDate = revenue_endDate.getValue();

	    // Kiểm tra ngày hợp lệ
	    checkValidDate(startDate, endDate);

	    // Tính toán doanh thu từ thuốc và dịch vụ
	    handleRevenueDrugReport(startDate, endDate);
	    handleRevenueServiceReport(startDate, endDate);
	    // Tổng doanh thu từ các loại phí
	    totalRevenue = examineFees.add(medicationFees).add(labTestFees);
	    // Cập nhật hiển thị
	    setRevenue();
	}

	private void handleRevenueServiceReport(LocalDate startDate, LocalDate endDate) {
	    // Tính doanh thu từ dịch vụ
	    try (Connection conn = Database.connectDB()) {
	        // Câu truy vấn lấy thông tin doanh thu dịch vụ
	        String sql = "SELECT s.Name AS ServiceName, " + "COUNT(*) AS Quantity, " + "s.Price, " + "s.Type, "
	                + "SUM(s.Price) AS TotalRevenue " + "FROM SERVICE s "
	                + "JOIN APPOINTMENT_SERVICE aps ON aps.Service_id = s.Id "
	                + "JOIN APPOINTMENT p ON p.Id = aps.Appointment_id "
	                + "WHERE DATE(p.Time) BETWEEN ? AND ? AND p.Status != 'Cancel' "
	                + "GROUP BY s.Name, s.Price, s.Type " + "ORDER BY DATE(p.Time) DESC;";

	        // Chuẩn bị câu truy vấn
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setDate(1, java.sql.Date.valueOf(startDate));
	        ps.setDate(2, java.sql.Date.valueOf(endDate));

	        ResultSet rs = ps.executeQuery();
	        ObservableList<RevenueService> dataList = FXCollections.observableArrayList();
	        i = 1; // Khởi tạo biến đếm
	        serviceRevenueList.clear(); // Xóa danh sách cũ
	        // Duyệt kết quả truy vấn
	        while (rs.next()) {
	            String serviceName = rs.getString("ServiceName");
	            int quantity = rs.getInt("quantity");
	            BigDecimal price = rs.getBigDecimal("Price");
	            String type = rs.getString("Type");
	            BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");

	            // Phân loại doanh thu theo loại dịch vụ
	            switch (type.toLowerCase()) {
	            case "examination":
	                examineFees = examineFees.add(totalRevenue);
	                break;
	            case "test":
	                labTestFees = labTestFees.add(totalRevenue);
	                break;
	            }

	            // Thêm dữ liệu vào danh sách
	            dataList.add(new RevenueService(serviceName, type, quantity, price, totalRevenue));

	            // Thêm vào danh sách để xuất báo cáo
	            serviceRevenueList.add(Map.of("noService", String.valueOf(i), "serviceName", serviceName, "type", type,
	                    "ServiceQuantity", String.valueOf(quantity), "ServicePrice", price.toString(), "totalService", totalRevenue.toString()));

	            i++;
	        }

	        // Gán giá trị cho các cột trong bảng
	        revenue_col_serviceName
	                .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getServiceName()));
	        revenue_col_serviceQuantity.setCellValueFactory(
	                data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));
	        revenue_col_servicePrice
	                .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedPrice()));
	        revenue_col_serviceRevenue
	                .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedTotalRevenue()));
	        revenue_col_serviceType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));

	        // Gán danh sách vào bảng
	        revenue_service.setItems(dataList);

	    } catch (Exception e) {
	        e.printStackTrace();
	        // Hiển thị thông báo lỗi nếu truy vấn thất bại
	        Alert error = new Alert(Alert.AlertType.ERROR, "Failed to load revenue report: " + e.getMessage());
	        error.showAndWait();
	    }
	}

	private void handleRevenueDrugReport(LocalDate startDate, LocalDate endDate) {
	    // Tính doanh thu từ thuốc
	    try (Connection conn = Database.connectDB()) {
	        // Câu truy vấn lấy thông tin doanh thu thuốc
	        String sql = "SELECT d.Name AS DrugName, " + "SUM(pd.Quantity) AS Quantity, " + "d.Price, "
	                + "SUM(d.Price * pd.Quantity) AS TotalRevenue " + "FROM PRESCRIPTION_DETAILS pd "
	                + "JOIN PRESCRIPTION p ON pd.Prescription_id = p.Id "
	                + "JOIN APPOINTMENT a ON p.Appointment_id = a.Id " + "JOIN DRUG d ON pd.Drug_id = d.Id "
	                + "WHERE a.Prescription_Status = 'Paid' " + "AND DATE(pd.Update_date) BETWEEN ? AND ? "
	                + "GROUP BY d.Name, d.Price " + "ORDER BY DATE(pd.Update_date) DESC";

	        // Chuẩn bị câu truy vấn
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setDate(1, java.sql.Date.valueOf(startDate));
	        ps.setDate(2, java.sql.Date.valueOf(endDate));

	        ResultSet rs = ps.executeQuery();
	        ObservableList<RevenueDrug> dataList = FXCollections.observableArrayList();
	        i = 1; // Khởi tạo biến đếm
	        drugRevenueList.clear(); // Xóa danh sách cũ
	        // Duyệt kết quả truy vấn
	        while (rs.next()) {
	            String drugName = rs.getString("DrugName");
	            int quantity = rs.getInt("quantity");
	            BigDecimal price = rs.getBigDecimal("Price");
	            BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");
	            // Cộng dồn phí thuốc
	            medicationFees = medicationFees.add(totalRevenue);
	            // Thêm dữ liệu vào danh sách
	            dataList.add(new RevenueDrug(drugName, quantity, price, totalRevenue));

	            // Thêm vào danh sách để xuất báo cáo
	            drugRevenueList.add(Map.of("noDrug", String.valueOf(i), "drugName", drugName, "quantity",
	                    String.valueOf(quantity), "DrugPrice", price.toString(), "totalDrug", totalRevenue.toString()));

	            i++;
	        }

	        // Gán giá trị cho các cột trong bảng
	        revenue_col_drugName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDrugName()));
	        revenue_col_drugQuantity
	                .setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
	        revenue_col_drugPrice
	                .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedPrice()));
	        revenue_col_drugRevenue
	                .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedTotalRevenue()));

	        // Gán danh sách vào bảng
	        revenue_drug.setItems(dataList);

	    } catch (Exception e) {
	        e.printStackTrace();
	        // Hiển thị thông báo lỗi nếu truy vấn thất bại
	        Alert error = new Alert(Alert.AlertType.ERROR, "Failed to load revenue report: " + e.getMessage());
	        error.showAndWait();
	    }
	}

	private void handleDateReport(LocalDate startDate, LocalDate endDate) {
	    // Tạo báo cáo doanh thu theo ngày
	    try (Connection conn = Database.connectDB()) {
	        // Câu truy vấn tổng hợp doanh thu từ thuốc và dịch vụ theo ngày
	        String sql = "SELECT " + "    CreateDate, " + "    SUM(totalDrugRevenue) AS totalDrugRevenue, "
	                + "    SUM(totalServiceRevenue) AS totalServiceRevenue, " + "    SUM(grandTotal) AS grandTotal "
	                + "FROM ( " + "    SELECT " + "        DATE(pd.Update_date) AS CreateDate, "
	                + "        SUM(d.Price * pd.Quantity) AS totalDrugRevenue, " + "        0 AS totalServiceRevenue, "
	                + "        SUM(d.Price * pd.Quantity) AS grandTotal " + "    FROM PRESCRIPTION_DETAILS pd "
	                + "    JOIN PRESCRIPTION p ON p.Id = pd.Prescription_id " + "    JOIN DRUG d ON d.Id = pd.Drug_id "
	                + "    JOIN APPOINTMENT a ON a.Id = p.Appointment_id " + "    WHERE a.Prescription_Status = 'Paid' "
	                + "      AND DATE(pd.Update_date) BETWEEN ? AND ? " + "    GROUP BY DATE(pd.Update_date) "
	                + "    UNION ALL " + "    SELECT " + "        DATE(a.Time) AS CreateDate, "
	                + "        0 AS totalDrugRevenue, " + "        SUM(s.Price) AS totalServiceRevenue, "
	                + "        SUM(s.Price) AS grandTotal " + "    FROM APPOINTMENT a "
	                + "    JOIN APPOINTMENT_SERVICE aps ON aps.Appointment_id = a.Id "
	                + "    JOIN SERVICE s ON s.Id = aps.Service_id "
	                + "    WHERE DATE(a.Time) BETWEEN ? AND ? AND a.Status != 'Cancel' "
	                + "    GROUP BY DATE(a.Time) " + ") AS tmp " + "GROUP BY CreateDate "
	                + "ORDER BY CreateDate ASC;";

	        // Chuẩn bị câu truy vấn với các tham số ngày
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setDate(1, java.sql.Date.valueOf(startDate));
	        ps.setDate(2, java.sql.Date.valueOf(endDate));
	        ps.setDate(3, java.sql.Date.valueOf(startDate));
	        ps.setDate(4, java.sql.Date.valueOf(endDate));
	        ResultSet rs = ps.executeQuery();
	        i = 1; // Khởi tạo biến đếm
	        revenueListSortByDate.clear(); // Xóa danh sách cũ
	        medicationFees = BigDecimal.ZERO; // Đặt lại phí thuốc
	        serviceFees = BigDecimal.ZERO; // Đặt lại phí dịch vụ
	        // Duyệt kết quả truy vấn
	        while (rs.next()) {
	            LocalDate createDate = LocalDate.parse(rs.getString("CreateDate"));
	            BigDecimal totalDrugRevenue = rs.getBigDecimal("TotalDrugRevenue");
	            BigDecimal totalServiceRevenue = rs.getBigDecimal("TotalServiceRevenue");
	            BigDecimal grandTotal = rs.getBigDecimal("GrandTotal");

	            // Cộng dồn phí thuốc và dịch vụ
	            medicationFees = medicationFees.add(totalDrugRevenue);
	            serviceFees = serviceFees.add(totalServiceRevenue);

	            // Thêm dữ liệu vào danh sách doanh thu theo ngày
	            revenueListSortByDate.add(Map.of("noTotal", String.valueOf(i), "date",
	                    createDate.format(formatter).toString(), "totalDrug", totalDrugRevenue.toString(),
	                    "totalService", totalServiceRevenue.toString(), "totalRevenue", grandTotal.toString()));

	            i++;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Hiển thị thông báo lỗi nếu truy vấn thất bại
	        Alert error = new Alert(Alert.AlertType.ERROR, "Failed to load revenue report: " + e.getMessage());
	        error.showAndWait();
	    }
	}

	/* =====================LOAD PROFILE======================================== */
	private void loadAdminProfile() {
	    // Tải thông tin hồ sơ admin
	    String checkUserSQL = "SELECT id, name, email,username, gender, create_date  FROM user_account WHERE username = ?";
	    Connection connect = Database.connectDB();

	    try {
	        // Chuẩn bị câu truy vấn
	        PreparedStatement prepare = connect.prepareStatement(checkUserSQL);
	        prepare.setString(1, username);

	        ResultSet result = prepare.executeQuery();

	        // Kiểm tra nếu không tìm thấy người dùng
	        if (!result.next()) {
	            alert.errorMessage("Username does not match data." + username);
	            return;
	        }
	        this.currentUserId = result.getString("id"); // Lưu id để update sau

	        // Lấy thông tin từ cơ sở dữ liệu
	        String name = result.getString("name");
	        String username = result.getString("username");
	        String email = result.getString("email");
	        String gender = result.getString("gender");
	        String createdAt = result.getString("create_date");

	        // Gán thông tin cho các nhãn
	        name_adminDB.setText(name != null ? name : "UNKNOWN");
	        username_admin.setText(username != null ? username : "");
	        name_admin.setText(name != null ? name : "UNKNOWN");
	        username_adminDB.setText(username != null ? username : "");
	        email_admin.setText(email != null ? email : "");
	        gender_admin.setText(gender != null ? gender : "");
	        createdDate_admin.setText(createdAt != null ? FormatterUtils.formatTimestamp(createdAt) : "");

	        // Gán thông tin cho các ô nhập liệu
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
	    // Hiển thị ảnh đại diện từ cơ sở dữ liệu
	    String sql = "SELECT Avatar FROM user_account WHERE username = ?";
	    connect = Database.connectDB();

	    try {
	        // Chuẩn bị câu truy vấn
	        prepare = connect.prepareStatement(sql);
	        prepare.setString(1, username);
	        result = prepare.executeQuery();
	        if (result.next()) {
	            InputStream inputStream = result.getBinaryStream("Avatar");

	            if (inputStream != null) {
	                // Đọc dữ liệu ảnh từ InputStream
	                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	                byte[] data = new byte[1024];
	                int nRead;
	                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
	                    buffer.write(data, 0, nRead);
	                }
	                buffer.flush();
	                byte[] imageBytes = buffer.toByteArray();
	                inputStream.close();

	                // Tạo các luồng ảnh từ dữ liệu
	                InputStream imgStream1 = new ByteArrayInputStream(imageBytes);
	                InputStream imgStream2 = new ByteArrayInputStream(imageBytes);

	                // Hiển thị ảnh lên giao diện
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
	@FXML
	public void profileUpdateBtn(ActionEvent event) {
	    // Cập nhật hồ sơ admin
	    String name = txt_name_admin.getText().trim();
	    String username = txt_username_admin.getText().trim();
	    String gender = gender_cb.getValue();
	    String email = txt_email_admin.getText().trim();

	    // Kiểm tra dữ liệu đầu vào
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

	    // Câu truy vấn kiểm tra username trùng
	    String checkUsernameSQL = "SELECT * FROM user_account WHERE username = ? AND id <> ?";
	    // Câu truy vấn cập nhật thông tin
	    String updateUserSQL = "UPDATE user_account SET name = ?, username = ?, gender = ?, email = ? WHERE id = ?";

	    try (Connection connect = Database.connectDB();
	            PreparedStatement checkStmt = connect.prepareStatement(checkUsernameSQL);
	            PreparedStatement updateStmt = connect.prepareStatement(updateUserSQL)) {

	        // Kiểm tra username đã tồn tại
	        checkStmt.setString(1, username);
	        checkStmt.setString(2, currentUserId);
	        ResultSet result = checkStmt.executeQuery();

	        if (result.next()) {
	            alert.errorMessage("Username '" + username + "' already exists!");
	            return;
	        }

	        // Cập nhật thông tin
	        updateStmt.setString(1, name);
	        updateStmt.setString(2, username);
	        updateStmt.setString(3, gender);
	        updateStmt.setString(4, email);
	        updateStmt.setString(5, currentUserId);

	        int rowsUpdated = updateStmt.executeUpdate();

	        // Kiểm tra kết quả cập nhật
	        if (rowsUpdated > 0) {
	            alert.successMessage("Profile updated successfully.");
	            loadAdminProfile(); // Tải lại hồ sơ
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
	    // Thêm ảnh đại diện
	    FileChooser open = new FileChooser();
	    open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*jpg", "*jpeg", "*png"));

	    // Chọn file ảnh
	    File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());

	    if (file != null) {
	        Data.path = file.getAbsolutePath();

	        // Hiển thị ảnh lên giao diện
	        image = new Image(file.toURI().toString(), 137, 95, false, true);
	        profile_circle.setFill(new ImagePattern(image));

	        // Lưu ảnh vào cơ sở dữ liệu
	        try {
	            connect = Database.connectDB();
	            String updateAvatarSQL = "UPDATE user_account SET Avatar = ? WHERE email = ?";

	            FileInputStream input = new FileInputStream(file);
	            prepare = connect.prepareStatement(updateAvatarSQL);
	            prepare.setBinaryStream(1, input, (int) file.length());
	            prepare.setString(2, email_admin.getText());

	            int rows = prepare.executeUpdate();
	            // Kiểm tra kết quả cập nhật
	            if (rows > 0) {
	                alert.successMessage("Avatar updated successfully.");
	            } else {
	                alert.errorMessage("Failed to update Avatar.");
	            }
	            profileDisplayImages(); // Tải lại ảnh
	        } catch (Exception e) {
	            e.printStackTrace();
	            alert.errorMessage("Error uploading Avatar: " + e.getMessage());
	        }
	    }
	}

	/* =====================FORMAT AND INTIALIZE======================================== */
	public class FormatterUtils {
	    // Định dạng ngày giờ
	    public static String formatTimestamp(String createdAt) {
	        try {
	            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
	            return outputFormat.format(inputFormat.parse(createdAt));
	        } catch (Exception e) {
	            return "";
	        }
	    }

	    // Định dạng tiền tệ VNĐ
	    public static String formatCurrencyVND(BigDecimal amount) {
	        if (amount == null)
	            return "0 VNĐ";
	        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
	        return formatter.format(amount) + " VNĐ";
	    }
	}

	public void runTime() {
	    // Cập nhật thời gian thực
	    new Thread() {
	        public void run() {
	            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	            while (true) {
	                try {
	                    Thread.sleep(1000); // 1000 ms = 1s
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

	@FXML
	private void logoutBtn() {
	    // Đăng xuất
	    try {
	        // Mở form đăng nhập
	        Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.setTitle("Login");
	        stage.show();

	        // Đóng cửa sổ hiện tại
	        logout_btn.getScene().getWindow().hide();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@FXML
	private void switchForm(ActionEvent event) {
	    // Chuyển đổi giữa các form
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
	    // Hiển thị form tương ứng
	    // Ẩn tất cả form
	    dashboard_form.setVisible(false);
	    doctors_form.setVisible(false);
	    receptionist_form.setVisible(false);
	    service_form.setVisible(false);
	    profile_form.setVisible(false);
	    report_form.setVisible(false);

	    // Hiển thị form được chọn
	    switch (formName) {
	    case "dashboard":
	        dashboard_form.setVisible(true);
	        current_form.setText("Dashboard Form");
	        break;
	    case "doctors":
	        doctors_form.setVisible(true);
	        current_form.setText("Doctors Form");
	        loadDoctorTable(); // Tải dữ liệu bác sĩ
	        break;
	    case "receptionists":
	        receptionist_form.setVisible(true);
	        current_form.setText("Receptionists Form");
	        loadReceptionistTable(); // Tải dữ liệu lễ tân
	        break;
	    case "service":
	        service_form.setVisible(true);
	        current_form.setText("Service Form");
	        loadServiceTable(); // Tải dữ liệu dịch vụ
	        break;
	    case "profile":
	        profile_form.setVisible(true);
	        current_form.setText("Profile Form");
	        break;
	    case "report":
	        report_form.setVisible(true);
	        current_form.setText("Report Form");
	        resetRevenueFilter(); // Đặt lại bộ lọc doanh thu
	        break;
	    }
	}

	public void loadComboBox() {
	    // Tải danh sách giới tính vào ComboBox
	    gender_cb.setItems(FXCollections
	            .observableArrayList(Arrays.stream(Gender.values()).map(Enum::name).collect(Collectors.toList())));
	}

	@FXML
	public void initialize() {
	    // Khởi tạo giao diện
	    runTime(); // Bắt đầu cập nhật thời gian
	    loadComboBox(); // Tải danh sách giới tính
	    showForm("dashboard"); // Hiển thị form mặc định
	}
}