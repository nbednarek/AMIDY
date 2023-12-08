package com.example.listaprezentow;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ListaPrezentow extends Application {
    private Worek worek = new Worek();
    private ListView<Prezent> listaPrezentow;
    private Label sumaLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ListaPrezentow");

        // przyciski
        Button dodajButton = new Button("Dodaj prezent");
        Button usunButton = new Button("Usuń prezent");
        Button wymarzonyButton = new Button("Oznacz wymarzony prezent");
        Button dostarczButton = new Button("Dostarcz prezenty");
        sumaLabel = new Label("Suma wartości worka: ");

        listaPrezentow = new ListView<>();
        listaPrezentow.setItems(worek.getListaPrezentow());
        listaPrezentow.setPrefWidth(300);

        // przypisywanie metody do przycisku
        dodajButton.setOnAction(e -> pokazOknoDodawaniaPrezentu());
        usunButton.setOnAction(e -> UsuwaniePrezentu());
        wymarzonyButton.setOnAction(e -> OznaczaniaWymarzonegoPrezentu());
        dostarczButton.setOnAction(e -> dostarczPrezenty());

        // okno
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.getChildren().addAll(dodajButton, usunButton, wymarzonyButton, dostarczButton, sumaLabel, listaPrezentow);

        // style dla aplikacji
        vBox.setStyle("-fx-background-color: #f7d5d5;");
        dodajButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        usunButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        wymarzonyButton.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
        dostarczButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        dodajButton.setStyle("-fx-background-color: #ffffff;");
        sumaLabel.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; ");

        Scene scene = new Scene(vBox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void pokazOknoDodawaniaPrezentu() {
        Dialog<Prezent> dialog = new Dialog<>();
        dialog.setTitle("Dodaj prezent");
        dialog.setHeaderText("Wprowadź dane prezentu:");

        // Ustawienie rodzajów przycisków
        ButtonType dodajButton = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(dodajButton, ButtonType.CANCEL);

        // układ
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nazwaField = new TextField();
        TextField opisField = new TextField();
        TextField cenaField = new TextField();

        grid.add(new Label("Nazwa:"), 0, 0);
        grid.add(nazwaField, 1, 0);
        grid.add(new Label("Opis:"), 0, 1);
        grid.add(opisField, 1, 1);
        grid.add(new Label("Cena:"), 0, 2);
        grid.add(cenaField, 1, 2);
        dialog.getDialogPane().setContent(grid);

        // Konwersja wyniku na obiekt Prezent po naciśnięciu przycisku "Dodaj".
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dodajButton) {
                try {
                    String nazwa = nazwaField.getText();
                    String opis = opisField.getText();
                    double cena = Double.parseDouble(cenaField.getText());
                    return new Prezent(nazwa, opis, cena);
                } catch (NumberFormatException e) {
                    alert("Błąd", "Wprowadź poprawną cenę.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(prezent -> {
            if (prezent != null) {
                worek.dodajPrezent(prezent);
                sumaWartosci();
            }
        });
    }

    private void UsuwaniePrezentu() {
        Prezent wybranyPrezent = listaPrezentow.getSelectionModel().getSelectedItem();
        if (wybranyPrezent != null) {
            worek.usunPrezent(wybranyPrezent);
            sumaWartosci();
        } else {
            alert("Błąd", "Wybierz prezent do usunięcia.");
        }
    }

    private void OznaczaniaWymarzonegoPrezentu() {
        Prezent wybranyPrezent = listaPrezentow.getSelectionModel().getSelectedItem();
        if (wybranyPrezent != null) {
            worek.oznaczWymarzonyPrezent(wybranyPrezent);
            sumaWartosci();
        } else {
            alert("Błąd", "Wybierz prezent do oznaczenia jako wymarzony.");
        }
    }

    private void dostarczPrezenty() {
        worek.clearPresents();
        sumaWartosci();
    }

    private void sumaWartosci() {
        double sumaWartosci = worek.obliczSumeWartosci();
        sumaLabel.setText("Suma wartości worka: " + sumaWartosci);
    }

    private void alert(String tytul, String tresc) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(tytul);
        alert.setHeaderText(null);
        alert.setContentText(tresc);
        alert.showAndWait();
    }

    public static class Prezent {
        private static int nextId = 1;
        private int id;
        private String nazwa;
        private String opis;
        private double cena;
        private boolean wymarzony;

        public Prezent(String nazwa, String opis, double cena) {
            this.id = nextId++;
            this.nazwa = nazwa;
            this.opis = opis;
            this.cena = cena;
            this.wymarzony = false;
        }

        public int getId() {
            return id;
        }

        public String getNazwa() {
            return nazwa;
        }

        public String getOpis() {
            return opis;
        }

        public double getCena() {
            return cena;
        }

        public boolean isWymarzony() {
            return wymarzony;
        }

        public void setWymarzony(boolean wymarzony) {
            this.wymarzony = wymarzony;
        }

        @Override
        public String toString() {
            return nazwa + " - " + opis; // dodano opis do wyświetlania w liście
        }
    }

    public static class Worek {
        private ObservableList<Prezent> listaPrezentow = FXCollections.observableArrayList();

        public ObservableList<Prezent> getListaPrezentow() {
            return listaPrezentow;
        }

        public void dodajPrezent(Prezent prezent) {
            listaPrezentow.add(prezent);
        }

        public void usunPrezent(Prezent prezent) {
            listaPrezentow.remove(prezent);
        }

        public void oznaczWymarzonyPrezent(Prezent prezent) {
            prezent.setWymarzony(true);
        }

        public void clearPresents() {
            listaPrezentow.clear();
        }

        public double obliczSumeWartosci() {
            double sumaWartosci = 0;
            for (Prezent prezent : listaPrezentow) {
                sumaWartosci += prezent.getCena();
            }
            return sumaWartosci;
        }
    }
}
