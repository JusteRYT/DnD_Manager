package com.example.dnd_manager.info.editors.common;

public class EntityEditorStyleProvider {

    public String titleStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                -fx-letter-spacing: 1.2px;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.22), 10, 0.28, 0, 0);
                """;
    }

    public String inputCardStyle() {
        return """
                    -fx-background-color:
                        radial-gradient(center 0% 0%, radius 110%, rgba(223, 230, 236, 0.06), transparent 60%),
                        rgba(18, 26, 48, 0.76);
                    -fx-padding: 15;
                    -fx-background-radius: 16;
                    -fx-border-color: rgba(75, 93, 127, 0.42);
                    -fx-border-radius: 16;
                    -fx-border-width: 1;
                """;
    }

    public String fieldLabelStyle() {
        return "-fx-text-fill: #b7c9dd; -fx-font-size: 10px; -fx-font-weight: bold;";
    }

    public String requiredLabelStyle() {
        return "-fx-text-fill: #ff6b6b; -fx-font-size: 10px; -fx-font-weight: bold;";
    }

    public String editorBodyStyle() {
        return """
                -fx-background-color: transparent;
                -fx-background-radius: 0;
                -fx-border-color: transparent;
                -fx-border-width: 0;
                """;
    }

    public String listPanelStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 0% 0%, radius 100%, rgba(196, 189, 214, 0.06), transparent 60%),
                    rgba(17, 23, 41, 0.70);
                -fx-background-radius: 16;
                -fx-border-color: rgba(75, 93, 127, 0.38);
                -fx-border-radius: 16;
                -fx-border-width: 1;
                """;
    }

    public String listTitleStyle() {
        return "-fx-text-fill: #b7c9dd; -fx-font-size: 11px; -fx-font-weight: bold;";
    }

    public String formSectionStyle() {
        return """
                -fx-background-color: rgba(10, 16, 31, 0.22);
                -fx-background-radius: 12;
                -fx-border-color: transparent;
                -fx-border-width: 0;
                -fx-padding: 10;
                """;
    }

    public String iconPreviewStyle() {
        return """
                -fx-text-fill: #b7c9dd;
                -fx-font-size: 11px;
                -fx-font-weight: bold;
                -fx-background-color: rgba(16, 23, 42, 0.66);
                -fx-background-radius: 14;
                -fx-border-color: rgba(75, 93, 127, 0.40);
                -fx-border-radius: 14;
                -fx-border-width: 1;
                -fx-padding: 8 12 8 8;
                """;
    }

    public String effectsBuilderStyle() {
        return """
                -fx-background-color: rgba(16, 23, 42, 0.42);
                -fx-padding: 14;
                -fx-background-radius: 14;
                -fx-border-color: rgba(75, 93, 127, 0.36);
                -fx-border-radius: 14;
                -fx-border-width: 1;
                """;
    }

    public String entityRowStyle(boolean hover, String accentGlow, String borderColor) {
        String effect = hover
                ? "-fx-effect: dropshadow(gaussian, " + accentGlow + ", 20, 0.28, 0, 0);"
                : "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.18), 10, 0.16, 0, 2);";
        String background = hover ? "rgba(33, 45, 73, 0.96)" : "rgba(18, 26, 48, 0.92)";
        return """
                -fx-background-color: %s;
                -fx-background-radius: 16;
                -fx-border-radius: 16;
                -fx-border-width: 1;
                -fx-border-color: %s;
                -fx-padding: 12 14 12 14;
                %s
                """.formatted(background, borderColor, effect);
    }

    public String entityIconFrameStyle(String accentGlow, String borderColor) {
        return """
                -fx-background-color: rgba(16, 23, 42, 0.72);
                -fx-background-radius: 14;
                -fx-border-color: %s;
                -fx-border-radius: 14;
                -fx-border-width: 1;
                -fx-effect: dropshadow(gaussian, %s, 10, 0.18, 0, 0);
                """.formatted(borderColor, accentGlow);
    }

    public String entityTitleStyle(String color, String glow) {
        return """
                -fx-font-weight: bold;
                -fx-text-fill: %s;
                -fx-font-size: 14px;
                -fx-effect: dropshadow(gaussian, %s, 9, 0.24, 0, 0);
                """.formatted(color, glow);
    }

    public String entityMetaStyle() {
        return "-fx-text-fill: #9fb2c8; -fx-font-size: 11px;";
    }

    public String entityDescriptionStyle() {
        return "-fx-text-fill: #d9e1ec; -fx-font-size: 12px;";
    }

    public String entityChipStyle(String background, String borderColor, String textColor) {
        return """
                -fx-background-color: %s;
                -fx-background-radius: 999;
                -fx-border-color: %s;
                -fx-border-radius: 999;
                -fx-border-width: 1;
                -fx-text-fill: %s;
                -fx-font-size: 10px;
                -fx-font-weight: bold;
                -fx-padding: 4 9 4 9;
                """.formatted(background, borderColor, textColor);
    }

    public String emptyStateStyle() {
        return """
                -fx-background-color: rgba(10, 16, 31, 0.34);
                -fx-background-radius: 14;
                -fx-border-color: rgba(75, 93, 127, 0.30);
                -fx-border-radius: 14;
                -fx-border-width: 1;
                -fx-padding: 22;
                """;
    }

    public String emptyStateTextStyle() {
        return "-fx-text-fill: #9fb2c8; -fx-font-size: 12px; -fx-font-style: italic;";
    }
}













