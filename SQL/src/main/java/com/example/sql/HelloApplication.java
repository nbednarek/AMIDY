package com.example.sql;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    static String url = "jdbc:mysql://localhost:3306/amidy";
    static String username = "root";
    static String password = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);     //tworzenie kontenera i tekstu tytułowego pogrubionego
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("ZARZĄDZANIE BAZĄ DANYCH");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        //lista opcji, domyślnie stwórz tablicę i przycisk wykonaj wraz z obszarem tekstowym do komunikatów
        ComboBox<String> optionsComboBox = new ComboBox<>();
        optionsComboBox.getItems().addAll("STWÓRZ TABLICĘ", "DODAJ REKORD", "WYSZUKAJ DANE", "WYJŚCIE");
        optionsComboBox.setValue("STWÓRZ TABLICĘ");

        Button executeButton = new Button("WYKONAJ");

        TextArea outputTextArea = new TextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setPrefHeight(200);

        executeButton.setOnAction(e -> {
            String selectedOption = optionsComboBox.getValue();
            switch (selectedOption) {
                case "STWÓRZ TABLICĘ":
                    tworzenieTabeli(primaryStage, outputTextArea);
                    break;
                case "DODAJ REKORD":
                    dodawanieRekordu(primaryStage, outputTextArea);
                    break;
                case "WYSZUKAJ DANE":
                    wyszukiwarka(primaryStage, outputTextArea);
                    break;
                case "WYJŚCIE":
                    outputTextArea.appendText("DZIĘKUJEMY ZA SKORZYSTANIE Z PROGRAMU.");
                    System.exit(1);
                    break;
                default:
                    outputTextArea.appendText("NIE MA TAKIEJ OPCJI, SPRÓBUJ PONOWNIE\n");
                    break;
            }
        });

        root.getChildren().addAll(titleLabel, optionsComboBox, executeButton, outputTextArea);

        Scene scene = new Scene(root, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("NATALIA BEDNAREK - ZARZĄDZANIE BAZĄ DANYCH"); //tytuł okna
        primaryStage.show();
        primaryStage.getScene().getRoot().setStyle("-fx-background-color: #faebde;"); //kolor tła
    }

    private void tworzenieTabeli(Stage primaryStage, TextArea outputTextArea) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("TWORZENIE TABLICY");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label nameLabel = new Label("NAZWA TABLICY::");
        TextField nameTextField = new TextField();

        Label columnLabel = new Label("KOLUMNY:");

        List<HBox> columnBoxes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HBox columnBox = new HBox(10);
            Label columnNameLabel = new Label("NAZWA KOLUMNY:");
            TextField columnNameTextField = new TextField();
            Label columnTypeLabel = new Label("TYP:");
            ComboBox<String> columnTypeComboBox = new ComboBox<>();
            columnTypeComboBox.getItems().addAll("INT", "VARCHAR(255)");
            CheckBox primaryKeyCheckBox = new CheckBox("PRIMARY KEY");
            CheckBox autoIncrementCheckBox = new CheckBox("AUTO_INCREMENT");
            columnBox.getChildren().addAll(columnNameLabel, columnNameTextField, columnTypeLabel, columnTypeComboBox,
                    primaryKeyCheckBox, autoIncrementCheckBox);
            columnBoxes.add(columnBox);
        }

        Button createButton = new Button("STWÓRZ");
        createButton.setOnAction(e -> {
            //zmienna do tworzenia zapytania SQL, pobieranie nazwy tabeli z pola tekstowego
            StringBuilder queryBuilder = new StringBuilder("CREATE TABLE ");
            String tableName = nameTextField.getText().trim();
            if (tableName.isEmpty()) {
                outputTextArea.appendText("PODAJ NAZWĘ TABELI!\n");
                return;
            }
            queryBuilder.append(tableName).append(" (");

            //pobieranie elementów z HBox, sprawdzenie czy jest nazwa kolumny każdej
            for (int i = 0; i < columnBoxes.size(); i++) {
                HBox columnBox = columnBoxes.get(i);
                TextField columnNameTextField = (TextField) columnBox.getChildren().get(1);
                ComboBox<String> columnTypeComboBox = (ComboBox<String>) columnBox.getChildren().get(3);
                CheckBox primaryKeyCheckBox = (CheckBox) columnBox.getChildren().get(4);
                CheckBox autoIncrementCheckBox = (CheckBox) columnBox.getChildren().get(5);

                String columnName = columnNameTextField.getText().trim();
                if (columnName.isEmpty()) {
                    outputTextArea.appendText("PODAJ NAZWĘ KOLUMNY!\n");
                    return;
                }
                //pobranie wybranych rzeczy, budowanie zapytania SQL dodając nazwę i typ, dodaje PK albo AI
                String columnType = columnTypeComboBox.getValue();
                if (columnType == null || columnType.isEmpty()) {
                    outputTextArea.appendText("WYBIERZ TYP KOLUMNY!\n");
                    return;
                }

                queryBuilder.append(columnName).append(" ").append(columnType).append(" ");

                if (primaryKeyCheckBox.isSelected()) {
                    queryBuilder.append("PRIMARY KEY ");
                }

                if (autoIncrementCheckBox.isSelected()) {
                    queryBuilder.append("AUTO_INCREMENT ");
                }

                queryBuilder.append(",");
            }

            queryBuilder.deleteCharAt(queryBuilder.length() - 1); // Usuwanie ostatniego przecinka
            queryBuilder.append(");");

            System.out.println("SQL Query: " + queryBuilder.toString()); //wyświetlanie zapytania

            //tworzenie tabeli wykonując zapytanie ze zmiennej queryBuilder i wyświetlanie informacji czy jest ok
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 Statement statement = connection.createStatement()) { //tworzenie obiektu dla zapytań
                statement.executeUpdate(queryBuilder.toString());
                outputTextArea.appendText("UTWORZONO TABELĘ\n");
            } catch (SQLException ex) {
                outputTextArea.appendText("NIE UDAŁO SIĘ UTWORZYĆ TABELI\n");
                ex.printStackTrace();
            }

            dialogStage.close();
        });

        vbox.getChildren().addAll(nameLabel, nameTextField, columnLabel);
        vbox.getChildren().addAll(columnBoxes);
        vbox.getChildren().add(createButton);

        Scene scene = new Scene(vbox, 600, 300);
        dialogStage.setScene(scene);
        dialogStage.show();
        dialogStage.getScene().getRoot().setStyle("-fx-background-color: #faebde;"); // Ustawienie koloru tła dla okna dialogowego
    }
    private void dodawanieRekordu(Stage primaryStage, TextArea outputTextArea) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("DODAWANIE REKORDU");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label nazwaTablicy = new Label("NAZWA TABLICY:");
        TextField nazwa = new TextField();

        Label rekord = new Label("REKORD:");

        List<HBox> recordBoxes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HBox recordBox = new HBox(10);
            Label columnNameLabel = new Label("NAZWA KOLUMNY:");
            TextField columnNameTextField = new TextField();
            Label valueLabel = new Label("WARTOŚĆ:");
            TextField valueTextField = new TextField();
            CheckBox autoIncrementCheckBox = new CheckBox("AUTO_INCREMENT");

            recordBox.getChildren().addAll(columnNameLabel, columnNameTextField, valueLabel, valueTextField, autoIncrementCheckBox);

            recordBoxes.add(recordBox);
        }

        Button addButton = new Button("DODAJ");
        addButton.setOnAction(e -> {
            String tableName = nazwa.getText().trim();
            if (tableName.isEmpty()) {
                outputTextArea.appendText("WPISZ NAZWĘ TABELI!\n"); //wyświetlane jeżeli jest pusta nazwa
                return;
            }

            StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
            queryBuilder.append(tableName).append(" VALUES (");

            //sprawdzanie pól do wprowadzania danych, pobieranie nazw kolumn i wartości,
            // sprawdzenie czy nie są puste
            for (int i = 0; i < recordBoxes.size(); i++) {
                HBox recordBox = recordBoxes.get(i);
                TextField columnNameTextField = (TextField) recordBox.getChildren().get(1);
                TextField valueTextField = (TextField) recordBox.getChildren().get(3);

                String columnName = columnNameTextField.getText().trim();
                String value = valueTextField.getText().trim();

                if (columnName.isEmpty()) {
                    outputTextArea.appendText("PODAJ NAZWĘ KOLUMNY!\n");
                    return;
                }

                CheckBox autoIncrementCheckBox = (CheckBox) recordBox.getChildren().get(4);
                // Sprawdzenie, czy kolumna ma AUTO_INCREMENT
                if (autoIncrementCheckBox.isSelected()) {
                    // Jeśli kolumna ma AUTO_INCREMENT, wartość jest pomijana
                    queryBuilder.append("null");
                } else {
                    // Wartość jest pobierana tylko wtedy, gdy kolumna nie ma AUTO_INCREMENT
                    if (value.isEmpty()) {
                        outputTextArea.appendText("PODAJ WARTOŚĆ DLA KOLUMNY: " + columnName + "\n");
                        return;
                    } else {
                        queryBuilder.append("'").append(value).append("'");
                    }
                }

                if (i < recordBoxes.size() - 1) {
                    queryBuilder.append(", ");
                }
            }

            queryBuilder.append(");");

            System.out.println("SQL Query: " + queryBuilder.toString());

            //próba połączenia z bazą, tworzenie i wykonywanie zapytania SQL, wyświetlanie odp. wiadomości
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 Statement statement = connection.createStatement()) { //tworzenie obiektu dla zapytań
                statement.executeUpdate(queryBuilder.toString());
                outputTextArea.appendText("DODANO REKORD DO TABELI\n");
            } catch (SQLException ex) {
                outputTextArea.appendText("NIE UDAŁO SIĘ DODAĆ REKORDU DO TABELI\n");
                ex.printStackTrace();
            }

            dialogStage.close();
        });

        vbox.getChildren().addAll(nazwaTablicy, nazwa, rekord);
        vbox.getChildren().addAll(recordBoxes);
        vbox.getChildren().add(addButton);

        Scene scene = new Scene(vbox, 600, 300);
        dialogStage.setScene(scene);
        dialogStage.show();
        dialogStage.getScene().getRoot().setStyle("-fx-background-color: #faebde;"); //kolor tła
    }


    private void wyszukiwarka(Stage primaryStage, TextArea outputTextArea) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("WYSZUKIWARKA DANYCH"); //tytuł

        //etykiety, pola tekstowe, lista rozwijana, pole do wprowadzania
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label tableNameLabel = new Label("NAZWA TABLICY:");
        TextField tableNameTextField = new TextField();

        Label searchCriteriaLabel = new Label("SZUKAJ WEDŁUG:");

        HBox searchCriteriaBox = new HBox(10);
        ComboBox<String> columnComboBox = new ComboBox<>();
        TextField valueTextField = new TextField();
        valueTextField.setPromptText("WARTOŚĆ");
        searchCriteriaBox.getChildren().addAll(columnComboBox, valueTextField);

        TextArea resultTextArea = new TextArea();
        resultTextArea.setEditable(false);

        Button searchButton = new Button("SZUKAJ");
        searchButton.setOnAction(e -> {
            //pobieranie nazwy tabeli z pola, jezeli pusto to trzeba wpisac nazwe,
            // pobieranie nazwy kolumny i szukaną wartość
            String tableName = tableNameTextField.getText().trim();
            if (tableName.isEmpty()) {
                outputTextArea.appendText("WPISZ NAZWĘ TABELI!\n");
                return;
            }

            String columnName = columnComboBox.getValue();
            String value = valueTextField.getText().trim();

            if (columnName == null || columnName.isEmpty() || value.isEmpty()) {
                outputTextArea.appendText("WYBIERZ KOLUMNĘ I WPISZ SZUKANĄ WARTOŚĆ!\n");
                return;
            }

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ");
            queryBuilder.append(tableName).append(" WHERE ")
                    .append(columnName).append(" = ?");

            System.out.println("SQL Query: " + queryBuilder.toString()); //zapytanie

            //przypisywanie parametrów zapytania do wartości, wyświetla wyniki,
            // jeżeli błąd wyświetli informację
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {

                preparedStatement.setString(1, value);
                ResultSet resultSet = preparedStatement.executeQuery();

                ResultSetMetaData dane = resultSet.getMetaData();
                int columnCount = dane.getColumnCount();

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        resultTextArea.appendText(dane.getColumnName(i) + ": " + resultSet.getString(i) + "\n");
                    }
                    resultTextArea.appendText("\n");
                }
            } catch (SQLException ex) {
                outputTextArea.appendText("NIE UDAŁO SIĘ WYSZUKAĆ DANYCH\n");
                ex.printStackTrace();
            }
        });
    //gdy wprowadzana lub edytowana tabela to jest próba pobierania nazw kolumn,
    // przez co póki nazwa nie jest pełna lub zła to pokazuje w oknie, że niedokończone
        tableNameTextField.setOnKeyReleased(event -> {
            String tableName = tableNameTextField.getText().trim();
            columnComboBox.getItems().clear();
            //jeżeli jest nazwa to pobiera liste kolumn z tabeli, dodaje do listy rozwijanej
            if (!tableName.isEmpty()) {
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     Statement statement = connection.createStatement()) { //tworzenie obiektu dla zapytań
                    ResultSet rs = statement.executeQuery("DESCRIBE " + tableName);
                    while (rs.next()) {
                        columnComboBox.getItems().add(rs.getString("Field"));
                    }
                } catch (SQLException ex) {
                    outputTextArea.appendText("NIEDOKOŃCZONA LUB NIEPRAWIDŁOWA NAZWA!\n");
                }
            }
        });

        vbox.getChildren().addAll(tableNameLabel, tableNameTextField, searchCriteriaLabel, searchCriteriaBox, searchButton, resultTextArea);

        Scene scene = new Scene(vbox, 600, 300);
        dialogStage.setScene(scene);
        dialogStage.show();
        dialogStage.getScene().getRoot().setStyle("-fx-background-color: #faebde;"); //kolor tła
    }
}
