module com.example.catfoxfuture {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.catfoxfuture to javafx.fxml;
    exports com.example.catfoxfuture;
}