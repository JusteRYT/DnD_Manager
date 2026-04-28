package com.example.dnd_manager.screen.form;

public class CharacterFormStyleProvider {

    public String formStyle() {
        return "-fx-background-color: transparent;";
    }

    public String statsSectionStyle() {
        return """
                -fx-background-color: #252526;
                -fx-background-radius: 0 8 8 0;
                -fx-border-color: transparent transparent transparent #333;
                -fx-border-width: 0 0 0 1;
                """;
    }

    public String statsTitleStyle() {
        return """
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: #FFC107;
                -fx-border-color: transparent transparent #FFC107 transparent;
                -fx-border-width: 0 0 1 0;
                """;
    }

    public String magicalBorderStyle() {
        return """
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1e1e1e);
                -fx-background-radius: 12;
                -fx-border-color: #3a3a3a;
                -fx-border-radius: 12;
                -fx-border-width: 1;
                """;
    }
}












