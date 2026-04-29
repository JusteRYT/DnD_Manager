package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Screen for editing an existing D&D character.
 */
public class CharacterEditScreen extends AbstractCharacterFormScreen {

    public CharacterEditScreen(
            Stage stage,
            Character character,
            ScreenNavigator screenNavigator,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction
    ) {
        super(stage, character, FormMode.EDIT, screenNavigator, saveCharacterUseCase, backToStartAction);
    }

    @Override
    protected Node buildTitle() {
        return buildStyledTitle(
                I18n.t("title.editScreen"),
                I18n.t("characterForm.title.editHint")
        );
    }

    @Override
    protected String getSaveButtonLabel() {
        return I18n.t("button.editSave");
    }

    @Override
    protected void handleSave() {
        if (baseInfoForm.validate()) return;

        syncDataToCharacter();
        saveCharacterUseCase.execute(character);
        backToStartAction.run();
    }

    @Override
    protected void handleExit() {
        backToStartAction.run();
    }
}












