package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.dialogs.familiar.FamiliarInfoDialog;
import javafx.stage.Stage;

public class JavaFxFamiliarInfoDialogLauncher implements FamiliarInfoDialogLauncher {

    @Override
    public void show(
            Stage parentStage,
            Character familiar,
            Character owner,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable onAnyUpdate
    ) {
        FamiliarInfoDialog dialog = new FamiliarInfoDialog(parentStage, familiar, owner, saveCharacterUseCase);
        dialog.setOnAnyUpdate(onAnyUpdate);
        dialog.show();
    }
}












