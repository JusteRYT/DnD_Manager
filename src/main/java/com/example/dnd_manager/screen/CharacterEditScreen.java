package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.store.StorageService;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Screen for editing an existing D&D character.
 */
public class CharacterEditScreen extends AbstractCharacterFormScreen {

    public CharacterEditScreen(Stage stage, Character character, StorageService storageService) {
        super(stage, storageService, character, FormMode.EDIT);
    }

    @Override
    protected Label buildTitle() {
        Label title = new Label(I18n.t("title.editScreen"));
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: 900; -fx-text-fill: #c89b3c;");
        BorderPane.setAlignment(title, Pos.CENTER);
        return title;
    }

    @Override
    protected String getSaveButtonLabel() {
        return I18n.t("button.editSave");
    }

    @Override
    protected void handleSave() {
        if (!baseInfoForm.validate()) return;

        syncDataToCharacter();
        storageService.saveCharacter(character);
        ScreenManager.setScreen(stage, new StartScreen(stage, storageService).getView());
    }

    @Override
    protected void handleExit() {
        ScreenManager.setScreen(stage, new StartScreen(stage, storageService).getView());
    }
}