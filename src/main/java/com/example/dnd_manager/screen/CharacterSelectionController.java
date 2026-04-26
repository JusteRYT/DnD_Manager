package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.usecase.character.DeleteCharacterUseCase;
import com.example.dnd_manager.application.usecase.character.ListCharacterNamesUseCase;
import com.example.dnd_manager.application.usecase.character.LoadCharacterUseCase;
import com.example.dnd_manager.domain.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller for character selection screen operations.
 */
public class CharacterSelectionController {

    private final ListCharacterNamesUseCase listCharacterNamesUseCase;
    private final LoadCharacterUseCase loadCharacterUseCase;
    private final DeleteCharacterUseCase deleteCharacterUseCase;
    private final Runnable backAction;

    public CharacterSelectionController(
            ListCharacterNamesUseCase listCharacterNamesUseCase,
            LoadCharacterUseCase loadCharacterUseCase,
            DeleteCharacterUseCase deleteCharacterUseCase,
            Runnable backAction
    ) {
        this.listCharacterNamesUseCase = Objects.requireNonNull(
                listCharacterNamesUseCase, "listCharacterNamesUseCase must not be null"
        );
        this.loadCharacterUseCase = Objects.requireNonNull(loadCharacterUseCase, "loadCharacterUseCase must not be null");
        this.deleteCharacterUseCase = Objects.requireNonNull(
                deleteCharacterUseCase, "deleteCharacterUseCase must not be null"
        );
        this.backAction = Objects.requireNonNull(backAction, "backAction must not be null");
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
        backAction.run();
    }
}
