package com.example.memoramamejora;









import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class HelloController extends Application {
    private Scene scene;
    private GridPane gameGrid;
    private Button[][] cardButtons;
    private List<String> imageNames = new ArrayList<>();
    private final int CARD_SIZE = 100; // Tamaño de los cuadros de las cartas
    private final int NUM_ROWS = 2; // Número de filas del tablero
    private final int NUM_COLS = 5; // Número de columnas del tablero
    private final int CARD_MARGIN = 10; // Margen entre los botones de carta

    private Button firstFlippedCard = null;
    private Button secondFlippedCard = null;

    @Override
    public void start(Stage primaryStage) {
        createUI(primaryStage);
        primaryStage.setTitle("Memorama :)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createUI(Stage primaryStage) {
        gameGrid = new GridPane();
        scene = new Scene(gameGrid, NUM_COLS * (CARD_SIZE + CARD_MARGIN) + 20, NUM_ROWS * (CARD_SIZE + CARD_MARGIN) + 100);

        // Inicializar lista de nombres de imágenes
        for (int i = 1; i <= 15; i++) {
            imageNames.add(i + ".jpg");
        }

        // Botón para iniciar el juego
        Button startButton = new Button("Iniciar Juego");
        startButton.setOnAction(event -> createGridPane(5)); // Cambiar a 5 para mostrar 5 pares

        // Añadir botón a la escena
        VBox vBox = new VBox(startButton);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        vBox.getChildren().add(gameGrid);
        scene.setRoot(vBox);
    }

    private void createGridPane(int numPairs) {
        int totalCards = numPairs * 2;
        int windowWidth = NUM_COLS * (CARD_SIZE + CARD_MARGIN) + 20;
        int windowHeight = NUM_ROWS * (CARD_SIZE + CARD_MARGIN) + 100;
        scene.getWindow().setWidth(windowWidth);
        scene.getWindow().setHeight(windowHeight);
        gameGrid.getColumnConstraints().clear();
        gameGrid.getRowConstraints().clear();
        gameGrid.getChildren().clear();

        for (int columnIndex = 0; columnIndex < NUM_COLS; ++columnIndex) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / NUM_COLS);
            gameGrid.getColumnConstraints().add(columnConstraints);
        }

        for (int rowIndex = 0; rowIndex < NUM_ROWS; ++rowIndex) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / NUM_ROWS);
            gameGrid.getRowConstraints().add(rowConstraints);
        }

        // Ajustar el número total de cartas al doble del número de pares
        List<String> uniqueImages = new ArrayList<>(new HashSet<>(imageNames)); // Elimina duplicados
        Collections.shuffle(uniqueImages);
        uniqueImages = uniqueImages.subList(0, numPairs);
        uniqueImages.addAll(new ArrayList<>(uniqueImages));

        // Mezclar las imágenes nuevamente para revolverlas más
        Collections.shuffle(uniqueImages);

        cardButtons = new Button[NUM_ROWS][NUM_COLS];

        // Cargar las imágenes y asignarlas a las cartas
        for (int rowIndex = 0; rowIndex < NUM_ROWS; ++rowIndex) {
            for (int columnIndex = 0; columnIndex < NUM_COLS; ++columnIndex) {
                Button cardButton = new Button();
                cardButton.setMaxSize(CARD_SIZE, CARD_SIZE);
                cardButton.setPrefSize(CARD_SIZE, CARD_SIZE);
                cardButton.setMinSize(CARD_SIZE, CARD_SIZE);
                cardButton.setPadding(new Insets(CARD_MARGIN)); // Agregar margen

                // Obtener el índice de la imagen para esta posición
                int imageIndex = rowIndex * NUM_COLS + columnIndex;

                // Obtener el nombre de la imagen para esta posición
                String imageName = uniqueImages.get(imageIndex);

                // Cargar la imagen trasera y establecerla como fondo del botón
                Image reverseImage = new Image(getClass().getResourceAsStream("/images/background.jpg"), CARD_SIZE, CARD_SIZE, false, false);
                BackgroundImage backgroundImage = new BackgroundImage(reverseImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                Background background = new Background(backgroundImage);
                cardButton.setBackground(background);

                // Guardar el nombre de la imagen en el botón
                cardButton.setUserData(imageName);

                // Identificador único para cada botón
                String id = "button_" + rowIndex + "_" + columnIndex;
                cardButton.setId(id);

                // Asignar evento de clic
                cardButton.setOnAction(event -> flipCard(cardButton));

                // Agregar el botón a la cuadrícula
                cardButtons[rowIndex][columnIndex] = cardButton;
                gameGrid.add(cardButton, columnIndex, rowIndex);
            }
        }
    }

    private void flipCard(Button cardButton) {
        if (cardButton.getBackground() != null && firstFlippedCard != null && secondFlippedCard != null) {
            return;
        }

        String imageName = (String) cardButton.getUserData();
        Image image = new Image(getClass().getResourceAsStream("/images/" + imageName), CARD_SIZE, CARD_SIZE, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        cardButton.setBackground(new Background(backgroundImage));

        if (firstFlippedCard == null) {
            firstFlippedCard = cardButton;
        } else if (secondFlippedCard == null) {
            secondFlippedCard = cardButton;
            if (firstFlippedCard.getUserData().equals(secondFlippedCard.getUserData())) {
                // Los botones son iguales, dejarlos destapados
                firstFlippedCard = null;
                secondFlippedCard = null;
            } else {
                // Los botones son diferentes, voltearlos nuevamente después de 1 segundo
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        javafx.application.Platform.runLater(() -> {
                            firstFlippedCard.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream("/images/background.jpg"), CARD_SIZE, CARD_SIZE, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
                            secondFlippedCard.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream("/images/background.jpg"), CARD_SIZE, CARD_SIZE, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
                            firstFlippedCard = null;
                            secondFlippedCard = null;
                        });
                    }
                }, 1000);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
