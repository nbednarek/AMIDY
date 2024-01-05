module com.example.nowyrokodliczanie {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.nowyrokodliczanie to javafx.fxml;
    exports com.example.nowyrokodliczanie;
}