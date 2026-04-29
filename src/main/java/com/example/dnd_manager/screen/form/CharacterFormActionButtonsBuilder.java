package com.example.dnd_manager.screen.form;

import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class CharacterFormActionButtonsBuilder {

    private final CharacterFormStyleProvider styleProvider = new CharacterFormStyleProvider();

    public HBox build(String saveButtonLabel, Runnable saveAction, Runnable exitAction) {
        Button saveButton = new Button(saveButtonLabel);
        saveButton.setPrefSize(230, 42);
        saveButton.setStyle(styleProvider.saveButtonStyle(false));
        saveButton.setOnMouseEntered(e -> saveButton.setStyle(styleProvider.saveButtonStyle(true)));
        saveButton.setOnMouseExited(e -> saveButton.setStyle(styleProvider.saveButtonStyle(false)));
        saveButton.setOnAction(event -> saveAction.run());

        Button exitButton = new Button(I18n.t("button.exit"));
        exitButton.setPrefSize(130, 42);
        exitButton.setStyle(styleProvider.exitButtonStyle(false));
        exitButton.setOnMouseEntered(e -> exitButton.setStyle(styleProvider.exitButtonStyle(true)));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(styleProvider.exitButtonStyle(false)));
        exitButton.setOnAction(event -> exitAction.run());

        HBox buttonBox = new HBox(20, exitButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        return buttonBox;
    }
}












