package Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Alert.AlertMessage;
import Controller.AdminMainFormController.FormatterUtils;
import DAO.Database;
import Enum.Gender;
import Model.AppointmentData;
import Model.Data;
import Model.PatientData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import Enum.AppointmentStatus;

/**
 *
 * @author WINDOWS 10
 */
public class DoctorMainFormController implements Initializable {

	@FXML
	private Button appointment_prescriptionBtn;

	@FXML
	private AnchorPane main_form;

	@FXML
	private Circle top_profile;

	@FXML
	private Label top_username;

	@FXML
	private Button logout_btn;

	@FXML
	private Label date_time;

	@FXML
	private Label current_form;

	@FXML
	private Label nav_adminID;

	@FXML
	private Label nav_username;

	@FXML
	private Button dashboard_btn;

	@FXML
	private Button patients_btn;

	@FXML
	private Button appointments_btn;

	@FXML
	private Button profile_btn;

	@FXML
	private AnchorPane dashboard_form;

	@FXML
	private Label dashboard_IP;

	@FXML
	private Label dashboard_TP;

	@FXML
	private Label dashboard_AP;

	@FXML
	private Label dashboard_tA;

	@FXML
	private AreaChart<?, ?> dashboad_chart_PD;

	@FXML
	private BarChart<?, ?> dashboad_chart_DD;
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

	public class DashBoardAppointmentData {

		private String id;
		private String name;
		private String description;
		private String date;
		private String status;

		public DashBoardAppointmentData(String id, String name, String description, String date, String status) {
			this.id = id;
			this.name = name;
			this.description = description;
			this.date = date;
			this.status = status;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}

	@FXML
	private TableView<DashBoardAppointmentData> dashboad_tableView;
	@FXML
	private TableColumn<DashBoardAppointmentData, String> dashboad_col_appointmentID;
	@FXML
	private TableColumn<DashBoardAppointmentData, String> dashboad_col_name;
	@FXML
	private TableColumn<DashBoardAppointmentData, String> dashboad_col_description;
	@FXML
	private TableColumn<DashBoardAppointmentData, String> dashboad_col_appointmentDate;
	@FXML
	private TableColumn<DashBoardAppointmentData, String> dashboad_col_status;
	ObservableList<DashBoardAppointmentData> dashboad_listData = FXCollections.observableArrayList();

	@FXML
	private AnchorPane patients_form;

	@FXML
	private AnchorPane appointments_form;

	public class DoctorAppointmentData {

		private String id;
		private String time;
		private String status;
		private String patientId;
		private String gender;
		private String name;
		private String contactNumber;
		private String reason;
		private String createdDate;
		private String lastModifiedDate;

