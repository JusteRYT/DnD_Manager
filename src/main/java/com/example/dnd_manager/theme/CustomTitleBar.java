package com.example.dnd_manager.theme;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CustomTitleBar extends HBox {
    private double xOffset = 0;
    private double yOffset = 0;
    private double restoreX;
    private double restoreY;
    private double restoreWidth;
    private double restoreHeight;
    private boolean customMaximized;

    public CustomTitleBar(Stage stage) {
        setStyle("""
                -fx-background-color:
                    linear-gradient(to right, #10162d, #1d2b58 52%, #2d145c);
                -fx-padding: 0 0 0 14;
                -fx-border-color: rgba(127, 185, 212, 0.20);
                -fx-border-width: 0 0 1 0;
                """);
        setAlignment(Pos.CENTER_LEFT);
        setPrefHeight(38);
        setMinHeight(38);
        setMaxHeight(38);

        Label titleLabel = new Label();
        titleLabel.textProperty().bind(stage.titleProperty());
        titleLabel.setStyle("-fx-text-fill: #dbe5ea; -fx-font-weight: bold; -fx-font-size: 12px;");
        titleLabel.setMinWidth(Region.USE_PREF_SIZE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnMinimize = createTitleButton("—", "#b9d2df");

        Region maxIcon = new Region();
        maxIcon.setPrefSize(11, 11);
        maxIcon.setMaxSize(11, 11);
        maxIcon.setStyle("-fx-border-color: #b9d2df; -fx-border-width: 1.5; -fx-background-color: transparent;");
        Button btnMaximize = createTitleButton("", "#b9d2df");
        btnMaximize.setGraphic(maxIcon);

        Button btnClose = createTitleButton("✕", "#ffd8f7");

        btnMinimize.setOnAction(e -> stage.setIconified(true));
        btnMaximize.setOnAction(e -> toggleMaximize(stage));
        btnClose.setOnAction(e -> stage.close());

        getChildren().addAll(titleLabel, spacer, btnMinimize, btnMaximize, btnClose);

        this.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        this.setOnMouseDragged(event -> {
            if (!customMaximized) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    private void toggleMaximize(Stage stage) {
        if (customMaximized) {
            restore(stage);
            return;
        }

        restoreX = stage.getX();
        restoreY = stage.getY();
        restoreWidth = stage.getWidth();
        restoreHeight = stage.getHeight();

        Rectangle2D bounds = screenFor(stage).getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        customMaximized = true;
    }

    private void restore(Stage stage) {
        stage.setX(restoreX);
        stage.setY(restoreY);
        stage.setWidth(restoreWidth);
        stage.setHeight(restoreHeight);
        customMaximized = false;
    }

    private Screen screenFor(Stage stage) {
        double centerX = stage.getX() + stage.getWidth() / 2;
        double centerY = stage.getY() + stage.getHeight() / 2;
        return Screen.getScreensForRectangle(centerX, centerY, 1, 1)
                .stream()
                .findFirst()
                .orElse(Screen.getPrimary());
    }

    private Button createTitleButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPadding(javafx.geometry.Insets.EMPTY);
        btn.setPrefSize(46, 38);
        btn.setFocusTraversable(false);

        String baseStyle = String.format("""
            -fx-background-color: transparent;
            -fx-text-fill: %s;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-cursor: hand;
            -fx-background-radius: 0;
            """, color);
        btn.setStyle(baseStyle);

        btn.setOnMouseEntered(e -> {
            if (text.equals("✕")) {
                btn.setStyle(baseStyle + "-fx-background-color: rgba(197, 103, 181, 0.42); -fx-text-fill: white;");
            } else {
                btn.setStyle(baseStyle + "-fx-background-color: rgba(127, 185, 212, 0.16);");
            }
        });

        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));

        return btn;
    }
}











