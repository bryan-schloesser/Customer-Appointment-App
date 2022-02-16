//Packages
package Core;

//Imports
import Utils.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.ZoneId;

/**
 * Main Class
 */
public class Main extends Application {

    /**Method to load the application interface and controller.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Loads FXML to display
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/LoginPage.fxml"));
        //Creates Controller to use with the loaded FXML
        Controllers.LoginPageController loginPageController = new Controllers.LoginPageController(loadZone());
        //Sets Controller
        loader.setController(loginPageController);
        //Loads Scene
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    /**Main Method
     *
     * @param args
     */
    public static void main(String[] args) {
        //Connects to database at application load.
        DBConnection.startConnection();
        //Runs Application until last window is closed
        launch(args);
        //Closes Database Connection on close of all windows
        DBConnection.closeConnection();
    }

    /**Pulls Region Information in from the users device.
     *
     * @return returns the the zoneID of the users device.
     */
    public String loadZone(){
        ZoneId z = ZoneId.systemDefault();
        return z.getId();
    }

}
