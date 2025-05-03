package Controller;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import DAO.Database;
import Model.DoctorData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.HBox;


public class AdminMainFormController {
	
	@FXML
	private AnchorPane main_form;


    // Top panel
    @FXML
    private Label top_username, current_form, date_time;

    @FXML
    private Circle top_profile;

    @FXML
    private Button logout_btn;

    // Left panel
    @FXML
    private Label nav_adminID, nav_username;

    @FXML
    private Button dashboard_btn, doctors_btn, receptionist_btn, salary_btn,revenue_btn, profile_btn;

    // Forms
    @FXML
    private AnchorPane dashboard_form, doctors_form, receptionist_form, salary_form, profile_form;
    @FXML
    private AnchorPane revenue_form;

    // Salary Form
    @FXML
    private TableView<TableRow> salary_tableView;
    @FXML
    private TableColumn<TableRow, String> salary_col_ID;
    @FXML
    private TableColumn<TableRow, String> salary_col_name;
    @FXML
    private TableColumn<TableRow, String> salary_col_role;
    @FXML
    private TableColumn<TableRow, String> salary_col_totalsalary;
    @FXML
    private TableColumn<TableRow, String> salary_col_salary;

    @FXML
    private Button salary_paySalaryBtn;
    
    
    @FXML
    private ComboBox<String> filterTypeComboBox;
    @FXML
    private DatePicker filterDatePicker;
    @FXML
    private Label totalRevenueLabel, totalCostLabel, netProfitLabel;
    
    
    /*=====================CRUD DOCTOR========================================*/
    
    @FXML private TableView<DoctorData> doctors_tableView;
    @FXML private TableColumn<DoctorData, String> doctors_col_doctorID;
    @FXML private TableColumn<DoctorData, String> doctors_col_name;
    @FXML private TableColumn<DoctorData, String> doctors_col_gender;
    @FXML private TableColumn<DoctorData, String> doctors_col_contactNumber;
    @FXML private TableColumn<DoctorData, String> doctors_col_email;
    @FXML private TableColumn<DoctorData, String> doctors_col_specialization;
    @FXML private TableColumn<DoctorData, String> doctors_col_address;
    @FXML private TableColumn<DoctorData, String> doctors_col_status;
    @FXML private TableColumn<DoctorData, String> doctors_col_action;

    /*=====================CRUD DOCTOR========================================*/
 


