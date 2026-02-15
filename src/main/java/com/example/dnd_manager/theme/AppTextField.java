package com.example.dnd_manager.theme;

import javafx.scene.control.TextField;
import lombok.Getter;

@Getter
public class AppTextField {

    private final TextField field;

    public AppTextField(String promptText) {
        field = new TextField();
        field.setPromptText(promptText);

        styleField(field, promptText);


        field.focusedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                field.setStyle(field.getStyle() + "-fx-border-color: #FFC107; -fx-effect: dropshadow(three-pass-box, rgba(255,193,7,0.1), 10, 0, 0, 0);");
            } else {
                field.setStyle(field.getStyle().replace("-fx-border-color: #FFC107; -fx-effect: dropshadow(three-pass-box, rgba(255,193,7,0.1), 10, 0, 0, 0);", ""));
            }
        });
    }

    private void styleField(TextField field, String prompt) {
        field.setPromptText(prompt);
        field.setStyle("""
                    -fx-background-color: #1e1e1e;
                    -fx-text-fill: #eee;
                    -fx-prompt-text-fill: #444;
                    -fx-border-color: #3a3a3a;
                    -fx-border-radius: 6;
                    -fx-background-radius: 6;
                    -fx-padding: 10 15 10 15; 
                    -fx-font-size: 13px;
                """);

        field.focusedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                field.setStyle(field.getStyle() + "-fx-border-color: #FFC107; -fx-effect: dropshadow(three-pass-box, rgba(255,193,7,0.1), 10, 0, 0, 0);");
            } else {
                field.setStyle(field.getStyle().replace("-fx-border-color: #FFC107; -fx-effect: dropshadow(three-pass-box, rgba(255,193,7,0.1), 10, 0, 0, 0);", ""));
            }
        });
    }

    public void allowOnlyInteger() {
        field.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {

            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }

            return null;
        }));
    }

    public int getInt() {
        if (getText().isEmpty()) {
            return 0;
        }

        return Integer.parseInt(getText());
    }

    public String getText() {
        return field.getText().trim();
    }

    public void setText(String text) {
        field.setText(text);
    }

    public void clear() {
        field.clear();
    }
}
