//Packages
package Controllers;

//Imports
import Core.Report;
import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
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
 * Main Class
 */
public class ReportsPageController implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    private Button backButton;
    @FXML
    private TableView<Report> reportTable1;
    @FXML
    private TableView<Report> reportTable2;
    @FXML
    private TableView<Report> reportTable3;

    //Observable Lists
    private ObservableList<Report> reportsList = FXCollections.observableArrayList();

    //Variables for date/time use
    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");


    /**Method that runs at page load.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //automatically runs the first report to populate the report table at form load.
        reportTable1.setItems(genReportTab1());

    }


    /**Method that chooses what report to run.
     * This method will determine which tab was clicked. It will then run the specific method associated
     * with that tab.
     *
     * @param event event is on mouse click of one of the tabs in the tab pane.
     */
    @FXML
    private void runReport(MouseEvent event){

        switch(tabPane.getSelectionModel().getSelectedItem().getId()) {
            case "reportTab1":  //Report to display the total number of customer appointments by type and month.
                reportTable1.setItems(genReportTab1());
                break;
            case "reportTab2": //Report to display list of appointments ordered by user and scheduled time.
                reportTable2.setItems(genReportTab2());
                break;
            case "reportTab3": //Report to get the amount of customers per region.
                reportTable3.setItems(genReportTab3());
                break;
        }

    }

    /**Method to convert database time to local time of user.
     * Method will convert the time from the database to the time of the user.
     *
     * @param time time is the time to convert
     * @return returns converted time.
     */
    public String timeConvert(String time){
        //convert database UTC to LocalDateTime
        LocalDateTime utcStartDT = LocalDateTime.parse(time, datetimeDTF);

        //convert times UTC zoneId to local zoneId
        ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

        //convert ZonedDateTime to a string for insertion into AppointmentsTableView

        return localZoneStart.format(datetimeDTF);

    }

    /**Method that generates the report to display the total number of customer appointments by type and month.
     * This method will query the database for the total number of customer appointments by type and month.
     * It will then display it for user viewing.
     *
     * @return returns the list reportsList to display.
     */
    private ObservableList<Report> genReportTab1(){
        try {
            reportsList.clear(); //clears the reportsList so only one query result is posted.
            //SQL database connection and query.
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet results = statement.executeQuery("select date_format(start, \"%M %Y\") AS Month, Type, Count(*) As Count from appointments group by date_format(start, \"%M %Y\"), Type order by date_format(start, \"%M %Y\") desc;");

            //Query results placed in variables to be created as reports objects. Then those report objects are added to the report list.
            while (results.next()){
                String month = results.getString(2);
                String type = results.getString(1);
                int count = results.getInt(3);

                //Report object to be added to the reportsList
                Report report = new Report(month, type, count);

                reportsList.add(report);
            }
            //Returns full query as a list of report objects.
            return reportsList;

        }catch(SQLException exception){
            System.out.println(exception);
        }

        return reportsList;
    }

    /**Method that generates the report to display list of appointments ordered by user and scheduled time.
     * This method will query the database for appointments ordered by user and scheduled time. It will then
     * display them for user viewing.
     *
     * @return returns the report list.
     */
    private ObservableList<Report> genReportTab2(){
        try {
            reportsList.clear(); //clears the report list so only one query result is listed.

            //SQL connection and query.
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet results = statement.executeQuery("select u.User_Name, a.Appointment_ID, a.title, a.type, a.description, a.start, a.end, c.customer_id, c.customer_name" +
                    " from appointments a join users u on u.User_ID = a.User_ID join customers c on c.customer_id = a.Customer_ID order by user_name, start;");

            //Results are stored in variables and then report objects are created out of them. Then those objects are stored
            //to the report list.
            while (results.next()){
                String username = results.getString(1);
                int appointmentID = results.getInt(2);
                String title = results.getString(3);
                String type = results.getString(4);
                String description = results.getString(5);
                String startTime = results.getString(6);
                String [] splitDateTimeStart = startTime.split(" ", 5);
                startTime = splitDateTimeStart[0] + " " + splitDateTimeStart[1];
                String endTime = results.getString(7);
                String [] splitDateTimeEnd = endTime.split(" ", 5);
                endTime = splitDateTimeEnd[0] + " " + splitDateTimeEnd[1];
                int customerID = results.getInt(8);
                String customerName = results.getString(9);

                //Report object created per record in query result.
                Report report = new Report(username, appointmentID, title, description, timeConvert(startTime), timeConvert(endTime), customerID, customerName);

                //Each report object is added to the report list.
                reportsList.add(report);
            }

            return reportsList;

        }catch(SQLException exception){
            System.out.println(exception);
        }

        return reportsList;

    }

    /**Method to display the report of how many customers there are per region.
     * This method will query the database for how many customers there are per region and display it for user viewing.
     *
     * @return returns the report list.
     */
    private ObservableList<Report> genReportTab3(){
        try {
            reportsList.clear(); //clears the report list so only one query result is displayed.
            //SQL connection and query
            Statement statement = DBConnection.startConnection().createStatement();
            ResultSet results = statement.executeQuery("select c.Country_ID, c.Country, d.Division, count(division) from countries c " +
                    "join first_level_divisions d on c.Country_ID = d.COUNTRY_ID join customers cu on cu.Division_ID = d.Division_ID " +
                    "group by c.Country_ID, c.Country, d.Division;");

            //Stores the records returned from the query in variables for use to store them as report objects to be then
            //added to the report list.
            while (results.next()){
                int countryID = results.getInt(1);
                String country = results.getString(2);
                String division = results.getString(3);
                int count = results.getInt(4);

                //Report object created per record returned from database.
                Report report = new Report(countryID, country, division, count);

                reportsList.add(report);
            }
            return reportsList;

        }catch(SQLException exception){
            System.out.println(exception);
        }

        return reportsList;

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
