package com.example.dnd_manager.repository;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.skills.ActivationType;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.skills.SkillEffect;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JsonCharacterRepositoryTest {

    @TempDir
    Path tempDir;

    private JsonCharacterRepository repository;

    @BeforeEach
    void setUp() {
        System.setProperty("user.home", tempDir.toString());
        repository = new JsonCharacterRepository();

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
    }

    @Test
    @DisplayName("Icons: Путь к иконке должен становиться относительным")
    void iconPathShouldBeConvertedToRelative() throws IOException {
        // Создаем фейковую иконку где-то на диске
        Path externalIcon = tempDir.resolve("external_avatar.png");
        Files.writeString(externalIcon, "fake-image-content");

        Character character = new Character();
        character.setName("Frodo");
        character.setAvatarImage(externalIcon.toString());

        repository.save(character);

        // После сохранения путь должен измениться
        assertThat(character.getAvatarImage()).isEqualTo("icon/external_avatar.png");

        // Проверяем, что файл физически скопирован в папку персонажа
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

        repository.delete("Boromir");

        assertThat(CharacterStoragePathResolver.getCharacterDir("Boromir")).doesNotExist();
    }
}