    @FXML
    public void initialize() {
        showForm("dashboard");

        // Nếu salary_tableView tồn tại thì load dữ liệu mẫu
        if (salary_tableView != null) {
            loadSampleSalaryData();
        }
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
            showForm("receptionist");
        } else if (event.getSource() == salary_btn) {
            showForm("salary");
        } else if (event.getSource() == profile_btn) {
            showForm("profile");
        }
        else if (event.getSource() == revenue_btn) {
            showForm("revenue");
        }
       
       
    }

    private void showForm(String formName) {
        dashboard_form.setVisible(false);
        doctors_form.setVisible(false);
        receptionist_form.setVisible(false);
        salary_form.setVisible(false);
        profile_form.setVisible(false);
        revenue_form.setVisible(false);

        switch (formName) {
            case "dashboard":
                dashboard_form.setVisible(true);
                current_form.setText("Dashboard Form");
                break;
            case "doctors":
                doctors_form.setVisible(true);
                current_form.setText("Doctors Form");
                loadDoctorTable();
                break;
            case "receptionist":
                receptionist_form.setVisible(true);
                current_form.setText("Receptionists Form");
                break;
            case "salary":
                salary_form.setVisible(true);
                current_form.setText("Salary Payment Form");
                break;
            
            case "profile":
                profile_form.setVisible(true);
                current_form.setText("Profile Form");
                break;
            case "revenue":
                revenue_form.setVisible(true);
                current_form.setText("Clinic Revenue Form");
                break;

        }
    }

    @FXML
    private void paySalaryAction() {
        TableRow selectedRow = salary_tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            System.out.println("Paying salary to: " + selectedRow.getName() + " (" + selectedRow.getRole() + ")");
        } else {
            System.out.println("Please select an employee to pay salary!");
        }
    }

    private void loadSampleSalaryData() {
        ObservableList<TableRow> data = FXCollections.observableArrayList(
                new TableRow("1", "Dr. John", "Doctor", "5000$", "5000$"),
                new TableRow("2", "Ms. Anna", "Receptionist", "3000$", "3000$")
        );
        salary_tableView.setItems(data);
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

        public String getId() { return id; }
        public String getName() { return name; }
        public String getRole() { return role; }
        public String getTotalsalary() { return totalsalary; }
        public String getSalary() { return salary; }
    }
    
    
    @FXML
    private void paySelectedEmployeeSalary(ActionEvent event) {
        System.out.println("Đã trả lương cho nhân viên được chọn!");
    }
    @FXML
    private void profileInsertImage(ActionEvent event) {
        System.out.println("Import hình ảnh cho profile...");
    }
    @FXML
    private void profileUpdateBtn(ActionEvent event) {
        System.out.println("Cập nhật thông tin hồ sơ nhân viên...");
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
    
    
    @FXML
    private void filterRevenueData(ActionEvent event) {
        // TODO: Viết code xử lý khi nhấn nút lọc revenue ở đây
    }
    
    @FXML
    private void resetRevenueFilter(ActionEvent event) {
        // TODO: Xử lý reset bộ lọc dữ liệu doanh thu ở đây
    }
    @FXML
    private void exportRevenueReport(ActionEvent event) {
        // TODO: Viết code xuất báo cáo doanh thu ở đây
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // =======================CRUD Doctor==================================
    
    
    private void loadDoctorTable() {
        ObservableList<DoctorData> list = FXCollections.observableArrayList();

        try {
            Connection conn = Database.connectDB();
            String sql = "SELECT * FROM DOCTOR";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new DoctorData(
                    rs.getString("Id"),
                    rs.getString("Username"),
                    rs.getString("Name"),
                    rs.getString("Gender"),
                    rs.getString("Phone"),
                    rs.getString("Email"),
                    rs.getString("Specialized"),
                    rs.getString("Address"),
                    rs.getString("Password")
                ));
            }

            // Gán dữ liệu vào các cột
            doctors_col_doctorID.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDoctorId()));
            doctors_col_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            doctors_col_gender.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));
            doctors_col_contactNumber.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
            doctors_col_email.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
            doctors_col_specialization.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSpecialized()));
            doctors_col_address.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));

            // Cột hành động: Sửa và Xoá
            doctors_col_action.setCellFactory(col -> new TableCell<>() {
                private final Button editBtn = new Button("Sửa");
                private final Button deleteBtn = new Button("Xoá");
                private final HBox hbox = new HBox(5, editBtn, deleteBtn);

                {
                    editBtn.setOnAction(e -> {
                        DoctorData doctor = getTableView().getItems().get(getIndex());
                        openEditDoctorForm(doctor); // TODO: mở form sửa
                    });

                    deleteBtn.setOnAction(e -> {
                        DoctorData doctor = getTableView().getItems().get(getIndex());
                        deleteDoctor(doctor.getDoctorId()); // TODO: viết hàm xoá
                        loadDoctorTable(); // Refresh lại
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbox);
                    }
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
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc muốn xóa bác sĩ này?");
        alert.setContentText("Thao tác này không thể hoàn tác.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Connection conn = Database.connectDB();
                String sql = "DELETE FROM DOCTOR WHERE Id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, doctorId);
                ps.executeUpdate();
                conn.close();

                // Tải lại bảng sau khi xóa
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
            
            // Get the controller and pass the doctor data
            EditDoctorFormController controller = loader.getController();
            controller.setDoctorData(doctor);
            
            // Create a new stage for the edit form
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Edit Doctor");
            stage.setScene(new Scene(root));
            
            // Set event to refresh doctor table when the form is closed
            stage.setOnHidden(e -> loadDoctorTable());
            
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading EditDoctorForm.fxml: " + e.getMessage());
        }
    }
    
    
    
// 
    
    
    @FXML
    private void openAddDoctorForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddDoctorForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Thêm bác sĩ");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadDoctorTable(); // Load lại sau khi thêm
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



   




}
