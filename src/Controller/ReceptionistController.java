/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Alert.AlertMessage;
import DAO.Database;
import Model.AppointmentData;
import Model.Data;
import Model.DoctorData;
import Model.PatientData;
import Model.ReceptionistData;
import Model.ReceptionistFullData;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import javafx.scene.layout.StackPane;
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
	
	/*=====================CRUD DRUG========================================*/

	@FXML private TableView<DrugData> drug_tableView;
	@FXML private TableColumn<DrugData, String> drug_col_id;
	@FXML private TableColumn<DrugData, String> drug_col_name;
	@FXML private TableColumn<DrugData, String> drug_col_manufacturer;
	@FXML private TableColumn<DrugData, String> drug_col_expiry;
	@FXML private TableColumn<DrugData, String> drug_col_unit;
	@FXML private TableColumn<DrugData, String> drug_col_price;
	@FXML private TableColumn<DrugData, String> drug_col_stock;
	@FXML private TableColumn<DrugData, String> drug_col_create;
	@FXML private TableColumn<DrugData, String> drug_col_update;
	@FXML private TableColumn<DrugData, Void> drug_col_action;


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
	private Button profile_btn;

	@FXML
	private AnchorPane home_form;

	@FXML
	private TableView<PatientData> home_patient_tableView;

	@FXML
	private TableColumn<PatientData, String> home_patient_col_description;

	@FXML
	private TableColumn<PatientData, String> home_patient_col_diagnosis;

	@FXML
	private TableColumn<PatientData, String> home_patient_col_treatment;

	@FXML
	private TableColumn<PatientData, String> home_patient_col_dateIn;

	@FXML
	private TableColumn<PatientData, String> home_patient_col_dateDischarge;

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
	private Label appointment_ad_name;

	@FXML
	private Label appointment_ad_mobileNumber;

	@FXML
	private Label appointment_ad_gender;

	@FXML
	private Label appointment_ad_address;

	@FXML
	private Label appointment_ad_description;

	@FXML
	private Label appointment_ad_doctorName;

	@FXML
	private Label appointment_ad_specialization;

	@FXML
	private Label appointment_ad_schedule;

	@FXML
	private Button appointment_d_confirmBtn;

	@FXML
	private TextArea appointment_d_description;

	@FXML
	private ComboBox<String> appointment_d_doctor;

	@FXML
	private DatePicker appointment_d_schedule;

	@FXML
	private Button appointment_d_clearBtn;
	
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
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		runTime();
		ObservableList<String> genderOptions = FXCollections.observableArrayList("Male", "Female");
		gender_cb.setItems(genderOptions);
		loadReceptionistProfile();
		
		showForm("dashboard");

		//homePatientDisplayData();
		//homeAppointmentDisplayData();
		//homeDoctorInfoDisplay();

		// doctorShowCard();

		//appointmentAppointmentInfoDisplay();
		//appointmentDoctor();

	}

	private void loadReceptionistProfile() {
		String checkUserSQL = "SELECT ua.name, ua.username, ua.email, ua.gender, ua.created_at, r.phone, r.address "
				+ "FROM user_account ua " + "JOIN receptionist r ON ua.id = r.receptionist_id "
				+ "WHERE ua.username = ?";

		Connection connect = Database.connectDB();

		try {
			PreparedStatement prepare = connect.prepareStatement(checkUserSQL);
			prepare.setString(1, username);

			ResultSet result = prepare.executeQuery();

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

			// Gán cho các Label
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

			profileDisplayImages();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void profileUpdateBtn() {
		String name = txt_name_recept.getText();
		String phone = txt_phone_recept.getText();
		String username = txt_username_recept.getText();
		String address = txt_address_recept.getText();
		String email = email_recept.getText();
		String gender = (String) gender_cb.getSelectionModel().getSelectedItem();

		if (username.isEmpty() || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
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
			prepare = connect.prepareStatement(checkUsernameSQL);
			prepare.setString(1, username);
			result = prepare.executeQuery();

			if (result.next()) {
				alert.errorMessage("Username \"" + username + "\" already exists!");
				return;
			}

			// Cập nhật user_account
			prepare = connect.prepareStatement(updateUserSQL);
			prepare.setString(1, name);
			prepare.setString(2, username);
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
		profileDisplayImages();
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
					profile_circle.setFill(new ImagePattern(img));

					// Thêm logic nếu cần thêm hình ảnh khác
					img = new Image(inputStream, 1012, 22, false, true);
					top_profile.setFill(new ImagePattern(img));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//
	@FXML
	private void profileImportBtn(ActionEvent event) {
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
//
//	// public ObservableList<PatientData> homePatientGetData() {
//	//
//	// ObservableList<PatientData> listData = FXCollections.observableArrayList();
//	//
//	// String sql = "SELECT * FROM patient WHERE date_delete IS NULL AND patient_id
//	// = " + Data.patient_id;
//	// connect = Database.connectDB();
//	//
//	// try {
//	// prepare = connect.prepareStatement(sql);
//	// result = prepare.executeQuery();
//	//
//	// PatientData pData;
//	// while (result.next()) {
//	//// PatientsData(Integer id, Integer patientID, String description
//	//// , String diagnosis, String treatment, Date date)
//	// pData = new PatientData(result.getInt("id"),
//	// result.getInt("patient_id"),
//	// result.getString("description"),
//	// result.getString("diagnosis"),
//	// result.getString("treatment"), result.getDate("date"));
//	//
//	// listData.add(pData);
//	// }
//	// } catch (Exception e) {
//	// e.printStackTrace();
//	// }
//	// return listData;
//	// }
//
//	public ObservableList<PatientData> homePatientListData;
//
//	// public void homePatientDisplayData() {
//	// homePatientListData = homePatientGetData();
//	//
//	// home_patient_col_description.setCellValueFactory(new
//	// PropertyValueFactory<>("description"));
//	// home_patient_col_diagnosis.setCellValueFactory(new
//	// PropertyValueFactory<>("diagnosis"));
//	// home_patient_col_treatment.setCellValueFactory(new
//	// PropertyValueFactory<>("treatment"));
//	// home_patient_col_dateIn.setCellValueFactory(new
//	// PropertyValueFactory<>("date"));
//	//
//	// home_patient_tableView.setItems(homePatientListData);
//	// }
//
//	public ObservableList<AppointmentData> homeAppointmentGetData() {
//
//		ObservableList<AppointmentData> listData = FXCollections.observableArrayList();
//
//		String sql = "SELECT * FROM appointment WHERE date_delete IS NULL AND patient_id = " + Data.patient_id;
//
//		connect = Database.connectDB();
//
//		try {
//			prepare = connect.prepareStatement(sql);
//			result = prepare.executeQuery();
//
//			AppointmentData aData;
//			while (result.next()) {
//				// AppointmentData(Integer appointmentID, String description,
//				// String diagnosis, String treatment, String doctorID, Date schedule)
//				aData = new AppointmentData(result.getInt("appointment_id"), result.getString("description"),
//						result.getString("diagnosis"), result.getString("treatment"), result.getString("doctor"),
//						result.getDate("schedule"));
//
//				listData.add(aData);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return listData;
//	}
//
//	public ObservableList<AppointmentData> homeAppointmentListData;
//
//	public void homeAppointmentDisplayData() {
//		homeAppointmentListData = homeAppointmentGetData();
//
//		home_appointment_col_appointmenID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
//		home_appointment_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
//		home_appointment_col_diagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
//		home_appointment_col_treatment.setCellValueFactory(new PropertyValueFactory<>("treatment"));
//		home_appointment_col_doctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
//		home_appointment_col_schedule.setCellValueFactory(new PropertyValueFactory<>("schedule"));
//
//		home_appointment_tableView.setItems(homeAppointmentListData);
//	}
//
//	public void homeDoctorInfoDisplay() {
//
//		String sql = "SELECT * FROM patient WHERE patient_id = " + Data.patient_id;
//
//		connect = Database.connectDB();
//
//		String tempDoctorID = "";
//		try {
//			prepare = connect.prepareStatement(sql);
//			result = prepare.executeQuery();
//
//			if (result.next()) {
//				tempDoctorID = result.getString("doctor");
//			}
//
//			String checkDoctor = "SELECT * FROM doctor WHERE doctor_id = '" + tempDoctorID + "'";
//
//			statement = connect.createStatement();
//			result = statement.executeQuery(checkDoctor);
//
//			if (result.next()) {
//				home_doctor_name.setText(result.getString("full_name"));
//				home_doctor_specialization.setText(result.getString("specialized"));
//				home_doctor_email.setText(result.getString("email"));
//				home_doctor_mobileNumber.setText(result.getString("mobile_number"));
//
//				String path = result.getString("image");
//
//				if (path != null) {
//					path = path.replace("\\", "\\\\");
//
//					image = new Image("File:" + path, 138, 82, false, true);
//					home_doctor_circle.setFill(new ImagePattern(image));
//				}
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	// private ObservableList<DoctorData> doctorList =
//	// FXCollections.observableArrayList();
//
//	// public ObservableList<DoctorData> doctorGetData() {
//	//
//	// String sql = "SELECT * FROM doctor WHERE status = 'Active'";
//	//
//	// connect = Database.connectDB();
//	//
//	// ObservableList<DoctorData> listData = FXCollections.observableArrayList();
//	//
//	// try {
//	// prepare = connect.prepareStatement(sql);
//	// result = prepare.executeQuery();
//	//
//	// DoctorData dData;
//	//
//	// while (result.next()) {
//	//// DoctorData(Integer id, String doctorID, String fullName, String
//	// specialized, String email)
//	// dData = new DoctorData(result.getInt("id"),
//	// result.getString("doctor_id"),
//	// result.getString("full_name"),
//	// result.getString("specialized"),
//	// result.getString("email"),
//	// result.getString("image"));
//	//
//	// listData.add(dData);
//	// }
//	//
//	// } catch (Exception e) {
//	// e.printStackTrace();
//	// }
//	// return listData;
//	// }
//
//	// public void doctorShowCard() {
//	// doctorList.clear();
//	// doctorList.addAll(doctorGetData());
//	//
//	// doctors_gridPane.getChildren().clear();
//	// doctors_gridPane.getColumnConstraints().clear();
//	// doctors_gridPane.getRowConstraints().clear();
//	//
//	// int row = 0, column = 0;
//	//
//	// for (int q = 0; q < doctorList.size(); q++) {
//	// try {
//	// FXMLLoader loader = new FXMLLoader();
//	// loader.setLocation(getClass().getResource("DoctorCard.fxml"));
//	// StackPane stack = loader.load();
//	//
//	// DoctorCardController dController = loader.getController();
//	// dController.setData(doctorList.get(q));
//	//
//	// if (column == 3) {
//	// column = 0;
//	// row++;
//	// }
//	//
//	// doctors_gridPane.add(stack, column++, row);
//	//
//	// GridPane.setMargin(stack, new Insets(15));
//	//
//	// } catch (Exception e) {
//	// e.printStackTrace();
//	// }
//	// }
//	//
//	// }
//
//	public void appointmentAppointmentInfoDisplay() {
//
//		String sql = "SELECT * FROM patient WHERE patient_id = " + Data.patient_id;
//
//		connect = Database.connectDB();
//
//		try {
//			prepare = connect.prepareStatement(sql);
//			result = prepare.executeQuery();
//
//			if (result.next()) {
//				appointment_ad_name.setText(result.getString("full_name"));
//				appointment_ad_mobileNumber.setText(result.getString("mobile_number"));
//				appointment_ad_gender.setText(result.getString("gender"));
//				appointment_ad_address.setText(result.getString("address"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public void appointmentConfirmBtn() {
//
//		if (appointment_d_description.getText().isEmpty() || appointment_d_schedule.getValue() == null
//				|| appointment_d_doctor.getSelectionModel().isEmpty()) {
//			alert.errorMessage("Please fill all blank fields");
//		} else {
//
//			appointment_ad_description.setText(appointment_d_description.getText());
//			appointment_ad_doctorName.setText(appointment_d_doctor.getSelectionModel().getSelectedItem());
//
//			String sql = "SELECT * FROM doctor WHERE doctor_id = '"
//					+ appointment_d_doctor.getSelectionModel().getSelectedItem() + "'";
//
//			connect = Database.connectDB();
//			String tempSpecialized = "";
//			try {
//				prepare = connect.prepareStatement(sql);
//				result = prepare.executeQuery();
//
//				if (result.next()) {
//					tempSpecialized = result.getString("specialized");
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			appointment_ad_specialization.setText(tempSpecialized);
//			appointment_ad_schedule.setText(String.valueOf(appointment_d_schedule.getValue()));
//		}
//
//	}
//
//	public void appointmentDoctor() {
//		String sql = "SELECT * FROM doctor WHERE delete_date IS NULL";
//		connect = Database.connectDB();
//
//		try {
//			prepare = connect.prepareStatement(sql);
//			result = prepare.executeQuery();
//
//			ObservableList listData = FXCollections.observableArrayList();
//			while (result.next()) {
//				listData.add(result.getString("doctor_id"));
//			}
//
//			appointment_d_doctor.setItems(listData);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void appointmentClearBtn() {
//		appointment_d_doctor.getSelectionModel().clearSelection();
//		appointment_d_description.clear();
//		appointment_d_schedule.setValue(null);
//
//		appointment_ad_description.setText("");
//		appointment_ad_doctorName.setText("");
//		appointment_ad_specialization.setText("");
//		appointment_ad_schedule.setText("");
//	}
//
//	public void appointmentBookBtn() {
//		connect = Database.connectDB();
//
//		if (appointment_ad_description.getText().isEmpty() || appointment_d_doctor.getSelectionModel().isEmpty()
//				|| appointment_ad_specialization.getText().isEmpty() || appointment_ad_schedule.getText().isEmpty()) {
//			alert.errorMessage("Invalid");
//		} else {
//			String selectData = "SELECT MAX(appointment_id) FROM appointment";
//
//			int tempAppID = 0;
//
//			try {
//				statement = connect.createStatement();
//				result = statement.executeQuery(selectData);
//
//				if (result.next()) {
//					tempAppID = result.getInt("MAX(appointment_id)") + 1;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			String insertData = "INSERT INTO appointment (appointment_id, patient_id, name, gender"
//					+ ", description, mobile_number, address, date" + ", doctor, specialized, schedule, status) "
//					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
//			Date date = new Date();
//			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
//			try {
//				if (alert.confirmationMessage("Are you sure you want to book?")) {
//					prepare = connect.prepareStatement(insertData);
//					prepare.setString(1, String.valueOf(tempAppID));
//					prepare.setString(2, String.valueOf(Data.patient_id));
//					prepare.setString(3, appointment_ad_name.getText());
//					prepare.setString(4, appointment_ad_gender.getText());
//					prepare.setString(5, appointment_ad_description.getText());
//					prepare.setString(6, appointment_ad_mobileNumber.getText());
//					prepare.setString(7, appointment_ad_address.getText());
//					prepare.setString(8, String.valueOf(appointment_d_schedule.getValue()));
//					prepare.setString(9, appointment_d_doctor.getSelectionModel().getSelectedItem());
//					prepare.setString(10, appointment_ad_specialization.getText());
//					prepare.setString(11, appointment_ad_schedule.getText());
//					prepare.setString(12, "Active");
//
//					prepare.executeUpdate();
//
//					alert.successMessage("Successful !");
//
//					appointmentClearBtn();
//				} else {
//					alert.errorMessage("Cancelled.");
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
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
		} else if (event.getSource() == profile_btn) {
			showForm("profile");
		} 
	}

	private void showForm(String formName) {
		home_form.setVisible(false);
		drugs_form.setVisible(false);
		patients_form.setVisible(false);
		appointments_form.setVisible(false);
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
				 //loadPatientTable(); 
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
	
	// =======================CRUD Drug==================================
	
	private void loadDrugTable() {
	    ObservableList<DrugData> list = FXCollections.observableArrayList();

	    try {
	        Connection conn = Database.connectDB();
	        String sql = "SELECT Id, Name, Manufacturer, Expiry_date, Unit, Price, Stock, Create_date, Update_date\r\n"
	        		+ "FROM DRUG ";

	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	        	list.add(new DrugData(
	        		    rs.getString("Id"),
	        		    rs.getString("Name"),
	        		    rs.getString("Manufacturer"),
	        		    rs.getString("Unit"),
	        		    rs.getBigDecimal("Price"),
	        		    rs.getInt("Stock"),
	        		    rs.getDate("Expiry_date").toLocalDate(),
	        		    rs.getTimestamp("Create_date"),
	        		    rs.getTimestamp("Update_date")
	        		));
	        }

	        // Cột dữ liệu
	        drug_col_id.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDrugId()));
	        drug_col_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
	        drug_col_manufacturer.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getManufacturer()));
	        drug_col_expiry.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getExpiryDate().toString()));
	        drug_col_unit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnit()));
	        drug_col_price.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrice().toString()));
	        drug_col_stock.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStock())));
	        drug_col_create.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreateDate().toString()));
	        drug_col_update.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUpdateDate().toString()));
	      
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

	        drug_tableView.setItems(list);
	        conn.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
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

	
}
