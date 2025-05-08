package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import DAO.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddPatientFormController {

    @FXML private TextField txtPatientName, txtHeight, txtWeight, txtPhone, txtDiagnosis, txtAddress;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> cmbGender;

    @FXML private Label lblPatientNameError, lblEmailError, lblHeightError, lblWeightError,
                         lblGenderError, lblPhoneError, lblDiagnosisError, lblAddressError;

    @FXML private Button btnSave, btnCancel;

    @FXML
    private void initialize() {
        cmbGender.getItems().addAll("Male", "Female", "Other");

        // Ràng buộc chỉ cho nhập số
        txtHeight.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                txtHeight.setText(oldVal);
            }
        });

        txtWeight.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                txtWeight.setText(oldVal);
            }
        });

        txtPhone.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtPhone.setText(oldVal);
            }
        });
    }

    @FXML
    private void handleSave() {
        if (validateAllFields()) {
            savePatient();
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        isValid = validateTextField(txtPatientName, lblPatientNameError, "Name is required") && isValid;
        isValid = validateTextField(txtEmail, lblEmailError, "Email is required") && isValid;
        isValid = validateNumericField(txtHeight, lblHeightError, "Invalid height") && isValid;
        isValid = validateNumericField(txtWeight, lblWeightError, "Invalid weight") && isValid;
        isValid = validateComboBox(cmbGender, lblGenderError, "Select gender") && isValid;
        isValid = validateTextField(txtPhone, lblPhoneError, "Phone is required") && isValid;
        isValid = validateTextField(txtDiagnosis, lblDiagnosisError, "Diagnosis is required") && isValid;
        isValid = validateTextField(txtAddress, lblAddressError, "Address is required") && isValid;

        return isValid;
    }

    private boolean validateTextField(TextField field, Label errorLabel, String errorMessage) {
        if (field.getText().trim().isEmpty()) {
            showError(errorLabel, errorMessage);
            return false;
        }
        hideError(errorLabel);
        return true;
    }

    private boolean validateNumericField(TextField field, Label errorLabel, String errorMessage) {
        try {
            double value = Double.parseDouble(field.getText().trim());
            if (value <= 0) throw new NumberFormatException();
            hideError(errorLabel);
            return true;
        } catch (NumberFormatException e) {
            showError(errorLabel, errorMessage);
            return false;
        }
    }

    private boolean validateComboBox(ComboBox<String> comboBox, Label errorLabel, String errorMessage) {
        if (comboBox.getValue() == null || comboBox.getValue().isEmpty()) {
            showError(errorLabel, errorMessage);
            return false;
        }
        hideError(errorLabel);
        return true;
    }

    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }

    private void hideError(Label label) {
        label.setVisible(false);
    }

    private void savePatient() {
        try (Connection conn = Database.connectDB()) {
            String sql = "INSERT INTO PATIENT (Name, Email, Height, Weight, Gender, Phone, Diagnosis, Address) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtPatientName.getText().trim());
            ps.setString(2, txtEmail.getText().trim());
            ps.setDouble(3, Double.parseDouble(txtHeight.getText().trim()));
            ps.setDouble(4, Double.parseDouble(txtWeight.getText().trim()));
            ps.setString(5, cmbGender.getValue());
            ps.setString(6, txtPhone.getText().trim());
            ps.setString(7, txtDiagnosis.getText().trim());
            ps.setString(8, txtAddress.getText().trim());

            ps.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Patient added successfully!");
            alert.showAndWait();

            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add patient: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
