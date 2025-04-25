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

	opens application to javafx.fxml, javafx.graphics;

	

	opens Alert to javafx.fxml, javafx.graphics;
	opens Controller to javafx.fxml, javafx.graphics;
	opens DAO to javafx.fxml, javafx.graphics;
	opens Model to javafx.fxml, javafx.graphics;
	
	exports application; // <-- Dòng này để JavaFX truy cập được class Main
}