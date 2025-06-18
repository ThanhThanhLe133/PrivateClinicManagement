package Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.DrugData;
import Model.ServiceData;
import DAO.Database;
import Enum.ServiceType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EditServiceFormController {

    @FXML private TextField txtServiceName, txtPrice;
    @FXML private Button btnSave, btnCancel;
    // Validation error labels
    // Nhãn hiển thị lỗi xác thực
    @FXML private Label lbServiceNameError, lbPriceError,lbTypeError;
    
	@FXML
	private ComboBox<String> cbType;

    private ServiceData service;

    public void setServiceData(ServiceData service) {
        // Gán dữ liệu dịch vụ vào các trường hiển thị
        this.service = service;
        txtServiceName.setText(service.getName());
        txtPrice.setText(String.valueOf(service.getPrice()));
        cbType.setValue(service.getType()); 
    }

    @FXML
    private void initialize() {
        // Tải danh sách loại dịch vụ vào ComboBox
    	cbType.setItems(FXCollections.observableArrayList(
    		    Arrays.stream(ServiceType.values())
    		          .map(Enum::name)
    		          .collect(Collectors.toList())
    		));

        // Ràng buộc nhập số cho giá
        txtPrice.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                txtPrice.setText(oldVal);
            }
        });

    }

    @FXML
    private void handleCancel() {
        // Đóng cửa sổ khi nhấn hủy
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        // Lưu thông tin dịch vụ nếu các trường hợp lệ
        if (validateAllFields()) {
            updateService();
        }
    }

    private boolean validateAllFields() {
        // Kiểm tra tất cả các trường nhập liệu
        boolean isValid = true;

        isValid = validateTextField(txtServiceName.getText(), lbServiceNameError, "Service name is required") && isValid;
        
        isValid = validateNumericField(txtPrice, lbPriceError, "Invalid price") && isValid;
        isValid = validateTextField(cbType.getValue(), lbTypeError, "Invalid type") && isValid;

        return isValid;
    }

    private boolean validateTextField(String string, Label errorLabel, String message) {
        // Kiểm tra trường văn bản
        if (string.trim().isEmpty()) {
            showError(errorLabel, message);
            return false;
        }
        hideError(errorLabel);
        return true;
    }

    private boolean validateNumericField(TextField field, Label errorLabel, String message) {
        // Kiểm tra trường số
        try {
            double value = Double.parseDouble(field.getText().trim());
            if (value < 0) throw new NumberFormatException();
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

    private void updateService() {
        // Cập nhật thông tin dịch vụ vào cơ sở dữ liệu
        try (Connection conn = Database.connectDB()) {
            String sql = "UPDATE SERVICE SET Name = ?, Type = ?, Price = ? WHERE Id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtServiceName.getText().trim());
            ps.setString(2, cbType.getValue().trim());
            ps.setBigDecimal(3, new BigDecimal(txtPrice.getText().trim()));
            ps.setString(4, service.getServiceId());

            ps.executeUpdate();

            // Hiển thị thông báo thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Service updated successfully!");
            alert.showAndWait();

            // Đóng form
            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            // Hiển thị thông báo lỗi
            Alert error = new Alert(Alert.AlertType.ERROR, "Error updating service: " + e.getMessage());
            error.showAndWait();
        }
    }
}