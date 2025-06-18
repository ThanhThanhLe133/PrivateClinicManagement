package Controller;

import DAO.Database;
import Model.PatientData;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class PatientDetailController {

    @FXML private Label lblName, lblEmail, lblGender, lblPhone, lblDiagnosis, lblHeight, lblWeight, lblAddress;
    @FXML private TableView<AppointmentRow> appointmentTable;
    @FXML private TableColumn<AppointmentRow, String> colTime, colStatus, colReason;
    @FXML private TableColumn<AppointmentRow, String> colDoctor;
    
    private String patientId;

    public void setPatientData(PatientData patient) {
    	// Gán dữ liệu bệnh nhân vào các nhãn hiển thị
    	this.patientId = patient.getPatientId();

        lblName.setText(patient.getName());
        lblEmail.setText(patient.getEmail());
        lblGender.setText(patient.getGender());
        lblPhone.setText(patient.getPhone());
        lblDiagnosis.setText(patient.getDiagnosis());
        lblHeight.setText(String.valueOf(patient.getHeight()));
        lblWeight.setText(String.valueOf(patient.getWeight()));
        lblAddress.setText(patient.getAddress());

        // Tải lịch sử cuộc hẹn
        loadAppointmentHistory();
    }

    private void loadAppointmentHistory() {
        ObservableList<AppointmentRow> history = FXCollections.observableArrayList();

        try (Connection conn = Database.connectDB()) {
            String sql = """
                SELECT a.Time, a.Status, a.Cancel_reason, u.Name AS DoctorName
                FROM APPOINTMENT a
                JOIN DOCTOR d ON a.Doctor_id = d.Doctor_id
                JOIN USER_ACCOUNT u ON d.Doctor_id = u.Id
                WHERE a.Patient_id = ?
                ORDER BY a.Time DESC
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String time = rs.getTimestamp("Time").toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String status = rs.getString("Status");
                String reason = rs.getString("Cancel_reason") == null ? "" : rs.getString("Cancel_reason");
                String doctorName = rs.getString("DoctorName");

                history.add(new AppointmentRow(time, status, reason, doctorName));
            }

            colTime.setCellValueFactory(data -> data.getValue().timeProperty());
            colDoctor.setCellValueFactory(data -> data.getValue().doctorNameProperty());
            colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
            colReason.setCellValueFactory(data -> data.getValue().reasonProperty());

            appointmentTable.setItems(history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class AppointmentRow {
        private final StringProperty time, status, reason, doctorName;

        public AppointmentRow(String time, String status, String reason, String doctorName) {
            this.time = new SimpleStringProperty(time);
            this.status = new SimpleStringProperty(status);
            this.reason = new SimpleStringProperty(reason);
            this.doctorName = new SimpleStringProperty(doctorName);
        }

        public StringProperty doctorNameProperty() { return doctorName; }
        public StringProperty timeProperty() { return time; }
        public StringProperty statusProperty() { return status; }
        public StringProperty reasonProperty() { return reason; }
    }
}