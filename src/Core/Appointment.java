//Packages
package Core;

/**
 * Main class for Appointment
 */
public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String startTime;
    private String endTime;
    private int customerID;
    private String customerName;
    private int userID;
    private int contactID;
    private String contactName;

    /**Main constructor for Appointment object creation.
     * This is the main Appointment object that gets created.
     *
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startTime
     * @param endTime
     * @param customerID
     * @param customerName
     * @param userID
     * @param contactID
     * @param contactName
     */
    public Appointment(int appointmentID, String title, String description, String location, String type,
                       String startTime, String endTime, int customerID, String customerName, int userID, int contactID, String contactName){
        setAppointmentID(appointmentID);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setType(type);
        setStartTime(startTime);
        setEndTime(endTime);
        setCustomerID(customerID);
        setCustomerName(customerName);
        setUserID(userID);
        setContactID(contactID);
        setContactName(contactName);


    }

    /**Secondary constructor for creation of appointment objects.
     * This constructor is used to check start/end times for conflicts in the database.
     *
     * @param appointmentID specified appointmentID for the appointment.
     * @param startTime specified startTime for the appointment.
     * @param endTime specified endTime for the appointment.
     */
    public Appointment(int appointmentID, String startTime, String endTime){
        setAppointmentID(appointmentID);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    //Setters

    /**Sets the appointmentID for the appointment object.
     *
     * @param appointmentID specified appointmentID for the appointment object.
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**Sets the title for the appointment object.
     *
     * @param title specified title for the appointment object.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**Sets the description for the appointment object.
     *
     * @param description specified description for the appointment object.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**Sets the location for the appointment object.
     *
     * @param location specified location for the appointment object.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**Sets the type for the appointment object.
     *
     * @param type specified type for the appointment object.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**Sets the start time for the appointment object.
     *
     * @param startTime specified start time for the appointment object.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**Sets the end time for the appointment object.
     *
     * @param endTime specified end time for the appointment object.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**Sets the customerID for the appointment object.
     *
     * @param customerID specified customerID for the appointment object.
     */
    public void setCustomerID(int customerID){
        this.customerID = customerID;
    }

    /**Sets the customer name for the appointment object.
     *
     * @param customerName specified customer name for the appointment object.
     */
    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    /**Sets the User ID for the appointment Object.
     *
     * @param userID specified user ID for the appointment object.
     */
    public void setUserID(int userID){
        this.userID = userID;
    }

    /** Sets the Contact ID for the appointment Object.
     *
     * @param contactID specified contactID for the appointment object.
     */
    public void setContactID(int contactID){
        this.contactID = contactID;
    }

    /** Sets the Contact Name for the appointment Object.
     *
     * @param contactName specified contact name for the appointment object.
     */
    public void setContactName(String contactName){
        this.contactName = contactName;
    }



    //Getters

    /** Gets the appointment ID for the appointment Object.
     *
     * @return returns the appointmentID for the appointment object.
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /** Gets the title for the appointment object.
     *
     * @return returns the title for the appointment object.
     */
    public String getTitle() {
        return title;
    }

    /** Gets the description for the appointment object.
     *
     * @return returns the description for the appointment object.
     */
    public String getDescription() {
        return description;
    }

    /** Gets the location for the appointment object.
     *
     * @return returns the location for the appointment object.
     */
    public String getLocation() {
        return location;
    }

    /** Gets the type for the appointment object.
     *
     * @return returns the location for the appointment object.
     */
    public String getType() {
        return type;
    }

    /** Gets the start time for the appointment object.
     *
     * @return returns the start time for the appointment object.
     */
    public String getStartTime() {
        return startTime;
    }

    /** Takes the start time and removes the date.
     *
     * @return returns only the time portion of the start time.
     */
    public String getOnlyStartTime() {

        String [] splitDateTime = startTime.split(" ", 10);
        return splitDateTime[1];
    }

    /** Gets the end time of the appointment object.
     *
     * @return returns the end time of the appointment object.
     */
    public String getEndTime(){
        return  endTime;

    }

    /** Takes the end time and removes the date.
     *
     * @return returns only the time portion of the end time.
     */
    public String getOnlyEndTime() {
        String [] splitDateTime = endTime.split(" ", 10);
        return splitDateTime[1];
    }

    /**Takes the start time and removes the time portion. Only keeps the date.
     *
     * @return returns only the date portion of the start time.
     */
    public String getDate(){
        String [] splitDateTime = startTime.split(" ", 10);
        return splitDateTime[0];
    }

    /** Gets the customer ID for the appointment object.
     *
     * @return returns the customerID of the appointment object.
     */
    public int getCustomerID(){
        return customerID;
    }

    /** Gets the customer name of the appointment object.
     *
     * @return returns the customer name of the appointment object.
     */
    public String getCustomerName(){
        return customerName;
    }

    /** Gets the UserID of the appointment object.
     *
     * @return returns the userID of the appointment object.
     */
    public int getUserID(){
        return userID;
    }

    /** Gets the contactID for the appointment object.
     *
     * @return returns the contactID of the appointment object.
     */
    public int getContactID(){
        return contactID;
    }

    /** Gets the contact name of the appointment object.
     *
     * @return returns the contact name of the appointment object.
     */
    public String getContactName(){
        return contactName;
    }

}
