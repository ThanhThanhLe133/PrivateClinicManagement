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

public class AddServiceFormController {

    @FXML private TextField txtServiceName, txtPrice; // Trường nhập tên dịch vụ và giá
    @FXML private Button btnSave, btnCancel; // Nút lưu và hủy
    // Nhãn lỗi xác thực
    @FXML private Label lbServiceNameError, lbPriceError, lbTypeError;
    
    @FXML
    private ComboBox<String> cbType; // ComboBox để chọn loại dịch vụ

    @FXML
    private void initialize() {
        // Khởi tạo danh sách loại dịch vụ từ enum ServiceType
        cbType.setItems(FXCollections.observableArrayList(
                Arrays.stream(ServiceType.values())
                      .map(Enum::name)
                      .collect(Collectors.toList())
        ));

        // Ràng buộc chỉ cho phép nhập số và dấu chấm thập phân vào trường giá
        txtPrice.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                txtPrice.setText(oldVal);
            }
        });
    }

    @FXML
    private void handleCancel() {
        // Xử lý sự kiện hủy, đóng cửa sổ hiện tại
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        // Xử lý sự kiện lưu, kiểm tra hợp lệ trước khi lưu
        if (validateAllFields()) {
            saveService();
        }
    }

    private boolean validateAllFields() {
        // Kiểm tra tính hợp lệ của tất cả các trường
        boolean isValid = true;

        isValid = validateTextField(txtServiceName.getText(), lbServiceNameError, "Service name is required") && isValid;
        isValid = validateNumericField(txtPrice, lbPriceError, "Invalid price") && isValid;
        isValid = validateTextField(cbType.getValue(), lbTypeError, "Invalid type") && isValid;

        return isValid;
    }

    private boolean validateTextField(String string, Label errorLabel, String message) {
        // Kiểm tra trường văn bản có rỗng không
        if (string.trim().isEmpty()) {
            showError(errorLabel, message);
            return false;
        }
        hideError(errorLabel);
        return true;
    }

    private boolean validateNumericField(TextField field, Label errorLabel, String message) {
        // Kiểm tra trường số có hợp lệ không
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

    private void saveService() {
        // Lưu dịch vụ vào cơ sở dữ liệu
        try (Connection conn = Database.connectDB()) {
            String sql = "INSERT INTO SERVICE (Id, Name, Type, Price) VALUES (UUID(), ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtServiceName.getText().trim());
            ps.setString(2, cbType.getValue().trim());
            ps.setBigDecimal(3, new BigDecimal(txtPrice.getText().trim()));

            ps.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Service added successfully!");
            alert.showAndWait();

            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "Error updating service: " + e.getMessage());
            error.showAndWait();
        }
    }
}