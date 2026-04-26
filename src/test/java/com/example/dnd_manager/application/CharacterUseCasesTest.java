package com.example.dnd_manager.application;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterUseCasesTest {

    @Test
    void saveCharacterUseCase_delegatesToGateway() {
        FakeCharacterGateway gateway = new FakeCharacterGateway();
        CharacterUseCases useCases = new CharacterUseCases(gateway);
        Character character = new Character();
        character.setName("Rogue");

        useCases.saveCharacterUseCase().execute(character);

        assertEquals(1, gateway.savedCount);
        assertEquals("Rogue", gateway.lastSavedName);
    }

    @Test
    void loadCharacterUseCase_returnsGatewayResult() {
        FakeCharacterGateway gateway = new FakeCharacterGateway();
        CharacterUseCases useCases = new CharacterUseCases(gateway);
        Character character = new Character();
        character.setName("Wizard");
        gateway.saved.add(character);

        Optional<Character> loaded = useCases.loadCharacterUseCase().execute("Wizard");

        assertTrue(loaded.isPresent());
        assertEquals("Wizard", loaded.get().getName());
    }

    @Test
    void listCharacterNamesUseCase_returnsGatewayNames() {
        FakeCharacterGateway gateway = new FakeCharacterGateway();
        CharacterUseCases useCases = new CharacterUseCases(gateway);
        gateway.names.add("Cleric");
        gateway.names.add("Bard");

        List<String> names = useCases.listCharacterNamesUseCase().execute();

        assertEquals(List.of("Cleric", "Bard"), names);
    }

    @Test
    void deleteCharacterUseCase_delegatesToGateway() {
        FakeCharacterGateway gateway = new FakeCharacterGateway();
        CharacterUseCases useCases = new CharacterUseCases(gateway);
        Character character = new Character();
        character.setName("Paladin");
        gateway.names.add("Paladin");

        useCases.deleteCharacterUseCase().execute(character);

        assertEquals(1, gateway.deletedCount);
        assertTrue(gateway.names.isEmpty());
    }

    @Test
    void exposesDerivedCharacterUpdateUseCases() {
        FakeCharacterGateway gateway = new FakeCharacterGateway();
        CharacterUseCases useCases = new CharacterUseCases(gateway);

        assertNotNull(useCases.updateCharacterStatsUseCase());
        assertNotNull(useCases.levelUpCharacterUseCase());
    }

    private static final class FakeCharacterGateway implements CharacterGateway {
        private final List<Character> saved = new ArrayList<>();
        private final List<String> names = new ArrayList<>();
        private int savedCount;
        private int deletedCount;
        private String lastSavedName;

        @Override
        public void saveCharacter(Character character) {
            savedCount++;
            lastSavedName = character.getName();
            saved.add(character);
            if (character.getName() != null && !names.contains(character.getName())) {
                names.add(character.getName());
            }
        }

        @Override
        public Optional<Character> loadCharacter(String name) {
            return saved.stream().filter(c -> name.equals(c.getName())).findFirst();
        }

        @Override
        public List<String> listCharacterNames() {
            return new ArrayList<>(names);
        }

        @Override
        public void deleteCharacter(Character character) {
            deletedCount++;
            names.remove(character.getName());
            saved.removeIf(c -> character.getName().equals(c.getName()));
        }
    }
}
