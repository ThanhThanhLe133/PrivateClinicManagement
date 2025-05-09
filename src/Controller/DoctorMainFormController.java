package Controller;

import java.io.File;
import java.io.FileInputStream;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Alert.AlertMessage;
import Controller.AdminMainFormController.FormatterUtils;
import DAO.Database;
import Enum.Gender;
import Model.AppointmentData;
import Model.Data;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author WINDOWS 10
 */
public class DoctorMainFormController implements Initializable {

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
	private TableView<AppointmentData> dashboad_tableView;

	@FXML
	private TableColumn<AppointmentData, String> dashboad_col_appointmentID;

	@FXML
	private TableColumn<AppointmentData, String> dashboad_col_name;

	@FXML
	private TableColumn<AppointmentData, String> dashboad_col_description;

	@FXML
	private TableColumn<AppointmentData, String> dashboad_col_appointmentDate;

	@FXML
	private TableColumn<AppointmentData, String> dashboad_col_status;

	@FXML
	private AnchorPane patients_form;

	@FXML
	private TextField patients_patientID;

	@FXML
	private TextField patients_patientName;

	@FXML
	private TextField patients_mobileNumber;

	@FXML
	private TextField patients_password;

	@FXML
	private TextArea patients_address;
	@FXML
	private TextField patients_email;
	@FXML
	private TextField patients_height;
	@FXML
	private TextField patients_weight;
	@FXML
	private ComboBox<String> patients_appointmentID;

	@FXML
	private Button patients_confirmBtn;

	@FXML
	private Label patients_PA_patientID;

	@FXML
	private Label patients_PA_password;

	@FXML
	private Label patients_PA_dateCreated;

	@FXML
	private Label patients_PI_patientName;

	@FXML
	private Label patients_PI_gender;

	@FXML
	private Label patients_PI_mobileNumber;

	@FXML
	private Label patients_PI_address;

	@FXML
	private Button patients_PI_addBtn;

	@FXML
	private Button patients_PI_recordBtn;

	@FXML
	private AnchorPane appointments_form;

	@FXML
	private TableView<AppointmentData> appointments_tableView;

	@FXML
	private TableColumn<AppointmentData, String> appointments_col_appointmentID;

	@FXML
	private TableColumn<AppointmentData, String> appointments_col_name;

	@FXML
	private TableColumn<AppointmentData, String> appointments_col_gender;

	@FXML
	private TableColumn<AppointmentData, String> appointments_col_contactNumber;

	@FXML
	private TableColumn<AppointmentData, String> appointments_col_description;

	@FXML
	private TableColumn<AppointmentData, String> appointments_col_date;

	@FXML
	private TableColumn<AppointmentData, String> appointments_col_dateModify;

	@FXML
	private TableColumn<AppointmentData, String> appointments_col_dateDelete;

	@FXML
	private TableColumn<AppointmentData, String> appointments_col_status;

	@FXML
	private TableColumn<AppointmentData, String> appointments_col_action;

	@FXML
	private TextField appointment_appointmentID;

	@FXML
	private TextField appointment_name;

	@FXML
	private ComboBox<String> appointment_gender;

	@FXML
	private TextField appointment_mobileNumber;

	@FXML
	private TextArea appointment_address;

	@FXML
	private ComboBox<String> appointment_status;

	@FXML
	private DatePicker appointment_schedule;

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
	private Label name_doctor, username_doctor, email_doctor, phone_doctor, gender_doctor, specialization_doctor,createdDate_doctor;
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

	public void setUsername(String username) {
		this.username = username;
	}

	public void dashbboardDisplayIP() {

	}

	public void dashbboardDisplayTP() {

	}

	public void dashbboardDisplayAP() {

	}

	public void dashbboardDisplayTA() {

	}

	public ObservableList<AppointmentData> dashboardAppointmentTableView() {
		return null;

	}

	private ObservableList<AppointmentData> dashboardGetData;

	public void dashboardDisplayData() {

	}

	public void dashboardNOP() {

	}

	public void dashboardNOA() {

	}

