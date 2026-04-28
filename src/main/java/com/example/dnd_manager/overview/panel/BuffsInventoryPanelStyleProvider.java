package com.example.dnd_manager.overview.panel;

public class BuffsInventoryPanelStyleProvider {

    private static final String ACCENT_COLOR = "#3aa3c3";

    public String buffsWrapperIdleStyle() {
        return commonStyle() + "-fx-effect: dropshadow(three-pass-box, rgba(58, 163, 195, 0.2), 15, 0, 0, 0);";
    }

    public String buffsWrapperHoverStyle() {
        return commonStyle() + "-fx-effect: dropshadow(three-pass-box, %s, 10, 0.2, 0, 0);".formatted(ACCENT_COLOR);
    }

    private String commonStyle() {
        return """
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
                -fx-background-radius: 10;
                -fx-border-color: %s;
                -fx-border-radius: 10;
                -fx-border-width: 1;
                -fx-padding: 8;
                """.formatted(ACCENT_COLOR);
    }
}













