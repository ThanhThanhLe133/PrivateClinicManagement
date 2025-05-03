package Controller;
import Model.DoctorData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.regex.Pattern;
import DAO.Database;

public class AddDoctorFormController {
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> specializedComboBox;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    
    // Error labels
    @FXML private Label nameError;
    @FXML private Label usernameError;
    @FXML private Label genderError;
    @FXML private Label phoneError;
    @FXML private Label emailError;
    @FXML private Label specializedError;
    @FXML private Label addressError;
    @FXML private Label passwordError;
    @FXML private Label confirmPasswordError;
    
    // Validation patterns
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,15}$");
    private final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    
    @FXML
    private void initialize() {
        // Gender options
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        
        // Specialization options in English
        specializedComboBox.getItems().addAll(
            "General Medicine",
            "Surgery",
            "Pediatrics",
            "Dermatology",
            "Cardiology",
            "Neurology",
            "Orthopedics",
            "Ophthalmology",
            "ENT (Ear, Nose, Throat)",
            "Gynecology",
            "Urology",
            "Psychiatry",
            "Radiology",
            "Oncology",
            "Endocrinology",
            "Gastroenterology",
            "Pulmonology",
            "Nephrology",
            "Hematology",
            "Infectious Disease",
            "Emergency Medicine",
            "Anesthesiology",
            "Family Medicine"
        );
        
        // Add focus listeners for real-time validation
        setupValidationListeners();
    }
    
    private void setupValidationListeners() {
        // Clear errors when fields are edited
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            nameError.setVisible(false);
        });
        
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            usernameError.setVisible(false);
        });
        
        genderComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            genderError.setVisible(false);
        });
        
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            phoneError.setVisible(false);
        });
        
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            emailError.setVisible(false);
        });
        
        specializedComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            specializedError.setVisible(false);
        });
        
        addressField.textProperty().addListener((observable, oldValue, newValue) -> {
            addressError.setVisible(false);
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordError.setVisible(false);
            // Also check confirm password if it's already filled
            if (!confirmPasswordField.getText().isEmpty()) {
                confirmPasswordError.setVisible(!confirmPasswordField.getText().equals(newValue));
            }
        });
        
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            confirmPasswordError.setVisible(!newValue.equals(passwordField.getText()));
        });
    }
    
    @FXML
    private void handleAddDoctor() {
        // Reset all error messages
        hideAllErrors();
        
        // Run full validation
        if (!validateAllFields()) {
            return;
        }
        
        try {
            String id = UUID.randomUUID().toString();
            Connection conn = Database.connectDB();
            
            String sql = "INSERT INTO DOCTOR (Id, Username, Password, Name, Email, Gender, Phone, Specialized, Address, Is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                ps.setString(2, usernameField.getText().trim());
                ps.setString(3, passwordField.getText());
                ps.setString(4, nameField.getText().trim());
                ps.setString(5, emailField.getText().trim());
                ps.setString(6, genderComboBox.getValue());
                ps.setString(7, phoneField.getText().trim());
                ps.setString(8, specializedComboBox.getValue());
                ps.setString(9, addressField.getText().trim());
                ps.setBoolean(10, true);
                
                ps.executeUpdate();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Doctor added successfully!");
                alert.showAndWait();
                
                closeForm();
            }
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = e.getMessage();
            
            // Check for specific database errors
            if (errorMessage.contains("unique") || errorMessage.contains("duplicate")) {
                // Likely a duplicate username error
                usernameError.setText("Username already exists");
                usernameError.setVisible(true);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Database error: " + e.getMessage());
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    private void hideAllErrors() {
        nameError.setVisible(false);
        usernameError.setVisible(false);
        genderError.setVisible(false);
        phoneError.setVisible(false);
        emailError.setVisible(false);
        specializedError.setVisible(false);
        addressError.setVisible(false);
        passwordError.setVisible(false);
        confirmPasswordError.setVisible(false);
    }
    
    private boolean validateAllFields() {
        boolean isValid = true;
        
        // Name validation
        if (nameField.getText().trim().isEmpty()) {
            nameError.setText("Please enter doctor's name");
            nameError.setVisible(true);
            isValid = false;
        }
        
        // Username validation
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            usernameError.setText("Please enter a username");
            usernameError.setVisible(true);
            isValid = false;
        } else if (!USERNAME_PATTERN.matcher(username).matches()) {
            usernameError.setText("Username must be 3-20 characters (letters, numbers, underscore only)");
            usernameError.setVisible(true);
            isValid = false;
        }
        
        // Gender validation
        if (genderComboBox.getValue() == null) {
            genderError.setVisible(true);
            isValid = false;
        }
        
        // Phone validation
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            phoneError.setText("Please enter a phone number");
            phoneError.setVisible(true);
            isValid = false;
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            phoneError.setText("Please enter a valid phone number (10-15 digits)");
            phoneError.setVisible(true);
            isValid = false;
        }
        
        // Email validation
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            emailError.setText("Please enter an email address");
            emailError.setVisible(true);
            isValid = false;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            emailError.setText("Please enter a valid email address");
            emailError.setVisible(true);
            isValid = false;
        }
        
        // Specialization validation
        if (specializedComboBox.getValue() == null) {
            specializedError.setVisible(true);
            isValid = false;
        }
        
        // Address validation
        if (addressField.getText().trim().isEmpty()) {
            addressError.setVisible(true);
            isValid = false;
        }
        
        // Password validation
        String password = passwordField.getText();
        if (password.isEmpty()) {
            passwordError.setText("Please enter a password");
            passwordError.setVisible(true);
            isValid = false;
        } else if (password.length() < 6) {
            passwordError.setText("Password must be at least 6 characters");
            passwordError.setVisible(true);
            isValid = false;
        }
        
        // Confirm password validation
        String confirmPassword = confirmPasswordField.getText();
        if (confirmPassword.isEmpty()) {
            confirmPasswordError.setText("Please confirm your password");
            confirmPasswordError.setVisible(true);
            isValid = false;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordError.setText("Passwords do not match");
            confirmPasswordError.setVisible(true);
            isValid = false;
        }
        
        return isValid;
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to cancel? Any unsaved information will be lost.");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            closeForm();
        }
    }
    
    private void closeForm() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}