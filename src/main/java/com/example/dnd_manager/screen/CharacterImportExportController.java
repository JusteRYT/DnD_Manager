package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.usecase.character.ListCharacterNamesUseCase;
import com.example.dnd_manager.application.usecase.character.LoadCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.service.CharacterTransferService;
import com.example.dnd_manager.store.StorageService;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for import/export screen flow and operations.
 */
public class CharacterImportExportController {

    private static final Logger log = LoggerFactory.getLogger(CharacterImportExportController.class);

    private final Stage stage;
    private final StorageService storageService;
    private final CharacterTransferService transferService;
    private final ScreenNavigator screenNavigator;
    private final ListCharacterNamesUseCase listCharacterNamesUseCase;
    private final LoadCharacterUseCase loadCharacterUseCase;

    public CharacterImportExportController(
            Stage stage,
            StorageService storageService,
            CharacterTransferService transferService,
            ScreenNavigator screenNavigator
    ) {
        this.stage = stage;
        this.storageService = storageService;
        this.transferService = transferService;
        this.screenNavigator = screenNavigator;
        this.listCharacterNamesUseCase = new ListCharacterNamesUseCase(storageService);
        this.loadCharacterUseCase = new LoadCharacterUseCase(storageService);
    }

    public List<Character> loadCharacters() {
        List<Character> characters = new ArrayList<>();
        for (String name : listCharacterNamesUseCase.execute()) {
            loadCharacterUseCase.execute(name).ifPresent(characters::add);
        }
        return characters;
    }

    public void exportCharacter(String characterName, File targetZip) {
        try {
            transferService.exportCharacter(characterName, targetZip);
        } catch (IOException ex) {
            log.error("Failed to export character {} to {}", characterName, targetZip, ex);
        }
    }

    public boolean importCharacter(File sourceZip) {
        try {
            transferService.importCharacter(sourceZip);
            return true;
        } catch (IOException ex) {
            log.error("Failed to import character archive {}", sourceZip, ex);
            return false;
        }
    }

    public void goBack() {
        screenNavigator.open(new StartScreen(stage, storageService).getView());
    }
}

