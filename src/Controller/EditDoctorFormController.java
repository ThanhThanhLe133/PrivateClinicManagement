package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.DoctorData;
import DAO.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditDoctorFormController {

    @FXML private TextField txtUsername, txtPassword, txtName, txtEmail, txtPhone, txtAddress;
    @FXML private ComboBox<String> cmbGender;
    @FXML private ComboBox<String> cmbSpecialization;
    @FXML private Button btnSave, btnCancel;

    private DoctorData doctor;

    public void setDoctorData(DoctorData doctor) {
        this.doctor = doctor;
//        txtUsername.setText(doctor.getUsername());
//        txtPassword.setText(doctor.getPassword());
        txtName.setText(doctor.getName());
        txtEmail.setText(doctor.getEmail());
        cmbGender.setValue(doctor.getGender());
        txtPhone.setText(doctor.getPhone());
        cmbSpecialization.setValue(doctor.getSpecialized());
        txtAddress.setText(doctor.getAddress());
    }

    @FXML
    private void initialize() {
        cmbGender.getItems().addAll("Male", "Female", "Other");
        cmbSpecialization.getItems().addAll(
        	    "Internal Medicine", "Surgery", "ENT", "Cardiology",
        	    "Obstetrics", "Pediatrics", "Dermatology", "Neurology"
        	);

    }

    @FXML
    private void handleCancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        try (Connection conn = Database.connectDB()) {
            // Cập nhật bảng USER_ACCOUNT
//            String sqlUser = "UPDATE USER_ACCOUNT SET Username = ?, Password = ?, Email = ?, Name = ?, Gender = ? WHERE Id = ?";
            String sqlUser = "UPDATE USER_ACCOUNT SET  Email = ?, Name = ?, Gender = ? WHERE Id = ?";

            PreparedStatement psUser = conn.prepareStatement(sqlUser);
//            psUser.setString(1, txtUsername.getText());
//            psUser.setString(2, txtPassword.getText());
            psUser.setString(1, txtEmail.getText());
            psUser.setString(2, txtName.getText());
            psUser.setString(3, cmbGender.getValue());
            psUser.setString(4, doctor.getDoctorId());
            psUser.executeUpdate();

            // Cập nhật bảng DOCTOR
            String sqlDoctor = "UPDATE DOCTOR SET Phone = ?, Specialized = ?, Address = ? WHERE Doctor_id = ?";
            PreparedStatement psDoctor = conn.prepareStatement(sqlDoctor);
            psDoctor.setString(1, txtPhone.getText());
            psDoctor.setString(2, cmbSpecialization.getValue());
            psDoctor.setString(3, txtAddress.getText());
            psDoctor.setString(4, doctor.getDoctorId());
            psDoctor.executeUpdate();

            // Thông báo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText(null);
            alert.setContentText("Cập nhật bác sĩ thành công!");
            alert.showAndWait();

            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "Lỗi khi cập nhật dữ liệu!");
            error.showAndWait();
        }
    }
}
