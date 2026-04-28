package com.example.dnd_manager.overview.ui.topbar;

public class TopBarActionsStyleProvider {

    public String actionsRowStyle() {
        return """
                -fx-background-color: linear-gradient(to bottom, #2d2d2d, #1a1a1a);
                -fx-background-radius: 12;
                -fx-border-color: rgba(200, 155, 60, 0.3);
                -fx-border-radius: 12;
                -fx-border-width: 1.5;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 15, 0, 0, 5);
                """;
    }
}













