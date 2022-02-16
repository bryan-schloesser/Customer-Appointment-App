//Package
package Controllers;

//Imports

import Core.Appointment;
import Core.Contact;
import Core.Customer;
import Core.User;
import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;


/**
 * Main class for AppointmentsAddPage Controller
 */
public class AppointmentsAddPageController implements Initializable {

    //Declaration of Observable Lists
    private final ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> startTimeList = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> endTimeList = FXCollections.observableArrayList();
    private final ObservableList<Contact> contactList = FXCollections.observableArrayList();
    private final ObservableList<String> contactNameList = FXCollections.observableArrayList();
    ObservableList<Appointment> appointmentsList; //Observable List Object

    Appointment appointment; //Appointment Object

    //Variables for Date/Time use
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");
    private final ZoneId easternZoneID = ZoneId.of("US/Eastern");

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TextField appointmentIDTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private ComboBox<Date> dateCB;
    @FXML
    private ComboBox<LocalTime> startTimeCB;
    @FXML
    private ComboBox<LocalTime> endTimeCB;
    @FXML
    private ComboBox<String> contactCB;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField customerIDTextField;
    @FXML
    private DatePicker datePickerInput;

    /**Method that runs once the page is loaded.
     * This method calls various other methods once initialized. It generates data and sets data for use.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTable.setItems(GenerateCustomerList()); //Runs GenerateCustomerList Method and sets the list to the customerTable.
        customerTable.getSelectionModel().select(1); //Auto Selects the first customer in the list within the table.
        generateTimeList(); //Generates a list of times to select for the start/end times.
        generateContactList(); //Generates a list of contacts from the database to use for selection in the form.
        contactCB.setItems(contactNameList); //Takes the contactNameList and adds it to the contact combo box for selection.
        startTimeCB.setItems(startTimeList); //Takes the list of times and adds it to the start time combo box for selection.
        startTimeCB.getSelectionModel().selectFirst(); //Auto selects the first start time in the combo box so it is not blank.
        restrictEndTime(); //Runs the restrictEndTime method to limit the selection in the end time so it is not before the start time.
        contactCB.getSelectionModel().selectFirst(); //selects the first contact in the contact combo box so it is not blank.
        loadCustomerData(); //takes the CustomerID from the form and pulls the customer name associated to that customer ID for use.
    }

    /**Method to pull list of appointments in for Controller to use.
     * This method brings in a list of appointments for use from the other views/controllers.
     *
     * @param appointments appointments List.
     */
    public AppointmentsAddPageController(ObservableList<Appointment> appointments){
        appointments = appointmentsList;
    }

    /**Method that gets the contact name of selected appointment.
     * This method goes through the list of contacts and selects the contact name based on the contact ID of the appointment.
     * @return returns the name of the contact based on contact ID of appointment.
     */
    private String getContactName(){
        String contactName = "N/A"; //Filler value.

        //Loops through list of contacts to find name associated to specified contact id.
        for(int i = 0; i < contactList.size(); i++){
            if (contactList.get(i).getContactID() == appointment.getContactID()){
                contactName = contactList.get(i).getContactName();
                return contactName;
            }
        }
        return contactName;
    }

    /**Method that loads the customer data in the form.
     * This method is called to find the value of the customerID and customer name for the appointment.
     */
    @FXML
    private void loadCustomerData(){
        customerIDTextField.setText(String.valueOf(customerTable.getSelectionModel().getSelectedItem().getCustomerID()));
        //getCustomerName is called to find customer name based on the customerID in the appointment object.
        customerNameTextField.setText(customerTable.getSelectionModel().getSelectedItem().getCustomerName());

    }

    /**Method to generate the list of Customers.
     *This method will query the database for all customers and create an Observable list from the data.
     * @return returns ObservableList of Customer data.
     */
    public ObservableList<Customer> GenerateCustomerList(){

        try {
            //SQL connection and query
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet customerList = statement.executeQuery("select customer_id, customer_name, address, postal_code, phone, fld.division, fld.division_id, c.Country, c.Country_ID from customers join first_level_divisions fld on customers.Division_ID = fld.Division_ID join countries c on c.COUNTRY_ID = fld.COUNTRY_ID");

            //Loops through query results and assigns column to the appropriate variable to create a new customer.
            while(customerList.next()){
                int customerID = customerList.getInt(1);
                String customerName = customerList.getString(2);
                String address = customerList.getString(3);
                String postalCode = customerList.getString(4);
                String phoneNumber = customerList.getString(5);
                String division = customerList.getString(6);
                int divisionID = customerList.getInt(7);
                String country = customerList.getString(8);

                //Creates new customer based on the record returned from the database query result.
                Customer customer = new Customer(customerID, customerName, address, postalCode, phoneNumber, division, divisionID, country);

                //Adds the newly created customer to the customer observable list.
                customerObservableList.add(customer);
            }
            //Error catching
        }catch (SQLException sqlException){
            System.out.println(sqlException);
        }
        //Returns the list of customers.
        return customerObservableList;
    }

