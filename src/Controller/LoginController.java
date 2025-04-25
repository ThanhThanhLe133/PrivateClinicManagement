/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import Alert.AlertMessage;
import DAO.Database;
import Model.Data;
import Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author WINDOWS 10
 */
public class LoginController implements Initializable {
	// LETS NAME ALL COMPONENTS WE HAVE ON ADMIN PAGE
	@FXML
	private ImageView logo;

	@FXML
	private AnchorPane main_form;

	@FXML
	private AnchorPane login_form;

	@FXML
	private TextField login_username;

	@FXML
	private PasswordField login_password;

	@FXML
	private TextField login_showPassword;

	@FXML
	private CheckBox login_checkBox;

	@FXML
	private Button login_loginBtn;

	@FXML
	private ComboBox<?> login_user;

	@FXML
	private ComboBox<?> register_user;

	@FXML
	private Hyperlink login_registerHere;

	@FXML
	private AnchorPane register_form;

	@FXML
	private TextField register_email;

	@FXML
	private TextField register_username;

	@FXML
	private PasswordField register_password;

	@FXML
	private TextField register_showPassword;

	@FXML
	private CheckBox register_checkBox;

	@FXML
	private Button register_signupBtn;

	@FXML
	private Hyperlink register_loginHere;

//    DATABASE TOOLS
	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;

	private AlertMessage alert = new AlertMessage();
	
