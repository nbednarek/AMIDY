package com.example.formularzFX;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class FormularzFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Text imie = new Text("Imie");
        TextField imieText = new TextField();

        Text nazwisko = new Text("Nazwisko");
        TextField nazwiskoText = new TextField();

        Text adres = new Text("Adres");
        TextField adresText = new TextField();

        Text miejscowosc = new Text("Miejscowość");
        TextField miejscowoscText = new TextField();

        Text telefon = new Text("Telefon");
        TextField telefonText = new TextField();

        Text email = new Text("Email");
        TextField emailText = new TextField();

        Button przycisk = new Button("Zatwierdź");

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(500, 500);

        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.setVgap(25);
        gridPane.setHgap(20);

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(imie, 0, 0);
        gridPane.add(imieText, 1, 0);

        gridPane.add(nazwisko, 0, 1);
        gridPane.add(nazwiskoText, 1, 1);

        gridPane.add(adres, 0, 2);
        gridPane.add(adresText, 1, 2);

        gridPane.add(miejscowosc, 0, 3);
        gridPane.add(miejscowoscText, 1, 3);

        gridPane.add(telefon, 0, 4);
        gridPane.add(telefonText, 1, 4);

        gridPane.add(email, 0, 5);
        gridPane.add(emailText, 1, 5);

        gridPane.add(przycisk, 1, 6);

        //style dla tekstów, przycisku i kolorów

        imie.setStyle("-fx-font: normal bold 15px 'Arial'");
        nazwisko.setStyle("-fx-font: normal bold 15px 'Arial'");
        adres.setStyle("-fx-font: normal bold 15px 'Arial'");
        miejscowosc.setStyle("-fx-font: normal bold 15px 'Arial'");
        telefon.setStyle("-fx-font: normal bold 15px 'Arial'");
        email.setStyle("-fx-font: normal bold 15px 'Arial'");
        przycisk.setStyle("-fx-background-color: WHITE; -fx-text-fill: BLACK;");

        gridPane.setStyle("-fx-background-color: MOCCASIN;");


        Scene scene = new Scene(gridPane);

        stage.setTitle("Formularz JavaFX");
        stage.setScene(scene);
        stage.show();


        przycisk.setOnAction(actionEvent -> {
            if (prawidloweWartosci(imieText.getText(), nazwiskoText.getText(), adresText.getText(),
                    miejscowoscText.getText(), telefonText.getText(), emailText.getText())) {

                informacja2("Formularz wypełniony prawidłowo!");
            } else {
                informacja("Proszę wypełnić wszystkie pola zgodnie z wymaganiami.");
            }
        });

    }

    private boolean prawidloweWartosci(String imie, String nazwisko, String adres, String miejscowosc, String telefon, String email) {
        boolean prawidloweImie = prawidloweImie(imie);
        boolean prawidloweNazwisko = prawidloweNazwisko(nazwisko);
        boolean prawidlowyAdres = !adres.isEmpty();
        boolean prawidlowaMiejscowosc = prawidlowaMiejscowosc(miejscowosc);
        boolean prawidlowyTelefon = prawidlowyTelefon(telefon);
        boolean prawidlowyEmail = prawidlowyEmail(email);

        return prawidloweImie && prawidloweNazwisko && prawidlowyAdres
                && prawidlowaMiejscowosc && prawidlowyTelefon && prawidlowyEmail;
    }

    private boolean prawidloweImie(String imie) {
        return imie.matches("[a-zA-Z]+") && imie.length() >= 2;
    }

    private boolean prawidloweNazwisko(String nazwisko) {
        return nazwisko.matches("[a-zA-Z]+") && nazwisko.length() >= 2;
    }

    private boolean prawidlowaMiejscowosc(String miejscowosc) {
        return miejscowosc.matches("[a-zA-Z]+") && miejscowosc.length() >= 3;
    }

    private boolean prawidlowyTelefon(String telefon) {
        return telefon.matches("\\d{9,}");
    }

    private boolean prawidlowyEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private void informacja(String blad) {
        Alert informacja = new Alert(Alert.AlertType.ERROR);
        informacja.setTitle("Błąd");
        informacja.setHeaderText(null);
        informacja.setContentText(blad);
        informacja.showAndWait();
    }

    private void informacja2(String ok){
        Alert informacja2 = new Alert(Alert.AlertType.INFORMATION);
        informacja2.setTitle("Wypełniono formularz");
        informacja2.setHeaderText(null);
        informacja2.setContentText(ok);
        informacja2.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}