package com.example.dnd_manager.info.text;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Component for base character information.
 */
public class BaseInfoForm extends VBox {

    private final TextField nameField = new TextField();
    private final TextField raceField = new TextField();
    private final TextField classField = new TextField();

    public BaseInfoForm() {
        setSpacing(10);

        nameField.setPromptText("Name");
        raceField.setPromptText("Race");
        classField.setPromptText("Class");

        getChildren().addAll(nameField, raceField, classField);
    }

    public String getName() {
        return nameField.getText();
    }

    public String getRace() {
        return raceField.getText();
    }

    public String getCharacterClass() {
        return classField.getText();
    }
}
