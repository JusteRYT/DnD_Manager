package com.example.dnd_manager.theme;

import javafx.scene.control.TextField;
import lombok.Getter;

@Getter
public class AppTextField {

    private final TextField field;

    public AppTextField(String promptText) {
        field = new TextField();
        field.setPromptText(promptText);
        field.setStyle("""
            -fx-background-color: %s;
            -fx-text-fill: %s;
            -fx-prompt-text-fill: #aaaaaa;
            -fx-border-color: %s;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
            -fx-padding: 4 6 4 6;
        """.formatted(AppTheme.BACKGROUND_PRIMARY, AppTheme.TEXT_PRIMARY, AppTheme.BORDER_MUTED));
    }

    public String getText() {
        return field.getText();
    }

    public void setText(String text) {
        field.setText(text);
    }
}
