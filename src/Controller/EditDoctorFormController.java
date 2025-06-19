package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.DoctorData;
import DAO.Database;
import Enum.Gender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Alert.AlertMessage;

public class EditDoctorFormController {

    @FXML private TextField txtUsername, txtName, txtEmail, txtPhone, txtAddress;
    @FXML private ComboBox<String> cmbGender, cmbSpecialization;
    @FXML private Button btnSave, btnCancel;
    private final AlertMessage alert = new AlertMessage();
    private DoctorData doctor;

    public void setDoctorData(DoctorData doctor) {
        this.doctor = doctor;
        txtName.setText(doctor.getName());
        txtEmail.setText(doctor.getEmail());
        cmbGender.setValue(doctor.getGender());
        txtPhone.setText(doctor.getPhone());
        cmbSpecialization.setValue(doctor.getServiceName());
        txtAddress.setText(doctor.getAddress());
    }

    @FXML
    private void initialize() {
        loadComboBox();
    }

    private void loadComboBox() {
        cmbGender.setItems(FXCollections.observableArrayList(
            Arrays.stream(Gender.values()).map(Enum::name).collect(Collectors.toList())
        ));

        ObservableList<String> specializations = FXCollections.observableArrayList();
        try (Connection conn = Database.connectDB();
             PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT Name FROM SERVICE");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                specializations.add(rs.getString("Name"));
            }
            cmbSpecialization.setItems(specializations);
        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Error loading specializations: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void handleSave() {
        if (!validateAllFields()) {
            return;
        }

        Connection conn = null;
        try {
            conn = Database.connectDB();
            conn.setAutoCommit(false);

            // Check for associated appointments
            String checkAppointmentsSql = "SELECT Id, Prescription_status FROM APPOINTMENT WHERE Doctor_id = ?";
            PreparedStatement psCheckAppointments = conn.prepareStatement(checkAppointmentsSql);
            psCheckAppointments.setString(1, doctor.getId());
            ResultSet rsAppointments = psCheckAppointments.executeQuery();
            List<String> appointmentIds = new ArrayList<>();
            boolean hasAppointments = false;
            boolean hasPaidPrescriptions = false;

            while (rsAppointments.next()) {
                hasAppointments = true;
                appointmentIds.add(rsAppointments.getString("Id"));
                if ("Paid".equals(rsAppointments.getString("Prescription_status"))) {
                    hasPaidPrescriptions = true;
                }
            }

            // Show warning if doctor has appointments
            if (hasAppointments) {
                String warningMessage = "This doctor has existing appointments. Updating will:\n" +
                        "- Update related appointments and prescriptions.\n" +
                        "- Update appointment services if specialization changes.\n" +
                        "Proceed with the update?";
                Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
                warning.setTitle("Confirm Update");
                warning.setHeaderText("Doctor Has Appointments");
                warning.setContentText(warningMessage);
                if (warning.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                    conn.rollback();
                    return;
                }
            }

            // Get new Service ID for selected specialization
            String getNewServiceIdSql = "SELECT Id FROM SERVICE WHERE Name = ?";
            PreparedStatement psNewService = conn.prepareStatement(getNewServiceIdSql);
            psNewService.setString(1, cmbSpecialization.getValue());
            ResultSet rsNewService = psNewService.executeQuery();
            if (!rsNewService.next()) {
                conn.rollback();
                alert.errorMessage("Selected specialization not found.");
                return;
            }
            String newServiceId = rsNewService.getString("Id");

            // Get current Service ID to check if specialization changed
            String getCurrentServiceIdSql = "SELECT s.Id FROM SERVICE s JOIN DOCTOR d ON s.Id = d.Service_id WHERE d.Doctor_id = ?";
            PreparedStatement psCurrentService = conn.prepareStatement(getCurrentServiceIdSql);
            psCurrentService.setString(1, doctor.getId());
            ResultSet rsCurrentService = psCurrentService.executeQuery();
            String currentServiceId = null;
            if (rsCurrentService.next()) {
                currentServiceId = rsCurrentService.getString("Id");
            }

            // Update USER_ACCOUNT
            String sqlUser = "UPDATE USER_ACCOUNT SET Email = ?, Name = ?, Gender = ? WHERE Id = ?";
            PreparedStatement psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, txtEmail.getText().trim());
            psUser.setString(2, txtName.getText().trim());
            psUser.setString(3, cmbGender.getValue());
            psUser.setString(4, doctor.getId());
            psUser.executeUpdate();

            // Update DOCTOR
            String sqlDoctor = "UPDATE DOCTOR SET Phone = ?, Address = ?, Service_id = ? WHERE Doctor_id = ?";
            PreparedStatement psDoctor = conn.prepareStatement(sqlDoctor);
            psDoctor.setString(1, txtPhone.getText().trim());
            psDoctor.setString(2, txtAddress.getText().trim());
            psDoctor.setString(3, newServiceId);
            psDoctor.setString(4, doctor.getId());
            psDoctor.executeUpdate();
            
//            if (hasPaidPrescriptions) {
//                String updateAppointmentSql = "UPDATE APPOINTMENT SET Prescription_status = 'Created', Update_date = ? WHERE Id = ? AND Prescription_status = 'Paid'";
//                PreparedStatement psUpdateAppointment = conn.prepareStatement(updateAppointmentSql);
//                Timestamp now = Timestamp.valueOf(LocalDateTime.now());
//
//                for (String appointmentId : appointmentIds) {
//                    psUpdateAppointment.setTimestamp(1, now);
//                    psUpdateAppointment.setString(2, appointmentId);
//                    psUpdateAppointment.executeUpdate();
//                }
//            }

            // Update related tables if specialization changed
            if (currentServiceId != null && !newServiceId.equals(currentServiceId)) {
                String updateAppointmentServiceSql = "UPDATE APPOINTMENT_SERVICE SET Service_id = ? WHERE Appointment_id = ?";
                PreparedStatement psUpdateAppointmentService = conn.prepareStatement(updateAppointmentServiceSql);

                String updateAppointmentSql = "UPDATE APPOINTMENT SET Prescription_status = CASE WHEN Prescription_status = 'Paid' THEN 'Created' ELSE Prescription_status END, Update_date = ? WHERE Id = ?";
                PreparedStatement psUpdateAppointment = conn.prepareStatement(updateAppointmentSql);

                String updatePrescriptionSql = "UPDATE PRESCRIPTION SET Doctor_id = ?, Update_date = ? WHERE Appointment_id = ?";
                PreparedStatement psUpdatePrescription = conn.prepareStatement(updatePrescriptionSql);

                Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                for (String appointmentId : appointmentIds) {
                    // Update APPOINTMENT_SERVICE
                    psUpdateAppointmentService.setString(1, newServiceId);
                    psUpdateAppointmentService.setString(2, appointmentId);
                    psUpdateAppointmentService.executeUpdate();

                    // Update APPOINTMENT
                    psUpdateAppointment.setTimestamp(1, now);
                    psUpdateAppointment.setString(2, appointmentId);
                    psUpdateAppointment.executeUpdate();

                    // Update PRESCRIPTION
                    psUpdatePrescription.setString(1, doctor.getId());
                    psUpdatePrescription.setTimestamp(2, now);
                    psUpdatePrescription.setString(3, appointmentId);
                    psUpdatePrescription.executeUpdate();
                }
            }

            conn.commit();
            alert.successMessage("Doctor updated successfully!");
            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            alert.errorMessage("Error updating doctor: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean validateAllFields() {
        if (txtName.getText().trim().isEmpty()) {
            alert.errorMessage("Name is required");
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            alert.errorMessage("Email is required");
            return false;
        }
        if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            alert.errorMessage("Invalid email format");
            return false;
        }
        if (cmbGender.getValue() == null || cmbGender.getValue().trim().isEmpty()) {
            alert.errorMessage("Gender is required");
            return false;
        }
        if (txtPhone.getText().trim().isEmpty()) {
            alert.errorMessage("Phone is required");
            return false;
        }
        if (txtAddress.getText().trim().isEmpty()) {
            alert.errorMessage("Address is required");
            return false;
        }
        if (cmbSpecialization.getValue() == null || cmbSpecialization.getValue().trim().isEmpty()) {
            alert.errorMessage("Specialization is required");
            return false;
        }
        return true;
    }
}