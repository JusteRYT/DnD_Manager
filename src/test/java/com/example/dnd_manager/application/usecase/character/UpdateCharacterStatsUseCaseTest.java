package com.example.dnd_manager.application.usecase.character;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateCharacterStatsUseCaseTest {

    @Test
    void execute_updatesProvidedStatsAndSavesCharacter() {
        FakeCharacterGateway gateway = new FakeCharacterGateway();
        SaveCharacterUseCase saveUseCase = new SaveCharacterUseCase(gateway);
        UpdateCharacterStatsUseCase useCase = new UpdateCharacterStatsUseCase(saveUseCase);

        Character character = new Character();
        character.setMaxHp(10);
        character.setArmor(11);
        character.setMaxMana(12);
        character.setLevel(2);

        useCase.execute(character, 20, null, 30, 5);

        assertEquals(20, character.getMaxHp());
        assertEquals(11, character.getArmor());
        assertEquals(30, character.getMaxMana());
        assertEquals(5, character.getLevel());
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
