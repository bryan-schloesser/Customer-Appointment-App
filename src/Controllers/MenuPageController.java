package Controllers;

import Core.Appointment;
import Core.User;
import Utils.DBConnection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 *Main Class
 */
public class MenuPageController implements Initializable {

    //Declare button variables for use.
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button customersButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button logOutButton;

    //Variables for Date/Time use
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");

    //Observable Lists
    private ObservableList<Appointment> upcomingAppointmentsList = FXCollections.observableArrayList();


    /** Method to run once the content is loaded.
     * This method will run once the content is loaded.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        LocalDateTime localDateTime = LocalDateTime.now();

        //Checks for Upcoming appointments for the logged in user.
        checkForUpcomingAppointment(User.getUserID());

        //If the upcomingAppointmentList has any data in the list, an appointment is upcoming in the next 15 minutes.
        if (upcomingAppointmentsList.size() >= 1){
            //If there is an appointment in the next 15 minutes, it will trigger the notification.
            MessageBox.appointmentLoginNotification(upcomingAppointmentsList.get(0).getAppointmentID(), upcomingAppointmentsList.get(0).getStartTime(), upcomingAppointmentsList.get(0).getEndTime());
        }else{
            //If there are no appointments coming up, it will alert the user stating that there are no appointments.
            MessageBox.appointmentNotificationNone();
        }
    }

    /** Method to convert local time to database time.
     * This method takes the local time value and converts it to database time, then formats it.
     * @param time time is local time to convert.
     * @return returns the database time formatted.
     */
    public String timeConvertToDBTime(LocalDateTime time){

        //Converts time from local time to database time (UTC)
        ZonedDateTime localZoneStart = time.atZone(localZoneID).withZoneSameInstant(utcZoneID);
        return localZoneStart.format(datetimeDTF);

    }

    /** Method to convert database time to local time.
     * This method takes the database time value and converts it to local time, then formats it.
     * @param time time is database time to convert.
     * @return returns the local time formatted.
     */
    public String timeConvertToLocalTime (String time){

        //Converts time from database time to local time
        LocalDateTime dbStartTime = LocalDateTime.parse(time, datetimeDTF);
        ZonedDateTime zonedDBStartTime = dbStartTime.atZone(utcZoneID).withZoneSameInstant(localZoneID);
        return zonedDBStartTime.format(datetimeDTF);

    }

    /** Method that checks the database for upcoming appointments.
     * This method is called at login to check the database for any appointments scheduled within the next 15 minutes.
     * @param userID userID of the user to check upcoming appointments for.
     * @return returns an Observable List of appointments coming up in 15 minutes.
     */
    private ObservableList<Appointment> checkForUpcomingAppointment(int userID){

        String currentTime = timeConvertToDBTime(LocalDateTime.now());
        String time15Added = timeConvertToDBTime(LocalDateTime.now().plusMinutes(15));

        try {
            //SQL Connection and Query
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select appointment_id, start, end from appointments where user_id = " + userID + " and start >= '" + currentTime + "' and start <= '" + time15Added + "';");
            System.out.println("select appointment_id, start, end from appointments where user_id = " + userID + " and start >= '" + currentTime + "' and start <= '" + time15Added + "';");

            //Takes the results of the query and creates appointment objects for each returned record.
            while (resultSet.next()) {
                int appointmentID = resultSet.getInt(1);
                String startTime = timeConvertToLocalTime(resultSet.getString(2));
                String endTime = timeConvertToLocalTime(resultSet.getString(3));

                //Creates new appointment with data returned in the query.
                Appointment appointment = new Appointment(appointmentID, startTime, endTime);

                //Adds appointment to the appointment list
                upcomingAppointmentsList.add(appointment);

            }
            //Returns appointment list
            return upcomingAppointmentsList;

        }catch(SQLException exception) {

        }
        //Returns appointment list
        return upcomingAppointmentsList;
    }

    /**Method to load Appointments Page.
     * This method changes the scene from the Menu Screen to the Appointments page.
     * @param event event is on Appointments button click.
     * @throws IOException
     */
    @FXML
    private void appointmentsPageLoad(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/AppointmentsMainPage.fxml"));
        AppointmentsMainPageController controller = new AppointmentsMainPageController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    /**Method to load Customers Page.
     * THis method changes the scene from the Menu screen to the Customers page.
     * @param event event is on Customers button click.
     * @throws IOException
     */
    @FXML
    private void customersPageLoad(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/CustomerPage.fxml"));
        CustomerPageController controller = new CustomerPageController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**Method to load Report Page.
     * This method changes the scene from the Menu screen to the Report Page.
     * @param event event is on Report button click.
     * @throws IOException
     */
    @FXML
    private void reportsPageLoad(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/ReportsPage.fxml"));
        ReportsPageController controller = new ReportsPageController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**Method to close out of Application.
     * This method will close out of the application when pressed.
     * @param event event is on Exit button click.
     * @throws IOException
     */
    @FXML
    private void exitApp(MouseEvent event) throws IOException{
        Platform.exit();
    }
}
