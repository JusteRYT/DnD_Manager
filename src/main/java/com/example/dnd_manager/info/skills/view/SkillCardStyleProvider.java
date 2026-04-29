package com.example.dnd_manager.info.skills.view;

public class SkillCardStyleProvider {

    private static final String CARD_GLOW = "rgba(196, 189, 214, 0.20)";
    private static final String IDLE_BORDER = "rgba(75, 93, 127, 0.46)";
    private static final String HOVER_BORDER = "rgba(175, 196, 216, 0.62)";

    public String cardIdleStyle() {
        return baseStyle(IDLE_BORDER, "rgba(0, 0, 0, 0.18)");
    }

    public String cardHoverStyle() {
        return baseStyle(HOVER_BORDER, CARD_GLOW)
                + "-fx-background-color: rgba(33, 45, 73, 0.96);"
                + "-fx-effect: dropshadow(gaussian, " + CARD_GLOW + ", 18, 0.22, 0, 0);";
    }

    public String headerBandStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 0% 0%, radius 110%, rgba(223, 230, 236, 0.07), transparent 62%),
                    rgba(10, 16, 31, 0.24);
                -fx-background-radius: 12;
                -fx-border-color: rgba(75, 93, 127, 0.20);
                -fx-border-radius: 12;
                -fx-border-width: 1;
                -fx-padding: 8;
                """;
    }

    public String iconFrameStyle() {
        return """
                    -fx-background-color: rgba(16, 23, 42, 0.72);
                    -fx-border-color: rgba(175, 196, 216, 0.48);
                    -fx-border-width: 1;
                    -fx-border-radius: 12;
                    -fx-background-radius: 12;
                    -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.12), 10, 0.20, 0, 0);
                """;
    }

    public String nameStyle() {
        return """
                -fx-font-size: 14px;
                -fx-font-weight: 900;
                -fx-text-fill: #edf2f8;
                """;
    }

    public String briefDescriptionStyle() {
        return "-fx-font-size: 11px; -fx-text-fill: #d5deea; -fx-font-style: italic; -fx-line-spacing: 2px;";
    }

    public String sectionCaptionStyle() {
        return "-fx-text-fill: #9fb2c8; -fx-font-size: 9px; -fx-font-weight: bold; -fx-letter-spacing: 0.8px;";
    }

    public String activationBadgeStyle() {
        return """
                    -fx-background-color: rgba(39, 47, 79, 0.70);
                    -fx-border-color: rgba(196, 189, 214, 0.40);
                    -fx-border-width: 1;
                    -fx-border-radius: 999;
                    -fx-background-radius: 999;
                    -fx-text-fill: #e7edf5;
                    -fx-font-size: 11px;
                    -fx-font-weight: bold;
                    -fx-padding: 3 9 3 9;
                """;
    }

    public String descriptionPanelStyle() {
        return """
                -fx-background-color: rgba(10, 16, 31, 0.26);
                -fx-background-radius: 10;
                -fx-border-color: rgba(75, 93, 127, 0.22);
                -fx-border-radius: 10;
                -fx-border-width: 1;
                -fx-padding: 8;
                """;
    }

    public String effectsPanelStyle() {
        return """
                -fx-background-color: rgba(10, 16, 31, 0.26);
                -fx-background-radius: 10;
                -fx-border-color: rgba(75, 93, 127, 0.22);
                -fx-border-radius: 10;
                -fx-border-width: 1;
                -fx-padding: 7 8 7 8;
                """;
    }

    public String effectBadgeStyle(String color) {
        return """
                    -fx-background-color: %1$s26;
                    -fx-border-color: %1$s80;
                    -fx-border-width: 1;
                    -fx-border-radius: 999;
                    -fx-background-radius: 999;
                    -fx-text-fill: #f6fbff;
                    -fx-font-size: 10px;
                    -fx-font-weight: bold;
                    -fx-padding: 3 8 3 8;
                    -fx-letter-spacing: 0.5px;
                """.formatted(color);
    }

    public String popupContainerStyle() {
        return "-fx-background-color: #11172a; "
                + "-fx-border-color: rgba(183, 162, 220, 0.58); "
                + "-fx-border-width: 1.5; "
                + "-fx-background-radius: 10; "
                + "-fx-border-radius: 10;";
    }

    public String popupDescriptionStyle() {
        return "-fx-font-size: 14px; -fx-text-fill: #dcdcdc; -fx-line-spacing: 3px;";
    }

    public String sourceInfoStyle() {
        return "-fx-text-fill: #b7c9dd; -fx-font-size: 11px; -fx-font-weight: bold;";
    }

    public String sourceBadgeStyle(String backgroundColor, String textColor) {
        return """
                    -fx-background-color: %1$s;
                    -fx-text-fill: %2$s;
                    -fx-font-size: 11px;
                    -fx-font-weight: bold;
                    -fx-background-radius: 50;
                    -fx-border-color: rgba(223, 230, 236, 0.32);
                    -fx-border-width: 1.5;
                    -fx-border-radius: 50;
                """.formatted(backgroundColor, textColor);
    }

    private String baseStyle(String borderColor, String shadowColor) {
        return """
                -fx-background-color: rgba(18, 26, 48, 0.92);
                -fx-background-radius: 16;
                -fx-border-radius: 16;
                -fx-border-width: 1;
                -fx-border-color: %s;
                -fx-padding: 10;
                -fx-effect: dropshadow(gaussian, %s, 10, 0.14, 0, 1);
                """.formatted(borderColor, shadowColor);
    }
}