    /** Below method not needed. Keeping just in case.
    @FXML
    private void getCustomerID(){
        int customerID = customerTable.getSelectionModel().getSelectedItem().getCustomerID();
        String customerName = customerTable.getSelectionModel().getSelectedItem().getCustomerName();

        customerIDTextField.setText(Integer.toString(customerID));
        customerNameTextField.setText(customerName);

    }**/

    /**Method go generate a list of Contacts.
     * This method generates a list of contact objects and a separate list of contact Names for use.
     */
    private void generateContactList(){

        try {
            //SQL connection and query
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts");

            //takes the records returned from the query and creates a list of contact objects
            while (resultSet.next()){
                int contactID = resultSet.getInt(1);
                String contactName = resultSet.getString(2);
                String contactEmail = resultSet.getString(3);

                //creates new contact object
                Contact contact = new Contact(contactID, contactName, contactEmail);
                contactList.add(contact); //adds contact to list of contact objects.
                contactNameList.add(contactName); //adds contact name to list of contact names.
            }
        }catch (SQLException exception){
            System.out.println(exception);
        }
    }

    /**Method to generate a list of times to select.
     * This method will generate a list of times for selection in 30 minute increments.
     */
    @FXML
    private void generateTimeList() {
        //Sets the start time for the time list
        LocalTime time = LocalTime.of(0, 0, 0);

        //increments the time in 30 minutes to add to the list for selection.
        while (!time.equals(LocalTime.of(23,30,0))){
            time = time.plusMinutes(30);
            startTimeList.add(time);
        }
    }

    /**Method used to restrict selection of end times.
     * This method reads the user defined start time and generates a new list for the end time so only later times
     * are available to select.
     */
    @FXML
    private void restrictEndTime(){
        //Beginning time for the list based on the selected start time.
        LocalTime endTime = startTimeCB.getSelectionModel().getSelectedItem();

        endTimeList.clear(); //resets end time list.

        //Adds time slots in 30 minute increments to the end time list.
        while (!endTime.equals(LocalTime.of(23,30,0))){
            endTime = endTime.plusMinutes(30);
            endTimeList.add(endTime);
        }
        endTimeCB.setItems(endTimeList); //sets the created list for use in the end time combo box.
        endTimeCB.getSelectionModel().selectFirst(); //automatically selects the first time slot in the list.

    }


    /**Method that converts time.
     *This method takes a time in string format, converts it and returns it as a LocalDateTime object for use.
     * @param time time is the time to convert.
     * @return returns LocalDateTime object.
     */
    public String timeConvert(String time){

        LocalDateTime timeToConvert = LocalDateTime.parse(time + ":00", datetimeDTF);

        //Converts local time to UTC time for database insertion.
        ZonedDateTime localZoneStart = timeToConvert.atZone(localZoneID).withZoneSameInstant(utcZoneID);

        //convert ZonedDateTime to a string for insertion into AppointmentsTableView
        System.out.println(localZoneStart.format(datetimeDTF));
        return localZoneStart.format(datetimeDTF);

    }

