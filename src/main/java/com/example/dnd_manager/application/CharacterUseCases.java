package com.example.dnd_manager.application;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.application.usecase.character.DeleteCharacterUseCase;
import com.example.dnd_manager.application.usecase.character.LevelUpCharacterUseCase;
import com.example.dnd_manager.application.usecase.character.ListCharacterNamesUseCase;
import com.example.dnd_manager.application.usecase.character.LoadCharacterUseCase;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.application.usecase.character.UpdateCharacterStatsUseCase;

import java.util.Objects;

/**
 * Aggregates character-related application use cases.
 */
public class CharacterUseCases {

    private final SaveCharacterUseCase saveCharacterUseCase;
    private final LoadCharacterUseCase loadCharacterUseCase;
    private final ListCharacterNamesUseCase listCharacterNamesUseCase;
    private final DeleteCharacterUseCase deleteCharacterUseCase;
    private final UpdateCharacterStatsUseCase updateCharacterStatsUseCase;
    private final LevelUpCharacterUseCase levelUpCharacterUseCase;

    public CharacterUseCases(CharacterGateway characterGateway) {
        Objects.requireNonNull(characterGateway, "characterGateway must not be null");
        this.saveCharacterUseCase = new SaveCharacterUseCase(characterGateway);
        this.loadCharacterUseCase = new LoadCharacterUseCase(characterGateway);
        this.listCharacterNamesUseCase = new ListCharacterNamesUseCase(characterGateway);
        this.deleteCharacterUseCase = new DeleteCharacterUseCase(characterGateway);
        this.updateCharacterStatsUseCase = new UpdateCharacterStatsUseCase(this.saveCharacterUseCase);
        this.levelUpCharacterUseCase = new LevelUpCharacterUseCase(this.saveCharacterUseCase);
    }

    public SaveCharacterUseCase saveCharacterUseCase() {
        return saveCharacterUseCase;
    }

    public LoadCharacterUseCase loadCharacterUseCase() {
        return loadCharacterUseCase;
    }

    public ListCharacterNamesUseCase listCharacterNamesUseCase() {
        return listCharacterNamesUseCase;
    }

    public DeleteCharacterUseCase deleteCharacterUseCase() {
        return deleteCharacterUseCase;
    }

    public UpdateCharacterStatsUseCase updateCharacterStatsUseCase() {
        return updateCharacterStatsUseCase;
    }

    public LevelUpCharacterUseCase levelUpCharacterUseCase() {
        return levelUpCharacterUseCase;
    }
}
