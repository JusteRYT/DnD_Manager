package com.example.dnd_manager.screen.update;

import javafx.scene.control.Button;

import java.util.Objects;

public class JavaFxUpdateCheckButtonView implements UpdateCheckButtonView {

    private final Button button;

    public JavaFxUpdateCheckButtonView(Button button) {
        this.button = Objects.requireNonNull(button, "button must not be null");
    }

    @Override
    public void setDisabled(boolean disabled) {
        button.setDisable(disabled);
    }

    @Override
    public void setText(String text) {
        button.setText(text);
    }
}













