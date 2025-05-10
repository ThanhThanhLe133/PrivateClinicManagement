/**
 * 
 */
/**
 * 
 */
module myModule {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	requires mysql.connector.j;
	requires jakarta.mail;
	requires org.apache.poi.ooxml;
	requires java.desktop;

	opens application to javafx.fxml, javafx.graphics;
	opens Alert to javafx.fxml;
	opens Controller to javafx.fxml;
	opens DAO to javafx.fxml;
	opens Model to javafx.fxml;
	

	exports application;
	exports Controller;
	exports DAO;
	exports Model;
	exports Alert; // <-- Dòng này để JavaFX truy cập được class Main
}