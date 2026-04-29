package com.example.dnd_manager.theme.window;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainWindowConfigurer {

    private static final double MIN_WIDTH = 900;
    private static final double MIN_HEIGHT = 620;
    private static final double MAX_INITIAL_WIDTH = 1360;
    private static final double MAX_INITIAL_HEIGHT = 860;
    private static final double SCREEN_USAGE = 0.88;

    public Scene configure(Stage stage, VBox root) {
        root.setPadding(new Insets(0, 1, 1, 1));
        root.setStyle("""
                -fx-background-color: #0b0d10;
                -fx-border-color: rgba(127, 185, 212, 0.46);
                -fx-border-width: 1;
                """);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double width = Math.max(MIN_WIDTH, Math.min(MAX_INITIAL_WIDTH, bounds.getWidth() * SCREEN_USAGE));
        double height = Math.max(MIN_HEIGHT, Math.min(MAX_INITIAL_HEIGHT, bounds.getHeight() * SCREEN_USAGE));

        Scene scene = new Scene(root, width, height);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        return scene;
    }
}
