package com.example.dnd_manager.theme;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class CustomTitleBar extends HBox {
    private double xOffset = 0;
    private double yOffset = 0;

    public CustomTitleBar(Stage stage) {
        setStyle("-fx-background-color: #1e1e1e; -fx-padding: 0 0 0 10; -fx-border-color: #3a3a3a; -fx-border-width: 0 0 1 0;");
        setAlignment(Pos.CENTER_RIGHT);
        setPrefHeight(35);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnMinimize = createTitleButton("—", "#ffffff");

        Region maxIcon = new Region();
        maxIcon.setPrefSize(12, 12);
        maxIcon.setMaxSize(12, 12);
        maxIcon.setStyle("-fx-border-color: #ffffff; -fx-border-width: 2; -fx-background-color: transparent;");
        Button btnMaximize = createTitleButton("", "#ffffff");
        btnMaximize.setGraphic(maxIcon);

        Button btnClose = createTitleButton("✕", "#ff6b6b");

        btnMinimize.setOnAction(e -> stage.setIconified(true));
        btnMaximize.setOnAction(e -> stage.setMaximized(!stage.isMaximized()));
        btnClose.setOnAction(e -> stage.close());

        getChildren().addAll(spacer, btnMinimize, btnMaximize, btnClose);

        this.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        this.setOnMouseDragged(event -> {
            if (!stage.isMaximized()) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    private Button createTitleButton(String text, String color) {
        Button btn = new Button(text);

        btn.setPadding(javafx.geometry.Insets.EMPTY);
        btn.setPrefSize(46, 35);

        btn.setStyle(String.format("""
            -fx-background-color: transparent;
            -fx-text-fill: %s;
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            -fx-cursor: hand;
            -fx-background-radius: 0;
            """, color));

        btn.setOnMouseEntered(e -> {
            if (text.equals("✕")) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: #e81123; -fx-text-fill: white;");
            } else {
                btn.setStyle(btn.getStyle() + "-fx-background-color: rgba(255, 255, 255, 0.1);");
            }
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(btn.getStyle().replace("-fx-background-color: #e81123;", "")
                    .replace("-fx-text-fill: white;", "")
                    .replace("-fx-background-color: rgba(255, 255, 255, 0.1);", ""));
        });

        return btn;
    }
}