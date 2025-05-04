package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.DoctorData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * Controller cho giao diện hiển thị thông tin bác sĩ.
 */
public class DoctorCardController implements Initializable {

    @FXML
    private Circle doctor_circle;

    @FXML
    private Label doctor_id;

    @FXML
    private Label doctor_fullName;

    @FXML
    private Label doctor_specialization;

    @FXML
    private Label doctor_email;



    /**
     * Thiết lập dữ liệu bác sĩ vào giao diện.
     * @param dData Đối tượng DoctorData chứa thông tin bác sĩ.
     */
    public void setData(DoctorData dData) {
       
    }

    @FXML
    private void openDoctorPage() {
        System.out.println("Đang mở DoctorPage...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DoctorPage.fxml"));
            Parent root = loader.load();

            // Tạo cửa sổ mới để hiển thị thông tin bác sĩ
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Doctor Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Controller sẵn sàng để hiển thị dữ liệu
    }
}