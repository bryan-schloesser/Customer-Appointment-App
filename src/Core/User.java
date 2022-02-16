package Core;

public class User {

    //Variables
    private static int userID;
    private static String username;
    private static String password;

    /** User Constructor.
     * This constructor is used at login to keep track of the user who is currently logged in.
     *
     * @param userID userID of the user that is currently logged in.
     * @param username username of the user that is currently logged in.
     * @param password password of the user that is currently logged in.
     */
    public User(int userID, String username, String password){
        setUserID(userID);
        setUsername(username);
        setPassword(password);

    }

    //SETTERS

    /** Sets the user ID for the user object.
     *
     * @param userID
     */
    public static void setUserID(int userID){
        User.userID = userID;
    }

    /** Sets the username for the user object.
     *
     * @param username
     */
    public static void setUsername(String username){
        User.username = username;
    }

    /** Sets the password for the user object.
     *
     * @param password
     */
    public static void setPassword(String password){
        User.password = password;
    }

    //GETTERS

    /** Gets the userID for the user object.
     *
     * @return
     */
    public static int getUserID(){
        return userID;
    }

    /** Gets the username for the user object.
     *
     * @return
     */
    public static String getUsername(){
        return User.username;
    }

    /** Gets the password for the user object.
     *
     * @return
     */
    public static String getPassword(){
        return password;
    }


}
