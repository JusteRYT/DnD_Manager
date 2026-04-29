package com.example.dnd_manager.screen.form;

public class CharacterFormStyleProvider {

    private static final String ASTRAL_TEXT = "#f0f2f7";

    public String formStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 18% 24%, radius 58%, rgba(23, 35, 58, 0.76), transparent 62%),
                    radial-gradient(center 78% 10%, radius 64%, rgba(42, 36, 69, 0.58), transparent 66%),
                    radial-gradient(center 54% 90%, radius 72%, rgba(24, 37, 59, 0.58), transparent 62%),
                    linear-gradient(from 0% 0% to 100% 100%, #070b14, #11172a 52%, #151229 82%, #070b14);
                -fx-background-radius: 0;
                """;
    }

    public String titlePanelStyle() {
        return """
                -fx-background-color: rgba(10, 16, 31, 0.72);
                -fx-border-color: transparent transparent rgba(75, 93, 127, 0.44) transparent;
                -fx-border-width: 0 0 1 0;
                -fx-padding: 16 28 14 28;
                """;
    }

    public String screenSubtitleStyle() {
        return """
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                -fx-text-fill: #9fb2c8;
                -fx-letter-spacing: 1px;
                """;
    }

    public String heroCardStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 16% 0%, radius 96%, rgba(223, 230, 236, 0.07), transparent 58%),
                    radial-gradient(center 100% 100%, radius 100%, rgba(196, 189, 214, 0.08), transparent 62%),
                    linear-gradient(from 0% 0% to 100% 100%, rgba(24, 34, 56, 0.86), rgba(15, 19, 35, 0.82));
                -fx-background-radius: 20;
                -fx-border-color: rgba(75, 93, 127, 0.58);
                -fx-border-radius: 20;
                -fx-border-width: 1;
                -fx-effect:
                    dropshadow(gaussian, rgba(0, 0, 0, 0.24), 24, 0.18, 0, 10),
                    innershadow(gaussian, rgba(223, 230, 236, 0.05), 20, 0.16, 0, 0);
                """;
    }

    public String heroColumnStyle() {
        return """
                -fx-background-color: transparent;
                -fx-background-radius: 0;
                -fx-border-color: transparent;
                -fx-border-width: 0;
                """;
    }

    public String statsSectionStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 0%, radius 82%, rgba(175, 196, 216, 0.07), transparent 64%),
                    rgba(18, 26, 48, 0.72);
                -fx-background-radius: 16;
                -fx-border-color: rgba(75, 93, 127, 0.48);
                -fx-border-radius: 16;
                -fx-border-width: 1;
                -fx-effect:
                    dropshadow(gaussian, rgba(0, 0, 0, 0.22), 18, 0.18, 0, 8),
                    innershadow(gaussian, rgba(223, 230, 236, 0.04), 18, 0.15, 0, 0);
                """;
    }

    public String statsTitleStyle() {
        return """
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: #f0f2f7;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.18), 9, 0.22, 0, 0);
                -fx-border-color: transparent transparent rgba(196, 189, 214, 0.42) transparent;
                -fx-border-width: 0 0 1 0;
                -fx-padding: 0 0 8 0;
                """;
    }

    public String magicalBorderStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 0% 0%, radius 110%, rgba(223, 230, 236, 0.06), transparent 60%),
                    rgba(17, 23, 41, 0.74);
                -fx-background-radius: 18;
                -fx-border-color: rgba(75, 93, 127, 0.42);
                -fx-border-radius: 18;
                -fx-border-width: 1;
                """;
    }

    public String screenTitleStyle() {
        return """
                -fx-font-size: 34px;
                -fx-font-weight: 900;
                -fx-text-fill: #f0f2f7;
                -fx-padding: 18 0 8 0;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.24), 14, 0.24, 0, 1);
                """;
    }

    public String actionBarStyle() {
        return """
                -fx-background-color: rgba(17, 23, 41, 0.88);
                -fx-background-radius: 16;
                -fx-border-color: rgba(75, 93, 127, 0.48);
                -fx-border-radius: 16;
                -fx-border-width: 1;
                """;
    }

    public String tabContentStyle() {
        return """
                -fx-background-color: transparent;
                -fx-background-radius: 0;
                -fx-border-color: transparent;
                -fx-border-width: 0;
                """;
    }

    public String sectionSwitchStyle() {
        return """
                -fx-background-color: rgba(11, 19, 35, 0.64);
                -fx-background-radius: 16;
                -fx-border-color: rgba(75, 93, 127, 0.34);
                -fx-border-radius: 16;
                -fx-border-width: 1;
                """;
    }

    public String sectionSwitchButtonStyle(boolean selected, boolean hover) {
        String background;
        String border;
        String text;
        if (selected) {
            background = "linear-gradient(to bottom, #dfe6ec, #b7c7d3)";
            border = "#b3c4d3";
            text = "#0f1a2b";
        } else {
            background = hover ? "rgba(33, 45, 73, 0.88)" : "rgba(26, 36, 59, 0.74)";
            border = hover ? "rgba(175, 196, 216, 0.52)" : "rgba(75, 93, 127, 0.38)";
            text = "#eef5fb";
        }
        return """
                -fx-background-color: %s;
                -fx-background-radius: 8;
                -fx-border-color: %s;
                -fx-border-radius: 8;
                -fx-text-fill: %s;
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                -fx-padding: 0;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.12), 10, 0.18, 0, 0);
                """.formatted(background, border, text);
    }

    public String saveButtonStyle(boolean hover) {
        String background = hover
                ? "linear-gradient(to bottom, #eef3f6, #c7d5df)"
                : "linear-gradient(to bottom, #dfe6ec, #b7c7d3)";
        String border = hover ? "#d8e4eb" : "#b3c4d3";
        String glow = hover ? "rgba(179, 196, 211, 0.48)" : "rgba(179, 196, 211, 0.28)";
        return """
                -fx-background-color: %s;
                -fx-background-radius: 8;
                -fx-border-color: %s;
                -fx-border-radius: 8;
                -fx-text-fill: #0c1018;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, %s, 14, 0.25, 0, 1);
                """.formatted(background, border, glow);
    }

    public String exitButtonStyle(boolean hover) {
        String background = hover ? "rgba(33, 45, 73, 0.88)" : "rgba(26, 36, 59, 0.74)";
        String border = hover ? "rgba(175, 196, 216, 0.52)" : "rgba(75, 93, 127, 0.38)";
        return """
                -fx-background-color: %s;
                -fx-background-radius: 8;
                -fx-border-color: %s;
                -fx-border-radius: 8;
                -fx-text-fill: #e9edf3;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                """.formatted(background, border);
    }
}












