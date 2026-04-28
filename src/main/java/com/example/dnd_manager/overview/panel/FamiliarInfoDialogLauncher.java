package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import javafx.stage.Stage;

public interface FamiliarInfoDialogLauncher {

    void show(
            Stage parentStage,
            Character familiar,
            Character owner,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable onAnyUpdate
    );
}