		public DoctorAppointmentData(String id, String time, String status, String patientId, String gender,
				String name, String contactNumber, String reason, String createdDate, String lastModifiedDate) {
			this.id = id;
			this.time = time;
			this.status = status;
			this.patientId = patientId;
			this.gender = gender;
			this.name = name;
			this.contactNumber = contactNumber;
			this.reason = reason;
			this.createdDate = createdDate;
			this.lastModifiedDate = lastModifiedDate;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getPatientId() {
			return patientId;
		}

		public void setPatientId(String patientId) {
			this.patientId = patientId;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		public String getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(String createdDate) {
			this.createdDate = createdDate;
		}

		public String getLastModifiedDate() {
			return lastModifiedDate;
		}

		public void setLastModifiedDate(String lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

		public String toString() {
			return "DoctorAppointmentData [id=" + id + ", time=" + time + ", status=" + status + ", patientId="
					+ patientId + ", name=" + name + ", contactNumber=" + contactNumber + ", reason=" + reason
					+ ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate + "]";
		}
	}

	@FXML
	private TableView<DoctorAppointmentData> appointments_tableView;
	@FXML
	private TableColumn<DoctorAppointmentData, String> appointments_col_appointmentID;
	@FXML
	private TableColumn<DoctorAppointmentData, String> appointments_col_time;
	@FXML
	private TableColumn<DoctorAppointmentData, String> appointments_col_status;
	@FXML
	private TableColumn<DoctorAppointmentData, String> appointments_col_name;
	@FXML
	private TableColumn<DoctorAppointmentData, String> appointments_col_contactNumber;
	@FXML
	private TableColumn<DoctorAppointmentData, String> appointments_col_reason;
	ObservableList<DoctorAppointmentData> appoinmentListData = FXCollections.observableArrayList();
	@FXML
	private ComboBox<String> appointments_searchBy;
	@FXML
	private TextField appointments_searchQuery;

	@FXML
	private TextField appointment_appointmentID;
	@FXML
	private TextField appointment_name;
	@FXML
	private ComboBox<String> appointment_gender;
	@FXML
	private TextField appointment_mobileNumber;
	@FXML
	private ComboBox<String> appointment_patientID;
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
	private ComboBox<String> patients_gender;

	@FXML
	private AnchorPane profile_form;

	@FXML
	private Circle profile_circleImage;

	@FXML
	private Button profile_importBtn;

	@FXML
	private TextField profile_name;

	@FXML
	private TextField profile_email;

	@FXML
	private ComboBox<String> profile_gender;

	@FXML
	private TextField profile_mobileNumber;

	@FXML
	private TextArea profile_address;

	@FXML
	private Label name_doctor, username_doctor, email_doctor, phone_doctor, gender_doctor, specialization_doctor,
			createdDate_doctor;
	@FXML
	private TextField profile_username;
	@FXML
	private Button profile_updateBtn;

	private Connection connect;
	private PreparedStatement prepare;
	private Statement statement;
	private ResultSet result;

	private Image image;

	private final AlertMessage alert = new AlertMessage();

	// load data admin
	private String username;
	private String doctor_id;

	public void setUserData(String username, String id) {
	    // Gán thông tin người dùng
	    this.username = username;
	    this.doctor_id = id;

	    // Tải hồ sơ bác sĩ và hiển thị ảnh đại diện
	    loadDoctorProfile();
	    profileDisplayImages();
	}

	public void appointmentPrescriptionBtn() {
	    // Xử lý nút tạo đơn thuốc cho cuộc hẹn
	    DoctorAppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();
	    // Kiểm tra xem có chọn cuộc hẹn nào không
	    if (selectedAppointment == null) {
	        alert.errorMessage("Please select an appointment first.");
	        return;
	    }
	    // Kiểm tra trạng thái cuộc hẹn
	    if (selectedAppointment.getStatus().equals(AppointmentStatus.Cancel.toString())) {
	        alert.errorMessage("Cannot create prescription for cancelled appointment.");
	        return;
	    }
	    if (selectedAppointment.getStatus().equals(AppointmentStatus.Coming.toString())) {
	        alert.errorMessage("Cannot create prescription for upcoming appointment.");
	        return;
	    }

	    try {
	        // Tải giao diện chi tiết đơn thuốc
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PrescriptionDetail.fxml"));
	        Parent root = loader.load();

	        // Truyền dữ liệu cuộc hẹn sang controller
	        PrescriptionDetailController controller = loader.getController();
	        controller.setAppointmentData(selectedAppointment.getId(), selectedAppointment.getPatientId(), doctor_id);

	        // Hiển thị form trong cửa sổ modal
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Prescription Details");
	        stage.setResizable(false);
	        stage.setScene(new Scene(root));

	        // Xử lý khi form đóng
	        stage.setOnHidden(e -> {
	            // Cập nhật số lượng đơn thuốc và dữ liệu cuộc hẹn trên dashboard
	            dashboardDisplayNumPrescriptions();
	            dashboardLoadAppointmentData();

	            // Cập nhật trạng thái đơn thuốc thành "Created"
	            connect = Database.connectDB();
	            if (connect != null) {
	                try {
	                    String updateSql = "UPDATE appointment SET Prescription_Status = ? WHERE id = ?";
	                    prepare = connect.prepareStatement(updateSql);
	                    prepare.setString(1, "Created");
	                    prepare.setString(2, selectedAppointment.getId());
	                    int rowsUpdated = prepare.executeUpdate();
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                    alert.errorMessage("Error updating Prescription_Status: " + ex.getMessage());
	                } finally {
	                    // Đóng tài nguyên
	                    try {
	                        if (prepare != null)
	                            prepare.close();
	                        if (connect != null)
	                            connect.close();
	                    } catch (SQLException ex) {
	                        ex.printStackTrace();
	                    }
	                }
	            }
	        });

	        stage.showAndWait();

	    } catch (IOException e) {
	        e.printStackTrace();
	        alert.errorMessage("Error opening prescription form: " + e.getMessage());
	    }
	}

	public void appointmentUpdateBtn() {
	    // Get selected appointment
	    DoctorAppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();
	    if (selectedAppointment == null) {
	        alert.errorMessage("Please select an appointment to update!");
	        return;
	    }

	    // Get current appointment details
	    String appointmentId = appointment_appointmentID.getText();
	    String currentStatus = selectedAppointment.getStatus();
	    String currentPrescriptionStatus = null;
	    String currentPatientId = selectedAppointment.getPatientId();

	    // Get updated values from input fields
	    String newPatientId = appointment_patientID.getValue();
	    String newStatus = appointment_status.getValue();
	    String newCancelReason = appointment_cancelReason.getText() != null ? appointment_cancelReason.getText().trim() : "";
	    LocalDate newDate = appointment_date.getValue();
	    String newTime = appointment_time.getText() != null ? appointment_time.getText().trim() : "";

	    // Validate inputs
	    if (appointmentId.isEmpty() || newPatientId == null || newStatus == null || newDate == null || newTime.isEmpty()) {
	        alert.errorMessage("Please fill all required fields!");
	        return;
	    }

	    // Parse time
	    LocalTime parsedTime;
	    try {
	        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	        parsedTime = LocalTime.parse(newTime, timeFormatter);
	    } catch (Exception e) {
	        alert.errorMessage("Invalid time format! Please use HH:mm (e.g., 14:30)");
	        return;
	    }

	    // Combine date and time
	    LocalDateTime newDateTime;
	    newDateTime = LocalDateTime.of(newDate, parsedTime);
//	    try {
//	        newDateTime = LocalDateTime.of(newDate, parsedTime);
//	        if (newDateTime.isBefore(LocalDateTime.now())) {
//	            alert.errorMessage("Appointment date and time cannot be in the past!");
//	            return;
//	        }
//	    } catch (Exception e) {
//	        alert.errorMessage("Invalid date or time!");
//	        return;
//	    }

	    // Format time for SQL
	    String sqlTimeStr = newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

	    Connection conn = null;
	    try {
	        conn = Database.connectDB();
	        conn.setAutoCommit(false);

	        // Get current Prescription_status
	        String checkPrescriptionStatusSQL = "SELECT Prescription_status FROM APPOINTMENT WHERE Id = ?";
	        PreparedStatement psCheckStatus = conn.prepareStatement(checkPrescriptionStatusSQL);
	        psCheckStatus.setString(1, appointmentId);
	        ResultSet rsStatus = psCheckStatus.executeQuery();
	        if (rsStatus.next()) {
	            currentPrescriptionStatus = rsStatus.getString("Prescription_status");
	        } else {
	            alert.errorMessage("Appointment not found!");
	            conn.rollback();
	            return;
	        }

	        // Prevent status change if Prescription_status is Paid or Created
	        if ((currentPrescriptionStatus.equals("Paid") || currentPrescriptionStatus.equals("Created")) &&
	                !newStatus.equals(currentStatus)) {
	            alert.errorMessage("Cannot change appointment status when Prescription_status is 'Paid' or 'Created'!");
	            conn.rollback();
	            return;
	        }

	        // Check if patient has changed
	        boolean patientChanged = !newPatientId.equals(currentPatientId);

	        // Check if prescription exists
	        String checkPrescriptionSQL = "SELECT Id, status FROM PRESCRIPTION WHERE Appointment_id = ?";
	        PreparedStatement psCheck = conn.prepareStatement(checkPrescriptionSQL);
	        psCheck.setString(1, appointmentId);
	        ResultSet rs = psCheck.executeQuery();
	        boolean prescriptionExists = rs.next();
	        String prescriptionId = prescriptionExists ? rs.getString("Id") : null;
	        String prescriptionStatus = prescriptionExists ? rs.getString("status") : null;

	        // If prescription is Paid and patient changed, warn user
	        if (prescriptionExists && prescriptionStatus.equals("Paid") && patientChanged) {
	            Alert warning = new Alert(Alert.AlertType.WARNING);
	            warning.setTitle("Prescription Already Paid");
	            warning.setHeaderText("The prescription for this appointment is already paid.");
	            warning.setContentText("Updating patient will reset the Prescription_status to 'Created' and require a new invoice. Proceed?");
	            warning.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
	            if (warning.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
	                conn.rollback();
	                return;
	            }
	        }

	        // Check doctor availability
	        String sqlDoctor = "SELECT COUNT(*) FROM AVAILABLE_SLOT WHERE Doctor_id = ? AND Slot_time = ? AND Slot_date = ? AND Is_booked = TRUE AND Appointment_id != ?";
	        PreparedStatement psDoctor = conn.prepareStatement(sqlDoctor);
	        psDoctor.setString(1, doctor_id);
	        psDoctor.setString(2, newDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	        psDoctor.setString(3, newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	        psDoctor.setString(4, appointmentId);
	        ResultSet rsDoctor = psDoctor.executeQuery();
	        if (rsDoctor.next() && rsDoctor.getInt(1) > 0) {
	            alert.errorMessage("You are already booked at this time!");
	            conn.rollback();
	            return;
	        }

	        // Check patient availability
	        String sqlPatient = "SELECT COUNT(*) FROM APPOINTMENT WHERE Patient_id = ? AND Time = ? AND Id != ?";
	        PreparedStatement psPatient = conn.prepareStatement(sqlPatient);
	        psPatient.setString(1, newPatientId);
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
	        psUpdate.setString(1, newPatientId);
	        psUpdate.setString(2, doctor_id);
	        psUpdate.setString(3, newStatus);
	        psUpdate.setString(4, newCancelReason.isEmpty() ? null : newCancelReason);
	        psUpdate.setString(5, sqlTimeStr);
	        psUpdate.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
	        psUpdate.setString(7, (prescriptionExists && prescriptionStatus.equals("Paid") && patientChanged) ? "Created" : currentPrescriptionStatus);
	        psUpdate.setString(8, appointmentId);
	        int rowsAffected = psUpdate.executeUpdate();

	        // Update prescription if it exists and patient changed
	        if (prescriptionExists && patientChanged) {
	            String updatePrescriptionSQL = "UPDATE PRESCRIPTION SET Patient_id = ?, Doctor_id = ?, Update_date = ? WHERE Id = ?";
	            PreparedStatement psUpdatePrescription = conn.prepareStatement(updatePrescriptionSQL);
	            psUpdatePrescription.setString(1, newPatientId);
	            psUpdatePrescription.setString(2, doctor_id);
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
	        psInsertSlot.setString(1, doctor_id);
	        psInsertSlot.setString(2, newDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	        psInsertSlot.setString(3, newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	        psInsertSlot.setString(4, appointmentId);
	        psInsertSlot.setString(5, appointmentId);
	        psInsertSlot.executeUpdate();

	        if (rowsAffected > 0) {
	            conn.commit();
	            alert.successMessage("Appointment updated successfully!");
	            loadAppointmentData();
	            dashboardLoadAppointmentData();
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

	public void appointmentDeleteBtn() {
	    // Lấy lịch hẹn được chọn
		AlertMessage alert1 = new AlertMessage();
	    DoctorAppointmentData selectedAppointment = appointments_tableView.getSelectionModel().getSelectedItem();
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
	                dashboardLoadAppointmentData(); // Refresh dashboard
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
	            alert1.errorMessage("Error deleting appointment: " + e.getMessage());
	        } finally {
	            try {
	                if (conn != null) conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	public void appointmentClearBtn() {
	    // Xóa dữ liệu trên form nhập liệu
	    appointment_appointmentID.clear();
	    appointment_name.clear();
	    appointment_gender.getSelectionModel().clearSelection();
	    appointment_gender.setValue("");
	    appointment_mobileNumber.clear();
	    appointment_patientID.getSelectionModel().clearSelection();
	    appointment_patientID.setValue("");
	    appointment_status.getSelectionModel().clearSelection();
	    appointment_status.setValue("");
	    appointment_cancelReason.clear();
	    appointment_date.getEditor().clear();
	    appointment_time.clear();
	    appointment_createdDate.setText("");
	    appointment_updatedDate.setText("");
	}

	// =======================CRUD Patient==================================
	private ObservableList<PatientData> patientMasterList = FXCollections.observableArrayList();

	private void initializePatientFilters() {
	    // Khởi tạo bộ lọc bệnh nhân
	    cmbPatientSearchBy.setItems(FXCollections.observableArrayList("Name", "Email", "Phone", "Address", "Diagnosis",
	            "Height", "Weight"));
	    cmbPatientSearchBy.setValue("Name");

	    txtPatientSearch.clear();

	    cmbPatientGenderFilter.setItems(FXCollections.observableArrayList("All", "Male", "Female", "Other"));
	    cmbPatientGenderFilter.setValue("All");

	    // Gắn sự kiện thay đổi cho các bộ lọc
	    txtPatientSearch.textProperty().addListener((obs, oldVal, newVal) -> applyPatientFilters());
	    cmbPatientSearchBy.valueProperty().addListener((obs, o, n) -> applyPatientFilters());
	    cmbPatientGenderFilter.valueProperty().addListener((obs, o, n) -> applyPatientFilters());
	}

	private void applyPatientFilters() {
	    // Áp dụng bộ lọc cho danh sách bệnh nhân
	    String keyword = txtPatientSearch.getText().toLowerCase();
	    String searchBy = cmbPatientSearchBy.getValue();
	    String selectedGender = cmbPatientGenderFilter.getValue();

	    ObservableList<PatientData> filtered = FXCollections.observableArrayList();

	    // Lọc dữ liệu theo từ khóa và giới tính
	    for (PatientData p : patientMasterList) {
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
	    // Tải danh sách bệnh nhân
	    patientMasterList.clear();

	    String sql = "SELECT Patient_id, Name, Email, Gender, Phone, Address, Diagnosis, Height, Weight, Create_date, Update_date FROM PATIENT";

	    try {
	        Connection conn = Database.connectDB();
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

	        // Tạo cột hành động với nút chi tiết
	        patients_col_action.setCellFactory(col -> new TableCell<>() {
	            private final Button detailBtn = new Button("Detail");
	            private final HBox hbox = new HBox(5, detailBtn);
	            {
	                hbox.setAlignment(Pos.CENTER);
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

	    } catch (Exception e) {
	        e.printStackTrace();
	        // Hiển thị thông báo lỗi
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setContentText("Error loading patient list!");
	        alert.showAndWait();
	    }
	}

	private void handleViewPatientDetail(String patientId) {
	    // Xem chi tiết thông tin bệnh nhân
	    try (Connection conn = Database.connectDB()) {
	        String sql = "SELECT * FROM PATIENT WHERE Patient_id = ?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, patientId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            // Tạo đối tượng bệnh nhân từ dữ liệu
	            PatientData patient = new PatientData(rs.getString("Patient_id"), rs.getString("Name"),
	                    rs.getString("Email"), rs.getString("Gender"), rs.getString("Phone"), rs.getString("Address"),
	                    rs.getString("Diagnosis"), rs.getBigDecimal("Height"), rs.getBigDecimal("Weight"),
	                    rs.getTimestamp("Create_date"), rs.getTimestamp("Update_date"));

	            // Tải giao diện chi tiết bệnh nhân
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DoctorPatientDetailView.fxml"));
	            Parent root = loader.load();

	            // Truyền dữ liệu sang controller
	            PatientDetailController controller = loader.getController();
	            controller.setPatientData(patient);

	            // Hiển thị cửa sổ
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
	        alert.errorMessage("Error loading patient detail: " + e.getMessage());
	    }
	}

	/* =====================LOAD PROFILE======================================== */
	public void loadDoctorProfile() {
	    // Tải thông tin hồ sơ bác sĩ
	    String selectData = "SELECT ua.name, ua.username, ua.email, ua.gender, ua.create_date, d.phone, d.address, s.name AS ServiceName "
	            + "FROM user_account ua " + "JOIN doctor d ON ua.id = d.doctor_id JOIN SERVICE s ON s.Id=d.Service_id "
	            + "WHERE ua.username = ?";

	    connect = Database.connectDB();

	    try {
	        prepare = connect.prepareStatement(selectData);
	        prepare.setString(1, username);
	        result = prepare.executeQuery();

	        // Kiểm tra dữ liệu
	        if (!result.next()) {
	            alert.errorMessage("No data found!");
	            return;
	        }

	        // Lấy thông tin từ cơ sở dữ liệu
	        String name = result.getString("name");
	        String username = result.getString("username");
	        String email = result.getString("email");
	        String phone = result.getString("phone");
	        String address = result.getString("address");
	        String gender = result.getString("gender");
	        String createdAt = result.getString("create_date");
	        String specialization = result.getString("ServiceName");

	        // Gán thông tin cho các nhãn
	        name_doctor.setText(name != null ? name : "UNKNOWN");
	        username_doctor.setText(username != null ? username : "");
	        gender_doctor.setText(gender != null ? gender : "");
	        phone_doctor.setText(phone != null ? phone : "");
	        email_doctor.setText(email != null ? email : "");
	        createdDate_doctor.setText(createdAt != null ? FormatterUtils.formatTimestamp(createdAt) : "");
	        specialization_doctor.setText(specialization != null ? specialization : "");
	        top_username.setText(name != null ? name : "UNKNOWN");

	        // Gán thông tin cho các ô nhập liệu
	        profile_name.setText(name != null ? name : "");
	        profile_username.setText(username != null ? username : "");
	        profile_mobileNumber.setText(phone != null ? phone : "");
	        profile_gender.setValue(gender != null ? gender : "");
	        profile_address.setText(address != null ? address : "");

	        nav_adminID.setText(username != null ? username : "");
	        nav_username.setText(name != null ? name : "");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public void profileDisplayImages() {
	    // Hiển thị ảnh đại diện từ cơ sở dữ liệu
	    String sql = "SELECT Avatar FROM user_account WHERE username = ?";
	    connect = Database.connectDB();

	    try {
	        prepare = connect.prepareStatement(sql);
	        prepare.setString(1, username);
	        result = prepare.executeQuery();
	        if (result.next()) {
	            InputStream inputStream = result.getBinaryStream("Avatar");

	            if (inputStream != null) {
	                // Đọc dữ liệu ảnh
	                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	                byte[] data = new byte[1024];
	                int nRead;
	                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
	                    buffer.write(data, 0, nRead);
	                }
	                buffer.flush();
	                byte[] imageBytes = buffer.toByteArray();
	                inputStream.close();

	                // Tạo ảnh và hiển thị
	                InputStream imgStream1 = new ByteArrayInputStream(imageBytes);
	                InputStream imgStream2 = new ByteArrayInputStream(imageBytes);

	                Image img1 = new Image(imgStream1, 0, 0, true, true);
	                Image img2 = new Image(imgStream2, 0, 0, true, true);

	                profile_circleImage.setFill(new ImagePattern(img1));
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
	    // Cập nhật hồ sơ bác sĩ
	    String name = profile_name.getText();
	    String phone = profile_mobileNumber.getText();
	    String usernameEdit = profile_username.getText();
	    String address = profile_address.getText();
	    String email = email_doctor.getText();
	    String gender = (String) profile_gender.getSelectionModel().getSelectedItem();

	    // Kiểm tra dữ liệu đầu vào
	    if (usernameEdit.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
	        alert.errorMessage("Please fill in all the fields.");
	        return;
	    } else if (gender == null || gender.isEmpty()) {
	        alert.errorMessage("Please select a gender.");
	        return;
	    }

	    // Câu truy vấn kiểm tra và cập nhật
	    String checkUsernameSQL = "SELECT * FROM user_account WHERE username = ?";
	    String updateUserSQL = "UPDATE user_account SET name = ?, username = ?, gender = ? WHERE email = ?";
	    String updateDoctorSQL = "UPDATE doctor SET phone = ?, address = ?  WHERE doctor_id = (SELECT id FROM user_account WHERE email = ?)";

	    connect = Database.connectDB();

	    try {
	        // Kiểm tra username trùng
	        if (!username.equals(usernameEdit)) {
	            prepare = connect.prepareStatement(checkUsernameSQL);
	            prepare.setString(1, usernameEdit);
	            result = prepare.executeQuery();

	            if (result.next()) {
	                alert.errorMessage("Username \"" + usernameEdit + "\" already exists!");
	                return;
	            }
	        }

	        // Cập nhật thông tin user_account
	        prepare = connect.prepareStatement(updateUserSQL);
	        prepare.setString(1, name);
	        prepare.setString(2, usernameEdit);
	        prepare.setString(3, gender);
	        prepare.setString(4, email);
	        int rowsUserUpdated = prepare.executeUpdate();

	        // Cập nhật thông tin doctor
	        prepare = connect.prepareStatement(updateDoctorSQL);
	        prepare.setString(1, phone);
	        prepare.setString(2, address);
	        prepare.setString(3, email);
	        int rowsReceptionistUpdated = prepare.executeUpdate();

	        // Kiểm tra kết quả cập nhật
	        if (rowsUserUpdated > 0 || rowsReceptionistUpdated > 0) {
	            alert.successMessage("Profile updated successfully.");
	            this.username = usernameEdit;
	            loadDoctorProfile();
	        } else {
	            alert.errorMessage("No update.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        alert.errorMessage("Error updating profile: " + e.getMessage());
	    }
	}

	public void profileInsertImage() {
	    // Thêm ảnh đại diện
	    FileChooser open = new FileChooser();
	    open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*jpg", "*jpeg", "*png"));

	    // Chọn file ảnh
	    File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());

	    if (file.exists()) {
	        // Hiển thị ảnh lên giao diện
	        image = new Image(file.toURI().toString(), 0, 0, true, true);
	        if (image == null || image.isError()) {
	            alert.errorMessage("Error loading image: " + image.getException().getMessage());
	            return;
	        }
	        profile_circleImage.setFill(new ImagePattern(image));

	        Data.path = file.getAbsolutePath();

	        // Lưu ảnh vào cơ sở dữ liệu
	        try {
	            connect = Database.connectDB();
	            String updateAvatarSQL = "UPDATE user_account SET avatar = ? WHERE email = ?";

	            FileInputStream input = new FileInputStream(file);
	            prepare = connect.prepareStatement(updateAvatarSQL);
	            prepare.setBinaryStream(1, input, (int) file.length());
	            prepare.setString(2, email_doctor.getText());

	            int rows = prepare.executeUpdate();
	            // Kiểm tra kết quả cập nhật
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

	/* =====================FORMAT AND INTIALIZE======================================== */
	public void switchForm(ActionEvent event) {
	    // Chuyển đổi giữa các form
	    if (event.getSource() == dashboard_btn) {
	        showForm("dashboard");
	    } else if (event.getSource() == patients_btn) {
	        showForm("patients");
	    } else if (event.getSource() == appointments_btn) {
	        showForm("appointments");
	    } else if (event.getSource() == profile_btn) {
	        showForm("profile");
	    }
	}

	private void showForm(String formName) {
	    // Hiển thị form tương ứng
	    dashboard_form.setVisible(false);
	    patients_form.setVisible(false);
	    appointments_form.setVisible(false);
	    profile_form.setVisible(false);

	    switch (formName) {
	    case "dashboard":
	        dashboard_form.setVisible(true);
	        current_form.setText("Home Form");
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
	    case "profile":
	        profile_form.setVisible(true);
	        current_form.setText("Profile Form");
	        break;
	    }
	}

	public void logoutBtn() {
	    // Đăng xuất
	    try {
	        // Xác nhận đăng xuất
	        if (alert.confirmationMessage("Are you sure you want to logout?")) {
	            // Mở form đăng nhập
	            Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
	            Stage stage = new Stage();
	            stage.setOnCloseRequest(e -> {
	                Platform.exit();
	                System.exit(0);
	            });
	            stage.setScene(new Scene(root));
	            stage.setTitle("Login");
	            stage.show();

	            // Đóng cửa sổ hiện tại
	            logout_btn.getScene().getWindow().hide();

	            // Xóa dữ liệu tạm
	            Data.doctor_id = "";
	            Data.doctor_name = "";
	            Data.temp_PatientID = 0;
	            Data.temp_name = "";
	            Data.temp_gender = "";
	            Data.temp_number = Long.parseLong("0");
	            Data.temp_address = "";
	            Data.temp_status = "";
	            Data.temp_date = "";
	            Data.temp_path = "";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public void runTime() {
	    // Cập nhật thời gian thực
	    new Thread() {
	        public void run() {
	            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	            while (true) {
	                try {
	                    Thread.sleep(1000);
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

	public void loadComboBox() {
	    // Tải danh sách giới tính vào ComboBox
	    profile_gender.setItems(FXCollections
	            .observableArrayList(Arrays.stream(Gender.values()).map(Enum::name).collect(Collectors.toList())));
	}

	public class FormatterUtils {
	    // Định dạng tiền tệ VNĐ
	    public static String formatCurrencyVND(BigDecimal amount) {
	        if (amount == null)
	            return "0 VNĐ";
	        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
	        return formatter.format(amount) + " VNĐ";
	    }

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

	    // Định dạng thời gian
	    public static String formatTime(String time) {
	        try {
	            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
	            return outputFormat.format(inputFormat.parse(time));
	        } catch (Exception e) {
	            return "";
	        }
	    }

	    // Lấy ngày
	    public static String getDate(String time) {
	        try {
	            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
	            return outputFormat.format(inputFormat.parse(time));
	        } catch (Exception e) {
	            return "";
	        }
	    }

	    // Lấy giờ
	    public static String getTime(String time) {
	        try {
	            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
	            return outputFormat.format(inputFormat.parse(time));
	        } catch (Exception e) {
	            return "";
	        }
	    }

	    // Chuyển đổi sang định dạng SQL
	    public static String toSQLDate(String date) {
	        try {
	            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
	            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            return outputFormat.format(inputFormat.parse(date));
	        } catch (Exception e) {
	            return "";
	        }
	    }

	    // Chuyển đổi thành LocalDate
	    public static LocalDate localDate(String dateString) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	        LocalDate localDate = LocalDate.parse(dateString, formatter);
	        return localDate;
	    }
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    // Khởi tạo controller
	    System.out.println("DoctorMainFormController initialized " + username);

	    // Ẩn tất cả form ban đầu
	    dashboard_form.setVisible(false);
	    patients_form.setVisible(false);
	    appointments_form.setVisible(false);
	    profile_form.setVisible(true);

	    // Tải dữ liệu ban đầu
	    loadComboBox();
	    runTime();
	    initializePatientFilters();

	    // Gán giá trị cho các cột bảng
	    dashboad_col_appointmentID.setCellValueFactory(new PropertyValueFactory<>("id"));
	    dashboad_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
	    dashboad_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
	    dashboad_col_appointmentDate.setCellValueFactory(new PropertyValueFactory<>("date"));
	    dashboad_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
	    dashboad_tableView.setItems(dashboad_listData);

	    appointments_col_appointmentID.setCellValueFactory(new PropertyValueFactory<>("id"));
	    appointments_col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
	    appointments_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
	    appointments_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
	    appointments_col_contactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
	    appointments_col_reason.setCellValueFactory(new PropertyValueFactory<>("reason"));
	    appointments_tableView.setItems(appoinmentListData);

	    // Tải danh sách vào ComboBox
	    appointment_gender.setItems(FXCollections
	            .observableArrayList(Arrays.stream(Gender.values()).map(Enum::name).collect(Collectors.toList())));
	    appointment_status.setItems(FXCollections.observableArrayList(
	            Arrays.stream(AppointmentStatus.values()).map(Enum::name).collect(Collectors.toList())));
	    appointment_patientID.setItems(patientIds);

	    // Định dạng ngày cho DatePicker
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    appointment_date.setConverter(new StringConverter<LocalDate>() {
	        @Override
	        public String toString(LocalDate date) {
	            return (date != null) ? formatter.format(date) : appointment_date.getEditor().getText();
	        }

	        @Override
	        public LocalDate fromString(String string) {
	            return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
	        }
	    });

	    // Khởi tạo bộ lọc tìm kiếm
	    appointments_searchBy.setItems(FXCollections.observableArrayList("Name", "Contact", "Cancel Reason"));
	    appointments_searchBy.getSelectionModel().selectFirst();
	    appointments_searchBy.valueProperty().addListener((observable, oldValue, newValue) -> {
	        onSearchChanged();
	    });
	    appointments_searchQuery.textProperty().addListener((observable, oldValue, newValue) -> {
	        onSearchChanged();
	    });
	}

	private void onSearchChanged() {
	    // Xử lý tìm kiếm cuộc hẹn
	    String searchBy = appointments_searchBy.getSelectionModel().getSelectedItem();
	    String query = appointments_searchQuery.getText().trim().toLowerCase();

	    // Nếu không có từ khóa hoặc tiêu chí, hiển thị toàn bộ dữ liệu
	    if (query.isEmpty() || searchBy == null || searchBy.isEmpty()) {
	        appointments_tableView.setItems(appoinmentListData);
	        return;
	    }

	    // Lọc dữ liệu theo tiêu chí
	    ObservableList<DoctorAppointmentData> filteredData = appoinmentListData.filtered(appointment -> {
	        switch (searchBy) {
	        case "Name":
	            return appointment.getName() != null && appointment.getName().toLowerCase().contains(query);
	        case "Contact":
	            return appointment.getContactNumber() != null
	                    && appointment.getContactNumber().toLowerCase().contains(query);
	        case "Cancel Reason":
	            return appointment.getReason() != null && appointment.getReason().toLowerCase().contains(query);
	        default:
	            return false;
	        }
	    });
	    appointments_tableView.setItems(filteredData);
	}

	public void setDoctorId(String doctor_id) {
	    // Gán ID bác sĩ
	    this.doctor_id = doctor_id;
	    loadDoctorProfile();
	    profileDisplayImages();
	}

	public void setUsername(String username) {
	    // Gán username
	    this.username = username;
	    loadDoctorProfile();
	    profileDisplayImages();
	}

	ObservableList<String> patientIds = FXCollections.observableArrayList();

	public void load() {
	    // Tải dữ liệu ban đầu
	    System.out.println("DoctorMainFormController load: " + username + " " + doctor_id);

	    // Kiểm tra thông tin đăng nhập
	    if (username == null || username.isEmpty() || doctor_id == null || doctor_id.isEmpty()) {
	        alert.errorMessage("Please login first");
	        return;
	    }

	    // Tải dữ liệu dashboard
	    dashboardDisplayNumDrugs();
	    dashboardDisplayNumPatients();
	    dashboardDisplayNumPrescriptions();
	    dashboardDisplayAppointments();
	    dashboardLoadAppointmentData();

	    // Tải danh sách vào ComboBox
	    appointment_gender.setItems(FXCollections
	            .observableArrayList(Arrays.stream(Gender.values()).map(Enum::name).collect(Collectors.toList())));
	    appointment_status.setItems(FXCollections.observableArrayList(
	            Arrays.stream(AppointmentStatus.values()).map(Enum::name).collect(Collectors.toList())));
	    appointment_name.setEditable(false);
	    appointment_mobileNumber.setEditable(false);

	    loadAppointmentData();
	    appointment_appointmentID.setEditable(false);

	    appointment_patientID.setItems(patientIds);
	    loadPatientData();
	}

	private void dashboardDisplayNumDrugs() {
	    // Hiển thị số lượng thuốc
	    String sql = "SELECT COUNT(*) AS total FROM drug";
	    connect = Database.connectDB();
	    try {
	        prepare = connect.prepareStatement(sql);
	        result = prepare.executeQuery();
	        if (result.next()) {
	            dashboard_IP.setText(result.getString("total"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void dashboardDisplayNumPatients() {
	    // Hiển thị số lượng bệnh nhân
	    String sql = "SELECT COUNT(*) AS total FROM patient";
	    connect = Database.connectDB();
	    try {
	        prepare = connect.prepareStatement(sql);
	        result = prepare.executeQuery();
	        if (result.next()) {
	            dashboard_TP.setText(result.getString("total"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void dashboardDisplayNumPrescriptions() {
	    // Hiển thị số lượng đơn thuốc
	    String sql = "SELECT COUNT(*) AS total FROM prescription WHERE doctor_id = ?";
	    connect = Database.connectDB();
	    try {
	        prepare = connect.prepareStatement(sql);
	        prepare.setString(1, doctor_id);
	        result = prepare.executeQuery();
	        if (result.next()) {
	            dashboard_AP.setText(result.getString("total"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void dashboardDisplayAppointments() {
	    // Hiển thị số lượng cuộc hẹn
	    String sql = "SELECT COUNT(*) AS total FROM appointment WHERE doctor_id = ?";
	    connect = Database.connectDB();
	    try {
	        prepare = connect.prepareStatement(sql);
	        prepare.setString(1, doctor_id);
	        result = prepare.executeQuery();
	        if (result.next()) {
	            dashboard_tA.setText(result.getString("total"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void dashboardLoadAppointmentData() {
	    // Tải dữ liệu cuộc hẹn cho dashboard
	    String sql = "SELECT *, p.Name AS Name FROM appointment JOIN patient p ON p.Patient_id = appointment.Patient_id WHERE doctor_id = ?";
	    connect = Database.connectDB();
	    try {
	        prepare = connect.prepareStatement(sql);
	        prepare.setString(1, doctor_id);
	        result = prepare.executeQuery();
	        dashboad_listData.clear();
	        while (result.next()) {
	            String id = result.getString("id");
	            String name = result.getString("name");
	            String description = result.getString("cancel_reason");
	            String date = result.getString("time");
	            String status = result.getString("status");

	            dashboad_listData.add(new DashBoardAppointmentData(id, name, description, date, status));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void loadAppointmentData() {
	    // Tải dữ liệu cuộc hẹn
	    String sql = "SELECT *, p.Patient_id AS Patient_ID, p.Name AS Name, p.Phone AS Contact_Number FROM appointment JOIN patient p ON p.Patient_id = appointment.Patient_id WHERE doctor_id = ?";
	    connect = Database.connectDB();
	    try {
	        prepare = connect.prepareStatement(sql);
	        prepare.setString(1, doctor_id);
	        result = prepare.executeQuery();
	        appoinmentListData.clear();
	        while (result.next()) {
	            String id = result.getString("id");
	            String time = result.getString("time");
	            String status = result.getString("status");
	            String patientId = result.getString("Patient_ID");
	            String gender = result.getString("Gender");
	            String name = result.getString("name");
	            String contactNumber = result.getString("contact_number");
	            String reason = result.getString("cancel_reason");
	            String createdDate = result.getString("create_date");
	            String lastModifiedDate = result.getString("update_date");

	            appoinmentListData.add(new DoctorAppointmentData(id, time, status, patientId, gender, name,
	                    contactNumber, reason, createdDate, lastModifiedDate));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void loadPatientData() {
	    // Tải danh sách ID bệnh nhân
	    String sql = "SELECT * FROM patient";
	    connect = Database.connectDB();
	    try {
	        prepare = connect.prepareStatement(sql);
	        result = prepare.executeQuery();
	        patientIds.clear();
	        while (result.next()) {
	            String id = result.getString("patient_id");
	            patientIds.add(id);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public void appointmentSelect() {
	    // Chọn cuộc hẹn từ bảng
	    DoctorAppointmentData appointmentData = appointments_tableView.getSelectionModel().getSelectedItem();
	    int index = appointments_tableView.getSelectionModel().getSelectedIndex();
	    if (index <= -1 || appointmentData == null) {
	        System.out.println("No appointment selected");
	        return;
	    }

	    // Gán dữ liệu cuộc hẹn vào form
	    appointment_appointmentID.setText(appointmentData.getId());
	    appointment_name.setText(appointmentData.getName());
	    appointment_gender.setValue(appointmentData.getGender());
	    appointment_mobileNumber.setText(appointmentData.getContactNumber());
	    appointment_patientID.setValue(appointmentData.getPatientId());
	    appointment_status.setValue(appointmentData.getStatus());
	    appointment_cancelReason.setText(appointmentData.getReason());
	    appointment_date.setValue(FormatterUtils.localDate(FormatterUtils.getDate(appointmentData.getTime())));
	    appointment_time.setText(FormatterUtils.getTime(appointmentData.getTime()));
	    appointment_createdDate.setText(FormatterUtils.formatTime(appointmentData.getCreatedDate()));
	    appointment_updatedDate.setText(FormatterUtils.formatTime(appointmentData.getLastModifiedDate()));
	}

	public void patientsSelect() {
	    // Chọn bệnh nhân từ ComboBox
	    int index = appointment_patientID.getSelectionModel().getSelectedIndex();
	    String patientId = appointment_patientID.getSelectionModel().getSelectedItem();
	    if (index <= -1 || patientId == null) {
	        System.out.println("No patient selected");
	        return;
	    }

	    // Lấy thông tin bệnh nhân
	    String sql = "SELECT * FROM patient WHERE patient_id = ?";
	    connect = Database.connectDB();
	    try {
	        prepare = connect.prepareStatement(sql);
	        prepare.setString(1, patientId);
	        result = prepare.executeQuery();
	        if (result.next()) {
	            String name = result.getString("name");
	            String phone = result.getString("phone");
	            String gender = result.getString("gender");
	            appointment_name.setText(name);
	            appointment_mobileNumber.setText(phone);
	            appointment_gender.setValue(gender);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}