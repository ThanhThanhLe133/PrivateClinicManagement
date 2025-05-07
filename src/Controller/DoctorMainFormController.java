  package Controller;
  
  import java.io.File;
  import java.net.URL;
  import java.nio.file.Files;
  import java.nio.file.Path;
  import java.nio.file.Paths;
  import java.nio.file.StandardCopyOption;
  import java.sql.Connection;
  import java.sql.PreparedStatement;
  import java.sql.ResultSet;
  import java.sql.Statement;
  import java.text.SimpleDateFormat;
  import java.util.ArrayList;
  import java.util.Date;
  import java.util.List;
  import java.util.ResourceBundle;
  
  import Alert.AlertMessage;
  import DAO.Database;
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
      @FXML private TextField patients_email;
@FXML private TextField patients_height;
@FXML private TextField patients_weight;
@FXML private ComboBox<String> patients_appointmentID;

  
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
      private Label profile_label_doctorID;
  
      @FXML
      private Label profile_label_name;
  
      @FXML
      private Label profile_label_email;
  
      @FXML
      private Label profile_label_dateCreated;
  
      @FXML
      private TextField profile_doctorID;
  
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
      private ComboBox<String> profile_specialized;
  
      @FXML
      private ComboBox<String> profile_status;
  
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
  
          if (patients_patientID.getText().isEmpty()
                  || patients_patientName.getText().isEmpty()
                  || patients_gender.getSelectionModel().getSelectedItem() == null
                  || patients_mobileNumber.getText().isEmpty()
                  || patients_password.getText().isEmpty()
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
  
          if (patients_PA_patientID.getText().isEmpty()
                  || patients_PA_password.getText().isEmpty()
                  || patients_PA_dateCreated.getText().isEmpty()
                  || patients_PI_patientName.getText().isEmpty()
                  || patients_PI_gender.getText().isEmpty()
                  || patients_PI_mobileNumber.getText().isEmpty()
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
    
        String sql = "SELECT * FROM appointment WHERE date_delete IS NULL and doctor = ?";
    
        try (Connection connect = Database.connectDB();
             PreparedStatement prepare = connect.prepareStatement(sql)) {
             
            prepare.setString(1, Data.doctor_id);
            try (ResultSet result = prepare.executeQuery()) {
    
                while (result.next()) {
                    AppointmentData appData = new AppointmentData(
                            result.getInt("appointment_id"),
                            result.getString("name"),
                            result.getString("gender"),
                            result.getLong("mobile_number"),
                            result.getString("description"),
                            result.getString("diagnosis"),
                            result.getString("treatment"),
                            result.getString("address"),
                            result.getDate("date"),
                            result.getDate("date_modify"),
                            result.getDate("date_delete"),
                            result.getString("status"),
                            result.getDate("schedule"));
    
                    listData.add(appData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
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
  
      public void profileUpdateBtn() {
  
          connect = Database.connectDB();
  
          if (profile_doctorID.getText().isEmpty()
                  || profile_name.getText().isEmpty()
                  || profile_email.getText().isEmpty()
                  || profile_gender.getSelectionModel().getSelectedItem() == null
                  || profile_mobileNumber.getText().isEmpty()
                  || profile_address.getText().isEmpty()
                  || profile_specialized.getSelectionModel().getSelectedItem() == null
                  || profile_status.getSelectionModel().getSelectedItem() == null) {
              alert.errorMessage("Please fill all blank fields");
          } else {
              if (Data.path == null || "".equals(Data.path)) {
                  String updateData = "UPDATE doctor SET full_name = ?, email = ?"
                          + ", gender = ?, mobile_number = ?, address = ?, specialized = ?, status = ?, modify_date = ?"
                          + " WHERE doctor_id = '"
                          + Data.doctor_id + "'";
                  try {
                      Date date = new Date();
                      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                      prepare = connect.prepareStatement(updateData);
                      prepare.setString(1, profile_name.getText());
                      prepare.setString(2, profile_email.getText());
                      prepare.setString(3, profile_gender.getSelectionModel().getSelectedItem());
                      prepare.setString(4, profile_mobileNumber.getText());
                      prepare.setString(5, profile_address.getText());
                      prepare.setString(6, profile_specialized.getSelectionModel().getSelectedItem());
                      prepare.setString(7, profile_status.getSelectionModel().getSelectedItem());
                      prepare.setString(8, String.valueOf(sqlDate));
  
                      prepare.executeUpdate();
  
                      alert.successMessage("Updated Successfully!");
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              } else {
                  String updateData = "UPDATE doctor SET full_name = ?, email = ?"
                          + ", gender = ?, mobile_number = ?, address = ?, image = ?, specialized = ?, status = ?, modify_date = ?"
                          + " WHERE doctor_id = '"
                          + Data.doctor_id + "'";
                  try {
                      Date date = new Date();
                      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                      prepare = connect.prepareStatement(updateData);
                      prepare.setString(1, profile_name.getText());
                      prepare.setString(2, profile_email.getText());
                      prepare.setString(3, profile_gender.getSelectionModel().getSelectedItem());
                      prepare.setString(4, profile_mobileNumber.getText());
                      prepare.setString(5, profile_address.getText());
                      String path = Data.path;
                      path = path.replace("\\", "\\\\");
                      Path transfer = Paths.get(path);
  
                     
                      Path copy = Paths.get("C:\\Users\\WINDOWS 10\\Documents\\NetBeansProjects\\HospitalManagementSystem\\src\\Directory\\"
                              + Data.doctor_id + ".jpg");
  
                      try {
                            
                          Files.copy(transfer, copy, StandardCopyOption.REPLACE_EXISTING);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                      prepare.setString(6, copy.toAbsolutePath().toString());
                      prepare.setString(7, profile_specialized.getSelectionModel().getSelectedItem());
                      prepare.setString(8, profile_status.getSelectionModel().getSelectedItem());
                      prepare.setString(9, String.valueOf(sqlDate));
  
                      prepare.executeUpdate();
  
                      alert.successMessage("Updated Successfully!");
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
          }
      }
  
      public void profileChangeProfile() {
  
          FileChooser open = new FileChooser();
          open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*png", "*jpg", "*jpeg"));
  
          File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());
  
          if (file != null) {
              Data.path = file.getAbsolutePath();
  
              image = new Image(file.toURI().toString(), 128, 103, false, true);
              profile_circleImage.setFill(new ImagePattern(image));
          }
  
      }
  
      public void profileLabels() {
          String selectData = "SELECT * FROM doctor WHERE doctor_id = '"
                  + Data.doctor_id + "'";
          connect = Database.connectDB();
  
          try {
              prepare = connect.prepareStatement(selectData);
              result = prepare.executeQuery();
  
              if (result.next()) {
                  profile_label_doctorID.setText(result.getString("doctor_id"));
                  profile_label_name.setText(result.getString("full_name"));
                  profile_label_email.setText(result.getString("email"));
                  profile_label_dateCreated.setText(result.getString("date"));
              }
  
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  
      public void profileFields() {
          String selectData = "SELECT * FROM doctor WHERE doctor_id = '"
                  + Data.doctor_id + "'";
  
          connect = Database.connectDB();
          try {
              prepare = connect.prepareStatement(selectData);
              result = prepare.executeQuery();
  
              if (result.next()) {
                  profile_doctorID.setText(result.getString("doctor_id"));
                  profile_name.setText(result.getString("full_name"));
                  profile_email.setText(result.getString("email"));
                  profile_gender.getSelectionModel().select(result.getString("gender"));
                  profile_mobileNumber.setText(result.getString("mobile_number"));
                  profile_address.setText(result.getString("address"));
                  profile_specialized.getSelectionModel().select(result.getString("specialized"));
                  profile_status.getSelectionModel().select(result.getString("status"));
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
  
      }
  
      public void profileDisplayImages() {
  
          String selectData = "SELECT * FROM doctor WHERE doctor_id = '"
                  + Data.doctor_id + "'";
          String temp_path1 = "";
          String temp_path2 = "";
          connect = Database.connectDB();
  
          try {
              prepare = connect.prepareStatement(selectData);
              result = prepare.executeQuery();
  
              if (result.next()) {
                  temp_path1 = "File:" + result.getString("image");
                  temp_path2 = "File:" + result.getString("image");
  
                  if (result.getString("image") != null) {
                      image = new Image(temp_path1, 1012, 22, false, true);
                      top_profile.setFill(new ImagePattern(image));
  
                      image = new Image(temp_path2, 128, 103, false, true);
                      profile_circleImage.setFill(new ImagePattern(image));
                  }
  
              }
  
          } catch (Exception e) {
              e.printStackTrace();
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
  
      @Override
      public void initialize(URL location, ResourceBundle resources) {
          displayAdminIDNumberName();
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
  

          profileLabels();
          profileFields();     
          profileGenderList();
          profileSpecializedList();
          profileStatusList();
          profileDisplayImages();    
  
      }
  
  }