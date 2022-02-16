package Controllers;

import Core.Appointment;
import Core.Contact;
import Core.Customer;
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
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Main Class
 */
public class AppointmentsUpdatePageController implements Initializable {
    @FXML
    private TableView<Customer> customerTable;
    private final ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> startTimeList = FXCollections.observableArrayList();
    private final ObservableList<LocalTime> endTimeList = FXCollections.observableArrayList();
    private final ObservableList<Contact> contactList = FXCollections.observableArrayList();
    private final ObservableList<String> contactNameList = FXCollections.observableArrayList();

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

    //Appointment to update
    Appointment appointmentToUpdate;

    //Variables used for date/time use.
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");
    private final ZoneId easternZoneID = ZoneId.of("US/Eastern");

    /**Method that runs once the page is loaded.
     * This method calls the GenerateCustomerList method to generate a list of customers and adds them to
     * the customer table for use on the form.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTable.setItems(GenerateCustomerList());
        loadAppointmentData();
        generateTimeList();
        generateContactList();
        contactCB.setItems(contactNameList);
        startTimeCB.setItems(startTimeList);
        contactCB.getSelectionModel().select(getContactName());



    }

    /**Method that retrieves the contact name of the appointment that needs to be updated.
     * This method looks through the list of contacts to find a matching contact ID of the appointment to update.
     * It will then take that contact from the list and return the contact name.
     *
     * @return returns the contact name.
     */
    private String getContactName(){
        String contactName = ""; //Filler declaration.

        for(int i = 0; i < contactList.size(); i++){
            if (contactList.get(i).getContactID() == appointmentToUpdate.getContactID()){
                contactName = contactList.get(i).getContactName();
                return contactName;
            }
        }
        return contactName;
    }


    /**Method to load appointment data in the form.
     * This method will take the data from the appointment specified to update and fill the form with its information.
     */
    private void loadAppointmentData(){
        appointmentIDTextField.setText(Integer.toString(appointmentToUpdate.getAppointmentID()));
        titleTextField.setText(appointmentToUpdate.getTitle());
        descriptionTextField.setText(appointmentToUpdate.getDescription());
        typeTextField.setText(appointmentToUpdate.getType());
        datePickerInput.setValue(LocalDate.parse(appointmentToUpdate.getDate()));
        startTimeCB.getSelectionModel().select(LocalTime.parse(appointmentToUpdate.getOnlyStartTime()));
        endTimeCB.getSelectionModel().select(LocalTime.parse(appointmentToUpdate.getOnlyEndTime()));
        customerIDTextField.setText(Integer.toString(appointmentToUpdate.getCustomerID()));
        customerNameTextField.setText(appointmentToUpdate.getCustomerName());
        locationTextField.setText(appointmentToUpdate.getLocation());


    }

    /**Method to pull in the appointment object information.
     * This method is called to bring in the appointment object that needs to be updated for use.
     *
     * @param appointmentToUpdate appointmentToUpdate is the appointment object that needs to be updated.
     */
    public AppointmentsUpdatePageController(Appointment appointmentToUpdate){
        this.appointmentToUpdate = appointmentToUpdate;
    }


