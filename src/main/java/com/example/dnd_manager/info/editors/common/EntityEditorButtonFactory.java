package com.example.dnd_manager.info.editors.common;

import javafx.scene.control.Button;

public final class EntityEditorButtonFactory {

    private EntityEditorButtonFactory() {
    }

    public static Button primary(String text, double width) {
        return button(text, width, 38, primaryStyle(false), primaryStyle(true));
    }

    public static Button secondary(String text, double width) {
        return button(text, width, 38, secondaryStyle(false), secondaryStyle(true));
    }

    public static Button arcaneBuff(String text, double width) {
        return button(text, width, 38, attachmentStyle(false), attachmentStyle(true));
    }

    public static Button arcaneSkill(String text, double width) {
        return button(text, width, 38, attachmentStyle(false), attachmentStyle(true));
    }

    public static Button danger(String text) {
        return button(text, 38, 38, dangerStyle(false), dangerStyle(true));
    }

    public static Button statControl(String text) {
        return button(text, 32, 32, statControlStyle(false), statControlStyle(true));
    }

    public static Button statIncreaseControl() {
        return button("+", 32, 32,
                statSemanticStyle("rgba(26, 36, 59, 0.76)", "rgba(175, 196, 216, 0.46)", false),
                statSemanticStyle("rgba(33, 45, 73, 0.90)", "rgba(183, 201, 221, 0.68)", true));
    }

    public static Button statDecreaseControl() {
        return button("-", 32, 32,
                statSemanticStyle("rgba(26, 36, 59, 0.76)", "rgba(196, 189, 214, 0.38)", false),
                statSemanticStyle("rgba(33, 45, 73, 0.90)", "rgba(196, 189, 214, 0.58)", true));
    }

    public static Button iconPicker(String text) {
        return secondary(text, 140);
    }

    private static Button button(String text, double width, double height, String baseStyle, String hoverStyle) {
        Button button = new Button(text);
        button.setMnemonicParsing(false);
        button.setFocusTraversable(false);
        button.setMinHeight(height);
        button.setPrefHeight(height);
        button.setMaxHeight(height);
        button.setMinWidth(width);
        button.setPrefWidth(width);
        button.setMaxWidth(width);
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        button.setOnMousePressed(e -> button.setTranslateY(1));
        button.setOnMouseReleased(e -> button.setTranslateY(0));
        return button;
    }

    private static String sharedStyle() {
        return """
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                -fx-padding: 0 12 0 12;
                -fx-alignment: center;
                """;
    }

    private static String primaryStyle(boolean hover) {
        String background = hover
                ? "linear-gradient(to bottom, #eef3f6, #c7d5df)"
                : "linear-gradient(to bottom, #dfe6ec, #b7c7d3)";
        String border = hover ? "#d8e4eb" : "#b3c4d3";
        String glow = hover ? "rgba(179, 196, 211, 0.48)" : "rgba(179, 196, 211, 0.28)";
        return sharedStyle() + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #0c1018;
                -fx-effect: dropshadow(gaussian, %s, 12, 0.24, 0, 1);
                """.formatted(background, border, glow);
    }

    private static String secondaryStyle(boolean hover) {
        String background = hover ? "rgba(33, 45, 73, 0.88)" : "rgba(26, 36, 59, 0.74)";
        String border = hover ? "rgba(175, 196, 216, 0.52)" : "rgba(75, 93, 127, 0.38)";
        String glow = hover ? "rgba(175, 196, 216, 0.16)" : "rgba(0, 0, 0, 0.0)";
        return sharedStyle() + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #e9edf3;
                -fx-effect: dropshadow(gaussian, %s, 12, 0.22, 0, 0);
                """.formatted(background, border, glow);
    }

    private static String dangerStyle(boolean hover) {
        String background = hover ? "rgba(112, 33, 42, 0.76)" : "rgba(55, 24, 33, 0.66)";
        String border = hover ? "rgba(218, 130, 124, 0.58)" : "rgba(170, 92, 92, 0.34)";
        return sharedStyle() + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #f0d2ce;
                -fx-font-size: 15px;
                """.formatted(background, border);
    }

    private static String statControlStyle(boolean hover) {
        String background = hover ? "rgba(33, 45, 73, 0.88)" : "rgba(26, 36, 59, 0.74)";
        String border = hover ? "rgba(175, 196, 216, 0.52)" : "rgba(75, 93, 127, 0.38)";
        String glow = hover ? "rgba(175, 196, 216, 0.16)" : "rgba(0, 0, 0, 0.0)";
        return sharedStyle() + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #e9edf3;
                -fx-font-size: 15px;
                -fx-padding: 0;
                -fx-effect: dropshadow(gaussian, %s, 10, 0.22, 0, 0);
                """.formatted(background, border, glow);
    }

    private static String statSemanticStyle(String background, String border, boolean hover) {
        String glow = hover ? "rgba(175, 196, 216, 0.18)" : "rgba(0, 0, 0, 0.0)";
        return sharedStyle() + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 16px;
                -fx-font-weight: 900;
                -fx-padding: 0;
                -fx-effect: dropshadow(gaussian, %s, 9, 0.18, 0, 0);
                """.formatted(background, border, glow);
    }

    private static String attachmentStyle(boolean hover) {
        String background = hover ? "rgba(39, 47, 79, 0.86)" : "rgba(24, 31, 54, 0.72)";
        String border = hover ? "rgba(196, 189, 214, 0.58)" : "rgba(196, 189, 214, 0.28)";
        String glow = hover ? "rgba(175, 196, 216, 0.14)" : "rgba(0, 0, 0, 0.0)";
        return sharedStyle() + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #e9edf3;
                -fx-effect: dropshadow(gaussian, %s, 14, 0.30, 0, 0);
                """.formatted(background, border, glow);
    }
}
