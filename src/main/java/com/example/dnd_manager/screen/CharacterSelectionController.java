package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.usecase.character.DeleteCharacterUseCase;
import com.example.dnd_manager.application.usecase.character.ListCharacterNamesUseCase;
import com.example.dnd_manager.application.usecase.character.LoadCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.store.StorageService;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for character selection screen operations.
 */
public class CharacterSelectionController {

    private final Stage stage;
    private final StorageService storageService;
    private final ScreenNavigator screenNavigator;
    private final ListCharacterNamesUseCase listCharacterNamesUseCase;
    private final LoadCharacterUseCase loadCharacterUseCase;
    private final DeleteCharacterUseCase deleteCharacterUseCase;

    public CharacterSelectionController(Stage stage, StorageService storageService, ScreenNavigator screenNavigator) {
        this.stage = stage;
        this.storageService = storageService;
        this.screenNavigator = screenNavigator;
        this.listCharacterNamesUseCase = new ListCharacterNamesUseCase(storageService);
        this.loadCharacterUseCase = new LoadCharacterUseCase(storageService);
        this.deleteCharacterUseCase = new DeleteCharacterUseCase(storageService);
    }

    public List<Character> loadCharacters() {
        List<Character> characters = new ArrayList<>();
        for (String name : listCharacterNamesUseCase.execute()) {
            loadCharacterUseCase.execute(name).ifPresent(character -> {
                character.markSaved();
                characters.add(character);
            });
        }
        return characters;
    }

    public void deleteCharacter(Character character) {
        deleteCharacterUseCase.execute(character);
    }

    public void goBack() {
        StartScreen startScreen = new StartScreen(stage, storageService);
        screenNavigator.open(startScreen.getView());
    }
}

