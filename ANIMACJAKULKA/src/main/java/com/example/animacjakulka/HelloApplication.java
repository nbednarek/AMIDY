package com.example.animacjakulka;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloApplication extends Application {


    @Override
    public void start(Stage primaryStage) {
        // Tworzenie kulki
        Sphere kulka = new Sphere(30);
        kulka.setMaterial(new javafx.scene.paint.PhongMaterial(Color.ORANGE));

        // Umieszczenie kulki w grupie
        Group kula = new Group(kulka);

        // Tworzenie sceny
        Scene scene = new Scene(kula, 500, 400);
        scene.setFill(Color.LIGHTBLUE);

        // Tworzenie ścieżki dla animacji
        Path ścieżka = new Path();
        ścieżka.getElements().addAll(
                new MoveTo(scene.getWidth() / 2, scene.getHeight() - 20), // ustawienie na dole
                new LineTo(20, scene.getHeight() / 2), // ruch w lewo
                new LineTo(scene.getWidth() / 2, 27), // ruch w  górę
                new LineTo(scene.getWidth() - 20, scene.getHeight() / 2), // ruch w prawo
                new LineTo(scene.getWidth() / 2, scene.getHeight() - 20) // powrót na dół
        );

        // Tworzenie animacji na podstawie ścieżki
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(3.5)); // czas trwania jednego cyklu
        pathTransition.setPath(ścieżka);
        pathTransition.setNode(kulka);
        pathTransition.setInterpolator(Interpolator.LINEAR); // ruch równomierny
        pathTransition.setCycleCount(PathTransition.INDEFINITE); // nieskończona animacja

        // Uruchomienie animacji
        pathTransition.play();

        // Ustawienie sceny na głównym oknie
        primaryStage.setScene(scene);
        primaryStage.setTitle("Kuleczka");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
