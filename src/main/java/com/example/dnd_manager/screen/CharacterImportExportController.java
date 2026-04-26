package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.usecase.character.ListCharacterNamesUseCase;
import com.example.dnd_manager.application.usecase.character.LoadCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.service.CharacterTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller for import/export screen flow and operations.
 */
public class CharacterImportExportController {

    private static final Logger log = LoggerFactory.getLogger(CharacterImportExportController.class);

    private final CharacterTransferService transferService;
    private final ListCharacterNamesUseCase listCharacterNamesUseCase;
    private final LoadCharacterUseCase loadCharacterUseCase;
    private final Runnable backAction;

    public CharacterImportExportController(
            CharacterTransferService transferService,
            ListCharacterNamesUseCase listCharacterNamesUseCase,
            LoadCharacterUseCase loadCharacterUseCase,
            Runnable backAction
    ) {
        this.transferService = Objects.requireNonNull(transferService, "transferService must not be null");
        this.listCharacterNamesUseCase = Objects.requireNonNull(
                listCharacterNamesUseCase, "listCharacterNamesUseCase must not be null"
        );
        this.loadCharacterUseCase = Objects.requireNonNull(loadCharacterUseCase, "loadCharacterUseCase must not be null");
        this.backAction = Objects.requireNonNull(backAction, "backAction must not be null");
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
        backAction.run();
    }
}
