package Controllers;

import Core.User;
import Utils.DBConnection;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Main Class
 */
public class LoginPageController implements Initializable {

    //Declaration of variables
    @FXML
    private TextField usernameTextField;
    private String username;
    @FXML
    private TextField passwordTextField;
    private String password;
    @FXML
    private Label zoneID;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private Label emptyError;
    @FXML
    private Label incorrectError;
    @FXML
    private Button loginButton;

    String zone;

    /**Method that pulls in zone.
     * This method is called from the Main Program and passes through the zone information.
     * @param zone zone is the locale the users machine is set to.
     */
    public LoginPageController(String zone){
        this.zone = zone;

    }

    /**Method that sets resource bundle based on default Region.
     At loading of login screen, this method automatically chooses the resource bundle based on the default.
     It then displays either in English or French based on locale.
     * @param url url that is needed for initialization
     * @param resourceBundle resource bundle that is retrieved. can either me English or French.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //pulls resource bundle (english or french) based on OS region.
        resourceBundle = ResourceBundle.getBundle("ResourceBundles.LoginPage", Locale.getDefault());

        zoneID.setText(zone); //Used to display ZoneID on form.

        //sets Labels/Errors from resource bundle based on default language.
        loginLabel.setText(resourceBundle.getString("title"));
        usernameLabel.setText(resourceBundle.getString("username"));
        passwordLabel.setText(resourceBundle.getString("password"));
        emptyError.setText((resourceBundle.getString("empty")));
        incorrectError.setText(resourceBundle.getString("incorrect"));
        loginButton.setText(resourceBundle.getString("login"));


    }

    /**Method to run to initiate login attempt.
     * This method takes the user define username and password. It creates an object from that and pulls in
     * the userID from the database if the username is found. It then calls the validation check method to
     * validate user.
     * @param event event is on mouse click of the login button.
     * @throws IOException required due to menuScreen method call.
     */
    @FXML
    public void logIn(MouseEvent event) throws IOException {
        //Declare variables
        username = usernameTextField.getText();
        password = passwordTextField.getText();
        int userID = GetUserID(username); //Calls GetUserID method to find userID from database.

        //Clears error labels to prevent overlapping of errors on multiple failure attempts.
        incorrectError.setVisible(false);
        emptyError.setVisible(false);

        //Creates a new user object.
        User loggedUser = new User(userID, username, password);

        //Runs validity check on user credentials if both username and password are present.
        if(username.isEmpty() || password.isEmpty()){
            emptyError.setVisible(true); //Displays error if either one are empty.
        }else if(IsAuthenticated(loggedUser)){
            menuScreen(event); //Loads main menu on successful login.
        }else{
            incorrectError.setVisible(true); //Displays error if credentials are invalid.

        }

    }

    /**Method to get UserID from database.
     * This method will pull bring in the username the user entered and check the userID in the database for that user.
     * @param username user defined parameter to check database and find the userID.
     * @return returns value of -1 on failure to find user.
     */
    public int GetUserID(String username){
        try{
            int userID = -1;

            //Creates query object
            Statement statement = DBConnection.startConnection().createStatement();
            //stores query response
            ResultSet userIDResultSet = statement.executeQuery("Select user_id from users where user_name = '" + username + "';");

            //Loops through query response and stores userID from the response.
            while(userIDResultSet.next()){
                userID = userIDResultSet.getInt(1);
            }

            return userID;

        }catch(SQLException sqlException){
            System.out.println(sqlException);
        }
        return -1;
    }

    /**Method that will compare the username entered to the password in the database for said user.
     * This method will take the user object, attempt to retrieve the password from the database for that specific user. Then return true or false
     * depending on if it matches.
     * @param user user is the user object passed in to validate.
     * @return returns either true or false based on matching password.
     */
    public boolean IsAuthenticated(User user){

        try {
            //Creates query object
            Statement statement = DBConnection.startConnection().createStatement();
            //Stores query response
            ResultSet passwordResultSet = statement.executeQuery("select password from users where user_name = '" + User.getUsername() + "';");

            //Stores password returned from the query.
            while(passwordResultSet.next()){
                password = passwordResultSet.getString(1);
            }

            //Validation check.
            if (User.getUserID() == -1){    //Makes sure user actual exists in the database.
                loggingLogin(User.getUsername(), false);
                return false;
            }else if (User.getPassword().equals(password)){     //Compares passwords.
                loggingLogin(User.getUsername(), true);
                return true;
            }
        }catch(SQLException sqlException){
            System.out.println(sqlException);
        }
        loggingLogin(User.getUsername(), false);
        return false;
    }

    /**Method that validates user login.
     This method takes the user provided credentials and compares the password entered to that of the database for the user.
     * @param username username of user to check password of.
     * @param password password of user to compare the database value to.
     * @return returns boolean based on result of validation.
     */
    public boolean loginAttempt(String username, String password){
        boolean authenticated;

        //SQL query that compares the user entered password to the value in the database for the username specified.
        try {
            Statement loginStatement = DBConnection.startConnection().createStatement();
            String sqlStatement = "SELECT password FROM users WHERE User_Name = '" + username +"';";
            ResultSet result = loginStatement.executeQuery(sqlStatement);

            //loops through the results of the query and compares the password.
            while (result.next()){
                if (password.equals(result.getString(1))){
                    return true;
                }else{
                    return false;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    /** Method that logs the login attempt of any user.
     *
     * @param user user is the user name that the user typed into the username text field.
     * @param isSuccessful isSuccessful is either true or false based on external validation.
     */
    public void loggingLogin(String user, boolean isSuccessful){
        Logger loginLogger = Logger.getLogger("Login Logging");
        FileHandler fh;
        String isSuccessfulString = "";

        //Depending on if the authentication is successful, the string will be true or false for logging.
        if (isSuccessful){
            isSuccessfulString = "True";
        }else{
            isSuccessfulString = "False";
        }

        try{
            //Finds the directory to put the log file in, names it and determines what shows up in the logs
            String dir = System.getProperty("user.dir");
            fh = new FileHandler(dir + "/Log_Login.log",true);
            loginLogger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            //What shows up in the log file
            loginLogger.info("Username: " + user + "\n" + "Login Attempt: " + isSuccessfulString + "\n");

        } catch (SecurityException exception){
            exception.printStackTrace();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }



    /**Method to display MainPage on successful Login.
     This method will change the scene once the login was successful.
     @param event event is on button click of the login button.
     @throws IOException catches exception and ignores.
     */
    private void menuScreen(Event event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/MenuPage.fxml"));
            MenuPageController controller = new MenuPageController();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
    }

}
