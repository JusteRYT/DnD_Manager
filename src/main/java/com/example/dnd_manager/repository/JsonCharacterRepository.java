package com.example.dnd_manager.repository;


import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
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

    private void copyIcons(Character character, Path iconDir) throws IOException {
        // Сначала обрабатываем самого персонажа
        processCharacterAssets(character, iconDir);

        // Затем обрабатываем всех фамильяров (если они есть)
        if (character.getFamiliars() != null) {
            for (Character familiar : character.getFamiliars()) {
                processCharacterAssets(familiar, iconDir);
            }
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
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete character " + name, e);
        }
    }

    /**
     * Copies all icon files used by character into icon directory
     * and rewrites paths to relative ones.
     */
    private void copyIcon(
            String sourcePath,
            Path iconDir,
            Consumer<String> pathSetter
    ) throws IOException {

        if (sourcePath == null || sourcePath.isBlank()) {
            return;
        }

        // Если иконка уже в папке icon/, ничего не делаем
        if (sourcePath.startsWith(ICON_DIR + "/")) {
            return;
        }

        // 1. Извлекаем имя файла (обрабатываем и системные пути, и JAR-пути)
        String fileName;
        if (sourcePath.contains("/") || sourcePath.contains("\\")) {
            String[] parts = sourcePath.split("[/\\\\]");
            fileName = parts[parts.length - 1];
        } else {
            fileName = sourcePath;
        }

        // Убираем лишние символы, если имя файла пришло из JAR URL (например, "user.png")
        if (fileName.contains("!")) {
            fileName = fileName.substring(fileName.lastIndexOf("!") + 1);
        }

        Path target = iconDir.resolve(fileName);

        // 2. Копируем через универсальный поток ввода
        try (java.io.InputStream in = openStream(sourcePath)) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        pathSetter.accept(ICON_DIR + "/" + fileName);
    }

    private java.io.InputStream openStream(String sourcePath) throws IOException {
        if (sourcePath.startsWith("file:") || sourcePath.startsWith("jar:")) {
            // Если это URL (как в вашей ошибке)
            return new URL(sourcePath).openStream();
        } else {
            // Если это обычный путь к файлу на диске
            Path path = Path.of(sourcePath);
            if (Files.exists(path)) {
                return Files.newInputStream(path);
            } else {
                // Пытаемся загрузить как ресурс из classpath, если файл не найден
                java.io.InputStream is = getClass().getResourceAsStream(sourcePath.startsWith("/") ? sourcePath : "/" + sourcePath);
                if (is == null) throw new IOException("Resource not found: " + sourcePath);
                return is;
            }
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

    private void validate(Character character) {
        if (character.getName() == null || character.getName().isBlank()) {
            throw new IllegalArgumentException("Character name must not be empty");
        }
    }
}

