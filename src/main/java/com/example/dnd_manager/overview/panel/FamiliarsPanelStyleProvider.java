package com.example.dnd_manager.overview.panel;

public class FamiliarsPanelStyleProvider {

    public String accentColor() {
        return "#9c27b0";
    }

    public String titleStyle() {
        return "-fx-text-fill: %s; -fx-font-size: 16px; -fx-font-weight: bold; -fx-letter-spacing: 1.5;"
                .formatted(accentColor());
    }

    public String idleStyle() {
        String commonStyle = """
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
                -fx-background-radius: 10;
                -fx-border-color: %s;
                -fx-border-radius: 10;
                -fx-border-width: 1;
                -fx-padding: 12;
                """.formatted(accentColor());
        return commonStyle + "-fx-effect: dropshadow(three-pass-box, rgba(156, 39, 176, 0.15), 15, 0, 0, 0);";
    }

    public String hoverStyle() {
        String commonStyle = """
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
                -fx-background-radius: 10;
                -fx-border-color: %s;
                -fx-border-radius: 10;
                -fx-border-width: 1;
                -fx-padding: 12;
                """.formatted(accentColor());
        return commonStyle + "-fx-effect: dropshadow(three-pass-box, %s, 10, 0.2, 0, 0);".formatted(accentColor());
    }

    public String emptyLabelStyle() {
        return "-fx-text-fill: #666; -fx-font-style: italic;";
    }
}