    /**Method to generate the list of Customers.
     *This method will query the database for all customers and create an Observable list from the data.
     * @return returns ObservableList of Customer data.
     */
    public ObservableList<Customer> GenerateCustomerList(){
        //customerObservableList.clear();
        try {
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

    /**Method to get the customerID and Name.
     *This method takes the customerID and name and populates the text fields.
     */
    @FXML
    private void getCustomerID(){
        int customerID = customerTable.getSelectionModel().getSelectedItem().getCustomerID();
        String customerName = customerTable.getSelectionModel().getSelectedItem().getCustomerName();

        customerIDTextField.setText(Integer.toString(customerID));
        customerNameTextField.setText(customerName);

    }

    /**Method that generates the contact list.
     * This method generates the contact list from the database. This used so contacts are not hard coded. They
     * only need to be inserted into the database.
     *
     */
    private void generateContactList(){

        try {
            //SQL connection and query.
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts");

            //Results from the query are used to create contact objects and then those are added to the contact list.
            while (resultSet.next()){
                int contactID = resultSet.getInt(1);
                String contactName = resultSet.getString(2);
                String contactEmail = resultSet.getString(3);

                Contact contact = new Contact(contactID, contactName, contactEmail);
                contactList.add(contact);
                contactNameList.add(contactName);

            }


        }catch (SQLException exception){
            System.out.println(exception);
        }


    }

    /**Method used to generate the list of times for appointments.
     * This will generate the list of start times for appointments in 30 minute increments.
     */
    @FXML
    private void generateTimeList() {
        //Start time
        LocalTime time = LocalTime.of(0, 0, 0);

        //Loops and adds a new time to the startTimesList in 30 minute increments until 23:30
        while (!time.equals(LocalTime.of(23,30,0))){
            time = time.plusMinutes(30);
            startTimeList.add(time);
        }

    }

    /**Method used to restrict end time selection for appointments.
     * This will take the start time selected and only allow the end time to be greater than the start time.
     */
    @FXML
    private void restrictEndTime(){
        //endTime is going to start off as the same value as the start time.
        LocalTime endTime = startTimeCB.getSelectionModel().getSelectedItem();

        endTimeList.clear(); //clears list with each call.

        //adds an end time to the list based on the start time and increments times by 30 minutes until 23:30.
        while (!endTime.equals(LocalTime.of(23,30,0))){
            endTime = endTime.plusMinutes(30);
            endTimeList.add(endTime);
        }

        //sets the combo box with the end time list.
        endTimeCB.setItems(endTimeList);
        //Auto selects the first to make sure the selection always has a value.
        endTimeCB.getSelectionModel().selectFirst();

    }

    /**Method that converts the times from local time to database time.
     * This method takes a time value and converts it to database time for insertion into the database.
     *
     * @param time is time value to be converted.
     * @return returns the time formated.
     */
    public String timeConvert(String time){

        LocalDateTime timeToConvert = LocalDateTime.parse(time + ":00", datetimeDTF);

        //convert times UTC zoneId to local zoneId
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
        LocalDateTime businessHoursStart = LocalDateTime.parse( appointmentToUpdate.getDate() + " 08:00:00", datetimeDTF);
        LocalDateTime businessHoursEnd = LocalDateTime.parse(appointmentToUpdate.getDate() + " 22:00:00", datetimeDTF);

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
        int appointmentID = appointmentToUpdate.getAppointmentID();

        try {
            //SQL Connection and query
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM appointments WHERE (((('" + newStart + "' BETWEEN start AND end) OR ('" + newEnd + "' BETWEEN start AND end)) OR ('" + newStart + "' < start AND '" + newEnd + "' > end)) AND Appointment_ID != " + appointmentID + ");");
            //If a result is returned, this indicates there is a conflict.d
            if (resultSet.next()) {
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


    /**Method called to save the appointment.
     * Method will take the data in the form and attempt to save the appointment after validation checks.
     * @param event event is on click of save button.
     * @throws SQLException
     */
    @FXML
    private void saveAppointment(MouseEvent event) throws SQLException {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        String date = datePickerInput.getValue().toString();
        System.out.println(date);
        String startTime = date + " " + startTimeCB.getSelectionModel().getSelectedItem().toString();
        startTime = timeConvert(startTime);
        String endTime = date + " " + endTimeCB.getSelectionModel().getSelectedItem().toString();
        endTime = timeConvert(endTime);
        int customerID = Integer.parseInt(customerIDTextField.getText());
        int contactID = appointmentToUpdate.getContactID();
        String contactName = contactCB.getSelectionModel().getSelectedItem();

        //Checks to make sure there are no time conflicts and that it is within business hours
        if (hasConflict(startTime, endTime)){
            //Displays error message if there is a time conflict.
            MessageBox.timeConflict();
        } else if (!isBusinessHours(startTime, endTime)) {
            //Displays error message if time is not within Business Hours.
            MessageBox.notBusinessHours();
        } else {

            try {

                //Finds the contactID for the contact attached to appointment.
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactName.equals(contactList.get(i).getContactName())) {
                        contactID = contactList.get(i).getContactID();
                    }
                }

                //SQL connection and update script.
                Statement statement = DBConnection.startConnection().createStatement();
                //System.out.println("UPDATE appointments SET title = '" + title + "', description = '" + description + "', location = '" + location + "', type = '" + type + "', Start = '" + date + " " + startTime + "', End = '" + date + " " + endTime + "', Customer_ID = " + customerID + ", Contact_ID = " + contactID + " where appointment_id = " + appointmentToUpdate.getAppointmentID() + ";");
                int update = statement.executeUpdate("UPDATE appointments SET title = '" + title + "', description = '" + description + "', location = '" + location + "', type = '" + type + "', Start = '" + startTime + "', End = '" + endTime + "', Customer_ID = " + customerID + ", Contact_ID = " + contactID + " where appointment_id = " + appointmentToUpdate.getAppointmentID() + ";");

                appointmentsMainPage(event);

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
