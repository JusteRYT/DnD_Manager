package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.dialogs.EditStatsDialog;
import javafx.stage.Stage;

public class JavaFxEditStatsDialogLauncher implements EditStatsDialogLauncher {

    @Override
    public void show(Stage owner, Character character, SaveCharacterUseCase saveCharacterUseCase, Runnable onUpdated) {
        new EditStatsDialog(owner, character, saveCharacterUseCase, onUpdated).show();
    }
}

