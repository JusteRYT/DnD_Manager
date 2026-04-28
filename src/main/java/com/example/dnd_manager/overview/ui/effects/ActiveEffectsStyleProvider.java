package com.example.dnd_manager.overview.ui.effects;

public class ActiveEffectsStyleProvider {

    public String titleStyle() {
        return "-fx-text-fill: #888888; -fx-font-size: 11px; -fx-font-style: italic;";
    }

    public String effectLabelStyle() {
        return """
                    -fx-background-color: rgba(200, 155, 60, 0.15);
                    -fx-text-fill: #c89b3c;
                    -fx-padding: 2 6 2 6;
                    -fx-background-radius: 4;
                    -fx-border-color: rgba(200, 155, 60, 0.3);
                    -fx-border-radius: 4;
                    -fx-font-size: 11px;
                    -fx-font-weight: bold;
                """;
    }
}













