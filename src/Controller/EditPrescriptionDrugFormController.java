package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import DAO.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class EditPrescriptionDrugFormController {

    @FXML private ComboBox<String> cmbDrugName;
    @FXML private ComboBox<String> cmbManufacturer;
    @FXML private TextField txtQuantity;
    @FXML private TextArea txtInstruction;
    @FXML private Button btnSave, btnCancel;

    @FXML private Label lblDrugNameError, lblManufacturerError, lblQuantityError, lblInstructionError;
    
    // ID của đơn thuốc và thuốc
    private String prescriptionId;
    private String drugId;

    public void setPrescriptionId(String id) {
        // Gán ID đơn thuốc
        this.prescriptionId = id;
    }

    public void loadDrugData(String drugId) {
        // Tải dữ liệu thuốc hiện tại
        this.drugId = drugId;
        loadCurrentDrugData();
    }

    @FXML
    private void initialize() {
        // Tải danh sách thuốc
        loadDrugs();

        // Giới hạn txtQuantity chỉ nhận số nguyên dương
        if (txtQuantity != null) {
            txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal.matches("\\d*")) {
                    txtQuantity.setText(oldVal);
                }
            });
        }

        // Tải danh sách nhà sản xuất khi chọn thuốc
        if (cmbDrugName != null) {
            cmbDrugName.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    loadManufacturers(newVal);
                } else {
                    if (cmbManufacturer != null) {
                        cmbManufacturer.getItems().clear();
                        cmbManufacturer.setPromptText("Select Manufacturer");
                    }
                }
            });
        }
    }

    private void loadDrugs() {
        // Tải danh sách thuốc còn tồn kho
        if (cmbDrugName == null) return;
        try (Connection conn = Database.connectDB()) {
            String sql = """
                SELECT Name 
                FROM DRUG 
                WHERE Stock > 0 
                AND (Id NOT IN (
                    SELECT Drug_id 
                    FROM PRESCRIPTION_DETAILS 
                    WHERE Prescription_id = ? AND Drug_id != ?
                ) OR Id = ?)
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, prescriptionId);
                ps.setString(2, drugId != null ? drugId : ""); // Loại trừ thuốc hiện tại khỏi điều kiện NOT IN
                ps.setString(3, drugId != null ? drugId : ""); // Bao gồm thuốc hiện tại
                ResultSet rs = ps.executeQuery();
                Set<String> drugNames = new HashSet<>(); // Sử dụng Set để tránh trùng lặp
                while (rs.next()) {
                    drugNames.add(rs.getString("Name"));
                }
                cmbDrugName.getItems().addAll(drugNames);
            }
        } catch (SQLException e) {
            showError("Failed to load drugs: " + e.getMessage());
        }
    }

    private void loadManufacturers(String drugName) {
        // Tải danh sách nhà sản xuất cho thuốc được chọn
        if (cmbManufacturer == null) return;
        cmbManufacturer.getItems().clear();
        try (Connection conn = Database.connectDB()) {
            String sql = "SELECT Manufacturer FROM DRUG WHERE Name = ? AND Stock > 0";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, drugName);
                ResultSet rs = ps.executeQuery();
                Set<String> manufacturers = new HashSet<>();
                while (rs.next()) {
                    String manufacturer = rs.getString("Manufacturer");
                    if (manufacturer != null && !manufacturer.trim().isEmpty()) {
                        manufacturers.add(manufacturer);
                    }
                }
                if (manufacturers.isEmpty()) {
                    cmbManufacturer.getItems().add("No Manufacturer Available");
                    cmbManufacturer.setDisable(true);
                } else {
                    cmbManufacturer.getItems().addAll(manufacturers);
                    cmbManufacturer.setDisable(false);
                }
            }
        } catch (SQLException e) {
            showError("Failed to load manufacturers: " + e.getMessage());
        }
    }

    private void loadCurrentDrugData() {
        // Tải dữ liệu thuốc hiện tại vào form
        if (drugId == null || cmbDrugName == null || cmbManufacturer == null || txtQuantity == null || txtInstruction == null) return;
        try (Connection conn = Database.connectDB()) {
            String sql = """
                SELECT d.Name, d.Manufacturer, pd.Quantity, pd.Instructions
                FROM PRESCRIPTION_DETAILS pd
                JOIN DRUG d ON pd.Drug_id = d.Id
                WHERE pd.Prescription_id = ? AND pd.Drug_id = ?
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, prescriptionId);
                ps.setString(2, drugId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String drugName = rs.getString("Name");
                    String manufacturer = rs.getString("Manufacturer");
                    int quantity = rs.getInt("Quantity");
                    String instructions = rs.getString("Instructions");

                    // Gán giá trị vào form
                    cmbDrugName.setValue(drugName);
                    loadManufacturers(drugName); // Tải nhà sản xuất trước
                    cmbManufacturer.setValue(manufacturer);
                    txtQuantity.setText(String.valueOf(quantity));
                    txtInstruction.setText(instructions != null ? instructions : "");
                }
            }
        } catch (SQLException e) {
            showError("Failed to load current drug data: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        // Lưu thông tin thuốc nếu các trường hợp lệ
        if (validateAllFields()) {
            saveDrug();
        }
    }

    @FXML
    private void handleCancel() {
        // Đóng cửa sổ khi nhấn hủy
        if (btnCancel != null) {
            ((Stage) btnCancel.getScene().getWindow()).close();
        }
    }

    private boolean validateAllFields() {
        // Kiểm tra tất cả các trường nhập liệu
        boolean isValid = true;

        // Kiểm tra tên thuốc
        if (cmbDrugName != null && (cmbDrugName.getValue() == null || cmbDrugName.getValue().trim().isEmpty())) {
            showError(lblDrugNameError, "Drug name is required");
            isValid = false;
        } else {
            hideError(lblDrugNameError);
        }

        // Kiểm tra nhà sản xuất
        if (cmbManufacturer != null && (cmbManufacturer.getValue() == null || cmbManufacturer.getValue().trim().isEmpty() || 
                cmbManufacturer.getValue().equals("No Manufacturer Available"))) {
            showError(lblManufacturerError, "Manufacturer is required");
            isValid = false;
        } else {
            hideError(lblManufacturerError);
        }

        // Kiểm tra số lượng
        if (txtQuantity != null) {
            try {
                int value = Integer.parseInt(txtQuantity.getText().trim());
                if (value <= 0) {
                    showError(lblQuantityError, "Quantity must be greater than 0");
                    isValid = false;
                } else {
                    hideError(lblQuantityError);
                }
            } catch (NumberFormatException e) {
                showError(lblQuantityError, "Invalid quantity");
                isValid = false;
            }
        } else {
            showError(lblQuantityError, "Quantity field is missing");
            isValid = false;
        }

        // Kiểm tra hướng dẫn sử dụng
        if (txtInstruction != null && txtInstruction.getText().trim().isEmpty()) {
            showError(lblInstructionError, "Instructions are required");
            isValid = false;
        } else {
            hideError(lblInstructionError);
        }

        return isValid;
    }

    private void saveDrug() {
        // Lưu thông tin thuốc vào đơn thuốc
        try (Connection conn = Database.connectDB()) {
            // Bắt đầu giao dịch
            conn.setAutoCommit(false);

            // Tìm ID thuốc mới dựa trên tên và nhà sản xuất
            String sql = "SELECT Id FROM DRUG WHERE Name = ? AND Manufacturer = ? AND Stock > 0 LIMIT 1";
            String newDrugId = null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cmbDrugName.getValue());
                ps.setString(2, cmbManufacturer.getValue());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    newDrugId = rs.getString("Id");
                }
            }

            if (newDrugId == null) {
                showError("Drug not found in database.");
                conn.rollback();
                return;
            }

            // Kiểm tra xem thuốc mới đã có trong đơn thuốc chưa (ngoại trừ bản ghi hiện tại)
            if (!newDrugId.equals(drugId)) {
                sql = "SELECT COUNT(*) FROM PRESCRIPTION_DETAILS WHERE Prescription_id = ? AND Drug_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, prescriptionId);
                    ps.setString(2, newDrugId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                    	showError(lblDrugNameError, "This drug is already associated with the prescription.");
						lblDrugNameError.setVisible(true);
						conn.rollback();
						return;
					} else {
						hideError(lblDrugNameError);
                    }
                }
            }

            // Lấy số lượng ban đầu
            int originalQuantity = 0;
            String originalDrugId = drugId; // ID thuốc ban đầu
            sql = "SELECT Quantity FROM PRESCRIPTION_DETAILS WHERE Prescription_id = ? AND Drug_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, prescriptionId);
                ps.setString(2, originalDrugId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    originalQuantity = rs.getInt("Quantity");
                }
            }

            int newQuantity = Integer.parseInt(txtQuantity.getText().trim());

            // Xử lý cập nhật tồn kho
            if (!originalDrugId.equals(newDrugId)) {
                // Thuốc khác: hoàn lại tồn kho của thuốc cũ và trừ tồn kho của thuốc mới
                // Bước 1: Hoàn lại tồn kho của thuốc cũ
                sql = "UPDATE DRUG SET Stock = Stock + ? WHERE Id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, originalQuantity);
                    ps.setString(2, originalDrugId);
                    ps.executeUpdate();
                }

                // Bước 2: Kiểm tra và trừ tồn kho của thuốc mới
                int availableStock = 0;
                sql = "SELECT Stock FROM DRUG WHERE Id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, newDrugId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        availableStock = rs.getInt("Stock");
                    }
                }

                if (newQuantity > availableStock) {
                    showError("Requested quantity (" + newQuantity + ") exceeds available stock (" + availableStock + ").");
                    conn.rollback();
                    return;
                }

                // Trừ toàn bộ số lượng mới từ thuốc mới
                sql = "UPDATE DRUG SET Stock = Stock - ? WHERE Id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, newQuantity);
                    ps.setString(2, newDrugId);
                    ps.executeUpdate();
                }
            } else {
                // Cùng thuốc: tính toán thay đổi số lượng
                int stockChange = newQuantity - originalQuantity;
                int availableStock = 0;
                sql = "SELECT Stock FROM DRUG WHERE Id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, newDrugId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        availableStock = rs.getInt("Stock");
                    }
                }

                if (stockChange > availableStock) {
                    showError("Requested quantity change (" + stockChange + ") exceeds available stock (" + availableStock + ").");
                    conn.rollback();
                    return;
                }

                // Cập nhật tồn kho với thay đổi ròng
                sql = "UPDATE DRUG SET Stock = Stock - ? WHERE Id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, stockChange);
                    ps.setString(2, newDrugId);
                    ps.executeUpdate();
                }
            }

            // Cập nhật PRESCRIPTION_DETAILS
            sql = """
                UPDATE PRESCRIPTION_DETAILS
                SET Drug_id = ?, Quantity = ?, Instructions = ?
                WHERE Prescription_id = ? AND Drug_id = ?
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, newDrugId);
                ps.setInt(2, newQuantity);
                ps.setString(3, txtInstruction.getText().trim());
                ps.setString(4, prescriptionId);
                ps.setString(5, originalDrugId);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    // Cam kết giao dịch
                    conn.commit();
                    // Hiển thị thông báo thành công
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Drug updated successfully!");
                    alert.showAndWait();

                    // Đóng form
                    if (btnSave != null) {
                        ((Stage) btnSave.getScene().getWindow()).close();
                    }
                } else {
                    conn.rollback();
                    showError("Failed to update drug.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = Database.connectDB()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            // Hiển thị thông báo lỗi
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update drug: " + e.getMessage());
            alert.showAndWait();
        } finally {
            // Khôi phục trạng thái tự động cam kết
            try (Connection conn = Database.connectDB()) {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void showError(String message) {
        // Hiển thị thông báo lỗi chung
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void showError(Label label, String message) {
        // Hiển thị thông báo lỗi trên nhãn
        if (label != null) {
            label.setText(message);
            label.setVisible(true);
        }
    }

    private void hideError(Label label) {
        // Ẩn thông báo lỗi trên nhãn
        if (label != null) {
            label.setVisible(false);
        }
    }
}