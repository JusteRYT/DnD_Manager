package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import javafx.stage.Stage;

import java.util.Objects;

public class FamiliarsPanelController {

    private final Stage parentStage;
    private final Character ownerCharacter;
    private final SaveCharacterUseCase saveCharacterUseCase;
    private final FamiliarInfoDialogLauncher dialogLauncher;

    public FamiliarsPanelController(
            Stage parentStage,
            Character ownerCharacter,
            SaveCharacterUseCase saveCharacterUseCase
    ) {
        this(parentStage, ownerCharacter, saveCharacterUseCase, new JavaFxFamiliarInfoDialogLauncher());
    }

    FamiliarsPanelController(
            Stage parentStage,
            Character ownerCharacter,
            SaveCharacterUseCase saveCharacterUseCase,
            FamiliarInfoDialogLauncher dialogLauncher
    ) {
        this.parentStage = parentStage;
        this.ownerCharacter = Objects.requireNonNull(ownerCharacter, "ownerCharacter must not be null");
        this.saveCharacterUseCase = Objects.requireNonNull(saveCharacterUseCase, "saveCharacterUseCase must not be null");
        this.dialogLauncher = Objects.requireNonNull(dialogLauncher, "dialogLauncher must not be null");
    }

    public void openFamiliar(Character familiar, Runnable onAnyUpdate) {
        dialogLauncher.show(parentStage, familiar, ownerCharacter, saveCharacterUseCase, onAnyUpdate);
    }
}












