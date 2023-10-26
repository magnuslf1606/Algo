/**
 * Koden er laget av Magnus, Daniel og Endre
 **/

package com.example.demo;

import com.example.demo.Ui.LagUi;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreen extends Application {

    private int screenWidth = 1920;
    private int screenHeight = 1010;
    public static Pane myPane = new Pane();
    public static Pane UiPane = new Pane();

    /**
     * Starter programmet
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        LagUi lagUi = new LagUi();
        lagUi.lagUi();

        myPane.getChildren().add(UiPane);
        Scene scene = new Scene(myPane, screenWidth , screenHeight);
        stage.setTitle("Algo Oblig!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main metode
     * @param args
     */

    public static void main(String[] args) {
        launch();
    }
}