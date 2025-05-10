package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.DoctorData;
import DAO.Database;
import Enum.Gender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

import Alert.AlertMessage;

public class EditDoctorFormController {

    @FXML private TextField txtUsername, txtPassword, txtName, txtEmail, txtPhone, txtAddress;
    @FXML private ComboBox<String> cmbGender;
    @FXML private ComboBox<String> cmbSpecialization;
    @FXML private Button btnSave, btnCancel;
	private final AlertMessage alert = new AlertMessage();
    private DoctorData doctor;

    public void setDoctorData(DoctorData doctor) {
        this.doctor = doctor;
//        txtUsername.setText(doctor.getUsername());
//        txtPassword.setText(doctor.getPassword());
        txtName.setText(doctor.getName());
        txtEmail.setText(doctor.getEmail());
        cmbGender.setValue(doctor.getGender());
        txtPhone.setText(doctor.getPhone());
        cmbSpecialization.setValue(doctor.getServiceName());
        txtAddress.setText(doctor.getAddress());
    }

    @FXML
    private void initialize() {
    	 loadComboBox();

    }
    public void loadComboBox() {

    	cmbGender.setItems(FXCollections
				.observableArrayList(Arrays.stream(Gender.values()).map(Enum::name).collect(Collectors.toList())));
		ObservableList<String> specializationCb = FXCollections.observableArrayList();
		try (Connection conn = Database.connectDB()) {
			String sql = "SELECT DISTINCT Name FROM SERVICE";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				specializationCb.add(rs.getString("Name"));
			}

			cmbSpecialization.setItems(specializationCb);
		} catch (SQLException e) {
			e.printStackTrace();
			alert.errorMessage("Error loading specializations: " + e.getMessage());
		}
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
            psUser.setString(4, doctor.getId());
            psUser.executeUpdate();

            // Cập nhật bảng DOCTOR
            String getServiceIdSql = "SELECT Id FROM SERVICE WHERE Name = ?";
            PreparedStatement psService = conn.prepareStatement(getServiceIdSql);
            psService.setString(1, cmbSpecialization.getValue());
            ResultSet rs = psService.executeQuery();

            if (rs.next()) {
                String serviceId = rs.getString("Id");

                // Cập nhật bảng DOCTOR
                String sqlDoctor = "UPDATE DOCTOR SET Phone = ?, Address = ?, Service_id = ? WHERE Doctor_id = ?";
                PreparedStatement psDoctor = conn.prepareStatement(sqlDoctor);
                psDoctor.setString(1, txtPhone.getText());
                psDoctor.setString(2, txtAddress.getText());
                psDoctor.setString(3, serviceId); 
                psDoctor.setString(4, doctor.getId());
                psDoctor.executeUpdate();
            }

            // Thông báo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucess");
            alert.setHeaderText(null);
            alert.setContentText("Update doctor successfully!");
            alert.showAndWait();

            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "Error!");
            error.showAndWait();
        }
    }
}
