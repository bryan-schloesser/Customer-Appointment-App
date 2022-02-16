//Packages
package Core;

/**
 * Main Class for Contact
 */
public class Contact {

    //Variables
    private int contactID;
    private String contactName;
    private String contactEmail;

    /**Contact constructor
     *
     * @param contactID
     * @param contactName
     * @param contactEmail
     */
    public Contact(int contactID, String contactName, String contactEmail){
        setContactID(contactID);
        setContactName(contactName);
        setContactEmail(contactEmail);

    }

    /** Gets the contactID for the contact object.
     *
     * @return returns the contactID for the contact object.
     */
    public int getContactID() {
        return contactID;
    }

    /** Sets the contactID for the contact object.
     *
     * @param contactID specified contactID for the contact object.
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /** Gets the contact name of the contact object.
     *
     * @return returns the contactName for the contact object.
     */
    public String getContactName() {
        return contactName;
    }

    /** Sets the contact name for the contact object.
     *
     * @param contactName specified contact name for the contact object.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** Gets the contact email for the contact object.
     *
     * @return returns the contact email for the contact object.
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /** Sets the contact email for the contact object.
     *
     * @param contactEmail specified email for the contact object.
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
