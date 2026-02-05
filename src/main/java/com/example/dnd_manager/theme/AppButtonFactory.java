package com.example.dnd_manager.theme;

import javafx.scene.control.Button;

/**
 * Factory for creating styled application buttons.
 */
public final class AppButtonFactory {

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
        acceptColorTheme(button);

        return button;
    }

    public static Button customButton(String text, int width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        acceptColorTheme(button);

        return button;
    }

    private static void acceptColorTheme(Button button) {
        button.setStyle("""
            -fx-background-color: %s;
            -fx-text-fill: %s;
            -fx-font-weight: bold;
            -fx-background-radius: 6;
        """.formatted(
                AppTheme.BUTTON_PRIMARY,
                AppTheme.BUTTON_TEXT
        ));

        button.setOnMouseEntered(e ->
                button.setStyle("""
                    -fx-background-color: %s;
                    -fx-text-fill: %s;
                    -fx-font-weight: bold;
                    -fx-background-radius: 6;
                """.formatted(
                        AppTheme.BUTTON_PRIMARY_HOVER,
                        AppTheme.BUTTON_TEXT
                ))
        );

        button.setOnMouseExited(e ->
                button.setStyle("""
                    -fx-background-color: %s;
                    -fx-text-fill: %s;
                    -fx-font-weight: bold;
                    -fx-background-radius: 6;
                """.formatted(
                        AppTheme.BUTTON_PRIMARY,
                        AppTheme.BUTTON_TEXT
                ))
        );
    }
}
