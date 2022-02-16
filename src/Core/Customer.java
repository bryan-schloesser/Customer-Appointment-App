//Packages
package Core;

//Imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Main Class for Customer
 */
public class Customer {
    //Variables
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String division;
    private int divisionID;
    private String country;

    //Observable Lists
    private final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**Customer constructor
     *
     * @param customerID
     * @param customerName
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param division
     * @param divisionID
     * @param country
     */
    public Customer(int customerID, String customerName, String address, String postalCode, String phoneNumber,
                    String division, int divisionID, String country){
        setCustomerID(customerID);
        setCustomerName(customerName);
        setAddress(address);
        setPostalCode(postalCode);
        setPhoneNumber(phoneNumber);
        setDivision(division);
        setDivisionID(divisionID);
        setCountry(country);
    }

    //SETTERS

    /** Sets the customerID for the customer object.
     *
     * @param customerID specified customerID for the customer object.
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Sets the customer name for the customer object.
     *
     * @param customerName specified customer name for the customer object.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Sets the address for the customer object.
     *
     * @param address specified address for the customer object.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /** Sets the postal code for the customer object.
     *
     * @param postalCode specified postal code for the customer object.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** Sets the phone number for the customer object.
     *
     * @param phoneNumber specified phone number for the customer object.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /** Sets the division for the customer object.
     *
     * @param division specified division for the customer object.
     */
    public void setDivision(String division){
        this.division = division;
    }

    /** Sets the divisionID for the customer object.
     *
     * @param divisionID specified divisionID for the customer object.
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /** Sets the country for the customer object.
     *
     * @param country specified country for the customer object.
     */
    public void setCountry(String country){
        this.country = country;
    }

    //GETTERS

    /** Gets the customerID for the customer object.
     *
     * @return returns the customerID of the customer object.
     */
    public int getCustomerID() {
        return customerID;
    }

    /** Gets the customer name of the customer object
     *
     * @return returns the customer name of the customer object.
     */
    public String getCustomerName() {
        return customerName;
    }

    /** Gets the address of the customer object.
     *
     * @return returns the address of the customer object.
     */
    public String getAddress() {
        return address;
    }

    /** Gets the postal code of the customer object.
     *
     * @return returns the postal code of the customer object.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /** Gets the phone number of the customer object.
     *
     * @return returns the phone number of the customer object.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /** Gets the division of the customer object.
     *
     * @return returns the division of the customer object.
     */
    public String getDivision(){
        return division;
    }

    /** Gets the divisionID of the customer object.
     *
     * @return returns the divisionID of the customer object.
     */
    public int getDivisionID() {
        return divisionID;
    }

    /** Gets the country of the customer object.
     *
     * @return returns the country of the customer object.
     */
    public String getCountry(){
        return country;
    }


}
