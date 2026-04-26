package com.example.dnd_manager.service;

import com.example.dnd_manager.application.CharacterUseCases;
import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.repository.CharacterPathProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterImageIntegrityServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void validateAndRepairAllCharacters_repairsInvalidIconPathsAndCreatesDefaults() {
        FakeCharacterGateway gateway = new FakeCharacterGateway();
        CharacterUseCases useCases = new CharacterUseCases(gateway);
        CharacterPathProvider pathProvider = new FakePathProvider(tempDir.resolve("Characters"));
        CharacterImageIntegrityService service = new CharacterImageIntegrityService(useCases, pathProvider);

        Character character = new Character();
        character.setName("Hero");
        character.getInventory().add(new InventoryItem("Sword", "desc", ""));
        character.getBuffs().add(new Buff("Buff", "desc", "BUFF", ""));
        character.getSkills().add(new Skill("Skill", "desc", new ArrayList<>(), "ACTION", ""));
        gateway.characters.put("Hero", character);
        gateway.names.add("Hero");

        service.validateAndRepairAllCharacters();

        assertEquals("icon/no_image.png", character.getInventory().get(0).getIconPath());
        assertEquals("icon/no_image.png", character.getBuffs().get(0).iconPath());
        assertEquals("icon/no_image.png", character.getSkills().get(0).iconPath());
        assertEquals(1, gateway.saveCalls);

        Path iconDir = tempDir.resolve("Characters").resolve("Hero").resolve("icon");
        assertTrue(Files.exists(iconDir.resolve("no_image.png")));
        assertTrue(Files.exists(iconDir.resolve("user.png")));
    }

    private static final class FakeCharacterGateway implements CharacterGateway {
        private final List<String> names = new ArrayList<>();
        private final Map<String, Character> characters = new java.util.HashMap<>();
        private int saveCalls;

        @Override
        public void saveCharacter(Character character) {
            saveCalls++;
            characters.put(character.getName(), character);
            if (!names.contains(character.getName())) {
                names.add(character.getName());
            }
        }

        @Override
        public Optional<Character> loadCharacter(String name) {
            return Optional.ofNullable(characters.get(name));
        }

        @Override
        public List<String> listCharacterNames() {
            return new ArrayList<>(names);
        }

        @Override
        public void deleteCharacter(Character character) {
            names.remove(character.getName());
            characters.remove(character.getName());
        }
    }

    private static final class FakePathProvider implements CharacterPathProvider {
        private final Path root;

        private FakePathProvider(Path root) {
            this.root = root;
        }

        @Override
        public Path getRoot() {
            return root;
        }

        @Override
        public Path getCharacterDir(String characterName) {
            return root.resolve(characterName);
        }

        @Override
        public void ensureRootExists() throws java.io.IOException {
            Files.createDirectories(root);
        }

        @Override
        public void migrateIfNeeded() {
        }
    }
}
