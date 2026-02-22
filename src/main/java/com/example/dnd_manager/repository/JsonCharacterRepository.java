package com.example.dnd_manager.repository;


import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
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
    private static final Logger log = LoggerFactory.getLogger(JsonCharacterRepository.class);

    public JsonCharacterRepository() {
        CharacterStoragePathResolver.migrateIfNeeded();
        try {
            CharacterStoragePathResolver.ensureRootExists();
        } catch (IOException e) {
            log.error("Could not initialize storage", e);
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    private Path getRoot() {
        return CharacterStoragePathResolver.getRoot();
    }

    @Override
    public void save(Character character) {
        log.info("Saving character: {}", character.getName());
        validate(character);
        try {
            Path characterDir = CharacterStoragePathResolver.getCharacterDir(character.getName());
            Path iconDir = characterDir.resolve(ICON_DIR);

            Files.createDirectories(iconDir);
            copyIcons(character, iconDir);

            Path jsonFile = characterDir.resolve(character.getName() + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile.toFile(), character);
            log.debug("Character JSON saved to: {}", jsonFile);
        } catch (Exception e) {
            log.error("Failed to save character: {}", character.getName(), e);
            throw new RuntimeException("Failed to save character", e);
        }
    }

    @Override
    public Optional<Character> load(String name) {
        log.debug("Loading character: {}", name);
        try {
            Path jsonFile = CharacterStoragePathResolver.getCharacterDir(name).resolve(name + ".json");
            if (!Files.exists(jsonFile)) {
                log.warn("Character file not found: {}", jsonFile);
                return Optional.empty();
            }
            return Optional.of(mapper.readValue(jsonFile.toFile(), Character.class));
        } catch (Exception e) {
            log.error("Failed to load character: {}", name, e);
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
            log.error("Failed to list characters", e);
            throw new RuntimeException("Failed to list characters", e);
        }
    }

    @Override
    public void delete(String name) {
        log.info("Deleting character: {}", name);
        Path characterDir = CharacterStoragePathResolver.getCharacterDir(name);
        if (!Files.exists(characterDir)) return;

        try {
            Files.walk(characterDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            log.error("Failed to delete path: {}", path, e);
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            log.error("Failed to delete character directory: {}", name, e);
            throw new RuntimeException("Failed to delete character " + name, e);
        }
    }

    /**
     * Copies all icon files used by character into icon directory
     * and rewrites paths to relative ones.
     */
    private void copyIcon(String sourcePath, Path iconDir, Consumer<String> pathSetter) throws IOException {
        if (sourcePath == null || sourcePath.isBlank()) {
            return;
        }

        // 1. Извлекаем чистое имя файла
        String fileName = extractFileName(sourcePath);
        Path target = iconDir.resolve(fileName);

        // 2. Улучшенная проверка: если файл уже в целевой папке (локально или через URL)
        // Проверяем, содержит ли путь "/icon/имя_файла" в конце
        String internalPathMarker = ICON_DIR + "/" + fileName;
        if (sourcePath.endsWith(internalPathMarker)) {
            log.trace("Icon {} is already in storage, skipping", fileName);
            pathSetter.accept(internalPathMarker);
            return;
        }

        // 3. Физическая проверка на тот же файл (на случай разных стилей написания путей)
        try {
            Path sourceFile;
            if (sourcePath.startsWith("file:")) {
                sourceFile = Path.of(java.net.URI.create(sourcePath));
            } else {
                sourceFile = Path.of(sourcePath);
            }

            if (Files.exists(sourceFile) && Files.exists(target) && Files.isSameFile(sourceFile, target)) {
                pathSetter.accept(internalPathMarker);
                return;
            }
        } catch (Exception ignored) {
            // Если это jar: или сложный URL, просто идем дальше
        }

        // 4. Копируем только реально новые файлы
        log.debug("Copying new icon from {} to {}", sourcePath, target);
        try (InputStream in = openStream(sourcePath)) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Error copying icon: {}", sourcePath, e);
            throw e;
        }

        pathSetter.accept(internalPathMarker);
    }

    public String extractFileName(String sourcePath) {
        String name = sourcePath;
        if (sourcePath.contains("/") || sourcePath.contains("\\")) {
            String[] parts = sourcePath.split("[/\\\\]");
            name = parts[parts.length - 1];
        }
        if (name.contains("!")) {
            name = name.substring(name.lastIndexOf("!") + 1);
        }
        return name;
    }

    private InputStream openStream(String sourcePath) throws IOException {
        if (sourcePath.startsWith("file:") || sourcePath.startsWith("jar:")) {
            return new URL(sourcePath).openStream();
        } else {
            Path path = Path.of(sourcePath);
            if (Files.exists(path)) {
                return Files.newInputStream(path);
            } else {
                InputStream is = getClass().getResourceAsStream(sourcePath.startsWith("/") ? sourcePath : "/" + sourcePath);
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
            int index = i;
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

