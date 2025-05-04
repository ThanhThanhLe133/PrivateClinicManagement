package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import Alert.AlertMessage;
import DAO.Database;
import Model.Data;
import javafx.application.Platform;
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
	private Label name_adminDB, username_adminDB;

	@FXML
	private Button dashboard_btn, doctors_btn, receptionist_btn, salary_btn, revenue_btn, profile_btn;

	// Forms
	@FXML
	private AnchorPane dashboard_form, doctors_form, receptionist_form, salary_form, profile_form;
	@FXML
	private AnchorPane revenue_form;
	@FXML
	private Circle profile_circle;
	@FXML
	private TextField txt_name_admin, txt_username_admin, txt_email_admin;

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

	@FXML
	private Label name_admin, username_admin, email_admin, gender_admin, createdDate_admin;

	@FXML
	private ComboBox<String> gender_cb;
	// DATABASE TOOLS
	private Connection connect;
	private PreparedStatement prepare;
	private Statement statement;
	private ResultSet result;

	private AlertMessage alert = new AlertMessage();

	private Image image;

	// load data admin
	private String username;

	public void setUsername(String username) {
		this.username = username;
	}

	private void loadAdminProfile() {
		String checkUserSQL = "SELECT name, username, email, gender, created_at FROM user_account WHERE username = ?";
		Connection connect = Database.connectDB();

		try {
			PreparedStatement prepare = connect.prepareStatement(checkUserSQL);
			prepare.setString(1, username);

			ResultSet result = prepare.executeQuery();

			if (!result.next() || result.getInt(1) <= 0) {
				alert.errorMessage("Username does not match data.");
				return;
			}
			String name = result.getString("name");
			String username = result.getString("username");
			String email = result.getString("email");
			String gender = result.getString("gender");
			String createdAt = result.getString("created_at");

			// Gán cho các Label
			name_adminDB.setText(name != null ? name : "UNKNOWN");
			username_admin.setText(username != null ? username : "");
			name_admin.setText(name != null ? name : "UNKNOWN");
			username_adminDB.setText(username != null ? username : "");
			email_admin.setText(email != null ? email : "");
			gender_admin.setText(gender != null ? gender : "");
			createdDate_admin.setText(createdAt != null ? createdAt : "");

			// Gán cho các TextField (nếu có)
			txt_name_admin.setText(name != null ? name : "");
			txt_username_admin.setText(username != null ? username : "");
			txt_email_admin.setText(email != null ? email : "");
			gender_cb.setValue(gender != null ? gender : "");

			top_username.setText(name != null ? name : "UNKNOWN");

			// show avatar
			InputStream inputStream = result.getBinaryStream("avatar");
			if (inputStream != null) {
				// Chuyển InputStream thành Image để hiển thị trên giao diện
				Image img = new Image(inputStream, 137, 95, false, true);
				profile_circle.setFill(new ImagePattern(img));

				// Nếu cần hiển thị ảnh cho phần khác
				img = new Image(inputStream, 1012, 22, false, true);
				top_profile.setFill(new ImagePattern(img));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void runTime() {

		new Thread() {

			public void run() {
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
				while (true) {
					try {

						Thread.sleep(1000); // 1000 ms = 1s

					} catch (Exception e) {
						e.printStackTrace();
					}

					Platform.runLater(() -> {
						date_time.setText(format.format(new Date()));
					});
				}
			}
		}.start();

	}

	@FXML
	public void initialize() {
		runTime();
		ObservableList<String> genderOptions = FXCollections.observableArrayList("Male", "Female");
		gender_cb.setItems(genderOptions);
		loadAdminProfile();
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

	// update thông tin admin
	@FXML
	private void profileUpdateBtn(ActionEvent event) {
		String name = txt_name_admin.getText();
		String username = txt_username_admin.getText();
		String gender = (String) gender_cb.getSelectionModel().getSelectedItem();

		if (username.isEmpty() || name.isEmpty()) {
			alert.errorMessage("Please fill in all the fields.");
			return;
		}
		if (gender == null || gender.isEmpty()) {
			alert.errorMessage("Please select a gender.");
			return;
		}
		String checkUsernameSQL = "SELECT * FROM user_account WHERE username = ?";
		String updateUser = "UPDATE user_account SET name = ?, username = ?, gender = ? WHERE email = ?";

		connect = Database.connectDB();

		try {
			// kt username đã tồn tại chưa
			prepare = connect.prepareStatement(checkUsernameSQL);
			prepare.setString(1, username);
			result = prepare.executeQuery();

			if (result.next()) {
				alert.errorMessage(username + " already exists!");
				return;
			}

			// nếu username chưa tồn tại
			prepare = connect.prepareStatement(updateUser);
			prepare.setString(1, name);
			prepare.setString(2, username);
			prepare.setString(3, gender);
			prepare.setString(4, email_admin.getText());

			int rowsUpdated = prepare.executeUpdate();

			if (rowsUpdated > 0) {
				alert.successMessage("Profile updated successfully.");
				loadAdminProfile();
			} else {
				alert.errorMessage("No user found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			alert.errorMessage("Error updating profile: " + e.getMessage());
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

	public void profileDisplayImages() {

		String sql = "SELECT * FROM user_acount WHERE username = " + username;
		connect = Database.connectDB();

		try {
			prepare = connect.prepareStatement(sql);
			result = prepare.executeQuery();

			if (result.next()) {
				// Lấy ảnh nhị phân từ DB
				InputStream inputStream = result.getBinaryStream("avatar");

				if (inputStream != null) {
					// Chuyển InputStream thành Image
					Image img = new Image(inputStream, 137, 95, false, true);
					profile_circle.setFill(new ImagePattern(img));

					// Thêm logic nếu cần thêm hình ảnh khác
					img = new Image(inputStream, 1012, 22, false, true);
					top_profile.setFill(new ImagePattern(img));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void profileImportBtn(ActionEvent event) {
		FileChooser open = new FileChooser();
		open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*jpg", "*jpeg", "*png"));

		File file = open.showOpenDialog(profile_importBtn.getScene().getWindow());

		if (file != null) {
			Data.path = file.getAbsolutePath();

			// Hiển thị ảnh lên UI
			image = new Image(file.toURI().toString(), 137, 95, false, true);
			profile_circle.setFill(new ImagePattern(image));

			// Lưu ảnh vào DB
			try {
				connect = Database.connectDB();
				String updateAvatarSQL = "UPDATE user_account SET avatar = ? WHERE email = ?";

				FileInputStream input = new FileInputStream(file);
				prepare = connect.prepareStatement(updateAvatarSQL);
				prepare.setBinaryStream(1, input, (int) file.length());
				prepare.setString(2, email_admin.getText()); // email người đăng nhập

				int rows = prepare.executeUpdate();
				if (rows > 0) {
					alert.successMessage("Avatar updated successfully.");
				} else {
					alert.errorMessage("Failed to update avatar.");
				}
				profileDisplayImages();
			} catch (Exception e) {
				e.printStackTrace();
				alert.errorMessage("Error uploading avatar: " + e.getMessage());
			}
		}
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
