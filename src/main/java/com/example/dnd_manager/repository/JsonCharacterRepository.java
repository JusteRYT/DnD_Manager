package com.example.dnd_manager.repository;


import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * JSON-based character repository.
 * Stores each character in its own directory with icons.
 */
public class JsonCharacterRepository implements CharacterRepository {

    private static final String ICON_DIR = "icon";
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonCharacterRepository() {
        CharacterStoragePathResolver.migrateIfNeeded();
        try {
            CharacterStoragePathResolver.ensureRootExists();
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    private Path getRoot() {
        return CharacterStoragePathResolver.getRoot();
    }

    @Override
    public void save(Character character) {
        validate(character);
        try {
            Path characterDir = CharacterStoragePathResolver.getCharacterDir(character.getName());
            Path iconDir = characterDir.resolve(ICON_DIR);

            Files.createDirectories(iconDir);
            copyIcons(character, iconDir);

            Path jsonFile = characterDir.resolve(character.getName() + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile.toFile(), character);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save character", e);
        }
    }

    @Override
    public Optional<Character> load(String name) {
        try {
            Path jsonFile = CharacterStoragePathResolver.getCharacterDir(name).resolve(name + ".json");
            if (!Files.exists(jsonFile)) {
                return Optional.empty();
            }
            return Optional.of(mapper.readValue(jsonFile.toFile(), Character.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load character", e);
        }
    }

    @Override
    public List<String> listAll() {
        try {
            if (!Files.exists(getRoot())) return Collections.emptyList();
            return Files.list(getRoot())
                    .filter(Files::isDirectory)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to list characters", e);
        }
    }

    @Override
    public void delete(String name) {
        Path characterDir = CharacterStoragePathResolver.getCharacterDir(name);
        if (!Files.exists(characterDir)) return;

        try {
            Files.walk(characterDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try { Files.delete(path); }
                        catch (IOException e) { throw new RuntimeException(e); }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete character " + name, e);
        }
    }

    /**
     * Copies all icon files used by character into icon directory
     * and rewrites paths to relative ones.
     */
    private void copyIcons(Character character, Path iconDir) throws IOException {
        processCharacterAssets(character, iconDir);

        for (Character familiar : character.getFamiliars()) {
            processCharacterAssets(familiar, iconDir);
        }
    }

    private void processCharacterAssets(Character c, Path iconDir) throws IOException {
        // Аватар
        copyIcon(c.getAvatarImage(), iconDir, c::setAvatarImage);

        // Скиллы
        List<Skill> skills = c.getSkills();
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            int index = i; // final переменная для лямбды
            copyIcon(skill.iconPath(), iconDir, newPath ->
                    skills.set(index, new Skill(
                            skill.name(),
                            skill.description(),
                            skill.effects(),
                            skill.activationType(),
                            newPath
                    ))
            );
        }

        // Баффы
        List<Buff> buffs = c.getBuffs();
        for (int i = 0; i < buffs.size(); i++) {
            Buff buff = buffs.get(i);
            int index = i;
            copyIcon(buff.iconPath(), iconDir, newPath ->
                    buffs.set(index, new Buff(
                            buff.name(),
                            buff.description(),
                            buff.type(),
                            newPath
                    ))
            );
        }

        // Инвентарь
        for (InventoryItem item : c.getInventory()) {
            copyIcon(item.getIconPath(), iconDir, item::setIconPath);
        }
    }

    private void copyIcon(
            String sourcePath,
            Path iconDir,
            Consumer<String> pathSetter
    ) throws IOException {

        if (sourcePath == null || sourcePath.isBlank()) {
            return;
        }

        // already stored icon, do not copy again
        if (sourcePath.startsWith(ICON_DIR + "/")) {
            return;
        }

        Path source;

        if (sourcePath.startsWith("file:")) {
            source = Path.of(java.net.URI.create(sourcePath));
        } else {
            source = Path.of(sourcePath);
        }

        String fileName = source.getFileName().toString();
        Path target = iconDir.resolve(fileName);

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        pathSetter.accept("icon/" + fileName);
    }

    private void validate(Character character) {
        if (character.getName() == null || character.getName().isBlank()) {
            throw new IllegalArgumentException("Character name must not be empty");
        }
    }
}

