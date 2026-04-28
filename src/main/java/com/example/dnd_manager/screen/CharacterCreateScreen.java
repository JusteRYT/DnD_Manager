package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CharacterCreateScreen extends AbstractCharacterFormScreen {

    public CharacterCreateScreen(
            Stage stage,
            ScreenNavigator screenNavigator,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction
    ) {
        super(stage, new Character(), FormMode.CREATE, screenNavigator, saveCharacterUseCase, backToStartAction);
    }

    @Override
    protected Label buildTitle() {
        Label title = new Label(I18n.t("label.title.create_character"));
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: 900; -fx-text-fill: #c89b3c;");
        BorderPane.setAlignment(title, Pos.CENTER);
        return title;
    }

    @Override
    protected String getSaveButtonLabel() {
        return I18n.t("button.saveAndView");
    }

    @Override
    protected void handleSave() {
        if (baseInfoForm.validate()) return;

        syncDataToCharacter();
        saveCharacterUseCase.execute(character);

        CharacterOverviewScreen overviewScreen = new CharacterOverviewScreen(
                stage,
                character,
                screenNavigator,
                saveCharacterUseCase,
                backToStartAction
        );
        screenNavigator.open(overviewScreen);
    }

    @Override
    protected void handleExit() {
        backToStartAction.run();
    }
}












