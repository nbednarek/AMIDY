module com.example.listaprezentow {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.listaprezentow to javafx.fxml;
    exports com.example.listaprezentow;
}