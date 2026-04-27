package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.dialogs.LevelUpDialog;
import javafx.stage.Stage;

public class JavaFxLevelUpDialogLauncher implements LevelUpDialogLauncher {

    @Override
    public void show(Stage owner, Character character, SaveCharacterUseCase saveCharacterUseCase, Runnable onLevelUpdated) {
        new LevelUpDialog(owner, character, saveCharacterUseCase, onLevelUpdated).show();
    }
}

