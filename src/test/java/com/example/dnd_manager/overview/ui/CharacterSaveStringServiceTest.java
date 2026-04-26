package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterSaveStringServiceTest {

    @Test
    void persist_savesWhenValueChangedAfterTrim() {
        FakeGateway gateway = new FakeGateway();
        SaveCharacterUseCase saveCharacterUseCase = new SaveCharacterUseCase(gateway);
        CharacterSaveStringService service = new CharacterSaveStringService(saveCharacterUseCase);
        Character character = new Character();
        character.setSaveString("old");

        boolean changed = service.persist(character, "  new value  ");

        assertTrue(changed);
        assertEquals("new value", character.getSaveString());
        assertEquals(1, gateway.saveCalls);
    }

    @Test
    void persist_doesNotSaveWhenValueUnchangedAfterTrim() {
        FakeGateway gateway = new FakeGateway();
        SaveCharacterUseCase saveCharacterUseCase = new SaveCharacterUseCase(gateway);
        CharacterSaveStringService service = new CharacterSaveStringService(saveCharacterUseCase);
        Character character = new Character();
        character.setSaveString("value");

        boolean changed = service.persist(character, "  value ");

        assertFalse(changed);
        assertEquals("value", character.getSaveString());
        assertEquals(0, gateway.saveCalls);
    }

    private static final class FakeGateway implements CharacterGateway {
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
