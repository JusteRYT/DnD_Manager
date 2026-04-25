package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.CharacterNotesDialog;
import com.example.dnd_manager.overview.dialogs.EditStatsDialog;
import com.example.dnd_manager.overview.dialogs.FullDescriptionDialog;
import com.example.dnd_manager.overview.dialogs.LevelUpDialog;
import com.example.dnd_manager.screen.CharacterOverviewScreen;
import com.example.dnd_manager.screen.StartScreen;
import com.example.dnd_manager.service.CharacterExporter;
import com.example.dnd_manager.store.StorageService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Handles user actions triggered from TopBar.
 */
public class TopBarController {

    private static final Logger log = LoggerFactory.getLogger(TopBarController.class);

    private final Character character;
    private final CharacterOverviewScreen parentScreen;
    private final SaveCharacterUseCase saveCharacterUseCase;
    private final StorageService storageService;
    private final ScreenNavigator screenNavigator;

    public TopBarController(
            Character character,
            CharacterOverviewScreen parentScreen,
            StorageService storageService,
            ScreenNavigator screenNavigator
    ) {
        this.character = character;
        this.parentScreen = parentScreen;
        this.saveCharacterUseCase = new SaveCharacterUseCase(storageService);
        this.storageService = storageService;
        this.screenNavigator = screenNavigator;
    }

    public void exportDescription(Stage owner) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить описание персонажа");
        fileChooser.setInitialFileName(character.getName() + "_description.txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showSaveDialog(owner);
        if (file == null) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) {
            writer.print(CharacterExporter.generateFullDescription(character));
        } catch (IOException ex) {
            log.error("Failed to export character description to {}", file, ex);
        }
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
                storageService,
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
        new LevelUpDialog(owner, character, storageService, () ->
                levelValue.setText(String.valueOf(character.getLevel()))).show();
    }

    public void persistSaveString(String text) {
        character.setSaveString(text.trim());
        saveCharacterUseCase.execute(character);
    }

    public void backToStart(Stage stage) {
        screenNavigator.open(new StartScreen(stage, storageService).getView());
    }
}
