package com.example.dnd_manager.repository;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.skills.ActivationType;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.skills.SkillEffect;
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

    // Поля для сохранения оригинального состояния системы
    private String originalUserDir;
    private String originalUserHome;
    private String originalOsName;

    @BeforeEach
    void setUp() {
        // 1. Сохраняем оригинальные настройки
        originalUserDir = System.getProperty("user.dir");
        originalUserHome = System.getProperty("user.home");
        originalOsName = System.getProperty("os.name");

        // 2. Изолируем тест во временной папке
        String tempPath = tempDir.toAbsolutePath().toString();
        System.setProperty("user.dir", tempPath);
        System.setProperty("user.home", tempPath);
        System.setProperty("os.name", "Linux"); // Игнорируем Windows AppData

        log.info("--- Sandbox initialized: {} ---", tempPath);

        // Теперь инициализация репозитория пройдет внутри tempDir
        repository = new JsonCharacterRepository();
    }

    @AfterEach
    void tearDown() {
        // 3. Возвращаем настройки системы в исходное состояние
        System.setProperty("user.dir", originalUserDir);
        System.setProperty("user.home", originalUserHome);
        System.setProperty("os.name", originalOsName);
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
        Path expectedPath = CharacterStoragePathResolver.getCharacterDir("Frodo")
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

        assertThat(character.getSkills().get(0).iconPath()).isEqualTo("icon/fireball.png");
    }

    @Test
    @DisplayName("Delete: Полное удаление папки персонажа")
    void deleteShouldRemoveDirectory() {
        Character character = new Character();
        character.setName("Boromir");
        repository.save(character);

        Path charDir = CharacterStoragePathResolver.getCharacterDir("Boromir");
        assertThat(charDir).exists();

        repository.delete("Boromir");

        assertThat(charDir).doesNotExist();
        log.info("Verified deletion for: Boromir");
    }
}