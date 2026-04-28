package com.example.dnd_manager.screen.form;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class CharacterFormActionButtonsBuilder {

    public HBox build(String saveButtonLabel, Runnable saveAction, Runnable exitAction) {
        Button saveButton = AppButtonFactory.actionSave(saveButtonLabel);
        saveButton.setOnAction(event -> saveAction.run());

        Button exitButton = AppButtonFactory.actionExit(I18n.t("button.exit"), 100);
        exitButton.setOnAction(event -> exitAction.run());

        HBox buttonBox = new HBox(20, exitButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        return buttonBox;
    }
}












