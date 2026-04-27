package com.example.dnd_manager.overview.ui;

public class TopBarInfoStyleProvider {

    public String nameStyle() {
        return "-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ffffff;";
    }

    public String levelTextStyle() {
        return "-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 18px;";
    }

    public String levelValueStyle() {
        return "-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 18px;";
    }

    public String levelBoxStyle() {
        return """
                    -fx-background-color: #2b2b2b;
                    -fx-background-radius: 6;
                    -fx-border-color: #1a1a1a;
                    -fx-border-radius: 6;
                    -fx-border-width: 2;
                """;
    }

    public String metaLabelStyle() {
        return """
                    -fx-font-size: 20px;
                    -fx-text-fill: #c89b3c;
                    -fx-background-color: rgba(200, 155, 60, 0.1);
                    -fx-padding: 2 8 2 8;
                    -fx-background-radius: 4;
                """;
    }

    public String hpValueStyle() {
        return "-fx-text-fill: #ff5555; -fx-font-weight: bold; -fx-font-size: 18px;";
    }

    public String armorValueStyle() {
        return "-fx-text-fill: #55aaff; -fx-font-weight: bold; -fx-font-size: 18px;";
    }

    public String leftBoxStyle() {
        return """
                    -fx-background-color: linear-gradient(to right, #252525, #1e1e1e);
                    -fx-background-radius: 12;
                    -fx-border-width: 0 1 0 0;
                """;
    }
}

