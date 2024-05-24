package com.example.stoper;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    //przypisanie zmiennych użytych w programie
    private long czas_start = 0;
    private long czas_upłynął = 0;
    private boolean uruchomiono = false;
    private List<Long> loops = new ArrayList<>();
    private Label czasLista;
    private Label okrągły_czas;
    private Line sekundy;
    private File loops_plik = new File("loops.txt");

    @Override
    public void start(Stage primaryStage) {
        //przyciski, czcionka wyświetlana, czas
        czasLista = new Label("00:00:00.000");
        czasLista.setStyle("-fx-font-size: 48; -fx-font-weight: bold;");
        okrągły_czas = new Label("Round Time: ");
        okrągły_czas.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        Button resetButton = new Button("Reset");
        Button loopButton = new Button("Loop");
        Button zapiszButton = new Button("Zapisz do pliku");
        Button wczytajButton = new Button("Wczytaj z pliku");
        Button okrągłyButton = new Button("Okrągły czas");

        //ustawienie tego w okienku, odstępy itd
        HBox buttonsBox = new HBox(10, startButton, stopButton, resetButton, loopButton, zapiszButton, wczytajButton, okrągłyButton);
        buttonsBox.setAlignment(Pos.CENTER);
        VBox root = new VBox(20, zegar(), czasLista, okrągły_czas, buttonsBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        //miejsce na międzyczasy
        VBox loopsBox = new VBox();
        loopsBox.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(loopsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);

        //przechowywanie elementów, jasnoszare tło, wyśrodkowanie, odstępy 20px
        VBox main = new VBox(root, scrollPane);
        main.setAlignment(Pos.CENTER);
        main.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235), CornerRadii.EMPTY, Insets.EMPTY)));
        main.setPadding(new Insets(20));
        main.setSpacing(20);

        //uruchomienie wątku, który co 10 milisekund sprawdza, czy stoper jest uruchomiony,
        // jeżeli tak to wyświetla czas i obraca wskazówkę

        Thread stoperThread = new Thread(() -> {
            while (true) {
                if (uruchomiono) {
                    Platform.runLater(() -> {
                        long currentTime = System.currentTimeMillis();
                        czas_upłynął = currentTime - czas_start;
                        updateTimeLabel(czasLista, czas_upłynął);

                        double secondsAngle = (czas_upłynął / 1000.0) % 60 * 6;

                        // Rotate the second hand
                        wskazówka(sekundy, secondsAngle);
                    });
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        stoperThread.setDaemon(true); //ustawienie wątki jako daemon, aby działał w tle i zakończył się automatycznie
        stoperThread.start();

        //zapisanie czasu syst. aby rozpocząć pomiar czasu od tego momentu
        startButton.setOnAction(event -> {
            if (!uruchomiono) {
                czas_start = System.currentTimeMillis() - czas_upłynął;
                uruchomiono = true;
            }
        });

        //ostrzeżenie jeżeli nie został uruchomiony, inaczej zmienia uruchomiono na false
        stopButton.setOnAction(event -> {
            if (!uruchomiono) {
                warning("Uruchom stoper zanim go zatrzymasz!");
            } else {
                uruchomiono = false;
            }
        });

        //zerowanie czasu stopera, wyczyszczenie czasów i pozycji
        resetButton.setOnAction(event -> {
            if (!uruchomiono && czas_upłynął == 0) {
                warning("Uruchom stoper zanim go zresetujesz!");
            } else {
                uruchomiono = false;
                czas_upłynął = 0;
                updateTimeLabel(czasLista, czas_upłynął);
                loops.clear();
                loopsBox.getChildren().clear();
                // reset pozycji wskazówki
                sekundy.setRotate(0);
            }
        });

        //rejestr czasu okrążenia podczas pracy stopera i wyświetlenie wraz z numerem okrążenia
        loopButton.setOnAction(event -> {
            if (!uruchomiono) {
                warning("Uruchom stoper zanim pobierzesz czas!");
            } else {
                long loopTime = czas_upłynął;
                loops.add(loopTime);
                Label loopLabel = new Label(loops.size() + ". Loop: " + formatCzasu(loopTime));
                loopsBox.getChildren().add(loopLabel);
                loopsBox.getChildren().add(new VBox(new Label(), new Separator(), new Label()));
            }
        });

        //zapis do pliku, jeżeli nie ma to ostrzeżenie lub informacja o powodzeniu
        zapiszButton.setOnAction(event -> {
            if (loops.isEmpty()) {
                warning("Nie ma czasów do zapisu!");
            } else {
                zapisDoPliku();
                informacja("Pomyślnie zapisano czasy!");
            }
        });

        //wczytanie danych z pliku, aktualizacja listy i komunikat o powodzeniu
        wczytajButton.setOnAction(event -> {
            załadujZPliku();
            czasyLoop(loopsBox);
            informacja("Pomyślnie załadowano czasy!");
        });

        //obliczenie i wyświetlenie okrągłego czasu między dwoma ostatnimi zapisanymi
        okrągłyButton.setOnAction(event -> {
            if (loops.size() < 2) {
                warning("Potrzebujesz dwóch czasów, aby podać czas pomiędzy!");
            } else {
                int lastIndex = loops.size() - 1;
                long roundTime = okrągłyCzas(lastIndex - 1, lastIndex); //między poprzednim a ostatnim czas okrągły
                okrągły_czas.setText("Okrągły czas: " + formatCzasu(roundTime));
            }
        });

        //ustawienia okienka i tytuł
        Scene scene = new Scene(main, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("STOPER NATALIA BEDNAREK");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> zapisDoPliku());
    }

    //aktualizacja wyświetlania czasu na podstawie czasu w milisekundach
    private void updateTimeLabel(Label label, long time) {
        label.setText(formatCzasu(time));
    }

    //formatowanie czasu, aby było odpowiednio sekundami, milisekundami itd.
    private String formatCzasu(long time) {
        long milliseconds = time % 1000;
        long seconds = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        long hours = (time / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
    }

    private StackPane zegar() {
        //wczytanie obrazu, wyświetlenie za pomocą ImageView, szerokość 200px i zachowanie proporcji
        Image clockImage = new Image(getClass().getResourceAsStream("/tarcza.png"));
        ImageView clockImageView = new ImageView(clockImage);
        clockImageView.setFitWidth(200);
        clockImageView.setPreserveRatio(true);

        //czerwona linia reprezentująca wskazówkę, zaczyna 0,10 konczy 0,-80 obr. o 180 stopni
        sekundy = new Line(0, 10, 0, -80);
        sekundy.setStroke(Color.RED);
        sekundy.setStrokeWidth(3);
        sekundy.setRotate(180);

        //wyśrodkowanie obrazów
        StackPane clockPane = new StackPane(clockImageView, sekundy);
        clockPane.setAlignment(Pos.CENTER);

        return clockPane;
    }

    //ustawienie kątu obrotu dla linii
    private void wskazówka(Line hand, double angle) {

        hand.setRotate(angle);
    }

    //wyświetlenie ostrzeżenia okno dialogowe
    private void warning(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //wyświetlenie informacji okno dialogowe
    private void informacja(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //zapis listy do pliku, gdzie każdy z nich zapisywany jest w osobnej linii
    private void zapisDoPliku() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(loops_plik))) {
            for (Long loopTime : loops) {
                writer.println(loopTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //wczytywanie danych z pliku, usuwa obecne czasy, wczytuje pętle z pliku dodając do listy
    private void załadujZPliku() {
        loops.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(loops_plik))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loops.add(Long.parseLong(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //usuwanie istniejących elementów z kontenera,
    // sprawdzanie listy loops tworząc numery pętli i format czasu dodając do kontenera,
    // każda rozdzielona linią
    private void czasyLoop(VBox loopTimesBox) {
        loopTimesBox.getChildren().clear();
        for (int i = 0; i < loops.size(); i++) {
            Label loopLabel = new Label((i + 1) + ". Loop: " + formatCzasu(loops.get(i)));
            loopTimesBox.getChildren().addAll(loopLabel, new VBox(new Label(), new Separator(), new Label()));
        }
    }

    //obliczanie czasu trwania między dwoma loopami, zwraca różnicę czasów
    private long okrągłyCzas(int startIndex, int endIndex) {
        if (startIndex < 0 || endIndex < 0 || startIndex >= loops.size() || endIndex >= loops.size() || startIndex >= endIndex) {
            return 0;
        }
        return loops.get(endIndex) - loops.get(startIndex);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
