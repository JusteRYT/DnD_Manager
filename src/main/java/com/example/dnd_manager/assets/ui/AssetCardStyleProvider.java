package com.example.dnd_manager.assets.ui;

import com.example.dnd_manager.theme.AppTheme;

public class AssetCardStyleProvider {

    public String cardStyle(boolean selected) {
        String backgroundColor = selected ? "#404040" : "#2b2b2b";
        String borderColor = selected ? AppTheme.TEXT_ACCENT : "transparent";
        return """
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                -fx-border-width: 2;
                -fx-background-color: %s;
                -fx-border-color: %s;
                """.formatted(backgroundColor, borderColor);
    }

    public String fileNameLabelStyle() {
        return "-fx-text-fill: white; -fx-font-size: 11px;";
    }
}












