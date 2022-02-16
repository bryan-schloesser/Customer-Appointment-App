package Controllers;

//Imports
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

/**
 * Main MessageBox Class
 */
public class MessageBox {

    /** Method that alerts when there is a time conflict due to existing appointment.
     * This method handles informing the user that the appointment time cannot be chosen due to an existing appointment
     * already scheduled for that time slot.
     */
    public static void timeConflict(){
        Alert timeConflict = new Alert(Alert.AlertType.ERROR);
        timeConflict.setTitle("Appointment Time Error");
        timeConflict.setHeaderText("Timing Conflict");
        timeConflict.setContentText("The appointment time is during another appointment. Please choose a different time.");
        timeConflict.showAndWait();
    }

    public static void notBusinessHours(){
        Alert notBusinessHours = new Alert(Alert.AlertType.ERROR);
        notBusinessHours.setTitle("Appointment Time Error");
        notBusinessHours.setHeaderText("Appointment Time Not During Business Hours");
        notBusinessHours.setContentText("The proposed appointment time is not during Business Hours.");
        notBusinessHours.showAndWait();
    }

    /** Method that notifies the user of appointment within the next 15 minutes at login.
     * This method handles informing the user if there is an appointment within the next 15 minutes of their login.
     *
     * @param appointmentID appointmentID of the appointment scheduled in the next 15 minutes of the user login.
     * @param startTime start time of the appointment scheduled in the next 15 minutes of the user login.
     * @param endTime end time of the appointment scheduled in the next 15 minutes of the user login.
     */
    public static void appointmentLoginNotification(int appointmentID, String startTime, String endTime){
        Alert appointmentNotification = new Alert(Alert.AlertType.INFORMATION);
        appointmentNotification.setTitle("Appointment Notify");
        appointmentNotification.setHeaderText("Appointment ID: " + appointmentID + ". start time: " + startTime + ". end time: " + endTime + ".");
        appointmentNotification.showAndWait();
    }

    /** Method to inform user that no appointments are coming up in the next 15 minutes.
     * This method handles notifying the user, at login, that there are no upcoming appointments in the next
     * 15 minutes from login.
     */
    public static void  appointmentNotificationNone(){
        Alert appointmentNotification = new Alert(Alert.AlertType.INFORMATION);
        appointmentNotification.setTitle("No Upcoming Appointments.");
        appointmentNotification.setHeaderText("There are no appointments coming up in the next 15 minutes.");
        appointmentNotification.showAndWait();
    }

    /** Method to handle errors when adding/modifying customer data.
     * This method will get called with a specified case. Depending on the case integer passed, it will
     * display a different error.
     * @param errorNum errorNum is the case number for the error that needs to be displayed.
     * @param fieldname fieldname is the field that the error is focused on.
     */
    public static void customerError(int errorNum, TextField fieldname) {

        fieldError(fieldname); //method called to highlight the field that has an error.

        Alert customerError = new Alert(Alert.AlertType.ERROR);
        customerError.setTitle("Customer Handling Error");
        customerError.setHeaderText("Some customer information is empty. Please enter all required information.");

        //Switch statement to display specific error based on the case.
        switch(errorNum) {
            case 1: {
                customerError.setContentText(fieldname.getId() + " is blank. Please enter a value.");
                break;
            }
            case 2: {
                customerError.setContentText("Customer cannot be deleted due to existing appointments.");
                break;
            }
            default: {
                customerError.setContentText("default case.");
                break;
            }
        }
        customerError.showAndWait(); //displays error until user confirms.

    }

    /** Method that handles confirmation of an appointment deletion request.
     * This method will display an confirmation window before it deletes the selected appointment.
     *
     * @return
     */
    public static boolean appointmentDeleteConfirm(){
        Alert deleteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirm.setTitle("Confirm Appointment Deletion");
        deleteConfirm.setHeaderText("Are you sure you would like to delete the a selected appointment?");
        deleteConfirm.setContentText("Click OK to confirm");
        Optional<ButtonType> result = deleteConfirm.showAndWait();
        return result.get() == ButtonType.OK;

    }

    /** Method that handles confirmation of a customer deletion request.
     *  this method will display a confirmation window before it deletes the selected customer.
     *
     * @return
     */
    public static boolean customerDeleteConfirm(){
        Alert deleteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirm.setTitle("Confirm Customer Deletion");
        deleteConfirm.setHeaderText("Are you sure you would like to delete the selected Customer?");
        deleteConfirm.setContentText("Click OK to confirm");
        Optional<ButtonType> result = deleteConfirm.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /** Method that handles informing the user that the selected appointment was deleted.
     *  This method will let the user know the appointment was deleted and display the appointment ID
     *  and type of said appointment.
     * @param appointmentType Type of appointment for the deleted appointment.
     * @param appointmentID Appointment ID of the deleted appointment.
     */
    public static void appointmentSuccessfulDelete(String appointmentType, int appointmentID){
        Alert deleteSuccess = new Alert(Alert.AlertType.INFORMATION);
        deleteSuccess.setTitle("Appointment Successfully Deleted");
        deleteSuccess.setHeaderText("The appointment was deleted successfully");
        deleteSuccess.setContentText(appointmentType + " Appointment (Appointment ID: " + appointmentID + ") was deleted");
        Optional<ButtonType> okay = deleteSuccess.showAndWait();

    }

    /** Method that handles informing the user that the selected customer was deleted.
     * This method will let the user know the customer was deleted and display the customer ID
     * and customer name of said customer.
     *
     * @param customerName Name of the customer that was deleted.
     * @param customerID customerID of the customer that was deleted.
     */
    public static void customerSuccessfulDelete(String customerName, int customerID){
        Alert deleteSuccess = new Alert(Alert.AlertType.INFORMATION);
        deleteSuccess.setTitle("Customer Successfully Deleted");
        deleteSuccess.setHeaderText("The Customer was deleted successfully");
        deleteSuccess.setContentText(customerName + "(Customer ID: " + customerID + ") was deleted");
        Optional<ButtonType> okay = deleteSuccess.showAndWait();

    }

    /** Method that displays error due to being unable to delete an appointment.
     * This method notifies the user that the appointment that was suppose to be deleted, could not be.
     */
    public static void appointmentError(){
        Alert appointmentError = new Alert(Alert.AlertType.ERROR);
        appointmentError.setTitle("Error Deleting Appointment");
        appointmentError.setHeaderText("Could not delete appointment. Please select a valid appointment to delete.");
        appointmentError.showAndWait();
    }

    /** Method that changes the border of a textfield to red.
     * This method will receive the name of a textfield to change the border of. This is used on errors so the user
     * knows what fields they need to change.
     *
     * @param fieldname fieldname of the textfield with invalid data.
     */
    private static void fieldError(TextField fieldname){
        fieldname.setStyle("-fx-border-color:red");
    }

}
