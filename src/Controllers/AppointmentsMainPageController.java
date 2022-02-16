//Packages
package Controllers;

//Imports
import Core.Appointment;
import Core.User;
import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**Main Class for AppointmentsMainPageController
 *
 */
public class AppointmentsMainPageController implements Initializable {

    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button modifyAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;
    private Button backButton;
    @FXML
    private TextField appointmentIDTextfield;
    @FXML
    private TableView<Appointment> appointmentsTable;

    //Appointment List
    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    //Variables for time use.
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");


    /**Method to run at page load.
     * This method will run once the page is loaded. It calls the GenerateAppointmentsList
     * to retrieve a list of appointments from the database based on the User logged in.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Runs the GenerateAppointmentsList method based on what user is logged in and adds them to the table.
        appointmentsTable.setItems(GenerateAppointmentsList(User.getUserID()));

    }

    /** Not entirely sure if needed. Keeping just in case.
    public String timeConvert(String time){
        //convert database UTC to LocalDateTime
        LocalDateTime utcStartDT = LocalDateTime.parse(time, datetimeDTF);

        //convert times UTC zoneId to local zoneId
        ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

        //convert ZonedDateTime to a string for insertion into AppointmentsTableView

        return localZoneStart.format(datetimeDTF);

    }**/


    /**Method to convert the time of the appointments to local time.
     * This method takes the database values and converts them to local time for user display.
     *
     * @param time time is the LocalDateTime to convert to local time from UTC.
     * @return returns String of converted time to UTC time.
     */
    public String timeConvertToDBTime(LocalDateTime time){

        //Converts time from local time to database time (UTC)
        ZonedDateTime localZoneStart = time.atZone(localZoneID).withZoneSameInstant(utcZoneID);
        return localZoneStart.format(datetimeDTF);

    }

    /**Method to convert time to local time.
     * Method that takes a time string value, converts it to the local time of the user,
     * and returns the converted time.
     *
     * @param time time is the time value to convert to local time.
     * @return returns the converted local time.
     */
    public String timeConvertToLocalTime (String time){
        //Converts time from database time to local time
        LocalDateTime dbStartTime = LocalDateTime.parse(time, datetimeDTF);
        ZonedDateTime zonedDBStartTime = dbStartTime.atZone(utcZoneID).withZoneSameInstant(localZoneID);
        return zonedDBStartTime.format(datetimeDTF);

    }

    /**Method to Generate the list of Appointments for the logged in user.
     * This method takes the UserID of the logged in user and retrieves records from the database that contain
     * the users UserID. it adds the results to the observable list for use.
     *
     * @param activeUserID activeUserID is the userID of the logged in user.
     * @return returns the appointmentList observable list containing appointment data.
     */
    public ObservableList<Appointment> GenerateAppointmentsList(int activeUserID){

        //Clears the list of appointments so old data is removed.
        appointmentList.clear();

        try{
            //SQL connection and query
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from appointments a join customers c on a.customer_ID = c.customer_id join contacts con on con.Contact_ID = a.Contact_ID;");

            //Query results are assigned to variables for use.
            while(resultSet.next()){
                //Variables
                int appointmentID = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String description = resultSet.getString(3);
                String location = resultSet.getString(4);
                String type = resultSet.getString(5);
                String startTime = timeConvertToLocalTime(resultSet.getString(6));
                String endTime = timeConvertToLocalTime(resultSet.getString(7));

                int userID = resultSet.getInt(13);
                int contactID = resultSet.getInt(14);
                int customerID = resultSet.getInt(12);
                String customerName = resultSet.getString(16);
                String contactName = resultSet.getString(26);

                //An appointment object is created for each record returned from the database.
                Appointment appointment = new Appointment(appointmentID, title, description, location, type, startTime, endTime, customerID,
                         customerName, userID, contactID, contactName);


                //The appointment will only be added to the observable list if the userID of the logged in user
                //matches that of the record returned from the database.
                if (activeUserID == appointment.getUserID()){
                    appointmentList.add(appointment);

                }
            }
        }catch (SQLException exception){
            System.out.println(exception); //SQL exception printed to console if there is an exception.

        }

        return appointmentList;
    }


