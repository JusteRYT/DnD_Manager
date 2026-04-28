package com.example.dnd_manager.theme.button;

public class GradientButtonStyleProvider {

    public String valueAdjustCommonStyle() {
        return """
            -fx-background-radius: 4;
            -fx-border-radius: 4;
            -fx-border-width: 1;
            -fx-cursor: hand;
            -fx-padding: 0;
            """;
    }

    public String valueAdjustBaseStyle(String baseColor, String glowColor) {
        return valueAdjustCommonStyle() + """
            -fx-background-color: linear-gradient(to bottom, %1$s, derive(%1$s, -20%%));
            -fx-border-color: derive(%1$s, -30%%);
            -fx-effect: dropshadow(three-pass-box, %2$s, 6, 0, 0, 0);
            """.formatted(baseColor, glowColor);
    }

    public String valueAdjustHoverStyle(String hoverColor, String glowColor) {
        return valueAdjustCommonStyle() + """
            -fx-background-color: linear-gradient(to bottom, derive(%1$s, 20%%), %1$s);
            -fx-border-color: %1$s;
            -fx-effect: dropshadow(three-pass-box, %2$s, 12, 0, 0, 0);
            """.formatted(hoverColor, glowColor);
    }

    public String editIconBaseStyle() {
        return """
                    -fx-background-color: linear-gradient(to bottom, #FFC107, #FF8C00);
                    -fx-background-radius: 4;
                    -fx-cursor: hand;
                    -fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.3), 8, 0, 0, 0);
                """;
    }

    public String editIconHoverStyle() {
        return """
                    -fx-background-color: linear-gradient(to bottom, #ffd54f, #ffa726);
                    -fx-background-radius: 4;
                    -fx-cursor: hand;
                    -fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.6), 12, 0, 0, 0);
                """;
    }

    public String deleteButtonStyle(String color, boolean glowing) {
        String glowOpacity = glowing ? "0.6" : "0.3";
        int glowRadius = glowing ? 12 : 6;
        return valueAdjustCommonStyle() + """
                -fx-background-color: linear-gradient(to bottom, %1$s, derive(%1$s, -20%%));
                -fx-border-color: derive(%1$s, -30%%);
                -fx-effect: dropshadow(three-pass-box, rgba(255, 0, 0, %2$s), %3$d, 0, 0, 0);
                """.formatted(color, glowOpacity, glowRadius);
    }

    public String primaryGradientStyle(int fontSize, boolean hover) {
        String base = """
                -fx-background-color: linear-gradient(to bottom, #FFC107, #FF8C00);
                -fx-text-fill: #222;
                -fx-font-weight: bold;
                -fx-font-size: %dpx;
                -fx-background-radius: 4;
                -fx-cursor: hand;
                """.formatted(fontSize);

        if (hover) {
            return base + "-fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.6), 15, 0, 0, 0);";
        }
        return base + "-fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.3), 8, 0, 0, 0);";
    }
}