    @FXML
	public void loginAccount() {
		String username = login_username.getText();
		String password = login_password.getText();
		String selectedRole = (String) login_user.getSelectionModel().getSelectedItem();

		if (username.isEmpty() || password.isEmpty()) {
			alert.errorMessage("Please enter both Username and Password.");
			return;
		}

		if (selectedRole == null || selectedRole.isEmpty()) {
			alert.errorMessage("Please select a role.");
			return;
		}

		// Đồng bộ mật khẩu nhập ở ô Password và ShowPassword (nếu có)
		if (!login_showPassword.isVisible()) {
			if (!login_showPassword.getText().equals(password)) {
				login_showPassword.setText(password);
			}
		} else {
			if (!login_showPassword.getText().equals(password)) {
				login_password.setText(login_showPassword.getText());
				password = login_showPassword.getText();
			}
		}

		String sql = "SELECT * FROM user_account WHERE username = ? AND password = ? AND role = ?";
		connect = Database.connectDB();

		try {
			prepare = connect.prepareStatement(sql);
			prepare.setString(1, username);
			prepare.setString(2, password);
			prepare.setString(3, selectedRole.toUpperCase());

			result = prepare.executeQuery();
	
			if (result.next()) {
				// Kiểm tra nếu là role DOCTOR hoặc RECEPTIONIST
                if (selectedRole.equalsIgnoreCase("DOCTOR") || selectedRole.equalsIgnoreCase("RECEPTIONIST")) {
                	 String checkConfirmSQL = "";
                     if (selectedRole.equalsIgnoreCase("DOCTOR")) {
                         checkConfirmSQL = "SELECT is_confirmed FROM doctor WHERE doctor_id = ?";
                     } else if (selectedRole.equalsIgnoreCase("RECEPTIONIST")) {
                         checkConfirmSQL = "SELECT is_confirmed FROM receptionist WHERE receptionist_id = ?";
                     }
                     
                     PreparedStatement confirmStmt = connect.prepareStatement(checkConfirmSQL);
                     confirmStmt.setString(1, result.getString("id")); 
                     ResultSet confirmResult = confirmStmt.executeQuery();
                     
                     if (confirmResult.next()) {
                         boolean isConfirmed = confirmResult.getBoolean("is_confirmed");
                         if (!isConfirmed) {
                             alert.errorMessage("Your account has not been confirmed by the admin.");
                             return;
                         }
                     }
                }
				// Lưu thông tin người dùng
				Data.user_username = username;
				Data.user_id = result.getString("id"); 

				alert.successMessage("Login Successfully!");
				
				 // Cập nhật trạng thái is_active thành true sau khi đăng nhập thành công
                String updateStatusSQL = "UPDATE user_account SET is_active = ? WHERE id = ?";
                PreparedStatement updateStmt = connect.prepareStatement(updateStatusSQL);
                updateStmt.setBoolean(1, true); 
                updateStmt.setString(2, result.getString("id"));
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User status updated to active.");
                } else {
                    alert.errorMessage("Failed to update user status.");
                    return;
                }

				// Load Main Form tuỳ theo Role
				String fxmlFile = switch (selectedRole.toUpperCase()) {
				case "ADMIN" -> "AdminMainForm.fxml";
				case "DOCTOR" -> "DoctorMainForm.fxml";
				case "RECEPTIONIST" -> "ReceptionistMainForm.fxml";
				default -> null;
				};

				if (fxmlFile != null) {
					Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
					Stage stage = new Stage();
					stage.setTitle("Private Clinic | " + selectedRole);
					stage.setScene(new Scene(root));
					stage.show();

					// Đóng form đăng nhập
					login_loginBtn.getScene().getWindow().hide();
				} else {
					alert.errorMessage("Unknown role. Cannot open main form.");
				}
			} else {
				alert.errorMessage("Incorrect Username/Password or Role mismatch.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			alert.errorMessage("Something went wrong during login.");
		}
	}

    @FXML
	public void registerAccount() {

		String email = register_email.getText();
		String username = register_username.getText();
		String password = register_password.getText();
		String role = (String) register_user.getSelectionModel().getSelectedItem();

		if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
			alert.errorMessage("Please fill in all the fields.");
			return;
		}

		if (role == null || role.isEmpty()) {
			alert.errorMessage("Please select a role.");
			return;
		}

		// Đồng bộ password và showPassword
		if (!register_showPassword.isVisible()) {
			if (!register_showPassword.getText().equals(password)) {
				register_showPassword.setText(password);
			}
		} else {
			if (!register_showPassword.getText().equals(password)) {
				register_password.setText(register_showPassword.getText());
				password = register_showPassword.getText();
			}
		}

		// Kiểm tra độ dài mật khẩu
		if (password.length() < 8) {
			alert.errorMessage("Password must be at least 8 characters.");
			return;
		}

		String checkUsernameSQL = "SELECT * FROM user_account WHERE username = ?";
		String insertUserSQL = "INSERT INTO user_account (Id, email, username, password, role) VALUES (?, ?, ?, ?, ?)";

		connect = Database.connectDB();
		try {

			prepare = connect.prepareStatement(checkUsernameSQL);
			prepare.setString(1, username);
			result = prepare.executeQuery();

			if (result.next()) {
				alert.errorMessage(username + " already exists!");
				return;
			}

			// Nếu chưa tồn tại thì thêm mới
			String id = UUID.randomUUID().toString();
			String upperRole = role.toUpperCase();

			prepare = connect.prepareStatement(insertUserSQL);
			prepare.setString(1, id);
			prepare.setString(2, email);
			prepare.setString(3, username);
			prepare.setString(4, password);
			prepare.setString(5, upperRole);
			prepare.executeUpdate();

			// Chèn vào bảng phụ tương ứng theo role
			String insertChildSQL = switch (upperRole) {
			case "DOCTOR" -> "INSERT INTO doctor (Doctor_id) VALUES (?)";
			case "RECEPTIONIST" -> "INSERT INTO receptionist (Receptionist_id) VALUES (?)";
			default -> "";
			};

			if (!insertChildSQL.isEmpty()) {
				prepare = connect.prepareStatement(insertChildSQL);
				prepare.setString(1, id);
				prepare.executeUpdate();
			}

			alert.successMessage("Registered Successfully!");
			registerClear();

			// Chuyển về form đăng nhập
			login_form.setVisible(true);
			register_form.setVisible(false);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    @FXML
	public void registerClear() {
		register_email.clear();
		register_username.clear();
		register_password.clear();
		register_showPassword.clear();
	}
    
    @FXML
	public void loginShowPassword() {

		if (login_checkBox.isSelected()) {
			login_showPassword.setText(login_password.getText());
			login_showPassword.setVisible(true);
			login_password.setVisible(false);
		} else {
			login_password.setText(login_showPassword.getText());
			login_showPassword.setVisible(false);
			login_password.setVisible(true);
		}

	}
    
    @FXML
	public void registerShowPassword() {

		if (register_checkBox.isSelected()) {
			register_showPassword.setText(register_password.getText());
			register_showPassword.setVisible(true);
			register_password.setVisible(false);
		} else {
			register_password.setText(register_showPassword.getText());
			register_showPassword.setVisible(false);
			register_password.setVisible(true);
		}

	}

    @FXML
	public void userList() {
		// Đổ dữ liệu cho login_user ComboBox
		List<String> listU = new ArrayList<>();
		for (String data : Users.user) {
			listU.add(data);
		}
		ObservableList listData = FXCollections.observableList(listU);
		login_user.setItems(listData);

		// Đổ dữ liệu cho register_user ComboBox
		List<String> listRegister = new ArrayList<>();
		listRegister.add("Doctor");
		listRegister.add("Receptionist");
		ObservableList listRegist = FXCollections.observableList(listRegister);
		register_user.setItems(listRegist);
	}

	public void switchForm(ActionEvent event) {

		if (event.getSource() == login_registerHere) {
			login_form.setVisible(false);
			register_form.setVisible(true);
		} else if (event.getSource() == register_loginHere) {
			login_form.setVisible(true);
			register_form.setVisible(false);
		}

	}

	// NOW, LETS CREATE OUR DATABASE FOR OUR USERS
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		userList();
	}

}
