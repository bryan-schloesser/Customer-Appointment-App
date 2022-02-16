module Software.C195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;
    opens Core;
    opens Controllers;
    opens Utils;
    opens FXML_Layouts;

}