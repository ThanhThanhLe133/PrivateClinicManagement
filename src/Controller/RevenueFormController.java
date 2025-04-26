package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class RevenueFormController {

    @FXML
    private ComboBox<String> filterTypeComboBox;
    @FXML
    private DatePicker filterDatePicker;
    @FXML
    private Label totalRevenueLabel, totalCostLabel, netProfitLabel;

    @FXML
    public void initialize() {
        System.out.println("RevenueForm Initialized");
    }

    @FXML
    private void handleCalculateRevenue() {
        System.out.println("Calculate Revenue clicked!");
    }
    
    @FXML
    private void handleCalculateRevenue(ActionEvent event) {
        // Tạm thời log ra xem đã gọi được chưa
        System.out.println("Calculate button clicked!");
    }
    
}
    
