package com.example.dnd_manager.info.skills.view;

public class SkillCardStyleProvider {

    private static final String ACCENT_COLOR = "#c89b3c";
    private static final String IDLE_BORDER = "#4a4a4a";

    public String cardIdleStyle() {
        return baseStyle() + "-fx-border-color: " + IDLE_BORDER + ";";
    }

    public String cardHoverStyle() {
        return baseStyle()
                + "-fx-border-color: " + ACCENT_COLOR + ";"
                + "-fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.4), 20, 0.1, 0, 0);";
    }

    public String iconFrameStyle() {
        return """
                    -fx-border-color: #c89b3c;
                    -fx-border-width: 2;
                    -fx-border-radius: 6;
                    -fx-background-color: #1e1e1e;
                    -fx-background-radius: 6;
                """;
    }

    public String nameStyle() {
        return "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #c89b3c;";
    }

    public String briefDescriptionStyle() {
        return "-fx-font-size: 10px; -fx-text-fill: #888888; -fx-font-style: italic;";
    }

    public String effectBadgeStyle(String color) {
        return """
                    -fx-background-color: %1$s26;
                    -fx-border-color: %1$s80;
                    -fx-border-width: 1;
                    -fx-border-radius: 10;
                    -fx-background-radius: 10;
                    -fx-text-fill: white;
                    -fx-font-size: 10px;
                    -fx-font-weight: bold;
                    -fx-padding: 3 10 3 10;
                    -fx-letter-spacing: 0.5px;
                """.formatted(color);
    }

    public String popupContainerStyle() {
        return "-fx-background-color: #1a1a1a; "
                + "-fx-border-color: #c89b3c; "
                + "-fx-border-width: 1.5; "
                + "-fx-background-radius: 10; "
                + "-fx-border-radius: 10;";
    }

    public String popupDescriptionStyle() {
        return "-fx-font-size: 14px; -fx-text-fill: #dcdcdc; -fx-line-spacing: 3px;";
    }

    public String sourceInfoStyle() {
        return "-fx-text-fill: #55ccff; -fx-font-size: 11px; -fx-font-weight: bold;";
    }

    public String sourceBadgeStyle(String backgroundColor, String textColor) {
        return """
                    -fx-background-color: %1$s;
                    -fx-text-fill: %2$s;
                    -fx-font-size: 11px;
                    -fx-font-weight: bold;
                    -fx-background-radius: 50;
                    -fx-border-color: #1a1a1a;
                    -fx-border-width: 1.5;
                    -fx-border-radius: 50;
                """.formatted(backgroundColor, textColor);
    }

    private String baseStyle() {
        return """
            -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1a1a1a);
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-border-width: 1.5;
            -fx-padding: 15 10 15 10;
            """;
    }
}













