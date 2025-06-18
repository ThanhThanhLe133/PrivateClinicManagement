package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import DAO.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.regex.Pattern;

public class EditReceptionistFormController {
    @FXML private TextField txtName, txtEmail, txtPhone, txtAddress;
    @FXML private ComboBox<String> cmbGender;
    @FXML private Button btnUpdate, btnCancel;
    
    // Validation error labels
    // Nhãn hiển thị lỗi xác thực
    @FXML private Label lblNameError, lblEmailError, lblGenderError, lblPhoneError, lblAddressError;
    
    // Data fields
    // ID của lễ tân
    private String receptionistId;
    
    // Regex patterns for validation
    // Mẫu regex để kiểm tra định dạng
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    
    @FXML
    private void initialize() {
        // Khởi tạo danh sách giới tính
        cmbGender.getItems().addAll("Male", "Female", "Other");
        
        // Thêm listener để xác thực thời gian thực
        txtName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateName();
        });
        
        txtEmail.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateEmail();
        });
        
        txtPhone.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validatePhone();
        });
        
        txtAddress.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateAddress();
        });
    }
    
    /**
     * Set the receptionist data to edit
     * @param id The receptionist ID
     * @param name The receptionist name
     * @param email The receptionist email
     * @param gender The receptionist gender
     * @param phone The receptionist phone number
     * @param address The receptionist address
     */
    public void setReceptionistData(String id, String name, String email, String gender, String phone, String address) {
        // Gán dữ liệu lễ tân vào các trường hiển thị
        this.receptionistId = id;
        txtName.setText(name);
        txtEmail.setText(email);
        cmbGender.setValue(gender);
        txtPhone.setText(phone);
        txtAddress.setText(address);
    }
    
    @FXML
    private void updateReceptionist() {
        // Cập nhật thông tin lễ tân nếu các trường hợp lệ
        if (validateAllFields()) {
            saveReceptionist();
        }
    }
    
    @FXML
    private void handleCancel() {
        // Đóng cửa sổ khi nhấn hủy
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
    
    private boolean validateAllFields() {
        // Kiểm tra tất cả các trường nhập liệu
        boolean isValid = validateName();
        isValid = validateEmail() && isValid;
        isValid = validateGender() && isValid;
        isValid = validatePhone() && isValid;
        isValid = validateAddress() && isValid;
        
        return isValid;
    }
    
    private boolean validateName() {
        // Kiểm tra tên
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            showError(lblNameError, "Name is required");
            return false;
        }
        hideError(lblNameError);
        return true;
    }
    
    private boolean validateEmail() {
        // Kiểm tra email
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            showError(lblEmailError, "Email is required");
            return false;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError(lblEmailError, "Invalid email format");
            return false;
        }
        hideError(lblEmailError);
        return true;
    }
    
    private boolean validateGender() {
        // Kiểm tra giới tính
        if (cmbGender.getValue() == null) {
            showError(lblGenderError, "Please select a gender");
            return false;
        }
        hideError(lblGenderError);
        return true;
    }
    
    private boolean validatePhone() {
        // Kiểm tra số điện thoại
        String phone = txtPhone.getText().trim();
        if (phone.isEmpty()) {
            showError(lblPhoneError, "Phone number is required");
            return false;
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            showError(lblPhoneError, "Phone number must contain 10-15 digits only");
            return false;
        }
        hideError(lblPhoneError);
        return true;
    }
    
    private boolean validateAddress() {
        // Kiểm tra địa chỉ
        String address = txtAddress.getText().trim();
        if (address.isEmpty()) {
            showError(lblAddressError, "Address is required");
            return false;
        }
        hideError(lblAddressError);
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
    
    private void saveReceptionist() {
        // Lưu thông tin lễ tân vào cơ sở dữ liệu
        try (Connection conn = Database.connectDB()) {
            // Bắt đầu giao dịch
            conn.setAutoCommit(false);
            
            try {
                // Cập nhật bảng USER_ACCOUNT
                String sqlUser = "UPDATE USER_ACCOUNT SET Email = ?, Name = ?, Gender = ? WHERE Id = ?";
                PreparedStatement psUser = conn.prepareStatement(sqlUser);
                psUser.setString(1, txtEmail.getText().trim());
                psUser.setString(2, txtName.getText().trim());
                psUser.setString(3, cmbGender.getValue());
                psUser.setString(4, receptionistId);
                psUser.executeUpdate();
                
                // Cập nhật bảng RECEPTIONIST
                String sqlReception = "UPDATE RECEPTIONIST SET Phone = ?, Address = ? WHERE Receptionist_id = ?";
                PreparedStatement psReception = conn.prepareStatement(sqlReception);
                psReception.setString(1, txtPhone.getText().trim());
                psReception.setString(2, txtAddress.getText().trim());
                psReception.setString(3, receptionistId);
                psReception.executeUpdate();
                
                // Cam kết giao dịch
                conn.commit();
                
                // Hiển thị thông báo thành công
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Receptionist updated successfully!");
                alert.showAndWait();
                
                // Đóng form
                ((Stage) btnUpdate.getScene().getWindow()).close();
                
            } catch (Exception e) {
                // Hoàn tác giao dịch nếu có lỗi
                conn.rollback();
                
                // Hiển thị thông báo lỗi
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update receptionist: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            // Lỗi kết nối cơ sở dữ liệu
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not connect to database: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}