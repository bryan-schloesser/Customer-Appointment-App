package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    //JDBC Connection Parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String serverName = "//wgudb.ucertify.com/WJ06Ftc";

    //JDBC URL
    private static final String jdbcURL = protocol + vendorName + serverName;

    //Driver Interface Reference
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    //User Information
    private static final String username = "U06Ftc";
    private static final String password = "53688749135";


    public static Connection startConnection()
    {
        try
        {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void closeConnection(){
        try
        {
            conn.close();
            System.out.println("Connection Closed");
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }





}
