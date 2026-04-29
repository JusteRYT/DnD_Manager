package com.example.dnd_manager.info.stats.editor;

public class StatsEditorStyleProvider {

    public String rowStyle(boolean hover) {
        String border = hover ? "rgba(175, 196, 216, 0.54)" : "rgba(75, 93, 127, 0.34)";
        String effect = hover
                ? "-fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.18), 14, 0.28, 0, 0);"
                : "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.12), 8, 0.14, 0, 1);";
        return """
                -fx-background-color: rgba(16, 23, 42, 0.42);
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-border-width: 1;
                -fx-border-color: %s;
                -fx-padding: 8 10;
                %s
                """.formatted(border, effect);
    }
}
