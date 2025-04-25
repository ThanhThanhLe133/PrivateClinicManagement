package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	 @Override
	    public void start(Stage stage) throws Exception {
	        Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
	        
	        Scene scene = new Scene(root);
	        
	        stage.setMinWidth(340);
	        stage.setMinHeight(580);
	        
	        stage.setTitle("Private Clinic Management");
	        
	        stage.setScene(scene);
	        stage.show();
	    }

	    /**
	     * @param args the command line arguments
	     */
	    public static void main(String[] args) {
	        launch(args);
	    }
}
