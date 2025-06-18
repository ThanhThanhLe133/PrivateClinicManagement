package Controller;

import Model.PrescriptionDrugRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import DAO.Database;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

public class PrescriptionDetailController {

    @FXML private Label lblID;
    @FXML private Label lbPatientName;
    @FXML private Label lblDoctorName;
    @FXML private Label lblTotalAmount;
    @FXML private Label lblDiagnosis;
    @FXML private TextField txtAdvice;
    @FXML private Label lblCreatedDate;
    @FXML private Label lblUpdatedDate;

    @FXML private TableView<PrescriptionDrugRow> prescriptionTable;
    @FXML private TableColumn<PrescriptionDrugRow, String> colDrugId;
    @FXML private TableColumn<PrescriptionDrugRow, String> colDrugName;
    @FXML private TableColumn<PrescriptionDrugRow, Integer> colQuantity;
    @FXML private TableColumn<PrescriptionDrugRow, String> colInstruction;
    @FXML private TableColumn<PrescriptionDrugRow, Void> colAction;

    @FXML private Button prescription_adddrugbtn;

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String diagnose;

    private ObservableList<PrescriptionDrugRow> drugList = FXCollections.observableArrayList();

    public void setAppointmentData(String appointmentId, String patientId, String doctorId) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnose = "";
        loadOrCreatePrescription();
    }

    @FXML
    public void initialize() {
    	// Gán giá trị cho các cột trong bảng        
    	colDrugId.setCellValueFactory(data -> data.getValue().drugIdProperty());
        colDrugName.setCellValueFactory(data -> data.getValue().drugNameProperty());
        colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        colInstruction.setCellValueFactory(data -> data.getValue().instructionsProperty());

        // Thiết lập nút Sửa và Xóa trong cột hành động
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(10, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    PrescriptionDrugRow data = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PrescriptionEditDrugForm.fxml"));
                        if (loader.getLocation() == null) {
                            showAlert("Error: PrescriptionEditDrugForm.fxml not found in /view/ package.");
                            return;
                        }
                        Parent root = loader.load();
                        EditPrescriptionDrugFormController controller = loader.getController();
                        if (controller != null) {
                            controller.setPrescriptionId(lblID.getText());
                            controller.loadDrugData(data.getDrugId());
                        } else {
                            showAlert("Error: EditPrescriptionDrugFormController not initialized.");
                        }
                        Stage stage = new Stage();
                        stage.setTitle("Edit Drug");
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setResizable(false);
                        stage.showAndWait();
                        // Tải lại bảng sau khi chỉnh sửa
                        try (Connection conn = Database.connectDB()) {
                            loadPrescriptionDetails(lblID.getText(), conn);
                            updateTotalAmountLabel(conn, lblID.getText());
                        } catch (SQLException e) {
                            showAlert("Error reloading prescription details: " + e.getMessage());
                        }
                    } catch (IOException e) {
                        showAlert("Error opening Edit Drug form: " + e.getMessage());
                    }
                });

                deleteButton.setOnAction(event -> {
                    PrescriptionDrugRow data = getTableView().getItems().get(getIndex());
                    handleDeleteDrug(data);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        prescriptionTable.setItems(drugList);

        prescription_adddrugbtn.setOnAction(e -> handleAddDrug());
    }

    private void loadOrCreatePrescription() {
        try (Connection conn = Database.connectDB()) {
            String sql = """
                SELECT p.Id, p.diagnose, p.advice, p.TotalAmount,
                       p.Create_date, p.Update_date,
                       pa.Name  AS patient_name,
                       ua.Name  AS doctor_name
                FROM PRESCRIPTION p
                JOIN PATIENT pa     ON p.Patient_id = pa.Patient_id
                JOIN DOCTOR  d      ON p.Doctor_id  = d.Doctor_id
                JOIN USER_ACCOUNT ua ON d.Doctor_id = ua.Id
                WHERE p.Appointment_id = ?
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, appointmentId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    fillForm(rs, conn);
                    return;
                }
            }
            createPrescription(conn);
            loadOrCreatePrescription();
        } catch (SQLException e) {
            showAlert("Error loading or creating prescription: " + e.getMessage());
        }
    }

    private void fillForm(ResultSet rs, Connection conn) throws SQLException {
    	// Điền dữ liệu vào form
    	String prescriptionId = rs.getString("Id");

        lblID.setText(prescriptionId);
        lblDiagnosis.setText(rs.getString("diagnose"));
        txtAdvice.setText(rs.getString("advice"));
        BigDecimal total = rs.getBigDecimal("TotalAmount");
        lblTotalAmount.setText(total != null ? total.toPlainString() : "0");
        lblCreatedDate.setText(rs.getTimestamp("Create_date").toString());
        lblUpdatedDate.setText(rs.getTimestamp("Update_date").toString());

        lbPatientName.setText(rs.getString("patient_name"));
        lblDoctorName.setText(rs.getString("doctor_name"));

        loadPrescriptionDetails(prescriptionId, conn);
    }

    private void createPrescription(Connection conn) throws SQLException {
        String selectsql = "SELECT Patient_id, Doctor_id FROM APPOINTMENT WHERE Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectsql)) {
            ps.setString(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                patientId = rs.getString("Patient_id");
                doctorId = rs.getString("Doctor_id");
            } else {
                throw new SQLException("Cannot find Appointment with ID: " + appointmentId);
            }
        }

        selectsql = "SELECT Diagnosis FROM PATIENT WHERE Patient_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectsql)) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                diagnose = rs.getString("Diagnosis");
            } else {
                throw new SQLException("Cannot find Patient with ID: " + patientId);
            }
        }

        String insert = """
            INSERT INTO PRESCRIPTION
            (Id, Patient_id, Doctor_id, Appointment_id, TotalAmount, diagnose, advice)
            VALUES (?, ?, ?, ?, 0, ?, '')
        """;
        try (PreparedStatement ps = conn.prepareStatement(insert)) {
            ps.setString(1, java.util.UUID.randomUUID().toString());
            ps.setString(2, patientId);
            ps.setString(3, doctorId);
            ps.setString(4, appointmentId);
            ps.setString(5, diagnose);
            ps.executeUpdate();
        }
    }

    private void loadPrescriptionDetails(String prescriptionId, Connection conn) throws SQLException {
        drugList.clear();
        String sql = """
            SELECT pd.drug_id, d.name AS drug_name, pd.quantity, pd.instructions
            FROM PRESCRIPTION_DETAILS pd
            JOIN DRUG d ON pd.drug_id = d.Id
            WHERE pd.prescription_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prescriptionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                drugList.add(new PrescriptionDrugRow(
                        rs.getString("drug_id"),
                        rs.getString("drug_name"),
                        rs.getInt("quantity"),
                        rs.getString("instructions")
                ));
            }
        }
    }

    @FXML
    private void handleAddDrug() {
        try {
            // Load the FXML file as a resource from the classpath
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PrescriptionAddDrugForm.fxml"));
            if (loader.getLocation() == null) {
                showAlert("Error: PrescriptionAddDrugForm.fxml not found in /view/ package.");
                return;
            }
            Parent root = loader.load();

            AddPrescriptionDrugFormController controller = loader.getController();
            if (controller != null) {
                controller.setPrescriptionId(lblID.getText());
            } else {
                showAlert("Error: AddPrescriptionDrugFormController not initialized.");
            }

            Stage stage = new Stage();
            stage.setTitle("Add Drug");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            // Reload prescription details and total amount
            try (Connection conn = Database.connectDB()) {
                loadPrescriptionDetails(lblID.getText(), conn);
                updateTotalAmountLabel(conn, lblID.getText());
            }
        } catch (IOException | SQLException e) {
            showAlert("Error opening Add Drug form: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for debugging
        }
    }

    private void handleDeleteDrug(PrescriptionDrugRow data) {
        // Confirm deletion
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this drug from the prescription?", ButtonType.YES, ButtonType.NO);
        confirmAlert.showAndWait();
        if (confirmAlert.getResult() != ButtonType.YES) {
            return;
        }

        try (Connection conn = Database.connectDB()) {
            // Begin transaction
            conn.setAutoCommit(false);

            // Get the quantity to restore stock
            int quantity = data.getQuantity();
            String drugId = data.getDrugId();
            String prescriptionId = lblID.getText();

            // Delete from PRESCRIPTION_DETAILS
            String sql = "DELETE FROM PRESCRIPTION_DETAILS WHERE Prescription_id = ? AND Drug_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, prescriptionId);
                ps.setString(2, drugId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    showAlert("Failed to delete drug from prescription.");
                    return;
                }
            }

            // Restore stock in DRUG table
            sql = "UPDATE DRUG SET Stock = Stock + ? WHERE Id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, quantity);
                ps.setString(2, drugId);
                ps.executeUpdate();
            }

            // Commit transaction
            conn.commit();

            // Reload table and update total amount
            loadPrescriptionDetails(prescriptionId, conn);
            updateTotalAmountLabel(conn, prescriptionId);
            showAlert("Drug deleted successfully!");
        } catch (SQLException e) {
            try (Connection conn = Database.connectDB()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            showAlert("Error deleting drug: " + e.getMessage());
        } finally {
            try (Connection conn = Database.connectDB()) {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSave() {
        try (Connection conn = Database.connectDB()) {
            // Update advice in PRESCRIPTION table
            String updateSql = """
                UPDATE PRESCRIPTION
                SET advice = ?, Update_date = CURRENT_TIMESTAMP
                WHERE Id = ?
            """;
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setString(1, txtAdvice.getText().trim());
                ps.setString(2, lblID.getText());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert("Prescription updated successfully!");
                } else {
                    showAlert("Failed to update prescription.");
                }
            }

            // Update total amount
            updateTotalAmountLabel(conn, lblID.getText());

        } catch (SQLException e) {
            showAlert("Error saving prescription: " + e.getMessage());
        }
    }

    private void updateTotalAmountLabel(Connection conn, String prescriptionId) throws SQLException {
        // Calculate total quantity (sum of quantities)
        String sql = """
            SELECT SUM(pd.Quantity) as Total
            FROM PRESCRIPTION_DETAILS pd
            WHERE pd.Prescription_id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prescriptionId);
            ResultSet rs = ps.executeQuery();
            BigDecimal total = BigDecimal.ZERO;
            if (rs.next()) {
                total = rs.getBigDecimal("Total");
                if (total == null) total = BigDecimal.ZERO;
            }

            // Update TotalAmount in PRESCRIPTION table
            String updateSql = "UPDATE PRESCRIPTION SET TotalAmount = ? WHERE Id = ?";
            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setBigDecimal(1, total);
                updatePs.setString(2, prescriptionId);
                updatePs.executeUpdate();
            }

            lblTotalAmount.setText(total.toPlainString());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) lblID.getScene().getWindow();
        stage.close();
    }
}