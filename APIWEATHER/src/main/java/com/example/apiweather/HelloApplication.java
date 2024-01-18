package com.example.apiweather;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelloApplication extends Application {

    private static final String API_KEY = "9b4cc34b9955bdbe0873ea4bb7dd2ee1"; // przypisanie swojego klucza API

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("API WEATHER -NB");

        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox(10);
        HBox hBox = new HBox(5);

        // Środkowy tekst "API WEATHER"
        Label tekst = new Label("API WEATHER");
        tekst.setStyle("-fx-font-size: 25; -fx-font-weight: bold;");
        vBox.getChildren().add(tekst);

        TextField wyszukiwanieMiasta = new TextField();
        wyszukiwanieMiasta.setPromptText("Wpisz nazwę miasta");

        // Ładowanie obrazka lupy i dostosowanie rozmiaru
        Image image = new Image(getClass().getResourceAsStream("/search.png"));
        ImageView lupa = new ImageView(image);
        lupa.setFitWidth(16);
        lupa.setFitHeight(16);

        Button wyszukiwanie = new Button();
        wyszukiwanie.setGraphic(lupa);

        TextArea pogoda = new TextArea();
        pogoda.setEditable(false);

        ImageView ikonaPogody = new ImageView();
        ikonaPogody.setFitWidth(100); // wielkość ikony
        ikonaPogody.setFitHeight(100); // wielkość ikony

        // Ustawienie tła bezpośrednio dla ImageView
        StackPane ikonaStackPane = new StackPane(ikonaPogody);
        ikonaStackPane.setStyle("-fx-background-color: #ffe8d1; -fx-padding: 10px; -fx-background-radius: 10px;");
        ikonaStackPane.setAlignment(Pos.CENTER);
        VBox ikonaVBox = new VBox(ikonaStackPane);
        ikonaVBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(wyszukiwanieMiasta, wyszukiwanie);
        hBox.setAlignment(Pos.CENTER);

        // Dodanie kontenerów do głównego kontenera VBox
        vBox.getChildren().addAll(hBox, ikonaVBox, pogoda);
        vBox.setAlignment(Pos.CENTER);

        // Pobranie informacji o danym mieście i wyświetlenie ich
        wyszukiwanie.setOnAction(event -> {
            String miasto = wyszukiwanieMiasta.getText();
            if (!miasto.isEmpty()) {
                String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + miasto + "&appid=" + API_KEY + "&lang=pl";

                try {
                    String informacje = pobierzInformacje(apiUrl);
                    pogoda.setText(informacje);
                    String ikonaUrl = pobierzIkonePogody(apiUrl);
                    if (ikonaUrl != null) {
                        Image ikona = new Image(ikonaUrl);
                        ikonaPogody.setImage(ikona);
                    }
                } catch (IOException | ParseException e) {
                    pogoda.setText("Błąd podczas pobierania informacji o pogodzie.");
                }
            }
        });

        // Dodanie kontenerów do głównego kontenera BorderPane
        borderPane.setCenter(vBox);

        // Ustawienie koloru tła
        borderPane.setStyle("-fx-background-color: #fadba2;");

        Scene scene = new Scene(borderPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
    }

    private String pobierzInformacje(String apiUrl) throws IOException, ParseException {
        StringBuilder informacje = new StringBuilder();

        // Połączenie HTTP z adresem URL, odczytywanie
        URL url = new URL(apiUrl);
        HttpURLConnection połączenie = (HttpURLConnection) url.openConnection();
        połączenie.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(połączenie.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            informacje.append(line).append("\n");
        }

        reader.close();
        połączenie.disconnect();

        // Parsowanie odpowiedzi JSON
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(informacje.toString());
        JSONObject jsonObject = (JSONObject) obj;

        // Pobieranie informacji o temperaturze
        JSONObject mainObject = (JSONObject) jsonObject.get("main");
        Double temperatura = (Double) mainObject.get("temp");
        // Przekształcanie na stopnie Celsjusza z Kelwinów
        double temperaturaCelsjusza = temperatura.doubleValue() - 273.15;
        // Usunięcie liczb po przecinku
        String sformatowanaTemperatura = String.format("%.0f", temperaturaCelsjusza);

        // Pobieranie informacji o pogodzie
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherObject = (JSONObject) weatherArray.get(0);
        String opis = (String) weatherObject.get("description");

        // Wygląd wyświetlania temperatury i opisu jedno pod drugim
        return "Temperatura: " + sformatowanaTemperatura + "°C\nOpis pogody: " + opis;
    }

    private String pobierzIkonePogody(String apiUrl) throws IOException, ParseException {

        //obiekt URL, połączenie
        URL url = new URL(apiUrl);
        HttpURLConnection połączenie = (HttpURLConnection) url.openConnection();
        połączenie.setRequestMethod("GET");

        //BufferedReader odczytujący dane z InputStream, odczyt danych wiersz po wierszu i dodanie do obiektu
        BufferedReader reader = new BufferedReader(new InputStreamReader(połączenie.getInputStream()));
        String line;
        StringBuilder informacje = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            informacje.append(line);
        }

        reader.close();
        połączenie.disconnect();

        //tworzenie obiektu JSONParser, tekst do obiektu
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(informacje.toString());
        JSONObject jsonObject = (JSONObject) obj;

        //uzyskanie tablicy o nazwie weather, pobranie elementu, pobranie wartości id ikony pogody
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherObject = (JSONObject) weatherArray.get(0);
        String ikonaId = (String) weatherObject.get("icon");

        //sprawdzenie czy id ikony nie jest puste, zwracanie ikony w PNG
        if (ikonaId != null) {
            return "http://openweathermap.org/img/w/" + ikonaId + ".png";
        }

        return null;
    }
}
