package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import DAO.Database;
import Enum.Drug_Unit;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class EditPrescriptionDrugFormController {

    @FXML private TextField txtDrugName, txtManufacturer, txtPrice, txtStock;
    @FXML private DatePicker dtExpiryDate;
    @FXML private Button btnSave, btnCancel;

    @FXML private Label lblDrugNameError, lblManufacturerError, lblExpiryDateError,
                        lblUnitError, lblPriceError, lblStockError;
    
    @FXML private ComboBox<String> cmbUnit;

    @FXML
    private void initialize() {
        // Optional: thêm ràng buộc chỉ cho nhập số vào trường số
        txtPrice.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                txtPrice.setText(oldVal);
            }
        });

        txtStock.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtStock.setText(oldVal);
            }
        });
        
        // Đưa các giá trị enum vào ComboBox
        for (Drug_Unit unit : Drug_Unit.values()) {
            cmbUnit.getItems().add(unit.name()); // hoặc unit.toString() nếu bạn override toString()
        }
    }

    @FXML
    private void handleSave() {
        if (validateAllFields()) {
            saveDrug();
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        isValid = validateTextField(txtDrugName, lblDrugNameError, "Drug name is required") && isValid;
        isValid = validateTextField(txtManufacturer, lblManufacturerError, "Manufacturer is required") && isValid;
        isValid = validateDatePicker(dtExpiryDate, lblExpiryDateError, "Expiry date is required") && isValid;
        isValid = validateComboBox(cmbUnit, lblUnitError, "Unit is required") && isValid;
        isValid = validateNumericField(txtPrice, lblPriceError, "Invalid price") && isValid;
        isValid = validateIntegerField(txtStock, lblStockError, "Invalid stock") && isValid;

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

    private boolean validateNumericField(TextField field, Label errorLabel, String errorMessage) {
        try {
            double value = Double.parseDouble(field.getText().trim());
            if (value < 0) throw new NumberFormatException();
            hideError(errorLabel);
            return true;
        } catch (NumberFormatException e) {
            showError(errorLabel, errorMessage);
            return false;
        }
    }

    private boolean validateIntegerField(TextField field, Label errorLabel, String errorMessage) {
        try {
            int value = Integer.parseInt(field.getText().trim());
            if (value < 0) throw new NumberFormatException();
            hideError(errorLabel);
            return true;
        } catch (NumberFormatException e) {
            showError(errorLabel, errorMessage);
            return false;
        }
    }

    private boolean validateDatePicker(DatePicker datePicker, Label errorLabel, String errorMessage) {
        if (datePicker.getValue() == null || datePicker.getValue().isBefore(LocalDate.now())) {
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

    private void saveDrug() {
        try (Connection conn = Database.connectDB()) {
            String sql = "INSERT INTO DRUG (Id,Name, Manufacturer, Expiry_date, Unit, Price, Stock) " +
                         "VALUES (UUID(),?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtDrugName.getText().trim());
            ps.setString(2, txtManufacturer.getText().trim());
            ps.setDate(3, Date.valueOf(dtExpiryDate.getValue()));
            ps.setString(4, cmbUnit.getValue());
            ps.setDouble(5, Double.parseDouble(txtPrice.getText().trim()));
            ps.setInt(6, Integer.parseInt(txtStock.getText().trim()));

            ps.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Drug added successfully!");
            alert.showAndWait();

            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add drug: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
