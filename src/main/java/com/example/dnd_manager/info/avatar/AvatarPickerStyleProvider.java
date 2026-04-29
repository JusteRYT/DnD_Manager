package com.example.dnd_manager.info.avatar;

public class AvatarPickerStyleProvider {

    public String frameStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 0%, radius 88%, rgba(175, 196, 216, 0.08), transparent 62%),
                    rgba(18, 26, 48, 0.72);
                -fx-border-color: rgba(75, 93, 127, 0.44);
                -fx-border-width: 1;
                -fx-border-radius: 20;
                -fx-background-radius: 20;
                -fx-padding: 6;
                -fx-effect:
                    dropshadow(gaussian, rgba(0, 0, 0, 0.24), 20, 0.22, 0, 8),
                    innershadow(gaussian, rgba(223, 230, 236, 0.05), 18, 0.15, 0, 0);
                """;
    }

    public String actionButtonStyle(boolean hover) {
        String background = hover ? "rgba(33, 45, 73, 0.88)" : "rgba(26, 36, 59, 0.74)";
        String border = hover ? "rgba(175, 196, 216, 0.52)" : "rgba(75, 93, 127, 0.38)";
        String effect = hover
                ? "-fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.18), 12, 0.24, 0, 0);"
                : "";
        return """
                -fx-background-color: %s;
                -fx-text-fill: #e9edf3;
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                -fx-border-color: %s;
                -fx-border-width: 1;
                -fx-padding: 7 14;
                -fx-cursor: hand;
                %s
                """.formatted(background, border, effect);
    }
}
