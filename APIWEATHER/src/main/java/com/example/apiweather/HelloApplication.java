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
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloApplication extends Application {

    private static final String API_KEY = "9b4cc34b9955bdbe0873ea4bb7dd2ee1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("API WEATHER -NB");

        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox(10);
        HBox hBox = new HBox(5);

        Label tekst = new Label("API WEATHER");
        tekst.setStyle("-fx-font-size: 25; -fx-font-weight: bold;");
        vBox.getChildren().add(tekst);

        TextField wyszukiwanieMiasta = new TextField();
        wyszukiwanieMiasta.setPromptText("Wpisz nazwę miasta");

        Image image = new Image(getClass().getResourceAsStream("/search.png"));
        ImageView lupa = new ImageView(image);
        lupa.setFitWidth(16);
        lupa.setFitHeight(16);

        Button wyszukiwanie = new Button();
        wyszukiwanie.setGraphic(lupa);

        TextArea pogoda = new TextArea();
        TextArea prognozaText = new TextArea();
        pogoda.setEditable(false);
        prognozaText.setEditable(false);

        ImageView ikonaPogody = new ImageView();
        ikonaPogody.setFitWidth(100);
        ikonaPogody.setFitHeight(100);

        StackPane ikonaStackPane = new StackPane(ikonaPogody);
        ikonaStackPane.setStyle("-fx-background-color: #ffe8d1; -fx-padding: 10px; -fx-background-radius: 10px;");
        ikonaStackPane.setAlignment(Pos.CENTER);
        VBox ikonaVBox = new VBox(ikonaStackPane);
        ikonaVBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(wyszukiwanieMiasta, wyszukiwanie);
        hBox.setAlignment(Pos.CENTER);

        Label prognozaLabel = new Label("NASTĘPNE 5 DNI: ");
        prognozaLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        VBox prognozaVBox = new VBox(5);
        prognozaVBox.getChildren().addAll(prognozaLabel, prognozaText);

        vBox.getChildren().addAll(hBox, ikonaVBox, pogoda, prognozaVBox);
        vBox.setAlignment(Pos.CENTER);

        wyszukiwanie.setOnAction(event -> {
            String miasto = wyszukiwanieMiasta.getText();
            if (!miasto.isEmpty()) {
                String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + miasto + "&appid=" + API_KEY + "&lang=pl";
                String prognozaUrl = "http://api.openweathermap.org/data/2.5/forecast?q=" + miasto + "&appid=" + API_KEY + "&lang=pl";

                try {
                    String informacjePogoda = pobierzInformacje(apiUrl);
                    String prognozaInformacje = pobierzPrognozeInformacje(prognozaUrl);

                    pogoda.setText(informacjePogoda);
                    prognozaText.setText(prognozaInformacje);

                    String ikonaUrl = pobierzIkonePogody(apiUrl);
                    if (ikonaUrl != null) {
                        Image ikona = new Image(ikonaUrl);
                        ikonaPogody.setImage(ikona);
                    }
                } catch (IOException | ParseException e) {
                    pogoda.setText("Błąd podczas pobierania informacji o pogodzie");
                    prognozaText.setText("Błąd podczas pobierania informacji o prognozie pogody.");
                }
            }
        });

        borderPane.setCenter(vBox);
        borderPane.setStyle("-fx-background-color: #fadba2;");

        Scene scene = new Scene(borderPane, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
    }

    private String pobierzInformacje(String apiUrl) throws IOException, ParseException {
        StringBuilder informacje = new StringBuilder();

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

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(informacje.toString());
        JSONObject jsonObject = (JSONObject) obj;
        JSONObject mainObject = (JSONObject) jsonObject.get("main");
        Number temperaturaNumber = (Number) mainObject.get("temp");
        Number tempMinNumber = (Number) mainObject.get("temp_min");
        Number tempMaxNumber = (Number) mainObject.get("temp_max");
        Number feelsLikeNumber = (Number) mainObject.get("feels_like");
        Number pressureNumber = (Number) mainObject.get("pressure");
        Number humidityNumber = (Number) mainObject.get("humidity");

        Double temperatura = temperaturaNumber.doubleValue();
        Double tempMin = tempMinNumber.doubleValue();
        Double tempMax = tempMaxNumber.doubleValue();
        Double feelsLike = feelsLikeNumber.doubleValue();
        Double pressure = pressureNumber.doubleValue();
        Double humidity = humidityNumber.doubleValue();

        double temperaturaCelsjusza = temperatura - 273.15;
        double tempMinCelsjusza = tempMin - 273.15;
        double tempMaxCelsjusza = tempMax - 273.15;
        double feelsLikeCelsjusza = feelsLike - 273.15;

        String sformatowanaTemperatura = String.format("%.0f", temperaturaCelsjusza);
        String sformatowanaTempMin = String.format("%.0f", tempMinCelsjusza);
        String sformatowanaTempMax = String.format("%.0f", tempMaxCelsjusza);
        String sformatowanaFeelsLike = String.format("%.0f", feelsLikeCelsjusza);

        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherObject = (JSONObject) weatherArray.get(0);
        String opis = (String) weatherObject.get("description");

        JSONObject cloudsObject = (JSONObject) jsonObject.get("clouds");
        Long chmuryProcent = (Long) cloudsObject.get("all");

        String nazwaMiasta = (String) jsonObject.get("name");

        return "Nazwa miasta: " + nazwaMiasta + "\n" +
                "Temperatura: " + sformatowanaTemperatura + "°C\n" +
                "Opis pogody: " + opis + "\n" +
                "Temperatura minimalna: " + sformatowanaTempMin + "°C\n" +
                "Temperatura maksymalna: " + sformatowanaTempMax + "°C\n" +
                "Odczuwalna temperatura: " + sformatowanaFeelsLike + "°C\n" +
                "Ciśnienie: " + pressure + " hPa\n" +
                "Wilgotność: " + humidity + "%\n" +
                "Zachmurzenie: " + chmuryProcent + "%\n";
    }

    private String pobierzIkonePogody(String apiUrl) throws IOException, ParseException {
        URL url = new URL(apiUrl);
        HttpURLConnection połączenie = (HttpURLConnection) url.openConnection();
        połączenie.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(połączenie.getInputStream()));
        String line;
        StringBuilder informacje = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            informacje.append(line);
        }

        reader.close();
        połączenie.disconnect();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(informacje.toString());
        JSONObject jsonObject = (JSONObject) obj;

        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherObject = (JSONObject) weatherArray.get(0);
        String ikonaId = (String) weatherObject.get("icon");

        if (ikonaId != null) {
            return "http://openweathermap.org/img/w/" + ikonaId + ".png";
        }

        return null;
    }

    private String pobierzPrognozeInformacje(String prognozaUrl) throws IOException, ParseException {
        StringBuilder informacje = new StringBuilder();

        URL url = new URL(prognozaUrl);
        HttpURLConnection połączenie = (HttpURLConnection) url.openConnection();
        połączenie.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(połączenie.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            informacje.append(line).append("\n");
        }

        reader.close();
        połączenie.disconnect();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(informacje.toString());
        JSONObject jsonObject = (JSONObject) obj;

        JSONArray listaPrognozy = (JSONArray) jsonObject.get("list");
        StringBuilder prognozaStringBuilder = new StringBuilder();

        JSONObject prognoza = (JSONObject) listaPrognozy.get(0);
        String data = (String) prognoza.get("dt_txt");
        Date currentDate = null;

        //Jezeli jest w dobrym formacie to przechwytuje
        try {
            currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data);
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
        String dzisiejszyDzien = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);

        //każdy obiekt do JSONA, tekst z data, konwertowane na datę
        for (Object prognozaObj : listaPrognozy) {
            JSONObject prognozaJSON = (JSONObject) prognozaObj;

            String dataCzas = (String) prognozaJSON.get("dt_txt");
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataCzas);
            } catch (java.text.ParseException e) {
                throw new RuntimeException(e);
            }

            //przekształcenie daty na tekst i przypisanie do zmiennej
            String dzienPrognozy = new SimpleDateFormat("yyyy-MM-dd").format(date);

            //pobranie informacji, odczytanie wartości jako obiekt liczbowy z main
            JSONObject mainObject = (JSONObject) prognozaJSON.get("main");
            Number temperaturaNumber = (Number) mainObject.get("temp");

            //temperatura na Celsjusza, zaokrąglenie i formatowanie
            Double temperaturaCelsjusza = temperaturaNumber.doubleValue() - 273.15;
            String sformatowanaTemperatura = String.format("%.0f", temperaturaCelsjusza);

            //pobranie informacji z obiektu, uzyskanie tablicy, pobranie elementu, pobranie opisu jako ciąg znaków
            JSONArray weatherArray = (JSONArray) prognozaJSON.get("weather");
            JSONObject weatherObject = (JSONObject) weatherArray.get(0);
            String opis = (String) weatherObject.get("description");

            //sprawdzenie czy nie zawiera juz informacji o danym dniu i czy nie jest to dzisiejszy dzzien
            if (!prognozaStringBuilder.toString().contains(dzienPrognozy) && !dzienPrognozy.equals(dzisiejszyDzien)) {
                prognozaStringBuilder.append("Data: ").append(dzienPrognozy).append("\n");
                prognozaStringBuilder.append("Temperatura: ").append(sformatowanaTemperatura).append("°C\n");
                prognozaStringBuilder.append("Opis pogody: ").append(opis).append("\n\n");
            }
        }

        return prognozaStringBuilder.toString();
    }
}
