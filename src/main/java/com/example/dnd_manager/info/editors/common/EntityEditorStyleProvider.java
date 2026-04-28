package com.example.dnd_manager.info.editors.common;

public class EntityEditorStyleProvider {

    public String titleStyle() {
        return "-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 13px; -fx-letter-spacing: 1.5px;";
    }

    public String inputCardStyle() {
        return """
                    -fx-background-color: linear-gradient(to right, #252526, #1e1e1e);
                    -fx-padding: 15;
                    -fx-background-radius: 8;
                    -fx-border-color: #3a3a3a;
                    -fx-border-radius: 8;
                """;
    }

    public String fieldLabelStyle() {
        return "-fx-text-fill: #666; -fx-font-size: 10px; -fx-font-weight: bold;";
    }

    public String requiredLabelStyle() {
        return "-fx-text-fill: #ff6b6b; -fx-font-size: 10px; -fx-font-weight: bold;";
    }
}













