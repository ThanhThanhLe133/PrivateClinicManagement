package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.PatientData;
import DAO.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditPatientFormController {

    @FXML private TextField txtPatientName, txtEmail, txtHeight, txtWeight,
                             txtPhone, txtDiagnosis, txtAddress;
    @FXML private ComboBox<String> cmbGender;
    @FXML private Button btnSave, btnCancel;

    @FXML private Label lblPatientNameError, lblEmailError, lblHeightError, lblWeightError,
                         lblGenderError, lblPhoneError, lblDiagnosisError, lblAddressError;

    private PatientData patient;

    public void setPatientData(PatientData patient) {
        // Gán dữ liệu bệnh nhân vào các trường hiển thị
        this.patient = patient;
        txtPatientName.setText(patient.getName());
        txtEmail.setText(patient.getEmail());
        txtHeight.setText(String.valueOf(patient.getHeight()));
        txtWeight.setText(String.valueOf(patient.getWeight()));
        cmbGender.setValue(patient.getGender());
        txtPhone.setText(patient.getPhone());
        txtDiagnosis.setText(patient.getDiagnosis());
        txtAddress.setText(patient.getAddress());
    }

    @FXML
    private void initialize() {
        // Khởi tạo danh sách giới tính
        cmbGender.getItems().addAll("Male", "Female", "Other");

        // Ràng buộc nhập số cho chiều cao
        txtHeight.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                txtHeight.setText(oldVal);
            }
        });

        // Ràng buộc nhập số cho cân nặng
        txtWeight.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                txtWeight.setText(oldVal);
            }
        });

        // Thêm kiểm tra tự động khi mất focus
        addFocusValidation();
    }

    private void addFocusValidation() {
        // Thêm listener để kiểm tra khi mất focus
        txtPatientName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validatePatientName();
        });
        txtEmail.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateEmail();
        });
        txtHeight.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateHeight();
        });
        txtWeight.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateWeight();
        });
        cmbGender.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateGender();
        });
        txtPhone.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validatePhone();
        });
        txtDiagnosis.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateDiagnosis();
        });
        txtAddress.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateAddress();
        });
    }

    @FXML
    private void handleCancel() {
        // Đóng cửa sổ khi nhấn hủy
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        // Lưu thông tin bệnh nhân nếu các trường hợp lệ
        if (validateAllFields()) {
            // Show confirmation dialog for updating
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Update Confirmation");
            confirmAlert.setHeaderText("Are you sure you want to update patient: " + txtPatientName.getText().trim() + "?");
            confirmAlert.setContentText("This action will update the patient's information.");
            Optional<ButtonType> confirmResult = confirmAlert.showAndWait();

            if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
                updatePatient();
            }
        }
    }

    private boolean validateAllFields() {
        // Kiểm tra tất cả các trường nhập liệu
        boolean isValid = validatePatientName();
        isValid = validateEmail() && isValid;
        isValid = validateHeight() && isValid;
        isValid = validateWeight() && isValid;
        isValid = validateGender() && isValid;
        isValid = validatePhone() && isValid;
        isValid = validateDiagnosis() && isValid;
        isValid = validateAddress() && isValid;
        return isValid;
    }

    private boolean validatePatientName() {
        // Kiểm tra tên bệnh nhân
        return validateTextField(txtPatientName, lblPatientNameError, "Patient name is required");
    }

    private boolean validateEmail() {
        // Kiểm tra email
        String email = txtEmail.getText().trim();
        if (email.isEmpty() || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showError(lblEmailError, "Valid email is required");
            return false;
        }
        hideError(lblEmailError);
        return true;
    }

    private boolean validateHeight() {
        // Kiểm tra chiều cao
        return validatePositiveNumber(txtHeight, lblHeightError, "Height must be a positive number");
    }

    private boolean validateWeight() {
        // Kiểm tra cân nặng
        return validatePositiveNumber(txtWeight, lblWeightError, "Weight must be a positive number");
    }

    private boolean validateGender() {
        // Kiểm tra giới tính
        if (cmbGender.getValue() == null || cmbGender.getValue().isEmpty()) {
            showError(lblGenderError, "Gender is required");
            return false;
        }
        hideError(lblGenderError);
        return true;
    }

    private boolean validatePhone() {
        // Kiểm tra số điện thoại
        String phone = txtPhone.getText().trim();
        if (phone.isEmpty() || !phone.matches("\\d{9,15}")) {
            showError(lblPhoneError, "Valid phone number is required");
            return false;
        }
        hideError(lblPhoneError);
        return true;
    }

    private boolean validateDiagnosis() {
        // Kiểm tra chẩn đoán
        return validateTextField(txtDiagnosis, lblDiagnosisError, "Diagnosis is required");
    }

    private boolean validateAddress() {
        // Kiểm tra địa chỉ
        return validateTextField(txtAddress, lblAddressError, "Address is required");
    }

    private boolean validateTextField(TextField field, Label errorLabel, String message) {
        // Kiểm tra trường văn bản
        if (field.getText().trim().isEmpty()) {
            showError(errorLabel, message);
            return false;
        }
        hideError(errorLabel);
        return true;
    }

    private boolean validatePositiveNumber(TextField field, Label errorLabel, String message) {
        // Kiểm tra số dương
        try {
            double value = Double.parseDouble(field.getText().trim());
            if (value <= 0) throw new NumberFormatException();
            hideError(errorLabel);
            return true;
        } catch (NumberFormatException e) {
            showError(errorLabel, message);
            return false;
        }
    }

    private void showError(Label label, String message) {
        // Hiển thị thông báo lỗi
        label.setText(message);
        label.setVisible(true);
    }

    private void hideError(Label label) {
        // Ẩn thông báo lỗi
        label.setVisible(false);
    }

    private void updatePatient() {
        try (Connection conn = Database.connectDB()) {
            conn.setAutoCommit(false); // Start transaction

//            // Check for associated appointments
//            String checkAppointmentSQL = "SELECT COUNT(*) FROM APPOINTMENT WHERE Patient_id = ?";
//            PreparedStatement psCheck = conn.prepareStatement(checkAppointmentSQL);
//            psCheck.setString(1, patient.getPatientId());
//            ResultSet rs = psCheck.executeQuery();
//            rs.next();
//            int appointmentCount = rs.getInt(1);
//
//            if (appointmentCount > 0) {
//                // Show warning about associated appointments
//                Alert warning = new Alert(Alert.AlertType.WARNING);
//                warning.setTitle("Patient Has Appointments");
//                warning.setHeaderText("This patient is associated with " + appointmentCount + " appointment(s).");
//                warning.setContentText("Updating this patient will reset the Prescription_Status of associated appointments to 'Created'. Do you want to proceed?");
//                ButtonType proceedButton = new ButtonType("Proceed");
//                ButtonType cancelButton = new ButtonType("Cancel");
//                warning.getButtonTypes().setAll(proceedButton, cancelButton);
//
//                Optional<ButtonType> warningResult = warning.showAndWait();
//                if (warningResult.isPresent() && warningResult.get() != proceedButton) {
//                    conn.rollback();
//                    return; // User canceled
//                }
//
//                // Get appointment IDs to reset status
//                String getAppointmentsSQL = "SELECT Id FROM APPOINTMENT WHERE Patient_id = ?";
//                psCheck = conn.prepareStatement(getAppointmentsSQL);
//                psCheck.setString(1, patient.getPatientId());
//                rs = psCheck.executeQuery();
//                List<String> appointmentIds = new ArrayList<>();
//                while (rs.next()) {
//                    appointmentIds.add(rs.getString("Id"));
//                }
//
//                // Reset Prescription_Status to 'Created if it was 'Paid'
//                String updateStatusSQL = "UPDATE APPOINTMENT SET Prescription_Status = 'Created' WHERE Id = ? AND Prescription_Status = 'Paid'";
//                PreparedStatement psUpdateStatus = conn.prepareStatement(updateStatusSQL);
//                for (String appointmentId : appointmentIds) {
//                    psUpdateStatus.setString(1, appointmentId);
//                    psUpdateStatus.executeUpdate();
//                }
//            }

            // Update patient data
            String sql = "UPDATE PATIENT SET Name = ?, Email = ?, Height = ?, Weight = ?, Gender = ?, Phone = ?, Diagnosis = ?, Address = ?, Update_date = ? WHERE Patient_Id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtPatientName.getText().trim());
            ps.setString(2, txtEmail.getText().trim());
            ps.setDouble(3, Double.parseDouble(txtHeight.getText().trim()));
            ps.setDouble(4, Double.parseDouble(txtWeight.getText().trim()));
            ps.setString(5, cmbGender.getValue());
            ps.setString(6, txtPhone.getText().trim());
            ps.setString(7, txtDiagnosis.getText().trim());
            ps.setString(8, txtAddress.getText().trim());
            ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            ps.setString(10, patient.getPatientId());

            ps.executeUpdate();

            conn.commit(); // Commit transaction

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Patient updated successfully!");
            alert.showAndWait();

            // Close form
            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            try (Connection conn = Database.connectDB()) {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Error");
            alert.setHeaderText(null);
            alert.setContentText("Error updating patient: " + e.getMessage());
            alert.showAndWait();
        }
    }
}