module com.example.messengerserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.messengerserver to javafx.fxml;
    exports com.example.messengerserver;
}