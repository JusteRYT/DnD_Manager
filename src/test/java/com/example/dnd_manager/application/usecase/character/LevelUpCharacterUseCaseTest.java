package com.example.dnd_manager.application.usecase.character;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevelUpCharacterUseCaseTest {

    @Test
    void execute_incrementsLevelAndSavesCharacter() {
        FakeCharacterGateway gateway = new FakeCharacterGateway();
        SaveCharacterUseCase saveUseCase = new SaveCharacterUseCase(gateway);
        LevelUpCharacterUseCase useCase = new LevelUpCharacterUseCase(saveUseCase);

        Character character = new Character();
        character.setLevel(3);

        useCase.execute(character);

        assertEquals(4, character.getLevel());
        assertEquals(1, gateway.saveCalls);
    }

    private static final class FakeCharacterGateway implements CharacterGateway {
        private int saveCalls;

        @Override
        public void saveCharacter(Character character) {
            saveCalls++;
        }

        @Override
        public Optional<Character> loadCharacter(String name) {
            return Optional.empty();
        }

        @Override
        public List<String> listCharacterNames() {
            return List.of();
        }

        @Override
        public void deleteCharacter(Character character) {
        }
    }
}
