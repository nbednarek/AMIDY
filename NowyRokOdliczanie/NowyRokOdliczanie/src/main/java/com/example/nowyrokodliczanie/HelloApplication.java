package com.example.nowyrokodliczanie;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        DateTimeFormatter format_czasu = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  //ustalanie formatu w jakim chcemy
        //aby czas był wyświetlany

        // dostosowanie do strefy czasowej i ustalenie daty na początek 2025
        LocalDateTime nowyRokData = LocalDateTime.from(LocalDate.of(2025, 1, 1).atStartOfDay(ZoneId.systemDefault()));

        // Dodanie obrazu z adresu URL
        ImageView imageView = createImageViewFromUrl("https://static.vecteezy.com/system/resources/thumbnails/014/019/712/small/amazing-beautiful-firework-isolated-for-celebration-anniversary-merry-christmas-eve-and-happy-new-year-png.png");

        // tekst w czarnym kolorze z pogrubieniem i białym tłem, jest to czas odliczany
        Label czas = new Label();
        czas.setStyle("-fx-font-size: 18; -fx-text-fill: BLACK; -fx-font-family: 'Times New Roman'; -fx-font-weight: bold;");
        czas.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        czas.setAlignment(Pos.CENTER);

        // tekst w czarnym kolorze z pogrubieniem i białym tłem, jest to tekst nad czasem
        Label tekst = new Label("Do końca roku 2024 zostało: ");
        tekst.setStyle("-fx-font-size: 23; -fx-text-fill: BLACK; -fx-font-family: 'Times New Roman'; -fx-font-weight: bold;");
        tekst.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        tekst.setTranslateY(-5);  // margines górnej krawędzi

        VBox vbox = new VBox(imageView, tekst, czas);
        vbox.setAlignment(Pos.CENTER); // położenie napisu tuż nad godziną
        // ustawiony kolor tła
        vbox.setBackground(new Background(new BackgroundFill(Color.web("#e6ddbe"), null, null)));

        Scene scene = new Scene(vbox, 500, 300);

        Thread timerThread = new Thread(() -> {
            boolean countdownTrue = true;
            while (countdownTrue) {
                try {
                    Thread.sleep(1000); //co sekundę aktualizuje liczbe dni, godzin itd gdy dojdzie do pożądanej daty
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LocalDateTime dzisiejszaData = LocalDateTime.now();

                long sekundy = dzisiejszaData.until(nowyRokData, ChronoUnit.SECONDS);

                //obliczanie czasu do nowegoroku, a gdy czas jest równy zatrzymuje odliczanie i usuwa czas
                //a ukazuje się napis ,,Szczęśliwego Nowego Roku"
                if (sekundy <= 0) {
                    countdownTrue = false;
                    Platform.runLater(() -> {
                        tekst.setText("Szczęśliwego nowego roku!");
                        czas.setText("");
                    });
                } else {
                    int dni = (int) TimeUnit.SECONDS.toDays(sekundy);
                    long godziny = TimeUnit.SECONDS.toHours(sekundy) - (dni * 24);
                    long minuty = TimeUnit.SECONDS.toMinutes(sekundy) - (TimeUnit.SECONDS.toHours(sekundy) * 60);
                    long pozostaleSekundy = sekundy - (TimeUnit.SECONDS.toMinutes(sekundy) * 60);

                    //aktualizacja czasu, odliczanie, aktualizacja tekstu
                    String pozostały_czas = "Dni: " + dni + " Godzin: " + String.format("%02d", godziny) +
                            " Minut: " + String.format("%02d", minuty) + " Sekund: " + String.format("%02d", pozostaleSekundy);

                    Platform.runLater(() -> {
                        czas.setText(pozostały_czas);
                    });
                }
            }
        });
        timerThread.start();
        stage.setTitle("Odliczanie do Nowego Roku- NB");
        stage.setScene(scene);
        stage.show();
    }

    private ImageView createImageViewFromUrl(String imageUrl) {
        try {
            InputStream inputStream = new URL(imageUrl).openStream();
            Image image = new Image(inputStream);
            return new ImageView(image);
        } catch (Exception e) {
            e.printStackTrace();
            return new ImageView(); // Pusty ImageView w przypadku błędu
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
