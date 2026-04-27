package com.example.dnd_manager.overview.panel;

public class FamiliarCardStyleProvider {

    public String nameLabelStyle() {
        return "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;";
    }

    public String raceClassLabelStyle() {
        return "-fx-text-fill: #aaa; -fx-font-size: 11px;";
    }

    public String hpLabelStyle() {
        return "-fx-text-fill: #ff6b6b; -fx-font-size: 11px; -fx-font-weight: bold;";
    }

    public String acLabelStyle() {
        return "-fx-text-fill: #74c0fc; -fx-font-size: 11px; -fx-font-weight: bold;";
    }

    public String cardIdleStyle() {
        String base = """
                -fx-background-radius: 6; -fx-border-radius: 6;
                -fx-border-width: 1;
                -fx-cursor: hand;
                """;
        return base + """
                -fx-background-color: rgba(255, 255, 255, 0.05);
                -fx-border-color: rgba(156, 39, 176, 0.2);
                -fx-effect: null;
                """;
    }

    public String cardHoverStyle() {
        String base = """
                -fx-background-radius: 6; -fx-border-radius: 6;
                -fx-border-width: 1;
                -fx-cursor: hand;
                """;
        return base + """
                -fx-border-color: #9c27b0;
                -fx-background-color: #9c27b0, #2b2b2b;
                -fx-background-insets: 0, 1;
                -fx-effect: dropshadow(three-pass-box, rgba(156, 39, 176, 0.5), 15, 0.2, 0, 0);
                """;
    }
}