    /**Method used to determine if the start/end date and time is within business hours.
     * This method will check the times and make sure they are within business hours.
     *
     * @param startTimeString is the proposed start time.
     * @param endTimeString is the proposed end time.
     * @return returns true or false depending on if it is within business hours or not.
     */
    private boolean isBusinessHours(String startTimeString, String endTimeString){

        //Business Hours
        LocalDateTime businessHoursStart = LocalDateTime.parse( datePickerInput.getValue() + " 08:00:00", datetimeDTF);
        LocalDateTime businessHoursEnd = LocalDateTime.parse(datePickerInput.getValue() + " 22:00:00", datetimeDTF);

        //Business Hours Converted to UTC
        ZonedDateTime convertedBusinessHoursStart = businessHoursStart.atZone(easternZoneID).withZoneSameInstant(utcZoneID);
        ZonedDateTime convertedBusinessHoursEnd = businessHoursEnd.atZone(easternZoneID).withZoneSameInstant(utcZoneID);

        System.out.println("Converted bhs = " + convertedBusinessHoursStart);
        System.out.println("Converted bhe = " + convertedBusinessHoursEnd);


        //Appointment Time Already Converted to UTC
        LocalDateTime startTime = LocalDateTime.parse(startTimeString, datetimeDTF);
        LocalDateTime endTime = LocalDateTime.parse(endTimeString, datetimeDTF);



        //Checks to make sure the day of the week is not Saturday or Sunday.
        if (startTime.getDayOfWeek().toString().equals("SATURDAY") || startTime.getDayOfWeek().toString().equals("SUNDAY")){
            System.out.println("Business Hours Error: Appointment Cannot be on either Saturday or Sunday");
            return false;
            //Checks to make sure the hours are within business hours start time and end time.
        }else if (startTime.isBefore(ChronoLocalDateTime.from(convertedBusinessHoursStart)) || startTime.isAfter(ChronoLocalDateTime.from(convertedBusinessHoursEnd)) || endTime.isAfter(ChronoLocalDateTime.from(convertedBusinessHoursEnd))){
            System.out.println("Business Hours Error: Appointment is not within business hours");

            return false;
        }else{
            return true;
        }

    }

    /**Method that checks and makes sure there are no conflicts in the database for time selected.
     * This method will make sure there are no existing appointments within the time slot requested.
     *
     * @param newStart is the start time of the appointment to check.
     * @param newEnd is the end time of the appointment to check.
     * @throws SQLException
     */
    private boolean hasConflict(String newStart, String newEnd) throws SQLException {
        int appointmentID = -1;

        try {
            //SQL Connection and query
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM appointments WHERE ((('" + newStart + "' BETWEEN start AND end) OR ('" + newEnd + "' BETWEEN start AND end)) OR ('" + newStart + "' < start AND '" + newEnd + "' > end));");;

            //If a result is returned, this indicates there is a conflict.
            if (resultSet.next()) {
                System.out.println("Result set:" + resultSet.next());
                return true;
            } else{
                    return false;
                }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        System.out.println("it returned false");
        return false;
    }


    /**Method called to save appointment.
     * This method pulls the data from the form and inserts an appointment record into the database with those values.
     * @param event
     */
    @FXML
    private void saveAppointment(MouseEvent event) throws SQLException {

        //Values for the appointment based on data inserted into form.
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        String date = datePickerInput.getValue().toString();
        String startTime = date + " " + startTimeCB.getSelectionModel().getSelectedItem().toString();
        startTime = timeConvert(startTime); //takes the start time from above and converts it for database entry.
        String endTime = date + " " + endTimeCB.getSelectionModel().getSelectedItem().toString();
        endTime = timeConvert(endTime); //takes the end time from above and converts it for database entry.
        int customerID = Integer.parseInt(customerIDTextField.getText());
        int contactID = -1; //filler value
        String contactName = contactCB.getSelectionModel().getSelectedItem();

        //Checks if the appointment time has a conflict in the database.

        if (hasConflict(startTime, endTime)){
            //Displays error message if there is a time conflict.
            MessageBox.timeConflict();
        } else if (!isBusinessHours(startTime, endTime)) {
            //Displays error message if time is not within Business Hours.
            MessageBox.notBusinessHours();
        } else {

            try {
                //Loop to find contact name based on the contact ID of the appointment.
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactName.equals(contactList.get(i).getContactName())) {
                        contactID = contactList.get(i).getContactID();
                    }
                }
                //SQL connection and query.
                Statement statement = DBConnection.startConnection().createStatement();
                int addAppointmentToDatabase = statement.executeUpdate("insert into appointments(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)" +
                        " Values('" + title + "', '" + description + "', '" + location + "', '" + type + "', '" + startTime + "', '" + endTime + "', " + customerID + ", " + User.getUserID() + ", " + contactID + ");");

                appointmentsMainPage(event); //Returns view to the main appointments page after appointment has been inserted into database.

            } catch (SQLException | IOException exception) {
                System.out.println(exception);
            }
        }
    }

    /**Method to display MainPage on successful Login.
     This method will change the scene once the login was successful.
     @param event event is on button click of the login button.
     @throws IOException catches exception and ignores.
     */
    @FXML
    private void appointmentsMainPage(MouseEvent event) throws IOException {
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
}
