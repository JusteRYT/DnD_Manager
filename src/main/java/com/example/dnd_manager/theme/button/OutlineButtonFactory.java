package com.example.dnd_manager.theme.button;

import com.example.dnd_manager.theme.AppTheme;
import javafx.scene.control.Button;

/**
 * Factory for flat/outline buttons and simple color variants.
 */
final class OutlineButtonFactory {

    private static final int DEFAULT_FONT_SIZE = 14;

    private OutlineButtonFactory() {
    }

    static Button customButton(String text, int width, String primaryColor, String secondaryColor) {
        Button button = new Button(text);
        if (width != 0) {
            button.setPrefWidth(width);
        }
        applyColorTheme(button, primaryColor, secondaryColor);
        return button;
    }

    static Button actionImport(String text, int width) {
        final String hoverStyle = """
                -fx-background-color: rgba(200, 155, 60, 0.1);
                -fx-text-fill: #f6bb4a;
                -fx-border-color: #f6bb4a;
                -fx-border-radius: 4;
                -fx-cursor: hand;
                """;
        return transparentActionButton(text, width, hoverStyle);
    }

    static Button actionExit(String text, int width) {
        final String hoverStyle = """
                -fx-background-color: rgba(232, 17, 35, 0.1);
                -fx-text-fill: #ff6b6b;
                -fx-border-color: #ff6b6b;
                -fx-border-radius: 4;
                -fx-cursor: hand;
                """;
        return transparentActionButton(text, width, hoverStyle);
    }

    static Button actionAdd(String text, int width) {
        final String hoverStyle = """
            -fx-background-color: rgba(200, 155, 60, 0.1);
            -fx-text-fill: #c89b3c;
            -fx-border-color: #c89b3c;
            -fx-border-radius: 4;
            -fx-cursor: hand;
            """;
        return transparentActionButton(text, width, hoverStyle);
    }

    private static void applyColorTheme(Button button, String primaryColor, String secondaryColor) {
        String common = """
                -fx-font-weight: bold;
                -fx-background-radius: 6;
                -fx-border-radius: 6;
                -fx-border-width: 1;
                -fx-border-color: transparent;
                -fx-font-size: %dpx;
                -fx-text-fill: %s;
                """.formatted(DEFAULT_FONT_SIZE, AppTheme.BUTTON_TEXT);

        String base = common + "-fx-background-color: " + primaryColor + ";";
        String hover = common + "-fx-background-color: " + secondaryColor + ";";

        button.setStyle(base);
        button.setOnMouseEntered(e -> button.setStyle(hover));
        button.setOnMouseExited(e -> button.setStyle(base));
    }

    private static Button transparentActionButton(String text, int width, String hoverStyle) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        String baseStyle = """
                -fx-background-color: transparent;
                -fx-text-fill: #777;
                -fx-border-color: #444;
                -fx-border-radius: 4;
                -fx-cursor: hand;
                """;
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        return button;
    }
}













