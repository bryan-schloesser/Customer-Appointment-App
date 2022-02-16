//Packages
package Core;

/**
 * Main Class for Location
 */
public class Location {

    //Variables
    private int divisionID;
    private String division;
    private int countryID;
    private String country;

    /**Main Location Constructor
     *
     * @param divisionID
     * @param division
     * @param countryID
     * @param country
     */
    public Location(int divisionID, String division, int countryID, String country){
        setDivisionID(divisionID);
        setDivision(division);
        setCountryID(countryID);
        setCountry(country);
    }

    //Getters

    /** Gets the divisionID for the location object.
     *
     * @return returns the divisionID of the location object.
     */
    public int getDivisionID() {
        return divisionID;
    }

    /** Gets the division for the location object.
     *
     * @return returns the division of the division object.
     */
    public String getDivision() {
        return division;
    }

    /** Gets the country ID for the location object.
     *
     * @return returns the countryID for the location object.
     */
    public int getCountryID() {
        return countryID;
    }

    /** Gets the country for the location object.
     *
     * @return returns the country for the location object.
     */
    public String getCountry() {
        return country;
    }

    //Setters

    /** Sets the divisionID for the location object.
     *
     * @param divisionID specified divisionID for the location object.
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /** Sets the division for the location object.
     *
     * @param division specified division for the location object.
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /** Sets the countryID for the the location object.
     *
     * @param countryID specified countryID for the location object.
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /** Sets the country for the location object.
     *
     * @param country specified country for teh location object.
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
