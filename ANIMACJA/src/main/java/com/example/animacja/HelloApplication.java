package com.example.animacja;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloApplication extends Application {

    private RotateTransition osX1;
    private RotateTransition osY1;  //zmienne do obracania osiami później w kodzie
    private RotateTransition osX2;
    private RotateTransition osY2;
    private boolean ruszanie = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Tworzenie białej kostki
        Box kostka1 = new Box(100, 100, 100);
        kostka1.setTranslateX(300);
        kostka1.setTranslateY(300); // Połowa wysokości sceny
        kostka1.setTranslateZ(50); // Przesunięcie w osi Z, aby uniknąć zakrycia przez podłoże

        // Ustawienie materiału dla kostki1 z PhongMaterial, DiffuseMap do nadania koloru i wzoru
        PhongMaterial kostkajedynka = new PhongMaterial();
        kostkajedynka.setDiffuseMap(new Image(getClass().getResource("/kostka1.png").toExternalForm()));
        kostka1.setMaterial(kostkajedynka);

        // Tworzenie pomarańczowej kostki
        Box kostka2 = new Box(100, 100, 100);
        kostka2.setTranslateX(500);
        kostka2.setTranslateY(300); // Połowa wysokości sceny
        kostka2.setTranslateZ(50); // Przesunięcie w osi Z, aby uniknąć zakrycia przez podłoże

        // Ustawienie materiału dla kostki2 z PhongMaterial, DiffuseMap do nadania koloru i wzoru
        PhongMaterial kostkadwójka = new PhongMaterial();
        kostkadwójka.setDiffuseMap(new Image(getClass().getResource("/kostka2.png").toExternalForm()));
        kostka2.setMaterial(kostkadwójka);

        // Utworzenie przycisków "START", "STOP" i "AUTOR"
        Button start = new Button("START");
        start.setOnAction(event -> rozpocznij());

        Button stop = new Button("STOP");
        stop.setOnAction(event -> zatrzymaj());

        Button twórca = new Button("TWÓRCA");
        twórca.setOnAction(event -> twórcainfo());

        // Kontener dla przycisków, ustawienie ich obok siebie i na scenie
        HBox buttonBox = new HBox(10, start, stop, twórca);
        buttonBox.setLayoutX(300);
        buttonBox.setLayoutY(550);

        // Animacja obrotu kostki wzdłuż osi X
        osX1 = new RotateTransition(Duration.seconds(1.5), kostka1);
        osX1.setAxis(Rotate.X_AXIS); //określenie obrotu względem osi X
        osX1.setByAngle(360); // Obrót o 360 stopni
        osX1.setCycleCount(1); // Uruchamiamy raz
        osX1.setOnFinished(event -> osY1.play()); // Po zakończeniu obrotu X, odtwarzaj obrot Y

        // Animacja obrotu kostki wzdłuż osi Y
        osY1 = new RotateTransition(Duration.seconds(1.5), kostka1);
        osY1.setAxis(Rotate.Y_AXIS); //określenie obrotu względem osi Y
        osY1.setByAngle(360); // Obrót o 360 stopni (w lewo)
        osY1.setCycleCount(1); // Uruchamiamy raz
        osY1.setOnFinished(event -> osX1.play()); // Po zakończeniu obrotu Y, odtwarzaj obrot X

        // Animacja obrotu kostki wzdłuż osi X
        osX2 = new RotateTransition(Duration.seconds(1.5), kostka2);
        osX2.setAxis(Rotate.X_AXIS); //określenie obrotu względem osi X
        osX2.setByAngle(-360); // Obrót o 360 stopni
        osX2.setCycleCount(1); // Uruchamiamy raz
        osX2.setOnFinished(event -> osY2.play()); // Po zakończeniu obrotu X, odtwarzaj obrot Y

        // Animacja obrotu kostki wzdłuż osi Y (na bok) - dla drugiej kostki
        osY2 = new RotateTransition(Duration.seconds(1.5), kostka2);
        osY2.setAxis(Rotate.Y_AXIS); //określenie obrotu względem osi Y
        osY2.setByAngle(-360); // Obrót o 360 stopni (w lewo)
        osY2.setCycleCount(1); // Uruchamiamy raz
        osY2.setOnFinished(event -> osX2.play()); // Po zakończeniu obrotu Y, odtwarzaj obrot X

        // wszystkie elementy do pokazania w oknie, szerokość i wysokość oraz kolor tła
        Scene elementy = new Scene(new Group(kostka1, kostka2, buttonBox), 800, 600, Color.web("#fadf93"));

        // Wyświetlenie głównego okna, które zawiera elementy powyższe, tytuł
        primaryStage.setTitle("ANIMACJA NB");
        primaryStage.setScene(elementy);
        primaryStage.show();
    }

    private void rozpocznij() {
        if (!ruszanie) {
            osX1.play();
            osY1.play();
            osX2.play();
            osY2.play();
            ruszanie = true;
        }
    }

    private void zatrzymaj() {
        osX1.stop();
        osY1.stop();
        osX2.stop();
        osY2.stop();
        ruszanie = false;
    }

    private void twórcainfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Twórca");
        alert.setHeaderText(null);
        alert.setContentText("TWÓRCA: Natalia Bednarek 4TD");
        alert.show();
    }
}