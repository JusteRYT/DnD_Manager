package com.example.dnd_manager.info.editors.familiar;

public class FamiliarEditorStyleProvider {

    public String baseCardStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 20% 0%, radius 84%, rgba(175, 196, 216, 0.10), transparent 60%),
                    linear-gradient(to bottom right, rgba(24, 34, 56, 0.72), rgba(15, 19, 35, 0.70));
                -fx-padding: 15;
                -fx-background-radius: 16;
                -fx-border-color: rgba(75, 93, 127, 0.48);
                -fx-border-radius: 16;
                -fx-border-width: 1;
                """;
    }

    public String fieldLabelStyle() {
        return "-fx-text-fill: #b7c9dd; -fx-font-size: 10px; -fx-font-weight: bold;";
    }
}












