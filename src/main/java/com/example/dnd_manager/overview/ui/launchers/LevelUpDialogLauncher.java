package com.example.dnd_manager.overview.ui.launchers;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import javafx.stage.Stage;

public interface LevelUpDialogLauncher {

    void show(Stage owner, Character character, SaveCharacterUseCase saveCharacterUseCase, Runnable onLevelUpdated);
}













