package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.dialogs.CharacterNotesDialog;
import com.example.dnd_manager.overview.dialogs.EditStatsDialog;
import com.example.dnd_manager.overview.dialogs.FullDescriptionDialog;
import com.example.dnd_manager.overview.dialogs.LevelUpDialog;
import com.example.dnd_manager.screen.CharacterOverviewScreen;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.Objects;

/**
 * Handles user actions triggered from TopBar.
 */
public class TopBarController {

    private final Character character;
    private final CharacterOverviewScreen parentScreen;
    private final SaveCharacterUseCase saveCharacterUseCase;
    private final Runnable backToStartAction;
    private final CharacterDescriptionFileExporter descriptionFileExporter;
    private final CharacterSaveStringService saveStringService;

    public TopBarController(
            Character character,
            CharacterOverviewScreen parentScreen,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction
    ) {
        this(
                character,
                parentScreen,
                saveCharacterUseCase,
                backToStartAction,
                new CharacterDescriptionFileExporter(),
                new CharacterSaveStringService(saveCharacterUseCase)
        );
    }

    TopBarController(
            Character character,
            CharacterOverviewScreen parentScreen,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction,
            CharacterDescriptionFileExporter descriptionFileExporter,
            CharacterSaveStringService saveStringService
    ) {
        this.character = character;
        this.parentScreen = parentScreen;
        this.saveCharacterUseCase = Objects.requireNonNull(saveCharacterUseCase, "saveCharacterUseCase must not be null");
        this.backToStartAction = Objects.requireNonNull(backToStartAction, "backToStartAction must not be null");
        this.descriptionFileExporter = Objects.requireNonNull(descriptionFileExporter, "descriptionFileExporter must not be null");
        this.saveStringService = Objects.requireNonNull(saveStringService, "saveStringService must not be null");
    }

    public void exportDescription(Stage owner) {
        descriptionFileExporter.export(character, owner);
    }

    public void showDescription(Stage owner) {
        new FullDescriptionDialog(owner, character).show();
    }

    public void showNotes(Stage owner) {
        new CharacterNotesDialog(owner, character).show();
    }

    public void openEditStats(Stage owner, Label hpLabel, Label armorLabel, Label levelValue) {
        EditStatsDialog dialog = new EditStatsDialog(
                owner,
                character,
                saveCharacterUseCase,
                () -> {
                    hpLabel.setText(String.valueOf(character.getCurrentHp()));
                    armorLabel.setText(String.valueOf(character.getArmor()));
                    parentScreen.getManaBar().refresh();
                    levelValue.setText(String.valueOf(character.getLevel()));
                }
        );
        dialog.show();
    }

    public void openLevelUp(Stage owner, Label levelValue) {
        new LevelUpDialog(owner, character, saveCharacterUseCase, () ->
                levelValue.setText(String.valueOf(character.getLevel()))).show();
    }

    public void persistSaveString(String text) {
        saveStringService.persist(character, text);
    }

    public void backToStart() {
        backToStartAction.run();
    }
}
