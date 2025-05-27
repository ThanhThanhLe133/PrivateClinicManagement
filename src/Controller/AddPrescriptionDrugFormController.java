package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import DAO.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AddPrescriptionDrugFormController {

    @FXML private ComboBox<String> cmbDrugName;
    @FXML private ComboBox<String> cmbManufacturer;
    @FXML private TextField txtQuantity;
    @FXML private TextArea txtInstruction;
    @FXML private Button btnSave, btnCancel;

    @FXML private Label lblDrugNameError, lblManufacturerError, lblQuantityError, lblInstructionError;
    
    private String prescriptionId;

    public void setPrescriptionId(String id) {
        this.prescriptionId = id;
    }

    @FXML
    private void initialize() {
        // Load drugs into cmbDrugName
        loadDrugs();

        // Restrict txtQuantity to positive integers
        if (txtQuantity != null) {
            txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) {
                    txtQuantity.setText(oldVal);
                }
            });
        }

        // Load manufacturers when a drug is selected
        if (cmbDrugName != null) {
            cmbDrugName.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    loadManufacturers(newVal);
                } else {
                    if (cmbManufacturer != null) {
                        cmbManufacturer.getItems().clear();
                        cmbManufacturer.setPromptText("Select Manufacturer");
                    }
                }
            });
        }
    }

    private void loadDrugs() {
        if (cmbDrugName == null) return;
        try (Connection conn = Database.connectDB()) {
            String sql = """
                SELECT Name 
                FROM DRUG 
                WHERE Stock > 0 
                AND Id NOT IN (
                    SELECT Drug_id 
                    FROM PRESCRIPTION_DETAILS 
                    WHERE Prescription_id = ?
                )
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, prescriptionId);
                ResultSet rs = ps.executeQuery();
                Set<String> drugNames = new HashSet<>(); // Use Set to avoid duplicates
                while (rs.next()) {
                    drugNames.add(rs.getString("Name"));
                }
                cmbDrugName.getItems().addAll(drugNames);
            }
        } catch (SQLException e) {
            showError("Failed to load drugs: " + e.getMessage());
        }
    }

    private void loadManufacturers(String drugName) {
        if (cmbManufacturer == null) return;
        cmbManufacturer.getItems().clear();
        try (Connection conn = Database.connectDB()) {
            String sql = "SELECT Manufacturer FROM DRUG WHERE Name = ? AND Stock > 0";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, drugName);
                ResultSet rs = ps.executeQuery();
                Set<String> manufacturers = new HashSet<>();
                while (rs.next()) {
                    String manufacturer = rs.getString("Manufacturer");
                    if (manufacturer != null && !manufacturer.trim().isEmpty()) {
                        manufacturers.add(manufacturer);
                    }
                }
                if (manufacturers.isEmpty()) {
                    cmbManufacturer.getItems().add("No Manufacturer Available");
                    cmbManufacturer.setDisable(true);
                } else {
                    cmbManufacturer.getItems().addAll(manufacturers);
                    cmbManufacturer.setDisable(false);
                }
            }
        } catch (SQLException e) {
            showError("Failed to load manufacturers: " + e.getMessage());
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
        if (btnCancel != null) {
            ((Stage) btnCancel.getScene().getWindow()).close();
        }
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        // Validate Drug Name
        if (cmbDrugName != null && (cmbDrugName.getValue() == null || cmbDrugName.getValue().trim().isEmpty())) {
            showError(lblDrugNameError, "Drug name is required");
            isValid = false;
        } else {
            hideError(lblDrugNameError);
        }

        // Validate Manufacturer
        if (cmbManufacturer != null && (cmbManufacturer.getValue() == null || cmbManufacturer.getValue().trim().isEmpty() || 
                cmbManufacturer.getValue().equals("No Manufacturer Available"))) {
            showError(lblManufacturerError, "Manufacturer is required");
            isValid = false;
        } else {
            hideError(lblManufacturerError);
        }

        // Validate Quantity
        if (txtQuantity != null) {
            try {
                int value = Integer.parseInt(txtQuantity.getText().trim());
                if (value <= 0) {
                    showError(lblQuantityError, "Quantity must be greater than 0");
                    isValid = false;
                } else {
                    hideError(lblQuantityError);
                }
            } catch (NumberFormatException e) {
                showError(lblQuantityError, "Invalid quantity");
                isValid = false;
            }
        } else {
            showError(lblQuantityError, "Quantity field is missing");
            isValid = false;
        }

        // Validate Instructions
        if (txtInstruction != null && txtInstruction.getText().trim().isEmpty()) {
            showError(lblInstructionError, "Instructions are required");
            isValid = false;
        } else {
            hideError(lblInstructionError);
        }

        return isValid;
    }

    private void saveDrug() {
        try (Connection conn = Database.connectDB()) {
            // Begin transaction for consistency
            conn.setAutoCommit(false);

            // Find the drug ID based on selected name and manufacturer
            String sql = "SELECT Id FROM DRUG WHERE Name = ? AND Manufacturer = ? AND Stock > 0 LIMIT 1";
            String drugId = null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cmbDrugName.getValue());
                ps.setString(2, cmbManufacturer.getValue());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    drugId = rs.getString("Id");
                }
            }

            if (drugId == null) {
                showError("Drug not found in database.");
                conn.rollback();
                return;
            }

            // Check if the (Prescription_id, Drug_id) pair already exists
            sql = "SELECT COUNT(*) FROM PRESCRIPTION_DETAILS WHERE Prescription_id = ? AND Drug_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, prescriptionId);
                ps.setString(2, drugId);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                	showError(lblDrugNameError, "This drug is already associated with the prescription.");
					lblDrugNameError.setVisible(true);
					conn.rollback();
					return;
				} else {
					hideError(lblDrugNameError);
                }
            }

            // Check if quantity exceeds stock
            int availableStock = 0;
            sql = "SELECT Stock FROM DRUG WHERE Id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, drugId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    availableStock = rs.getInt("Stock");
                }
            }

            int requestedQuantity = Integer.parseInt(txtQuantity.getText().trim());
            if (requestedQuantity > availableStock) {
                showError(lblQuantityError, "Requested quantity exceeds available stock.");
                conn.rollback();
                return;
            }

            // Insert into PRESCRIPTION_DETAILS
            sql = """
                INSERT INTO PRESCRIPTION_DETAILS (Prescription_id, Drug_id, Quantity, Instructions)
                VALUES (?, ?, ?, ?)
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, prescriptionId);
                ps.setString(2, drugId);
                ps.setInt(3, requestedQuantity);
                ps.setString(4, txtInstruction.getText().trim());

                ps.executeUpdate();

                // Update stock in DRUG table
                sql = "UPDATE DRUG SET Stock = Stock - ? WHERE Id = ?";
                try (PreparedStatement psUpdate = conn.prepareStatement(sql)) {
                    psUpdate.setInt(1, requestedQuantity);
                    psUpdate.setString(2, drugId);
                    psUpdate.executeUpdate();
                }

                conn.commit();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Drug added to prescription successfully!");
                alert.showAndWait();

                if (btnSave != null) {
                    ((Stage) btnSave.getScene().getWindow()).close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = Database.connectDB()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add drug to prescription: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try (Connection conn = Database.connectDB()) {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void showError(Label label, String message) {
        if (label != null) {
            label.setText(message);
            label.setVisible(true);
        }
    }

    private void hideError(Label label) {
        if (label != null) {
            label.setVisible(false);
        }
    }
}