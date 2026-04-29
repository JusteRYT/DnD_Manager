package com.example.dnd_manager.assets.ui;

import com.example.dnd_manager.theme.AppTheme;

public class AssetCardStyleProvider {

    public String cardStyle(boolean selected) {
        String backgroundColor = selected ? "#182238" : AppTheme.BACKGROUND_SECONDARY;
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
        return "-fx-text-fill: #e9edf3; -fx-font-size: 11px;";
    }
}












