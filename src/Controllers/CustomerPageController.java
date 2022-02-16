package Controllers;

import Core.Customer;
import Core.Location;
import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Main Class
 */
public class CustomerPageController implements Initializable {
    //Declare Variables.
    @FXML
    private Label errorMessage;
    @FXML
    private Button saveCustomerButton;
    @FXML
    private Button updateCustomerButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button deleteCustomerButton;
    @FXML
    private Button backButton;
    @FXML
    private TextField customerIDTextField;
    @FXML
    private TextField companyNameTextField;
    @FXML
    private TextField customerAddressTextField;
    @FXML
    private ComboBox<String> customerDivision;
    @FXML
    private ComboBox<String> customerCountry;
    @FXML
    private TextField customerPostalCodeTextField;
    @FXML
    private TextField customerPhoneNumberTextField;
    @FXML
    private TableView<Customer> customerTable;

    //Observable List Variables
    private final ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    private final ObservableList<String> divisionList = FXCollections.observableArrayList();
    private final ObservableList<String> countryList = FXCollections.observableArrayList();
    private final ObservableList<Location> locationObservableList = FXCollections.observableArrayList();

    /**Method to set data and style on load.
     * This method will run at the time the page loads. It Generates a list of customers from the database and sets style of page.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTable.setItems(GenerateCustomerList()); //Generates customer data and populates table.
        getLocationList(); //Gets the list of Locations for use.
        //gets the list of countries from the database and sets them for use in the form.
        customerCountry.setItems(getCountryList());
        //Gets the list of divisions from the database and sets them for use in the form.
        customerDivision.setItems(getDivisionList());
        //Disables the Save Customer Button.
        saveCustomerButton.setVisible(false);
    }

    /**Method to generate the list of Customers.
     *This method will query the database for all customers and create an Observable list from the data.
     * @return returns ObservableList of Customer data.
     */
    public ObservableList<Customer> GenerateCustomerList(){
        customerObservableList.clear();
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

    /**Method to get the list of all possible Locations.
     * This method queries the database for all possible Locations users can use.
     */
    public void getLocationList(){

        try{
            //SQL connection and query.
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet locationListData = statement.executeQuery("select fld.Division, fld.Division_ID, c.Country_ID, c.Country from first_level_divisions fld " +
                    "join countries c on c.Country_ID = fld.COUNTRY_ID;");

            //Query results are assigned to variables
            while(locationListData.next()){
                int divisionID = locationListData.getInt(2);
                String division = locationListData.getString(1);
                int countryID = locationListData.getInt(3);
                String country = locationListData.getString(4);

                //location object created and values assigned based on records returned from query
                Location location = new Location(divisionID, division, countryID, country);
                locationObservableList.add(location);
            }

        }catch(SQLException exception){
            System.out.println(exception); //SQL Exception printed to console if there are errors.
        }

    }

    /**Method to get list of all available divisions.
     * This method will return an observable list of divisions based on what country is selected.
     *
     * @return Observable list divisionList is returned.
     */
    @FXML
    public ObservableList<String> getDivisionList(){

        divisionList.clear(); //Clears division list to "refresh" the list.

        //Country to filter results by.
        String country = customerCountry.getSelectionModel().getSelectedItem();

        //Loops through the list of all available locations and only adds them to the division list
        //if the division is apart of the country.
        for (int i = 1; i < locationObservableList.size(); i++){
            if (locationObservableList.get(i).getCountry().equals(country)){
                divisionList.add(locationObservableList.get(i).getDivision());
            }
        }
        //Auto selects the first option in the list.
        customerDivision.getSelectionModel().selectFirst();

        return divisionList;
    }

    /**Method to get list of all available countries.
     *This method returns an observable list of all available options for countries.
     * @return returns observable list countryList.
     */
    public ObservableList<String> getCountryList(){

        //Loops through the list of countries to remove duplicate results.
        for (int i = 1; i < locationObservableList.size(); i++){
            if(!countryList.contains(locationObservableList.get(i).getCountry())){
                countryList.add(locationObservableList.get(i).getCountry());
            }
        }
        return countryList;
    }



    /**Method that loads customer data into the form.
     * This method, on selection of customer in the table, will load that specific Customers data into the form.
     */
    public void loadCustomerData(){

        resetTextboxBorders(); //resets color of textbox borders due to errors.

        //customer that is selected in the table and loads associated data into form.
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        customerIDTextField.setText(Integer.toString(selectedCustomer.getCustomerID()));
        companyNameTextField.setText(selectedCustomer.getCustomerName());
        customerAddressTextField.setText(selectedCustomer.getAddress());
        customerPostalCodeTextField.setText(selectedCustomer.getPostalCode());
        customerPhoneNumberTextField.setText(selectedCustomer.getPhoneNumber());

        customerCountry.setValue(selectedCustomer.getCountry());
        String division = selectedCustomer.getDivision();
        customerDivision.setValue(division);
        System.out.println(division);
    }


    /**Method that updates existing customer in the database.
     * This method will update the customer data in the database with what is currently in the form.
     * @throws SQLException
     */
    @FXML
    public void updateCustomer() {
        resetTextboxBorders(); //resets color of textbox borders due to errors.

        if (isValidText()) {
            //Customer that is selected in the table and loads associated data into form.
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            int selectedCustomerID = selectedCustomer.getCustomerID();
            String companyName = companyNameTextField.getText();
            String customerAddress = customerAddressTextField.getText();
            String postalCode = customerPostalCodeTextField.getText();
            String phoneNumber = customerPhoneNumberTextField.getText();
            String country = customerCountry.getSelectionModel().getSelectedItem();
            int division = selectedCustomer.getDivisionID();
            selectedCustomer.setCustomerName(companyNameTextField.getText());

            try {
                //SQL Connection and Update Script
                Statement statement = DBConnection.startConnection().createStatement();
                int updateCustomer = statement.executeUpdate("UPDATE customers SET customer_name = '" + companyName + "', Address = '" + customerAddress + "', Postal_Code = '" + postalCode + "', Phone = '" + phoneNumber + "', Division_ID = " + division + " where customer_id = " + selectedCustomerID + ";");
                //Resets the customer information in the form.
                customerTable.setItems(GenerateCustomerList());
            } catch (SQLException exception) {
            }

        }
    }

    /** Method that saves the customer data into the database.
     * This method will take the data inputs and insert a record into the database.
     */
    @FXML
    public void saveCustomer() {
        resetTextboxBorders(); //resets borders in case of errors.

        String companyName = companyNameTextField.getText();
        String customerAddress = customerAddressTextField.getText();
        String postalCode = customerPostalCodeTextField.getText();
        String phoneNumber = customerPhoneNumberTextField.getText();
        String country = customerCountry.getSelectionModel().getSelectedItem();
        String division = customerDivision.getSelectionModel().getSelectedItem();
        int divisionID = 0; //filler value

        //Makes sure the form is not empty before inserting data into database.
        if (isValidText()) {
            //Gets the divisionID for the division selected.
            for (int i = 0; i < locationObservableList.size(); i++) {
                if (locationObservableList.get(i).getDivision().equals(division)) {
                    divisionID = locationObservableList.get(i).getDivisionID();
                }
            }

            try {
                //SQL Connection and Insert Script
                Statement statement = DBConnection.startConnection().createStatement();
                int insertCustomer = statement.executeUpdate("insert into customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) values('" +
                        companyName + "', '" + customerAddress + "', '" + postalCode + "', '" + phoneNumber + "', " + divisionID + ");");
            } catch (SQLException exception) {
                System.out.println(exception);
            }
            //Refreshes data in the customer table
            customerTable.setItems(GenerateCustomerList());

        }
    }


    /**Method that deletes the selected customer.
     * This method will delete the customer after user confirmation.
     */
    @FXML
    public void deleteCustomer(){
        resetTextboxBorders(); //resets text boxes border color.

        //Customer to delete.
        Customer customerToDelete = customerTable.getSelectionModel().getSelectedItem();
        int customerID = Integer.parseInt(customerIDTextField.getText());

        //Confirms deletion before actually deleting.
        if(MessageBox.customerDeleteConfirm()){
            //validation that there are no empty values on form and that there are no appointments are linked to the customer.
            if (isValidText() && isAppointmentFree(customerID)) {
                try {
                    //SQL connection and delete script.
                    Statement statement = DBConnection.startConnection().createStatement();
                    int resultSet = statement.executeUpdate("Delete from customers where customer_id = " + customerID + ";");
                    MessageBox.customerSuccessfulDelete(customerToDelete.getCustomerName(), customerID);

                } catch (SQLException exception) {

                }
                //Generates new customer list to remove deleted one.
                GenerateCustomerList();
            }

        }

    }

    /**Method to verify if the customer has an existing appointment.
     * Method to check and make sure that there are no appointments linked to the customer before deleting.
     *
     * @param customerID customerID is the customer that is about to be deleted.
     * @return returns true if the customer has no appointments and false if there are any appointments.
     */
    private boolean isAppointmentFree(int customerID){

        try {
            //SQL Connection and query
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet appointments = statement.executeQuery("select * from appointments where Customer_ID = " + customerID + ";");
            int count = 0; //declaration

            //loops through query results. There should be no results if there are no appointments for the customer.
            while(appointments.next()){
                count++;
            }
            //Error will show up if there are appointments returned.
            if (count != 0){
                MessageBox.customerError(2, customerIDTextField);
                return false;
            }else{
                return true;
            }
        }catch(SQLException exception){
            System.out.println(exception);
        }

        return false;
    }

    /**Method to make sure the text fields are populated.
     * This method loops through the text fields of the form to make sure they are not empty. If so, it will
     * display an error message stating which are empty.
     *
     * @return returns true if everything is valid. Returns false if there are issues.
     */
    public boolean isValidText(){

        TextField[] fieldNames = {companyNameTextField, customerAddressTextField, customerPostalCodeTextField, customerPhoneNumberTextField};
        System.out.println(Arrays.toString(fieldNames));
        for (TextField field : fieldNames){
            if (field.getText().trim().isEmpty()){
                MessageBox.customerError(1, field);
            }
        }
        return true;
    }

    /**Method to clear the customer form for new customer entry.
     * This method will clear the form of current values to prepare for new user information.
     */
    @FXML
    public void setCustomerFields(){
        saveCustomerButton.setVisible(true);
        updateCustomerButton.setVisible(false);
        customerIDTextField.setText("Auto-Generated");
        companyNameTextField.clear();
        companyNameTextField.clear();
        customerAddressTextField.clear();
        customerPostalCodeTextField.clear();
        customerPhoneNumberTextField.clear();
        customerCountry.getSelectionModel().selectFirst();
        deleteCustomerButton.setVisible(false);

    }

    /**Method that clears the style of the text fields.
     * This method clears the style of the text fields so when there are errors, they are not consistently red colored.
     */
    private void resetTextboxBorders(){
        TextField[] fieldNames = {customerIDTextField, companyNameTextField, customerAddressTextField, customerPostalCodeTextField, customerPhoneNumberTextField};
        for (TextField field : fieldNames) {
            field.setStyle("");
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
