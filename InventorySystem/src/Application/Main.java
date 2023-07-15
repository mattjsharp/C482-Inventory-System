package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** 
 * Contains the Main and Start methods for the Application.
 * 
 * @author Matthew Sharp
 */
public class Main extends Application {
    
    /** 
     * Entry point of the program. Calls the launch method inherited form Application
     * @param args user entered arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /** 
     * Loads and sets the initial scene.
     * @param primaryStage The initial stage created by the program.
     * @throws java.lang.Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Main.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
