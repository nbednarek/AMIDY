module com.example.animacjakulka {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.animacjakulka to javafx.fxml;
    exports com.example.animacjakulka;
}