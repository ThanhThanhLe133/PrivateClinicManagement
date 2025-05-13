package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.PatientData;
import DAO.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditPatientFormController {

    @FXML private TextField txtPatientName, txtEmail, txtHeight, txtWeight,
                             txtPhone, txtDiagnosis, txtAddress;
    @FXML private ComboBox<String> cmbGender;
    @FXML private Button btnSave, btnCancel;

    @FXML private Label lblPatientNameError, lblEmailError, lblHeightError, lblWeightError,
                         lblGenderError, lblPhoneError, lblDiagnosisError, lblAddressError;

    private PatientData patient;

    public void setPatientData(PatientData patient) {
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
        cmbGender.getItems().addAll("Male", "Female", "Other");

        txtHeight.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                txtHeight.setText(oldVal);
            }
        });

        txtWeight.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                txtWeight.setText(oldVal);
            }
        });

        // Tự động kiểm tra khi focus rời khỏi trường
        addFocusValidation();
    }

    private void addFocusValidation() {
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
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        if (validateAllFields()) {
            updatePatient();
        }
    }

    private boolean validateAllFields() {
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
        return validateTextField(txtPatientName, lblPatientNameError, "Patient name is required");
    }

    private boolean validateEmail() {
        String email = txtEmail.getText().trim();
        if (email.isEmpty() || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showError(lblEmailError, "Valid email is required");
            return false;
        }
        hideError(lblEmailError);
        return true;
    }

    private boolean validateHeight() {
        return validatePositiveNumber(txtHeight, lblHeightError, "Height must be a positive number");
    }

    private boolean validateWeight() {
        return validatePositiveNumber(txtWeight, lblWeightError, "Weight must be a positive number");
    }

    private boolean validateGender() {
        if (cmbGender.getValue() == null || cmbGender.getValue().isEmpty()) {
            showError(lblGenderError, "Gender is required");
            return false;
        }
        hideError(lblGenderError);
        return true;
    }

    private boolean validatePhone() {
        String phone = txtPhone.getText().trim();
        if (phone.isEmpty() || !phone.matches("\\d{9,15}")) {
            showError(lblPhoneError, "Valid phone number is required");
            return false;
        }
        hideError(lblPhoneError);
        return true;
    }

    private boolean validateDiagnosis() {
        return validateTextField(txtDiagnosis, lblDiagnosisError, "Diagnosis is required");
    }

    private boolean validateAddress() {
        return validateTextField(txtAddress, lblAddressError, "Address is required");
    }

    private boolean validateTextField(TextField field, Label errorLabel, String message) {
        if (field.getText().trim().isEmpty()) {
            showError(errorLabel, message);
            return false;
        }
        hideError(errorLabel);
        return true;
    }

    private boolean validatePositiveNumber(TextField field, Label errorLabel, String message) {
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
        label.setText(message);
        label.setVisible(true);
    }

    private void hideError(Label label) {
        label.setVisible(false);
    }

    private void updatePatient() {
        try (Connection conn = Database.connectDB()) {
            String sql = "UPDATE PATIENT SET Name=?, Email=?, Height=?, Weight=?, Gender=?, Phone=?, Diagnosis=?, Address=?, Update_date=? WHERE Patient_Id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtPatientName.getText().trim());
            ps.setString(2, txtEmail.getText().trim());
            ps.setDouble(3, Double.parseDouble(txtHeight.getText().trim()));
            ps.setDouble(4, Double.parseDouble(txtWeight.getText().trim()));
            ps.setString(5, cmbGender.getValue());
            ps.setString(6, txtPhone.getText().trim());
            ps.setString(7, txtDiagnosis.getText().trim());
            ps.setString(8, txtAddress.getText().trim());
            ps.setTimestamp(9, new java.sql.Timestamp(System.currentTimeMillis()));
            ps.setString(10, patient.getPatientId());

            ps.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Patient updated successfully!");
            alert.showAndWait();

            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Error");
            alert.setHeaderText(null);
            alert.setContentText("Error updating patient: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
