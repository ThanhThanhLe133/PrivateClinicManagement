/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javafx.scene.control.Label;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

import com.mysql.cj.Session;

import Alert.AlertMessage;
import DAO.Database;
import Model.Data;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import jakarta.mail.*;
import jakarta.mail.internet.*;

/**
 *
 * @author WINDOWS 10
 */
public class ForgotPassController implements Initializable {
	// LETS NAME ALL COMPONENTS WE HAVE ON ADMIN PAGE
	@FXML
	private ImageView logo;

	@FXML
	private AnchorPane main_form;

	@FXML
	private AnchorPane create_newPass;

	@FXML
	private AnchorPane forgot_pass;

	@FXML
	private TextField email;

	@FXML
	private TextField input_otp;

	@FXML
	private Label time;

	@FXML
	private Button send_OTP;

	@FXML
	private Button verify_OTP;

	@FXML
	private Hyperlink login_here;

	@FXML
	private PasswordField input_pass;

	@FXML
	private TextField input_showPass;

	@FXML
	private PasswordField retype_pass;

	@FXML
	private TextField retype_showPass;

	@FXML
	private TextField login_showPassword;

	@FXML
	private CheckBox show_pass;

	@FXML
	private Button create_new;

//    DATABASE TOOLS
	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;

	private Timeline timeline;
	private int timeRemaining = 120;
	private AlertMessage alert = new AlertMessage();

	@FXML
	public void countDown() {
		if (timeline != null) {
			timeline.stop();
		}

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			timeRemaining--; // Decrease time by 1 second
			time.setText("Time remaining: " + timeRemaining + "s"); // Update the timer label

			// When time is up, disable the OTP input and show a message
			if (timeRemaining <= 0) {
				time.setText("OTP has expired! Please resend OTP.");
				input_otp.setDisable(true); // Disable the OTP input field
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	public String generateOTP() {
		// Tạo mã OTP ngẫu nhiên 6 chữ số
		Random rand = new Random();
		int otp = 100000 + rand.nextInt(900000);
		return String.valueOf(otp);
	}

	public void sendVerificationCode(String to, String codeSend) {
		// Xác nhận người dùng
		// Gửi mã OTP qua email
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Notice!");
		confirmAlert.setHeaderText(null);
		confirmAlert.setContentText("OTP has been sent to your email");

		Optional<ButtonType> result = confirmAlert.showAndWait();
		if (result.isEmpty() || result.get() != ButtonType.OK) {
			return;
		}
		countDown();
		String from = "english4fun@zohomail.com";
		String password = "@4FFun@@";
		String subject = "Private Clinic - Changing Password";
		String messageBody = "Your verification code is: " + codeSend;

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		jakarta.mail.Session session = jakarta.mail.Session.getInstance(props, new Authenticator() {
			@Override
			protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
				return new jakarta.mail.PasswordAuthentication(from, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(messageBody);

			Transport.send(message);

			// Thông báo thành công
			alert.successMessage("OTP has been sent to your email successfully.");

		} catch (MessagingException e) {
			// Thông báo lỗi
			alert.errorMessage("Error sending OTP to email: " + e.getMessage());
		}
	}

	private String codeSend;

	@FXML
	public void sendOTP() {
		// Gửi OTP đến email người dùng
		String emailOTP = email.getText();
		if (emailOTP == null || emailOTP.isEmpty()) {
			alert.errorMessage("Please input your email!");
			return;
		}
		String checkUserSQL = "SELECT COUNT(*) FROM user_account WHERE email = ?";
		connect = Database.connectDB();

		try {
			PreparedStatement prepare = connect.prepareStatement(checkUserSQL);
			prepare.setString(1, email.getText());

			ResultSet resultSet = prepare.executeQuery();

			if (!resultSet.next() || resultSet.getInt(1) <= 0) {
				alert.errorMessage("Email does not exist.");
				return;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		codeSend = generateOTP();
		sendVerificationCode(emailOTP, codeSend);
	}

	@FXML
	public void verifyOTP() {
		// Xác minh mã OTP người dùng nhập
		String userInput = input_otp.getText();

		if (userInput.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Notice");
			alert.setHeaderText(null);
			alert.setContentText("Please input OTP code having been sent to your email");
			alert.showAndWait();
			return;
		}
		if (timeRemaining <= 0) {
			alert.errorMessage("OTP has expired.");
			return;
		}

		if (userInput.equals(codeSend)) {
			alert.successMessage("Verify successfully!");
			;
			forgot_pass.setVisible(false);
			create_newPass.setVisible(true);

		} else {
			alert.errorMessage("OTP doesn't match.");
		}
	}

	@FXML
	public void createNewPass() {
		// Tạo mật khẩu mới
		String newPass = input_pass.getText();
		String confirmPass = retype_pass.getText();

		if (newPass.isEmpty() || confirmPass.isEmpty()) {
			alert.errorMessage("Please input new password and retyping it to verify.");
			return;
		}

		if (newPass.length() < 8 || !newPass.matches(".*[A-Z].*") || !newPass.matches(".*[0-9].*")) {
			alert.errorMessage("Password must be at least 8 characters and include a number and an uppercase letter.");
			return;
		}

		if (!newPass.equals(confirmPass)) {
			alert.errorMessage("Password doesn't match each other.");
			return;
		}

		String updatePassSQL = "UPDATE user_account SET password = ? WHERE email = ?";
		connect = Database.connectDB();

		try {
			prepare = connect.prepareStatement(updatePassSQL);
			prepare.setString(1, newPass);
			prepare.setString(2, email.getText());

			int rowsUpdated = prepare.executeUpdate();

			if (rowsUpdated > 0) {
				alert.successMessage("Password has been updated sucessfully.");
				try {
					openLogin();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				alert.errorMessage("Error while creating new password. Please try again later.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void showPass() {
		// Hiển thị/ẩn mật khẩu
		if (show_pass.isSelected()) {
			input_showPass.setText(input_pass.getText());
			input_showPass.setVisible(true);
			input_pass.setVisible(false);

			retype_showPass.setText(retype_pass.getText());
			retype_showPass.setVisible(true);
			retype_pass.setVisible(false);
		} else {
			input_pass.setText(input_showPass.getText());
			input_pass.setVisible(true);
			input_showPass.setVisible(false);

			retype_pass.setText(retype_showPass.getText());
			retype_pass.setVisible(true);
			retype_showPass.setVisible(false);
		}

	}

	public void openLogin() throws IOException {
		//Mở form đăng nhập
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
		Parent root = loader.load();

		Stage stage = new Stage();
		stage.setMinWidth(340);
		stage.setMinHeight(580);
		stage.setTitle("Login");
		stage.setScene(new Scene(root));
		stage.show();
		create_new.getScene().getWindow().hide();
	}

	// Khởi tạo form, đặt focus vào panel quên mật khẩu
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		 Platform.runLater(() -> forgot_pass.requestFocus());
	}
}
