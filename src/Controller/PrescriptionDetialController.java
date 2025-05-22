package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import DAO.Database;
import Enum.Drug_Unit;
import Model.PrescriptionData;
import Model.PrescriptionDetailsData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import Alert.AlertMessage;

public class PrescriptionDetialController {

    @FXML private TextField txtDiagnose, txtAdvice, txtQuantity, txtInstructions;
    @FXML private ComboBox<String> cmbDrug;
    @FXML private Button btnSave, btnCancel, btnAddDrug;
    @FXML private Label lblDiagnoseError, lblAdviceError, lblDrugError, lblQuantityError, lblInstructionsError;

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private AlertMessage alert = new AlertMessage();

    @FXML
    private void initialize() {
        // Load drugs into ComboBox
        loadDrugs();
    }

    // Method to set appointment data
    public void setAppointmentData(String appointmentId, String patientId, String doctorId) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    private void loadDrugs() {
        String sql = "SELECT Id, Name FROM DRUG";
        try (Connection conn = Database.connectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            cmbDrug.getItems().clear();
            while (rs.next()) {
                cmbDrug.getItems().add(rs.getString("Id") + ": " + rs.getString("Name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Failed to load drugs: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        if (validateAllFields()) {
            savePrescription();
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void handleAddDrug() {
        if (validateDrugFields()) {
            addDrugToPrescription();
        }
    }

    private boolean validateAllFields() {
        boolean isValid = true;
        isValid = validateTextField(txtDiagnose, lblDiagnoseError, "Diagnosis is required") && isValid;
        isValid = validateTextField(txtAdvice, lblAdviceError, "Advice is required") && isValid;
        return isValid;
    }

    private boolean validateDrugFields() {
        boolean isValid = true;
        isValid = validateComboBox(cmbDrug, lblDrugError, "Drug is required") && isValid;
        isValid = validateIntegerField(txtQuantity, lblQuantityError, "Invalid quantity") && isValid;
        isValid = validateTextField(txtInstructions, lblInstructionsError, "Instructions are required") && isValid;
        return isValid;
    }

    private boolean validateComboBox(ComboBox<String> comboBox, Label errorLabel, String errorMessage) {
        if (comboBox.getValue() == null || comboBox.getValue().trim().isEmpty()) {
            showError(errorLabel, errorMessage);
            return false;
        }
        hideError(errorLabel);
        return true;
    }

    private boolean validateTextField(TextField field, Label errorLabel, String errorMessage) {
        if (field.getText().trim().isEmpty()) {
            showError(errorLabel, errorMessage);
            return false;
        }
        hideError(errorLabel);
        return true;
    }

    private boolean validateIntegerField(TextField field, Label errorLabel, String errorMessage) {
        try {
            int value = Integer.parseInt(field.getText().trim());
            if (value <= 0) throw new NumberFormatException();
            hideError(errorLabel);
            return true;
        } catch (NumberFormatException e) {
            showError(errorLabel, errorMessage);
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

    private void savePrescription() {
        try (Connection conn = Database.connectDB()) {
            conn.setAutoCommit(false); // Start transaction
            String prescriptionId = UUID.randomUUID().toString();
            String sql = "INSERT INTO PRESCRIPTION (Id, Patient_id, Doctor_id, TotalAmount, diagnose, advice, Create_date, Update_date) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, prescriptionId);
            ps.setString(2, patientId);
            ps.setString(3, doctorId);
            ps.setDouble(4, 0.0); // TotalAmount will be updated later
            ps.setString(5, txtDiagnose.getText().trim());
            ps.setString(6, txtAdvice.getText().trim());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();

            // Commit transaction
            conn.commit();

            alert.successMessage("Prescription added successfully!");
            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Failed to add prescription: " + e.getMessage());
        }
    }

    private void addDrugToPrescription() {
        try (Connection conn = Database.connectDB()) {
            String drugId = cmbDrug.getValue().split(":")[0].trim();
            String sql = "INSERT INTO PRESCRIPTION_DETAILS (Prescription_id, Drug_id, Quantity, Instructions, Create_date, Update_date) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, UUID.randomUUID().toString()); // Temporary prescription ID or link to existing
            ps.setString(2, drugId);
            ps.setInt(3, Integer.parseInt(txtQuantity.getText().trim()));
            ps.setString(4, txtInstructions.getText().trim());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();

            alert.successMessage("Drug added to prescription successfully!");
            txtQuantity.clear();
            txtInstructions.clear();
            cmbDrug.getSelectionModel().clearSelection();
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Failed to add drug to prescription: " + e.getMessage());
        }
    }
}