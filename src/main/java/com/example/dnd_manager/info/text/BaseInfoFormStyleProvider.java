package com.example.dnd_manager.info.text;

public class BaseInfoFormStyleProvider {

    public String sectionTitleStyle() {
        return """
                -fx-font-size: 18px;
                -fx-text-fill: #c89b3c;
                -fx-font-weight: bold;
                -fx-letter-spacing: 1px;
                """;
    }

    public String fieldLabelStyle() {
        return "-fx-text-fill: #c89b3c; -fx-font-size: 12px; -fx-font-weight: bold;";
    }

    public String requiredLabelStyle() {
        return """
                -fx-text-fill: #ff6b6b;
                -fx-font-size: 10px;
                -fx-font-weight: bold;
                """;
    }
}












