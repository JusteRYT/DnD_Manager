package com.example.dnd_manager.info.text;

public class BaseInfoFormStyleProvider {

    public String sectionTitleStyle() {
        return """
                -fx-font-size: 18px;
                -fx-text-fill: #f0f2f7;
                -fx-font-weight: bold;
                -fx-letter-spacing: 1px;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.22), 10, 0.28, 0, 0);
                """;
    }

    public String fieldLabelStyle() {
        return "-fx-text-fill: #b7c9dd; -fx-font-size: 11px; -fx-font-weight: bold;";
    }

    public String panelStyle() {
        return """
                -fx-background-color: transparent;
                -fx-background-radius: 16;
                -fx-border-color: transparent;
                -fx-border-radius: 16;
                -fx-border-width: 1;
                """;
    }

    public String fieldCardStyle() {
        return """
                -fx-background-color: transparent;
                -fx-background-radius: 0;
                -fx-border-color: transparent;
                -fx-border-width: 0;
                -fx-padding: 4 0 4 0;
                """;
    }

    public String requiredLabelStyle() {
        return """
                -fx-text-fill: #ff6b6b;
                -fx-font-size: 10px;
                -fx-font-weight: bold;
                """;
    }
}












