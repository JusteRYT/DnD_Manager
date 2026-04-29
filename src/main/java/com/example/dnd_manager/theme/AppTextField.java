package com.example.dnd_manager.theme;

import javafx.scene.control.TextField;
import lombok.Getter;

@Getter
public class AppTextField {

    private final TextField field;
    private static final String BASE_STYLE = """
                -fx-background-color: rgba(16, 23, 42, 0.94);
                -fx-text-fill: #f0f2f7;
                -fx-prompt-text-fill: #8fa4bd;
                -fx-border-color: rgba(75, 93, 127, 0.42);
                -fx-border-radius: 6;
                -fx-background-radius: 6;
                -fx-padding: 10 14 10 14;
                -fx-font-size: 13px;
                -fx-focus-color: transparent;
                -fx-faint-focus-color: transparent;
            """;
    private static final String FOCUS_STYLE = BASE_STYLE + """
                -fx-border-color: rgba(175, 196, 216, 0.72);
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.18), 12, 0.24, 0, 0);
            """;

    public AppTextField(String text, boolean isPrompt) {
        field = new TextField();
        if (isPrompt) {
            field.setPromptText(text);
        } else {
            field.setText(text);
        }

        styleField(field, text);


    }

    private void styleField(TextField field, String prompt) {
        field.setPromptText(prompt);
        field.setStyle(BASE_STYLE);

        field.focusedProperty().addListener((obs, old, newVal) -> field.setStyle(newVal ? FOCUS_STYLE : BASE_STYLE));
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












