package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import javafx.stage.Stage;
import java.util.Objects;

/**
 * Handles user actions triggered from TopBar.
 */
public class TopBarController {

    private final Character character;
    private final SaveCharacterUseCase saveCharacterUseCase;
    private final Runnable backToStartAction;
    private final CharacterDescriptionFileExporter descriptionFileExporter;
    private final CharacterSaveStringService saveStringService;
    private final DescriptionDialogLauncher descriptionDialogLauncher;
    private final NotesDialogLauncher notesDialogLauncher;
    private final EditStatsDialogLauncher editStatsDialogLauncher;
    private final LevelUpDialogLauncher levelUpDialogLauncher;

    public TopBarController(
            Character character,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction
    ) {
        this(
                character,
                saveCharacterUseCase,
                backToStartAction,
                new CharacterDescriptionFileExporter(),
                new CharacterSaveStringService(saveCharacterUseCase),
                new JavaFxDescriptionDialogLauncher(),
                new JavaFxNotesDialogLauncher(),
                new JavaFxEditStatsDialogLauncher(),
                new JavaFxLevelUpDialogLauncher()
        );
    }

    TopBarController(
            Character character,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction,
            CharacterDescriptionFileExporter descriptionFileExporter,
            CharacterSaveStringService saveStringService,
            DescriptionDialogLauncher descriptionDialogLauncher,
            NotesDialogLauncher notesDialogLauncher,
            EditStatsDialogLauncher editStatsDialogLauncher,
            LevelUpDialogLauncher levelUpDialogLauncher
    ) {
        this.character = Objects.requireNonNull(character, "character must not be null");
        this.saveCharacterUseCase = Objects.requireNonNull(saveCharacterUseCase, "saveCharacterUseCase must not be null");
        this.backToStartAction = Objects.requireNonNull(backToStartAction, "backToStartAction must not be null");
        this.descriptionFileExporter = Objects.requireNonNull(descriptionFileExporter, "descriptionFileExporter must not be null");
        this.saveStringService = Objects.requireNonNull(saveStringService, "saveStringService must not be null");
        this.descriptionDialogLauncher = Objects.requireNonNull(descriptionDialogLauncher, "descriptionDialogLauncher must not be null");
        this.notesDialogLauncher = Objects.requireNonNull(notesDialogLauncher, "notesDialogLauncher must not be null");
        this.editStatsDialogLauncher = Objects.requireNonNull(editStatsDialogLauncher, "editStatsDialogLauncher must not be null");
        this.levelUpDialogLauncher = Objects.requireNonNull(levelUpDialogLauncher, "levelUpDialogLauncher must not be null");
    }

    public void exportDescription(Stage owner) {
        descriptionFileExporter.export(character, owner);
    }

    public void showDescription(Stage owner) {
        descriptionDialogLauncher.show(owner, character);
    }

    public void showNotes(Stage owner) {
        notesDialogLauncher.show(owner, character);
    }

    public void openEditStats(Stage owner, Runnable onUpdated) {
        editStatsDialogLauncher.show(
                owner,
                character,
                saveCharacterUseCase,
                onUpdated
        );
    }

    public void openLevelUp(Stage owner, Runnable onLevelUpdated) {
        levelUpDialogLauncher.show(owner, character, saveCharacterUseCase, onLevelUpdated);
    }

    public void persistSaveString(String text) {
        saveStringService.persist(character, text);
    }

    public void backToStart() {
        backToStartAction.run();
    }
}
