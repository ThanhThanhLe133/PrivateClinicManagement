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

        // Ràng buộc nhập số cho giá
        txtPrice.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                txtPrice.setText(oldVal);
            }
        });

        // Ràng buộc nhập số nguyên cho số lượng tồn kho
        txtStock.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtStock.setText(oldVal);
            }
        });

        // Thêm kiểm tra tự động khi người dùng chỉnh sửa
        addFocusValidation();
    }

    private void addFocusValidation() {
        // Thêm listener để kiểm tra khi mất focus
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
        // Gán dữ liệu thuốc vào các trường hiển thị
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
        // Đóng cửa sổ khi nhấn hủy
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        // Lưu thông tin thuốc nếu các trường hợp lệ
        if (validateAllFields()) {
            updateDrug();
        }
    }

    private boolean validateAllFields() {
        // Kiểm tra tất cả các trường nhập liệu
        boolean isValid = validateDrugName();
        isValid = validateManufacturer() && isValid;
        isValid = validateExpiryDate() && isValid;
        isValid = validateUnit() && isValid;
        isValid = validatePrice() && isValid;
        isValid = validateStock() && isValid;

        return isValid;
    }

    private boolean validateDrugName() {
        // Kiểm tra tên thuốc
        return validateTextField(txtDrugName, lblDrugNameError, "Drug name is required");
    }

    private boolean validateManufacturer() {
        // Kiểm tra nhà sản xuất
        return validateTextField(txtManufacturer, lblManufacturerError, "Manufacturer is required");
    }

    private boolean validateExpiryDate() {
        // Kiểm tra ngày hết hạn
        LocalDate date = dtExpiryDate.getValue();
        if (date == null || date.isBefore(LocalDate.now())) {
            showError(lblExpiryDateError, "Expiry date must be today or later");
            return false;
        }
        hideError(lblExpiryDateError);
        return true;
    }

    private boolean validateUnit() {
        // Kiểm tra đơn vị thuốc
        if (cmbUnit.getValue() == null || cmbUnit.getValue().trim().isEmpty()) {
            showError(lblUnitError, "Unit is required");
            return false;
        }
        hideError(lblUnitError);
        return true;
    }

    private boolean validatePrice() {
        // Kiểm tra giá thuốc
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
        // Kiểm tra số lượng tồn kho
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
        // Kiểm tra trường văn bản
        if (field.getText().trim().isEmpty()) {
            showError(errorLabel, errorMessage);
            return false;
        }
        hideError(errorLabel);
        return true;
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

    private void updateDrug() {
        // Cập nhật thông tin thuốc vào cơ sở dữ liệu
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

            // Hiển thị thông báo thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Drug updated successfully!");
            alert.showAndWait();

            // Đóng form
            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            // Hiển thị thông báo lỗi
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update drug: " + e.getMessage());
            alert.showAndWait();
        }
    }
}