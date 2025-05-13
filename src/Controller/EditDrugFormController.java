package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import DAO.Database;
import Enum.Drug_Unit;
import Model.DrugData;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class EditDrugFormController {

    @FXML private TextField txtDrugName, txtManufacturer, txtPrice, txtStock;
    @FXML private ComboBox<String> cmbUnit;
    @FXML private DatePicker dtExpiryDate;
    @FXML private Button btnSave, btnCancel;

    @FXML private Label lblDrugNameError, lblManufacturerError, lblExpiryDateError,
                        lblUnitError, lblPriceError, lblStockError;

    private DrugData drug;

    @FXML
    private void initialize() {
        // Đưa giá trị enum vào ComboBox
        for (Drug_Unit unit : Drug_Unit.values()) {
            cmbUnit.getItems().add(unit.name());
        }

        // Ràng buộc nhập số
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

        // Tự động ẩn lỗi khi người dùng chỉnh sửa
        addFocusValidation();
    }

    private void addFocusValidation() {
        txtDrugName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateDrugName();
        });
        txtManufacturer.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateManufacturer();
        });
        dtExpiryDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateExpiryDate();
        });
        cmbUnit.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateUnit();
        });
        txtPrice.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validatePrice();
        });
        txtStock.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateStock();
        });
    }

    public void setDrugData(DrugData drug) {
        this.drug = drug;
        txtDrugName.setText(drug.getName());
        txtManufacturer.setText(drug.getManufacturer());
        dtExpiryDate.setValue(drug.getExpiryDate());
        cmbUnit.setValue(drug.getUnit());
        txtPrice.setText(String.valueOf(drug.getPrice()));
        txtStock.setText(String.valueOf(drug.getStock()));
    }

    @FXML
    private void handleCancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        if (validateAllFields()) {
            updateDrug();
        }
    }

    private boolean validateAllFields() {
        boolean isValid = validateDrugName();
        isValid = validateManufacturer() && isValid;
        isValid = validateExpiryDate() && isValid;
        isValid = validateUnit() && isValid;
        isValid = validatePrice() && isValid;
        isValid = validateStock() && isValid;

        return isValid;
    }

    private boolean validateDrugName() {
        return validateTextField(txtDrugName, lblDrugNameError, "Drug name is required");
    }

    private boolean validateManufacturer() {
        return validateTextField(txtManufacturer, lblManufacturerError, "Manufacturer is required");
    }

    private boolean validateExpiryDate() {
        LocalDate date = dtExpiryDate.getValue();
        if (date == null || date.isBefore(LocalDate.now())) {
            showError(lblExpiryDateError, "Expiry date must be today or later");
            return false;
        }
        hideError(lblExpiryDateError);
        return true;
    }

    private boolean validateUnit() {
        if (cmbUnit.getValue() == null || cmbUnit.getValue().trim().isEmpty()) {
            showError(lblUnitError, "Unit is required");
            return false;
        }
        hideError(lblUnitError);
        return true;
    }

    private boolean validatePrice() {
        try {
            double value = Double.parseDouble(txtPrice.getText().trim());
            if (value < 0) throw new NumberFormatException();
            hideError(lblPriceError);
            return true;
        } catch (NumberFormatException e) {
            showError(lblPriceError, "Invalid price");
            return false;
        }
    }

    private boolean validateStock() {
        try {
            int value = Integer.parseInt(txtStock.getText().trim());
            if (value < 0) throw new NumberFormatException();
            hideError(lblStockError);
            return true;
        } catch (NumberFormatException e) {
            showError(lblStockError, "Invalid stock quantity");
            return false;
        }
    }

    private boolean validateTextField(TextField field, Label errorLabel, String errorMessage) {
        if (field.getText().trim().isEmpty()) {
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

    private void updateDrug() {
        try (Connection conn = Database.connectDB()) {
            String sql = "UPDATE DRUG SET Name = ?, Manufacturer = ?, Expiry_date = ?, Unit = ?, Price = ?, Stock = ?, Update_date = ? WHERE Id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtDrugName.getText().trim());
            ps.setString(2, txtManufacturer.getText().trim());
            ps.setDate(3, Date.valueOf(dtExpiryDate.getValue()));
            ps.setString(4, cmbUnit.getValue());
            ps.setDouble(5, Double.parseDouble(txtPrice.getText().trim()));
            ps.setInt(6, Integer.parseInt(txtStock.getText().trim()));
            ps.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
            ps.setString(8, drug.getDrugId());

            ps.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Drug updated successfully!");
            alert.showAndWait();

            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update drug: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
