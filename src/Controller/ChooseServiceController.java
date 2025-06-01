package Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import DAO.Database;
import Model.DoctorData;
import Model.ServiceData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ChooseServiceController implements Initializable {
@FXML
private ComboBox<ServiceData> cb_service;


@FXML
private ComboBox<DoctorData> cb_doctor;

@SuppressWarnings("rawtypes")
@FXML
private Spinner<Integer> spHour, spMinute;
@FXML
private DatePicker select_time;

@FXML
private Button btn_remove;


private Connection connect;
private PreparedStatement prepare;
private ResultSet result;
private Statement statement;

private void loadComboBoxService() {
	ObservableList<ServiceData> serviceList = FXCollections.observableArrayList();

	String sql = "SELECT * FROM service";

	try {
		connect = Database.connectDB();
		prepare = connect.prepareStatement(sql);
		ResultSet rs = prepare.executeQuery();

		while (rs.next()) {
			serviceList.add(new ServiceData(rs.getString("id"), rs.getString("name"), rs.getBigDecimal("price"),
					rs.getString("type")));
		}

		cb_service.setItems(serviceList);
		cb_service.getSelectionModel().selectFirst();

	} catch (SQLException e) {
		e.printStackTrace();
	}
}

public void loadComboBoxDoctor(String serviceId, ComboBox<DoctorData> cbDoctor) {
	ObservableList<DoctorData> doctorList = FXCollections.observableArrayList();

	String sql = "SELECT d.Doctor_id, d.Phone, d.Service_id, d.Address, d.Is_confirmed, " +
            "u.Username, u.Password, u.Email, u.Name, u.Gender, u.Is_active, " +
            "s.Name AS ServiceName " +
            "FROM DOCTOR d " +
            "JOIN USER_ACCOUNT u ON d.Doctor_id = u.Id " +
            "LEFT JOIN SERVICE s ON d.Service_id = s.Id " +
            "WHERE d.Service_id = ?";

	try {
		connect = Database.connectDB();
		prepare = connect.prepareStatement(sql);
		prepare.setString(1, serviceId);
		ResultSet rs = prepare.executeQuery();

		 while (rs.next()) {
	            doctorList.add(new DoctorData(
	                rs.getString("Doctor_id"),
	                rs.getString("Username"),
	                rs.getString("Password"),
	                rs.getString("Name"),
	                rs.getString("Email"),
	                rs.getString("Gender"),
	                rs.getBoolean("Is_active"),
	                rs.getString("Phone"),
	                rs.getString("ServiceName"),
	                rs.getString("Address"),
	                rs.getBoolean("Is_confirmed")
	            ));
	        }

		cbDoctor.setItems(doctorList);
		cbDoctor.getSelectionModel().selectFirst();

	} catch (SQLException e) {
		e.printStackTrace();
	}
}

@FXML
private void removeForm(ActionEvent event) {
	Button btn = (Button) event.getSource();
	AnchorPane pane = (AnchorPane) btn.getParent();

	VBox vboxContainer = (VBox) pane.getParent();
	
//	if (vboxContainer.getChildren().size() <= 1) {
//		return;
//	}
	
	vboxContainer.getChildren().remove(pane);
}

@FXML
private void chooseService(ActionEvent event) throws IOException {
	ComboBox<ServiceData> cbService = (ComboBox<ServiceData>) event.getSource();
	AnchorPane currentPane = (AnchorPane) cbService.getParent();
	Label lbPrice = (Label) currentPane.lookup("#lb_price_service");

	ComboBox<DoctorData> cbDoctor = (ComboBox<DoctorData>) currentPane.lookup("#cb_doctor");

	ServiceData selectedService = cb_service.getSelectionModel().getSelectedItem();

	if (selectedService != null) {
		String sql = "SELECT * FROM service WHERE id = ?";
		try (Connection connect = Database.connectDB(); PreparedStatement ps = connect.prepareStatement(sql)) {

			ps.setString(1, selectedService.getServiceId());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				lbPrice.setText(formatCurrencyVND(rs.getBigDecimal("price")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		loadComboBoxDoctor(selectedService.getServiceId(), cbDoctor);
	} else {
		lbPrice.setText("");
	}

}

@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	// TODO Auto-generated method stub
	loadComboBoxService();
	SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 17, 8);
	spHour.setValueFactory(hourFactory);

	SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, 0, 15);
	spMinute.setValueFactory(minuteFactory);
}
public static String formatCurrencyVND(BigDecimal amount) {
	if (amount == null)
		return "0 VNĐ";
	NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
	return formatter.format(amount) + " VNĐ";
}

}