module com.example.apiweather {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.example.apiweather to javafx.fxml;
    exports com.example.apiweather;
}