//package Controller;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.stage.Stage;
//import DAO.Database;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.util.UUID;
//
//public class AddReceptionistFormController {
//    @FXML private TextField txtUsername, txtPassword, txtName, txtEmail, txtPhone, txtAddress;
//    @FXML private ComboBox<String> cmbGender;
//    @FXML private Button btnSave;
//
//    @FXML
//    private void initialize() {
//        cmbGender.getItems().addAll("Nam", "Nữ", "Khác");
//    }
//
//    @FXML
//    private void addReceptionist() {
//        String id = UUID.randomUUID().toString();
//        try (Connection conn = Database.connectDB()) {
//            String sqlUser = "INSERT INTO USER_ACCOUNT (Id, Username, Password, Email, Name, Gender, Role, Is_active) VALUES (?, ?, ?, ?, ?, ?, 'RECEPTIONIST', TRUE)";
//            PreparedStatement psUser = conn.prepareStatement(sqlUser);
//            psUser.setString(1, id);
//            psUser.setString(2, txtUsername.getText());
//            psUser.setString(3, txtPassword.getText());
//            psUser.setString(4, txtEmail.getText());
//            psUser.setString(5, txtName.getText());
//            psUser.setString(6, cmbGender.getValue());
//            psUser.executeUpdate();
//
//            String sqlReception = "INSERT INTO RECEPTIONIST (Receptionist_id, Phone, Address) VALUES (?, ?, ?)";
//            PreparedStatement psReception = conn.prepareStatement(sqlReception);
//            psReception.setString(1, id);
//            psReception.setString(2, txtPhone.getText());
//            psReception.setString(3, txtAddress.getText());
//            psReception.executeUpdate();
//
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Thành công");
//            alert.setHeaderText(null);
//            alert.setContentText("Đã thêm lễ tân thành công!");
//            alert.showAndWait();
//            ((Stage) btnSave.getScene().getWindow()).close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}


package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import DAO.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;
import java.util.regex.Pattern;

public class AddReceptionistFormController {
    @FXML private TextField txtUsername, txtName, txtEmail, txtPhone, txtAddress;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbGender;
    @FXML private Button btnSave, btnCancel;
    
    // Validation error labels
    @FXML private Label lblUsernameError, lblPasswordError, lblNameError, lblEmailError,
                     lblGenderError, lblPhoneError, lblAddressError;
    
    // Regex patterns for validation
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    
    @FXML
    private void initialize() {
        // Initialize gender options
        cmbGender.getItems().addAll("Male", "Female", "Other");
        
        // Add focus listeners for real-time validation
        txtUsername.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateUsername();
        });
        
        txtPassword.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validatePassword();
        });
        
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
    
    @FXML
    private void handleSave() {
        if (validateAllFields()) {
            saveReceptionist();
        }
    }
    
    @FXML
    private void handleCancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
    
    private boolean validateAllFields() {
        boolean isValid = validateUsername();
        isValid = validatePassword() && isValid;
        isValid = validateName() && isValid;
        isValid = validateEmail() && isValid;
        isValid = validateGender() && isValid;
        isValid = validatePhone() && isValid;
        isValid = validateAddress() && isValid;
        
        return isValid;
    }
    
    private boolean validateUsername() {
        String username = txtUsername.getText().trim();
        if (username.isEmpty()) {
            showError(lblUsernameError, "Username is required");
            return false;
        } else if (username.length() < 4) {
            showError(lblUsernameError, "Username must be at least 4 characters");
            return false;
        }
        hideError(lblUsernameError);
        return true;
    }
    
    private boolean validatePassword() {
        String password = txtPassword.getText();
        if (password.isEmpty()) {
            showError(lblPasswordError, "Password is required");
            return false;
        } else if (password.length() < 6) {
            showError(lblPasswordError, "Password must be at least 6 characters");
            return false;
        }
        hideError(lblPasswordError);
        return true;
    }
    
    private boolean validateName() {
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            showError(lblNameError, "Name is required");
            return false;
        }
        hideError(lblNameError);
        return true;
    }
    
    private boolean validateEmail() {
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
        if (cmbGender.getValue() == null) {
            showError(lblGenderError, "Please select a gender");
            return false;
        }
        hideError(lblGenderError);
        return true;
    }
    
    private boolean validatePhone() {
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
        String address = txtAddress.getText().trim();
        if (address.isEmpty()) {
            showError(lblAddressError, "Address is required");
            return false;
        }
        hideError(lblAddressError);
        return true;
    }
    
    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }
    
    private void hideError(Label label) {
        label.setVisible(false);
    }
    
    private void saveReceptionist() {
        String id = UUID.randomUUID().toString();
        
        try (Connection conn = Database.connectDB()) {
            // Begin transaction
            conn.setAutoCommit(false);
            
            try {
                // Insert into USER_ACCOUNT table
                String sqlUser = "INSERT INTO USER_ACCOUNT (Id, Username, Password, Email, Name, Gender, Role, Is_active) " +
                                "VALUES (?, ?, ?, ?, ?, ?, 'RECEPTIONIST', TRUE)";
                PreparedStatement psUser = conn.prepareStatement(sqlUser);
                psUser.setString(1, id);
                psUser.setString(2, txtUsername.getText().trim());
                psUser.setString(3, txtPassword.getText());
                psUser.setString(4, txtEmail.getText().trim());
                psUser.setString(5, txtName.getText().trim());
                psUser.setString(6, cmbGender.getValue());
                psUser.executeUpdate();
                
                // Insert into RECEPTIONIST table
                String sqlReception = "INSERT INTO RECEPTIONIST (Receptionist_id, Phone, Address) " +
                                    "VALUES (?, ?, ?)";
                PreparedStatement psReception = conn.prepareStatement(sqlReception);
                psReception.setString(1, id);
                psReception.setString(2, txtPhone.getText().trim());
                psReception.setString(3, txtAddress.getText().trim());
                psReception.executeUpdate();
                
                // Commit transaction
                conn.commit();
                
                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Receptionist added successfully!");
                alert.showAndWait();
                
                // Close the form
                ((Stage) btnSave.getScene().getWindow()).close();
                
            } catch (Exception e) {
                // Rollback transaction in case of error
                conn.rollback();
                
                // Show error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to add receptionist: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            // Connection error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not connect to database: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
