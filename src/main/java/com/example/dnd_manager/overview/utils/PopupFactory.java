package com.example.dnd_manager.overview.utils;

import javafx.scene.control.Label;

/**
 * Factory for creating styled popup content.
 * <p>
 * Follows Open/Closed principle allowing new popup styles to be added.
 */
public final class PopupFactory {

    private PopupFactory() {
    }

    /**
     * Creates a default styled tooltip-like popup label.
     *
     * @param text popup text
     * @return styled label
     */
    public static Label tooltip(String text) {
        Label label = new Label(text);
        label.setStyle("""
                -fx-background-color: #333333;
                -fx-text-fill: #ffffff;
                -fx-padding: 5 10 5 10;
                -fx-background-radius: 4;
                """);
        return label;
    }
}
