package Core;

public class Report {

    //Variables
    private String type;
    private String month;
    private int count;
    private String username;
    private int appointmentID;
    private String title;
    private String description;
    private String start;
    private String end;
    private int customerID;
    private String customerName;
    private String country;
    private String division;
    private int countryID;


    /**Constructor for Report.
     * This Report constructor is for the 1st report. It creates report objects with just the type, month, and count.
     *
     * @param type type is the appointment type
     * @param month month is the month the appointment is scheduled for
     * @param count count is the amount of appointments for the specific appointment type and month.
     */
    public Report(String type, String month, int count){
        setType(type);
        setMonth(month);
        setCount(count);

    }

    /**Constructor for the second Report.
     * This report constructor creates report objects for the second query.
     *
     * @param username user name for the specific report.
     * @param appointmentID appointment ID for the specific report.
     * @param title title for the specific report.
     * @param description description for the specific report.
     * @param startTime start time for the specific report.
     * @param endTime end time for the specific report.
     * @param customerID customer ID for the specific report.
     * @param customerName customer name for the specific report.
     */
    public Report(String username, int appointmentID, String title, String description, String startTime, String endTime, int customerID, String customerName){
        setUsername(username);
        setAppointmentID(appointmentID);
        setTitle(title);
        setType(type);
        setDescription(description);
        setStartTime(startTime);
        setEndTime(endTime);
        setCustomerID(customerID);
        setCustomerName(customerName);

    }

    /** Constructor for the third report.
     *
     * @param countryID
     * @param country
     * @param division
     * @param count
     */
    public Report(int countryID, String country, String division, int count){
        setCountryID(countryID);
        setCountry(country);
        setDivision(division);
        setCount(count);

    }

    /** Sets the countryID for the report object.
     *
     * @param countryID
     */
    public void setCountryID(int countryID){
        this.countryID = countryID;

    }

    /** Gets the countryID for the report object.
     *
     * @return
     */
    public int getCountryID(){
        return countryID;
    }

    /** Gets the country for the report object.
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /** Sets the country for the report object.
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /** Gets the division for the report object.
     *
     * @return
     */
    public String getDivision() {
        return division;
    }

    /** Sets the division for the report object.
     *
     * @param division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /** Gets the username for the report object.
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /** Sets the username for the report object.
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Gets the appointmentID for the report object.
     *
     * @return
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /** Sets the appointment ID for the report object.
     *
     * @param appointmentID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /** Gets the title for the report object.
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /** Sets the title for the report object.
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Gets the description for the report object.
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /** Sets the Description for the report object.
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Gets the start time for the report object.
     *
     * @return
     */
    public String getStartTime() {
        return start;
    }

    /** Sets the start time for the report object.
     *
     * @param start
     */
    public void setStartTime(String start) {
        this.start = start;
    }

    /** Gets the end time for the report object.
     *
     * @return
     */
    public String getEndTime() {
        return end;
    }

    /** Sets the end time for the report object.
     *
     * @param end
     */
    public void setEndTime(String end) {
        this.end = end;
    }

    /** Gets the customerID for the report object.
     *
     * @return
     */
    public int getCustomerID() {
        return customerID;
    }

    /** Sets the customer ID for the report object.
     *
     * @param customerID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Gets the customer name for the report object.
     *
     * @return
     */
    public String getCustomerName() {
        return customerName;
    }

    /** Sets the customer name for the report object.
     *
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Gets the type for the report object.
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /** Sets the type for the report object.
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /** Gets the month for the report object.
     *
     * @return
     */
    public String getMonth() {
        return month;
    }

    /** Sets the month for the report object.
     *
     * @param month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /** Gets the count for the report object.
     *
     * @return
     */
    public int getCount() {
        return count;
    }

    /** Sets the count for the report object.
     *
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }
}
