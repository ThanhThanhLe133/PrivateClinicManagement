package Controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import Alert.AlertMessage;
import Model.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class AdminMainFormController {

	@FXML
	private AnchorPane main_form;

	// Top panel
	@FXML
	private Label top_username, current_form, date_time;

	@FXML
	private Circle top_profile;

	@FXML
	private Button logout_btn;

	// Left panel
	@FXML
	private Label nav_adminID, nav_username;

	@FXML
	private Button dashboard_btn, doctors_btn, receptionist_btn, salary_btn, revenue_btn, profile_btn;

	// Forms
	@FXML
	private AnchorPane dashboard_form, doctors_form, receptionist_form, salary_form, profile_form;
	@FXML
	private AnchorPane revenue_form;
	@FXML
	private Circle profile_circle;

	// Salary Form
	@FXML
	private TableView<TableRow> salary_tableView;
	@FXML
	private TableColumn<TableRow, String> salary_col_ID;
	@FXML
	private TableColumn<TableRow, String> salary_col_name;
	@FXML
	private TableColumn<TableRow, String> salary_col_role;
	@FXML
	private TableColumn<TableRow, String> salary_col_totalsalary;
	@FXML
	private TableColumn<TableRow, String> salary_col_salary;

	@FXML
	private Button salary_paySalaryBtn;
	@FXML
    private Button profile_importBtn;

	@FXML
	private ComboBox<String> filterTypeComboBox;
	@FXML
	private DatePicker filterDatePicker;
	@FXML
	private Label totalRevenueLabel, totalCostLabel, netProfitLabel;

//  DATABASE TOOLS
	private Connection connect;
	private PreparedStatement prepare;
	private Statement statement;
	private ResultSet result;

	private AlertMessage alert = new AlertMessage();

	private Image image;

	@FXML
	public void initialize() {
		showForm("dashboard");

		// Nếu salary_tableView tồn tại thì load dữ liệu mẫu
		if (salary_tableView != null) {
			loadSampleSalaryData();
		}
	}

	@FXML
	private void logoutBtn() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Login");
			stage.show();

			logout_btn.getScene().getWindow().hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void switchForm(ActionEvent event) {

		if (event.getSource() == dashboard_btn) {
			showForm("dashboard");
		} else if (event.getSource() == doctors_btn) {
			showForm("doctors");
		} else if (event.getSource() == receptionist_btn) {
			showForm("receptionist");
		} else if (event.getSource() == salary_btn) {
			showForm("salary");
		} else if (event.getSource() == profile_btn) {
			showForm("profile");
		} else if (event.getSource() == revenue_btn) {
			showForm("revenue");
		}

	}

	private void showForm(String formName) {
		dashboard_form.setVisible(false);
		doctors_form.setVisible(false);
		receptionist_form.setVisible(false);
		salary_form.setVisible(false);
		profile_form.setVisible(false);
		revenue_form.setVisible(false);

		switch (formName) {
		case "dashboard":
			dashboard_form.setVisible(true);
			current_form.setText("Dashboard Form");
			break;
		case "doctors":
			doctors_form.setVisible(true);
			current_form.setText("Doctors Form");
			break;
		case "receptionist":
			receptionist_form.setVisible(true);
			current_form.setText("Receptionists Form");
			break;
		case "salary":
			salary_form.setVisible(true);
			current_form.setText("Salary Payment Form");
			break;

		case "profile":
			profile_form.setVisible(true);
			current_form.setText("Profile Form");
			break;
		case "revenue":
			revenue_form.setVisible(true);
			current_form.setText("Clinic Revenue Form");
			break;

		}
	}

	@FXML
	private void paySalaryAction() {
		TableRow selectedRow = salary_tableView.getSelectionModel().getSelectedItem();
		if (selectedRow != null) {
			System.out.println("Paying salary to: " + selectedRow.getName() + " (" + selectedRow.getRole() + ")");
		} else {
			System.out.println("Please select an employee to pay salary!");
		}
	}

	private void loadSampleSalaryData() {
		ObservableList<TableRow> data = FXCollections.observableArrayList(
				new TableRow("1", "Dr. John", "Doctor", "5000$", "5000$"),
				new TableRow("2", "Ms. Anna", "Receptionist", "3000$", "3000$"));
		salary_tableView.setItems(data);
	}

	// Simple TableRow class nội bộ (không cần model file riêng)
	public static class TableRow {
		private final String id;
		private final String name;
		private final String role;
		private final String totalsalary;
		private final String salary;

		public TableRow(String id, String name, String role, String totalsalary, String salary) {
			this.id = id;
			this.name = name;
			this.role = role;
			this.totalsalary = totalsalary;
			this.salary = salary;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getRole() {
			return role;
		}

		public String getTotalsalary() {
			return totalsalary;
		}

		public String getSalary() {
			return salary;
		}
	}

	@FXML
	private void paySelectedEmployeeSalary(ActionEvent event) {
		System.out.println("Đã trả lương cho nhân viên được chọn!");
	}

	@FXML
	private void profileInsertImage(ActionEvent event) {
		FileChooser open = new FileChooser();
		open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*jpg", "*jpeg", "*png"));

		File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());

		if (file != null) {
			Data.path = file.getAbsolutePath();

			image = new Image(file.toURI().toString(), 137, 95, false, true);
			profile_circle.setFill(new ImagePattern(image));
		}
	}

	@FXML
	private void profileUpdateBtn(ActionEvent event) {
		System.out.println("Cập nhật thông tin hồ sơ nhân viên...");
	}

	@FXML
	private void handleCalculateRevenue() {
		String type = filterTypeComboBox.getValue();
		if (type == null || filterDatePicker.getValue() == null) {
			System.out.println("Please select filter type and date!");
			return;
		}

		double totalRevenue = 12000.0;
		double totalCost = 5000.0;
		double netProfit = totalRevenue - totalCost;

		totalRevenueLabel.setText("$" + totalRevenue);
		totalCostLabel.setText("$" + totalCost);
		netProfitLabel.setText("$" + netProfit);

		System.out.println("Revenue calculated for " + type + ": " + filterDatePicker.getValue());
	}

	@FXML
	private void filterRevenueData(ActionEvent event) {
		// TODO: Viết code xử lý khi nhấn nút lọc revenue ở đây
	}

	@FXML
	private void resetRevenueFilter(ActionEvent event) {
		// TODO: Xử lý reset bộ lọc dữ liệu doanh thu ở đây
	}

	@FXML
	private void exportRevenueReport(ActionEvent event) {
		// TODO: Viết code xuất báo cáo doanh thu ở đây
	}

}