	public void patientConfirmBtn() {

		if (patients_patientID.getText().isEmpty() || patients_patientName.getText().isEmpty()
				|| patients_gender.getSelectionModel().getSelectedItem() == null
				|| patients_mobileNumber.getText().isEmpty() || patients_password.getText().isEmpty()
				|| patients_address.getText().isEmpty()) {
			alert.errorMessage("Please fill all blank fields");
		} else {
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());

			patients_PA_patientID.setText(patients_patientID.getText());
			patients_PA_password.setText(patients_password.getText());
			patients_PA_dateCreated.setText(String.valueOf(sqlDate));

			patients_PI_patientName.setText(patients_patientName.getText());
			patients_PI_gender.setText(patients_gender.getSelectionModel().getSelectedItem());
			patients_PI_mobileNumber.setText(patients_mobileNumber.getText());
			patients_PI_address.setText(patients_address.getText());
		}

	}

	public void patientAddBtn() {

		if (patients_PA_patientID.getText().isEmpty() || patients_PA_password.getText().isEmpty()
				|| patients_PA_dateCreated.getText().isEmpty() || patients_PI_patientName.getText().isEmpty()
				|| patients_PI_gender.getText().isEmpty() || patients_PI_mobileNumber.getText().isEmpty()
				|| patients_PI_address.getText().isEmpty()) {
			alert.errorMessage("Something wenr wrong");
		} else {

			Database.connectDB();

		}
	}

	public void patientRecordBtn() {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("RecordPageForm.fxml"));
			Stage stage = new Stage();

			stage.setTitle("Hospital Management System | Record of Patients");
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void patientClearFields() {
		patients_patientID.clear();
		patients_patientName.clear();
		patients_gender.getSelectionModel().clearSelection();
		patients_mobileNumber.clear();
		patients_password.clear();
		patients_address.clear();

		patients_PA_patientID.setText("");
		patients_PA_password.setText("");
		patients_PA_dateCreated.setText("");

		patients_PI_patientName.setText("");
		patients_PI_gender.setText("");
		patients_PI_mobileNumber.setText("");
		patients_PI_address.setText("");
	}

	private void patientGenderList() {

	}

	public void appointmentInsertBtn() {

	}

	public void appointmentUpdateBtn() {

	}

	public void appointmentDeleteBtn() {

		if (appointment_appointmentID.getText().isEmpty()) {
			alert.errorMessage("Please select the item first");
		} else {

			String updateData = "UPDATE appointment SET date_delete = ? WHERE appointment_id = '"
					+ appointment_appointmentID.getText() + "'";

			connect = Database.connectDB();

			try {
				java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());

				if (alert.confirmationMessage("Are you sure you want to DELETE Appointment ID: "
						+ appointment_appointmentID.getText() + "?")) {
					prepare = connect.prepareStatement(updateData);

					prepare.setString(1, String.valueOf(sqlDate));
					prepare.executeUpdate();

					appointmentShowData();
					appointmentAppointmentID();
					appointmentClearBtn();

					alert.successMessage("Successully Updated!");
				} else {
					alert.errorMessage("Cancelled.");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void appointmentClearBtn() {

	}

	private Integer appointmentID;

	public void appointmentGetAppointmentID() {
		String sql = "SELECT MAX(appointment_id) FROM appointment";
		connect = Database.connectDB();

		int tempAppID = 0;
		try {
			prepare = connect.prepareStatement(sql);
			result = prepare.executeQuery();
			if (result.next()) {
				tempAppID = result.getInt("MAX(appointment_id)");
			}
			if (tempAppID == 0) {
				tempAppID += 1;
			} else {
				tempAppID += 1;
			}
			appointmentID = tempAppID;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void appointmentAppointmentID() {
		appointmentGetAppointmentID();

		appointment_appointmentID.setText("" + appointmentID);
		appointment_appointmentID.setDisable(true);

	}

	public void appointmentGenderList() {

	}

	public void appointmentStatusList() {

	}

	public ObservableList<AppointmentData> appointmentGetData() {
		ObservableList<AppointmentData> listData = FXCollections.observableArrayList();

		

		return listData;
	}

	public ObservableList<AppointmentData> appoinmentListData;

	public void appointmentShowData() {

	}

	public void appointmentSelect() {

		AppointmentData appData = appointments_tableView.getSelectionModel().getSelectedItem();
		int num = appointments_tableView.getSelectionModel().getSelectedIndex();

		if ((num - 1) < -1) {
			return;
		}

	}
	/* =====================LOAD PROFILE======================================== */
	public void loadDoctorProfile() {
		String selectData = "SELECT ua.name, ua.username, ua.email, ua.gender, ua.created_at, r.phone, r.address, s.name AS ServiceName "
				+ "FROM user_account ua " + "JOIN doctor d ON ua.id = d.doctor_id JOIN SERVICE s ON s.Id=d.Service_id " + "WHERE ua.username = ?";

		connect = Database.connectDB();

		try {
			prepare = connect.prepareStatement(selectData);
			prepare.setString(1, username);
			result = prepare.executeQuery();

			if (!result.next() || result.getInt(1) <= 0) {
				alert.errorMessage("Username does not match data.");
				return;
			}
			String name = result.getString("name");
			String username = result.getString("username");
			String email = result.getString("email");
			String phone = result.getString("phone");
			String address = result.getString("address");
			String gender = result.getString("gender");
			String createdAt = result.getString("created_at");
			String specialization = result.getString("ServiceName");


			// Gán cho các Label
			name_doctor.setText(name != null ? name : "UNKNOWN");
			username_doctor.setText(username != null ? username : "");
			gender_doctor.setText(gender != null ? gender : "");
			phone_doctor.setText(phone != null ? phone : "");
			email_doctor.setText(email != null ? email : "");
			createdDate_doctor.setText(createdAt != null ? FormatterUtils.formatTimestamp(createdAt) : "");
			specialization_doctor.setText(specialization!=null?specialization:"");
			top_username.setText(name != null ? name : "UNKNOWN");
		
			profile_name.setText(name != null ? name : "");
			profile_username.setText(username != null ? username : "");
			profile_mobileNumber.setText(phone != null ? phone : "");
			profile_gender.setValue(gender != null ? gender : "");
			profile_address.setText(address != null ? address : "");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void profileDisplayImages() {

		String sql = "SELECT * FROM user_acount WHERE username = " + username;
		connect = Database.connectDB();

		try {
			prepare = connect.prepareStatement(sql);
			result = prepare.executeQuery();

			if (result.next()) {
				// Lấy ảnh nhị phân từ DB
				InputStream inputStream = result.getBinaryStream("image");

				if (inputStream != null) {
					// Chuyển InputStream thành Image
					Image img = new Image(inputStream, 137, 95, false, true);
					profile_circleImage.setFill(new ImagePattern(img));

					// Thêm logic nếu cần thêm hình ảnh khác
					img = new Image(inputStream, 1012, 22, false, true);
					top_profile.setFill(new ImagePattern(img));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/* =====================EDIT PROFILE======================================== */
	public void profileUpdateBtn() {

		String name = profile_name.getText();
		String phone = profile_mobileNumber.getText();
		String usernameEdit = profile_username.getText();
		String address = profile_address.getText();
		String email = email_doctor.getText();
		String gender = (String) profile_gender.getSelectionModel().getSelectedItem();

		if (usernameEdit.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
			alert.errorMessage("Please fill in all the fields.");
			return;
		}
		else if (gender == null || gender.isEmpty()) {
			alert.errorMessage("Please select a gender.");
			return;
		}
		String checkUsernameSQL = "SELECT * FROM user_account WHERE username = ?";
		String updateUserSQL = "UPDATE user_account SET name = ?, username = ?, gender = ? WHERE email = ?";
		String updateDoctorSQL = "UPDATE doctor SET phone = ?, address = ?  WHERE doctor_id = (SELECT id FROM user_account WHERE email = ?)";

		connect = Database.connectDB();

		try {
			// Kiểm tra username đã tồn tại (trừ chính mình)
			if (username != usernameEdit) {
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

			// Cập nhật doctor
			prepare = connect.prepareStatement(updateDoctorSQL);
			prepare.setString(1, phone);
			prepare.setString(2, address);
			prepare.setString(3, email);   
			int rowsReceptionistUpdated = prepare.executeUpdate();

			if (rowsUserUpdated > 0 || rowsReceptionistUpdated > 0) {
				alert.successMessage("Profile updated successfully.");
				loadDoctorProfile();
			} else {
				alert.errorMessage("No user found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			alert.errorMessage("Error updating profile: " + e.getMessage());
		}
	}
	public void profileInsertImage() {

		FileChooser open = new FileChooser();
		open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*jpg", "*jpeg", "*png"));

		File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());

		if (file != null) {
			Data.path = file.getAbsolutePath();

			// Hiển thị ảnh lên UI
			image = new Image(file.toURI().toString(), 137, 95, false, true);
			profile_circleImage.setFill(new ImagePattern(image));

			// Lưu ảnh vào DB
			try {
				connect = Database.connectDB();
				String updateAvatarSQL = "UPDATE user_account SET avatar = ? WHERE email = ?";

				FileInputStream input = new FileInputStream(file);
				prepare = connect.prepareStatement(updateAvatarSQL);
				prepare.setBinaryStream(1, input, (int) file.length());
				prepare.setString(2, email_doctor.getText());

				int rows = prepare.executeUpdate();
				if (rows > 0) {
					alert.successMessage("Avatar updated successfully.");
				} else {
					alert.errorMessage("Failed to update avatar.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				alert.errorMessage("Error uploading avatar: " + e.getMessage());
			}
		}

	}
	

	public void profileGenderList() {

	}

	public void profileSpecializedList() {

	}

	public void profileStatusList() {

	}

	public void displayAdminIDNumberName() {

	}
	/* =====================FORMAT AND INTIALIZE======================================== */
	public void switchForm(ActionEvent event) {
		if (event.getSource() == dashboard_btn) {
			dashboard_form.setVisible(true);
			patients_form.setVisible(false);
			appointments_form.setVisible(false);
			profile_form.setVisible(false);
		} else if (event.getSource() == patients_btn) {
			dashboard_form.setVisible(false);
			patients_form.setVisible(true);
			appointments_form.setVisible(false);
			profile_form.setVisible(false);
		} else if (event.getSource() == appointments_btn) {
			dashboard_form.setVisible(false);
			patients_form.setVisible(false);
			appointments_form.setVisible(true);
			profile_form.setVisible(false);
		} else if (event.getSource() == profile_btn) {
			dashboard_form.setVisible(false);
			patients_form.setVisible(false);
			appointments_form.setVisible(false);
			profile_form.setVisible(true);
		}
	}

	public void logoutBtn() {

		try {
			if (alert.confirmationMessage("Are you sure you want to logout?")) {
				Data.doctor_id = "";
				Data.doctor_name = "";
				Parent root = FXMLLoader.load(getClass().getResource("DoctorPage.fxml"));
				Stage stage = new Stage();

				stage.setScene(new Scene(root));
				stage.show();

				logout_btn.getScene().getWindow().hide();

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

		profile_gender.setItems(FXCollections
				.observableArrayList(Arrays.stream(Gender.values()).map(Enum::name).collect(Collectors.toList())));
	}

	public class FormatterUtils {
		public static String formatCurrencyVND(BigDecimal amount) {
			if (amount == null)
				return "0 VNĐ";
			NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
			return formatter.format(amount) + " VNĐ";
		}

		public static String formatTimestamp(String createdAt) {
			try {
		        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
		        return outputFormat.format(inputFormat.parse(createdAt));
		    } catch (Exception e) {
		        return "";
		    }
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		displayAdminIDNumberName();
		loadComboBox();
		runTime();

		dashbboardDisplayIP();
		dashbboardDisplayTP();
		dashbboardDisplayAP();
		dashbboardDisplayTA();
		dashboardDisplayData();
		dashboardNOP();
		dashboardNOA();

		appointmentShowData();
		appointmentGenderList();
		appointmentStatusList();
		appointmentAppointmentID();

		patientGenderList();

		loadDoctorProfile();
		profileDisplayImages();

	}

}