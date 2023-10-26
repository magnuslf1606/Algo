package com.example.demo.Ui;

import com.example.demo.Generator.OrdBehandler;
import com.example.demo.Generator.URLReader;
import com.example.demo.MainScreen;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Klasse for alt Med UI
 */
public class LagUi {

    private final int screenWidth = 1920;
    private final int screenHeight = 1080;

    private String url;

    private String ord1;
    private String ord2;


    /**
     * Lager UI
     */
    public void lagUi() {
        lagStartUi();
    }

    /**
     * Lager UI for Tekst som skal generes med Reset knapp og hBox med Info om URL og ord den har også en Print knapp som printer ut teksten til en txt fil
     */
    public void lagAiUi() {

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(20, 20, 20, 20));
        TextArea textArea = new TextArea();
        textArea.setText(OrdBehandler.text);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-size: 15px;");
        textArea.editableProperty().setValue(false);
        textArea.setPrefWidth(1920 - 450);
        textArea.setPrefHeight(1000);
        hbox.getChildren().addAll(textArea);



        VBox vbox = new VBox();
        Button print = new Button();
        print.setTranslateX(120);
        print.setTranslateY(600);
        print.setPrefWidth(100);
        print.setPrefHeight(50);
        print.setText("Print");

        print.setOnAction(e -> {
            try {
                OrdBehandler.printTilTxt();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        Button reset = new Button();
        reset.setTranslateX(120);
        reset.setTranslateY(600);
        reset.setText("Reset");
        reset.setOnAction(e -> {
            MainScreen.UiPane.getChildren().removeAll(vbox, hbox);
            lagStartUi();
        });
        reset.setPrefWidth(100);
        reset.setPrefHeight(50);

        vbox.setBackground(Background.fill(Color.LIGHTBLUE));

        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.setPrefWidth(420);
        vbox.setPrefHeight(1080);
        Label label = new Label("Url: " + url);
        Label label2 = new Label("Valgte Ord: " + ord1 + " " + "+" + " " + ord2);
        label2.setStyle("-fx-font-size: 20px;");
        label.setStyle("-fx-font-size: 20px;");
        vbox.setTranslateX((double) screenWidth - 400);
        vbox.getChildren().addAll(label, label2, reset, print);
        vbox.getChildren().addAll();
        MainScreen.UiPane.getChildren().addAll(vbox, hbox);
    }



    /**
     * Lager UI for å velge ord og URL kjører derreter WebScraper og generer tekst
     */
    public void lagStartUi() {
        //OrdVelger UI
        VBox vboxOrdVelger = new VBox();
        vboxOrdVelger.setSpacing(20);
        vboxOrdVelger.setPadding(new Insets(20, 20, 20, 20));
        Label label = new Label("Skriv inn to Start ord");
        label.setStyle("-fx-font-size: 20px;");

        TextField textField2 = new TextField();
        textField2.setPrefWidth(100);


        TextField textField3 = new TextField();
        textField3.setPrefWidth(100);


        vboxOrdVelger.setTranslateX((double) screenWidth /2 - 50);
        vboxOrdVelger.setTranslateY((double) screenHeight /2 - 200);

        vboxOrdVelger.getChildren().addAll(label, textField2, textField3);
        vboxOrdVelger.getChildren().addAll();
        MainScreen.UiPane.getChildren().add(vboxOrdVelger);

        //URL UI
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(20, 20, 20, 20));
        Label label2 = new Label("Skriv URL: ");
        label.setStyle("-fx-font-size: 20px;");

        TextField textField = new TextField();
        textField.setPrefWidth(600);



        Button button = new Button("Søk");
        button.setPrefWidth(100);
        button.setOnAction(e -> {
            ord1 = textField2.getText();
            ord2 = textField3.getText();


            url = textField.getText();
            //Skal Kjøre WebScraper her
            if (url.isBlank() || ord1.isBlank() || ord2.isBlank())
                System.out.println("Du må skrive inn en URL og to ord");
            else {
                System.out.println("Søker etter: " + url);
                URLReader urlReader = new URLReader(url);

                try {
                    new OrdBehandler(urlReader, ord1, ord2);

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                MainScreen.UiPane.getChildren().removeAll(vboxOrdVelger, hbox);
                lagAiUi();
            }
        });
        hbox.setTranslateX((double) screenWidth /2 - 350);
        hbox.setTranslateY((double) screenHeight /2);

        hbox.getChildren().addAll(label2, textField, button);

        MainScreen.UiPane.getChildren().add(hbox);
    }

}