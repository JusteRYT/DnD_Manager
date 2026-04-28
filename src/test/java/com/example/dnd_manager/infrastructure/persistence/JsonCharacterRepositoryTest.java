package com.example.dnd_manager.infrastructure.persistence;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.infrastructure.assets.CharacterAssetProcessor;
import com.example.dnd_manager.info.skills.model.ActivationType;
import com.example.dnd_manager.info.skills.model.Skill;
import com.example.dnd_manager.info.skills.model.SkillEffect;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JsonCharacterRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(JsonCharacterRepositoryTest.class);

    @TempDir
    Path tempDir;

    private JsonCharacterRepository repository;
    private CharacterPathProvider pathProvider;
    private Path charactersRoot;

    @BeforeEach
    void setUp() {
        charactersRoot = tempDir.resolve("Characters");
        pathProvider = new TestCharacterPathProvider(charactersRoot);
        log.info("--- Sandbox initialized: {} ---", charactersRoot);
        repository = new JsonCharacterRepository(new ObjectMapper(), new CharacterAssetProcessor(), pathProvider);
    }

    @Test
    @DisplayName("Save/Load: Базовый цикл сохранения и загрузки")
    void saveAndLoadShouldWork() {
        Character character = new Character();
        character.setName("Gandalf");
        character.getInventory().addAll(new ArrayList<>());
        character.getSkills().addAll(new ArrayList<>());

        repository.save(character);

        Optional<Character> loaded = repository.load("Gandalf");
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getName()).isEqualTo("Gandalf");
        log.info("Successfully saved and loaded: Gandalf");
    }

    @Test
    @DisplayName("Icons: Путь к иконке должен становиться относительным")
    void iconPathShouldBeConvertedToRelative() throws IOException {
        // Создаем фейковую иконку во временной папке
        Path externalIcon = tempDir.resolve("external_avatar.png");
        Files.writeString(externalIcon, "fake-image-content");

        Character character = new Character();
        character.setName("Frodo");
        character.setAvatarImage(externalIcon.toString());

        repository.save(character);

        // Путь в объекте должен стать относительным
        assertThat(character.getAvatarImage()).isEqualTo("icon/external_avatar.png");

        // Файл должен физически лежать в подпапке персонажа (внутри tempDir)
        Path expectedPath = pathProvider.getCharacterDir("Frodo")
                .resolve("icon/external_avatar.png");
        assertThat(expectedPath).exists();
    }

    @Test
    @DisplayName("Skills: Иконки скиллов также должны обрабатываться")
    void skillIconsShouldBeProcessed() throws IOException {
        Path skillIcon = tempDir.resolve("fireball.png");
        Files.writeString(skillIcon, "boom");

        Character character = new Character();
        character.setName("Mage");
        List<SkillEffect> effects = new ArrayList<>();
        SkillEffect skillEffect = new SkillEffect(ActivationType.ACTION.getName(), "test", "1d4");
        effects.add(skillEffect);

        Skill fireball = new Skill("Fireball", "Desc", effects, "Action", skillIcon.toString());
        character.getSkills().add(fireball);

        repository.save(character);

        assertThat(character.getSkills().getFirst().iconPath()).isEqualTo("icon/fireball.png");
    }

    @Test
    @DisplayName("Delete: Полное удаление папки персонажа")
    void deleteShouldRemoveDirectory() {
        Character character = new Character();
        character.setName("Boromir");
        repository.save(character);

        Path charDir = pathProvider.getCharacterDir("Boromir");
        assertThat(charDir).exists();

        repository.delete("Boromir");

        assertThat(charDir).doesNotExist();
        log.info("Verified deletion for: Boromir");
    }

    private static final class TestCharacterPathProvider implements CharacterPathProvider {
        private final Path root;

        private TestCharacterPathProvider(Path root) {
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
        public void ensureRootExists() throws IOException {
            Files.createDirectories(root);
        }

        @Override
        public void migrateIfNeeded() {
        }
    }
}