    /**Method called to delete an Appointment.
     * This method will delete the selected appointment.
     */
    @FXML
    private void deleteAppointment(){

        //Gets the appointmentID of the selected appointment from the table.
        Appointment appointmentToDelete = appointmentsTable.getSelectionModel().getSelectedItem();
        int appointmentID = appointmentToDelete.getAppointmentID();

        //If user confirms deletion by clicking the okay button in the message box, the appointment will be deleted.
        if (MessageBox.appointmentDeleteConfirm()) {

            try {
                //SQL Connection and Query
                Statement statement = DBConnection.startConnection().createStatement();
                int deleteAppointment = statement.executeUpdate("Delete from appointments where appointment_id = " +
                        appointmentID + ";");
                MessageBox.appointmentSuccessfulDelete(appointmentToDelete.getType(), appointmentID);


            } catch (SQLException exception) {
                System.out.println(exception); //SQL exception printed to console if there is an exception.
            }
            //Generates new list of Appointments to remove the deleted appointment from the table.
            appointmentsTable.setItems(GenerateAppointmentsList(User.getUserID()));
        }else {
            //Error message is displayed if appointment was unable to be deleted.
            MessageBox.appointmentError();
        }
    }

    /**Method that changes current view to the modify appointments page.
     * This method changes the current view and controller to the Add/Modify appointments page.
     *
     * @param event event is on mouse click of the "Add/Modify" button.
     * @throws IOException Exception
     */
    @FXML
    private void addAppointment(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/AppointmentsAddPage.fxml"));
        AppointmentsAddPageController controller = new AppointmentsAddPageController(appointmentList);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    /**Method to filter appointments by month.
     * This method uses a Lambda expression to create a filtered list of the appointments. The use of a lambda expression
     * was decided due to how they are efficient and filtering data. The code required was significant reduced and easy to read
     * compared to other methods of list filtering.
     *
     * @param event event is on mouse click of radio button.
     * @throws SQLException
     */
    @FXML
    public void filterAppointmentsByMonth(MouseEvent event) throws SQLException {

        //filter appointments for month
        LocalDate current = LocalDate.now();
        LocalDate currentTimePlusMonth = current.plusMonths(1);

        //Lambda Expression to filter results by Month. The use of the lambda Expression to filter the results was the
        //best way to go because it is one of the most efficient ways to filter data. It reduces the lines of code needed.

        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentList);
        filteredData.setPredicate(row -> {
            LocalDate date = LocalDate.parse(row.getStartTime(), datetimeDTF);

            return date.isAfter(current.minusDays(1)) && date.isBefore(currentTimePlusMonth);
        });

        appointmentsTable.setItems(filteredData);
    }

    /**Method to filter appointments by week.
     * This method uses a Lambda expression to create a filtered list of the appointments. The use of a lambda expression
     * was decided due to how they are efficient and filtering data. The code required was significant reduced and easy to read
     * compared to other methods of list filtering.
     *
     * @param event event is on mouse click of radio button.
     */
    @FXML
    public void filterAppointmentsByWeek(MouseEvent event) {
        //filter appointments for week
        LocalDate current = LocalDate.now();
        LocalDate currentPlusWeek = current.plusWeeks(1);

        //Lambda Expression to filter results by week. The use of the lambda Expression to filter the results was the
        //best way to go because it is one of the most efficient ways to filter data. It reduces the lines of code needed.
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentList);
        filteredData.setPredicate(row -> {
            LocalDate date = LocalDate.parse(row.getStartTime(), datetimeDTF);

            return date.isAfter(current.minusDays(1)) && date.isBefore(currentPlusWeek);
        });
        appointmentsTable.setItems(filteredData);
    }


    /**Method to clear Appointments Filter.
     * This method will "reset" the other radio buttons and go back to the unfiltered list.
     *
     * @param event event is on mouse click of the radio button.
     */
    @FXML
    public void clearAppointmentsFilter(MouseEvent event) {
        appointmentsTable.setItems(appointmentList);
        System.out.println(appointmentList);

    }

    /**This method updates the current appointment.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void updateAppointment(MouseEvent event) throws IOException {

        if(appointmentsTable.getSelectionModel().getSelectedItem() != null){

            Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/AppointmentsUpdatePage.fxml"));
            AppointmentsUpdatePageController controller = new AppointmentsUpdatePageController(selectedAppointment);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        }

    }

    /**Method to display MainPage on successful Login.
     This method will change the scene once the login was successful.
     @param event event is on button click of the login button.
     @throws IOException catches exception and ignores.
     */
    @FXML
    private void menuScreen(MouseEvent event) throws IOException {
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
