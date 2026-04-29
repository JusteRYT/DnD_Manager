package com.example.dnd_manager.screen.selection;

import javafx.scene.control.Button;

public class CharacterSelectionStyleProvider {

    public String rootStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 18% 24%, radius 58%, rgba(23, 35, 58, 0.76), transparent 62%),
                    radial-gradient(center 78% 10%, radius 64%, rgba(42, 36, 69, 0.58), transparent 66%),
                    radial-gradient(center 54% 90%, radius 72%, rgba(24, 37, 59, 0.58), transparent 62%),
                    linear-gradient(from 0% 0% to 100% 100%, #070b14, #11172a 52%, #151229 82%, #070b14);
                """;
    }

    public String headerStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 12% 0%, radius 92%, rgba(223, 230, 236, 0.08), transparent 58%),
                    linear-gradient(from 0% 0% to 100% 100%, rgba(24, 34, 56, 0.84), rgba(15, 19, 35, 0.78));
                -fx-background-radius: 18;
                -fx-border-color: rgba(75, 93, 127, 0.52);
                -fx-border-radius: 18;
                -fx-border-width: 1;
                -fx-effect:
                    dropshadow(gaussian, rgba(0, 0, 0, 0.24), 20, 0.18, 0, 8),
                    innershadow(gaussian, rgba(223, 230, 236, 0.05), 20, 0.16, 0, 0);
                """;
    }

    public String titleStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 34px;
                -fx-font-weight: 900;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.22), 14, 0.24, 0, 1);
                """;
    }

    public String subtitleStyle() {
        return """
                -fx-text-fill: #b6bed0;
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                """;
    }

    public String countChipStyle() {
        return """
                -fx-background-color: rgba(39, 47, 79, 0.76);
                -fx-background-radius: 999;
                -fx-border-color: rgba(196, 189, 214, 0.34);
                -fx-border-radius: 999;
                -fx-border-width: 1;
                -fx-text-fill: #e9edf3;
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                -fx-padding: 7 12 7 12;
                """;
    }

    public String contentPanelStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 0% 0%, radius 90%, rgba(223, 230, 236, 0.05), transparent 60%),
                    rgba(17, 23, 41, 0.72);
                -fx-background-radius: 18;
                -fx-border-color: rgba(75, 93, 127, 0.42);
                -fx-border-radius: 18;
                -fx-border-width: 1;
                """;
    }

    public String gridStyle() {
        return """
                -fx-background-color: transparent;
                -fx-padding: 4;
                """;
    }

    public String footerStyle() {
        return """
                -fx-background-color: rgba(17, 23, 41, 0.82);
                -fx-background-radius: 16;
                -fx-border-color: rgba(75, 93, 127, 0.44);
                -fx-border-radius: 16;
                -fx-border-width: 1;
                """;
    }

    public String emptyStateStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 0%, radius 88%, rgba(175, 196, 216, 0.08), transparent 62%),
                    rgba(10, 16, 31, 0.34);
                -fx-background-radius: 18;
                -fx-border-color: rgba(75, 93, 127, 0.34);
                -fx-border-radius: 18;
                -fx-border-width: 1;
                -fx-padding: 32;
                """;
    }

    public String emptyTitleStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 20px;
                -fx-font-weight: bold;
                """;
    }

    public String emptyHintStyle() {
        return """
                -fx-text-fill: #aab8cf;
                -fx-font-size: 13px;
                """;
    }

    public String cardStyle(boolean hover) {
        String background = hover ? "rgba(33, 45, 73, 0.96)" : "rgba(18, 26, 48, 0.92)";
        String border = hover ? "rgba(175, 196, 216, 0.62)" : "rgba(75, 93, 127, 0.46)";
        String glow = hover ? "rgba(196, 189, 214, 0.20)" : "rgba(0, 0, 0, 0.18)";
        return """
                -fx-background-color: %s;
                -fx-background-radius: 18;
                -fx-border-color: %s;
                -fx-border-radius: 18;
                -fx-border-width: 1;
                -fx-padding: 12;
                -fx-effect: dropshadow(gaussian, %s, 16, 0.18, 0, 4);
                -fx-cursor: hand;
                """.formatted(background, border, glow);
    }

    public String portraitFrameStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 10%, radius 90%, rgba(223, 230, 236, 0.08), transparent 62%),
                    rgba(16, 23, 42, 0.72);
                -fx-background-radius: 16;
                -fx-border-color: rgba(175, 196, 216, 0.38);
                -fx-border-radius: 16;
                -fx-border-width: 1;
                """;
    }

    public String cardNameStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 18px;
                -fx-font-weight: 900;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.16), 8, 0.20, 0, 0);
                """;
    }


    public String chipStyle() {
        return """
                -fx-background-color: rgba(39, 47, 79, 0.70);
                -fx-background-radius: 999;
                -fx-border-color: rgba(196, 189, 214, 0.30);
                -fx-border-radius: 999;
                -fx-border-width: 1;
                -fx-text-fill: #e9edf3;
                -fx-font-size: 11px;
                -fx-font-weight: bold;
                -fx-padding: 4 8 4 8;
                """;
    }

    public String metricStyle() {
        return """
                -fx-background-color: rgba(10, 16, 31, 0.28);
                -fx-background-radius: 10;
                -fx-border-color: rgba(75, 93, 127, 0.24);
                -fx-border-radius: 10;
                -fx-border-width: 1;
                -fx-padding: 6 8 6 8;
                """;
    }

    public String metricCaptionStyle() {
        return """
                -fx-text-fill: #8fa4bd;
                -fx-font-size: 9px;
                -fx-font-weight: bold;
                """;
    }

    public String metricValueStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                """;
    }

    public String openHintStyle() {
        return """
                -fx-text-fill: #8fa4bd;
                -fx-font-size: 11px;
                -fx-font-weight: bold;
                -fx-padding: 2 4 0 0;
                """;
    }

    public String actionButtonStyle(boolean hover) {
        String background = hover
                ? "linear-gradient(to bottom, #eef3f6, #c7d5df)"
                : "linear-gradient(to bottom, #dfe6ec, #b7c7d3)";
        String border = hover ? "#d8e4eb" : "#b3c4d3";
        String glow = hover ? "rgba(179, 196, 211, 0.44)" : "rgba(179, 196, 211, 0.24)";
        return sharedButtonStyle() + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #0c1018;
                -fx-effect: dropshadow(gaussian, %s, 12, 0.22, 0, 1);
                """.formatted(background, border, glow);
    }

    public String secondaryButtonStyle(boolean hover) {
        String background = hover ? "rgba(33, 45, 73, 0.88)" : "rgba(26, 36, 59, 0.74)";
        String border = hover ? "rgba(175, 196, 216, 0.52)" : "rgba(75, 93, 127, 0.38)";
        return sharedButtonStyle() + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #e9edf3;
                """.formatted(background, border);
    }

    public String dangerButtonStyle(boolean hover) {
        String background = hover ? "rgba(75, 43, 66, 0.88)" : "rgba(60, 35, 54, 0.74)";
        String border = hover ? "rgba(168, 121, 147, 0.58)" : "rgba(133, 96, 120, 0.42)";
        return sharedButtonStyle() + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #f1e7ee;
                """.formatted(background, border);
    }


    public void applySecondaryAction(Button button) {
        applyButtonStyle(button, secondaryButtonStyle(false), secondaryButtonStyle(true));
    }

    public void applyDangerAction(Button button) {
        applyButtonStyle(button, dangerButtonStyle(false), dangerButtonStyle(true));
    }

    private String sharedButtonStyle() {
        return """
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                -fx-padding: 0 12 0 12;
                """;
    }

    private void applyButtonStyle(Button button, String baseStyle, String hoverStyle) {
        button.setFocusTraversable(false);
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        button.setOnMousePressed(e -> button.setTranslateY(1));
        button.setOnMouseReleased(e -> button.setTranslateY(0));
    }
}
