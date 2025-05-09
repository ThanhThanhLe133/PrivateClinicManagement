
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

    @FXML private TextField txtServiceName, txtPrice;
    @FXML private Button btnSave, btnCancel;
 // Validation error labels
    @FXML private Label lbServiceNameError, lbPriceError,lbTypeError;
    
	@FXML
	private ComboBox<String> cbType;

    @FXML
    private void initialize() {
    	cbType.setItems(FXCollections.observableArrayList(
    		    Arrays.stream(ServiceType.values())
    		          .map(Enum::name)
    		          .collect(Collectors.toList())
    		));

        txtPrice.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                txtPrice.setText(oldVal);
            }
        });

    }

    @FXML
    private void handleCancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        if (validateAllFields()) {
            saveService();
        }
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        isValid = validateTextField(txtServiceName.getText(), lbServiceNameError, "Service name is required") && isValid;
        
        isValid = validateNumericField(txtPrice, lbPriceError, "Invalid price") && isValid;
        isValid = validateTextField(cbType.getValue(), lbTypeError, "Invalid type") && isValid;


        return isValid;
    }

    private boolean validateTextField(String string, Label errorLabel, String message) {
        if (string.trim().isEmpty()) {
            showError(errorLabel, message);
            return false;
        }
        hideError(errorLabel);
        return true;
    }

    private boolean validateNumericField(TextField field, Label errorLabel, String message) {
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
        label.setText(message);
        label.setVisible(true);
    }

    private void hideError(Label label) {
        label.setVisible(false);
    }

    private void saveService() {
        try (Connection conn = Database.connectDB()) {
            String sql = "INSERT INTO SERVICE (Id, Name, Type, Price) VALUES (UUID(), ?, ?, ?)\r\n"
            		+ "";
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
