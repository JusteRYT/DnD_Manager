package com.example.dnd_manager.theme;

import javafx.scene.control.Button;

import java.awt.*;

/**
 * Factory for creating styled application buttons.
 */
public final class AppButtonFactory {

    private static final int DEFAULT_SIZE_FONT = 14;

    private AppButtonFactory() {
    }

    /**
     * Creates primary action button.
     *
     * @param text button text
     * @return styled button
     */
    public static Button primary(String text) {
        Button button = new Button(text);
        button.setPrefWidth(220);
        acceptColorTheme(button, DEFAULT_SIZE_FONT, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);

        return button;
    }

    public static Button customButton(String text, int width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        acceptColorTheme(button, DEFAULT_SIZE_FONT, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);

        return button;
    }

    public static Button customButton(String text, int width, String primaryColor, String secondaryColor) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        acceptColorTheme(button, DEFAULT_SIZE_FONT, primaryColor,  secondaryColor);

        return button;
    }

    public static Button customButton(String text, int width, int height, int fontSize) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        acceptColorTheme(button, fontSize, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);

        return button;
    }

    private static void acceptColorTheme(Button button, int fontSize, String primaryColor, String secondaryColor) {
        String baseStyle = """
                    -fx-background-color: %s;
                    -fx-text-fill: %s;
                    -fx-font-weight: bold;
                    -fx-background-radius: 6;
                    -fx-font-size: %dpx;
                """.formatted(primaryColor, AppTheme.BUTTON_TEXT, fontSize);

        button.setStyle(baseStyle);

        button.setOnMouseEntered(e -> button.setStyle("""
                    -fx-background-color: %s;
                    -fx-text-fill: %s;
                    -fx-font-weight: bold;
                    -fx-background-radius: 6;
                    -fx-font-size: %dpx;
                """.formatted(secondaryColor, AppTheme.BUTTON_TEXT, fontSize)));

        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }
}
