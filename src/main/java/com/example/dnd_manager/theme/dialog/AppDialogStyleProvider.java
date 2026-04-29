package com.example.dnd_manager.theme.dialog;

import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;

public class AppDialogStyleProvider {

    public String rootStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 16% 8%, radius 86%, rgba(23, 35, 58, 0.84), transparent 64%),
                    radial-gradient(center 92% 12%, radius 76%, rgba(42, 36, 69, 0.62), transparent 68%),
                    linear-gradient(from 0% 0% to 100% 100%, #070b14, #11172a 56%, #151229);
                -fx-background-radius: 14;
                -fx-border-color: rgba(127, 185, 212, 0.46);
                -fx-border-radius: 14;
                -fx-border-width: 1;
                -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.42), 22, 0.22, 0, 10);
                """;
    }

    public String contentAreaStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 0%, radius 90%, rgba(223, 230, 236, 0.05), transparent 64%),
                    rgba(10, 16, 31, 0.36);
                -fx-background-radius: 14;
                """;
    }

    public String panelStyle() {
        return """
                -fx-background-color: rgba(17, 23, 41, 0.76);
                -fx-background-radius: 12;
                -fx-border-color: rgba(75, 93, 127, 0.42);
                -fx-border-radius: 12;
                -fx-border-width: 1;
                -fx-padding: 12;
                """;
    }

    public String labelStyle() {
        return """
                -fx-text-fill: #b7c9dd;
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                """;
    }

    public String messageStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 15px;
                -fx-font-weight: bold;
                -fx-line-spacing: 3px;
                """;
    }

    public String sectionTitleStyle() {
        return """
                -fx-text-fill: #c4bdd6;
                -fx-font-size: 12px;
                -fx-font-weight: 900;
                """;
    }

    public String sectionTextStyle() {
        return """
                -fx-text-fill: #dbe5ea;
                -fx-font-size: 13px;
                -fx-line-spacing: 3px;
                """;
    }

    public String resourceValueStyle(String color) {
        return "-fx-text-fill: %s; -fx-font-size: 15px; -fx-font-weight: bold;".formatted(color);
    }

    public String statLabelStyle(String color) {
        return "-fx-text-fill: %s; -fx-font-size: 12px; -fx-font-weight: bold;".formatted(color);
    }

    public String textInputStyle(boolean focused) {
        String border = focused ? "rgba(175, 196, 216, 0.72)" : "rgba(75, 93, 127, 0.46)";
        String glow = focused ? "-fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.18), 12, 0.24, 0, 0);" : "";
        return """
                -fx-background-color: rgba(16, 23, 42, 0.94);
                -fx-control-inner-background: #10172a;
                -fx-text-fill: #f0f2f7;
                -fx-prompt-text-fill: #8fa4bd;
                -fx-border-color: %s;
                -fx-border-radius: 7;
                -fx-background-radius: 7;
                -fx-padding: 9 12 9 12;
                -fx-font-size: 13px;
                -fx-focus-color: transparent;
                -fx-faint-focus-color: transparent;
                %s
                """.formatted(border, glow);
    }

    public String progressBarStyle() {
        return """
                -fx-accent: #b7c9dd;
                -fx-control-inner-background: rgba(16, 23, 42, 0.94);
                -fx-background-color: rgba(75, 93, 127, 0.42);
                -fx-background-radius: 999;
                """;
    }

    public String primaryButtonStyle(boolean hover) {
        String background = hover
                ? "linear-gradient(to bottom, #eef3f6, #c7d5df)"
                : "linear-gradient(to bottom, #dfe6ec, #b7c7d3)";
        String border = hover ? "#d8e4eb" : "#b3c4d3";
        String glow = hover ? "rgba(179, 196, 211, 0.46)" : "rgba(179, 196, 211, 0.24)";
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

    public void applyPrimaryButton(Button button) {
        applyButtonStyle(button, primaryButtonStyle(false), primaryButtonStyle(true));
    }

    public void applySecondaryButton(Button button) {
        applyButtonStyle(button, secondaryButtonStyle(false), secondaryButtonStyle(true));
    }

    public void applyTextInput(TextInputControl control) {
        control.setStyle(textInputStyle(false));
        control.focusedProperty().addListener((obs, oldValue, focused) ->
                control.setStyle(textInputStyle(focused)));
    }

    private String sharedButtonStyle() {
        return """
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                -fx-border-width: 1;
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                -fx-padding: 0 14 0 14;
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
