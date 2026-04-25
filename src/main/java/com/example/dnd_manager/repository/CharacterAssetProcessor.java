package com.example.dnd_manager.repository;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handles icon copying and path normalization for character assets.
 */
class CharacterAssetProcessor {

    private static final Logger log = LoggerFactory.getLogger(CharacterAssetProcessor.class);
    private static final String ICON_DIR = "icon";

    void copyIcons(Character character, Path iconDir) throws IOException {
        processCharacterAssets(character, iconDir);

        if (character.getFamiliars() != null) {
            for (Character familiar : character.getFamiliars()) {
                processCharacterAssets(familiar, iconDir);
            }
        }
    }

    String extractFileName(String sourcePath) {
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

    private void copyIcon(String sourcePath, Path iconDir, Consumer<String> pathSetter) throws IOException {
        if (sourcePath == null || sourcePath.isBlank()) {
            return;
        }

        String fileName = extractFileName(sourcePath);
        Path target = iconDir.resolve(fileName);

        String internalPathMarker = ICON_DIR + "/" + fileName;
        if (sourcePath.endsWith(internalPathMarker)) {
            pathSetter.accept(internalPathMarker);
            return;
        }

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
            // Skip physical path comparison for unsupported URI formats.
        }

        log.debug("Copying new icon from {} to {}", sourcePath, target);
        try (InputStream in = openStream(sourcePath)) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        pathSetter.accept(internalPathMarker);
    }

    private InputStream openStream(String sourcePath) throws IOException {
        if (sourcePath.startsWith("file:") || sourcePath.startsWith("jar:")) {
            return new URL(sourcePath).openStream();
        }

        Path path = Path.of(sourcePath);
        if (Files.exists(path)) {
            return Files.newInputStream(path);
        }

        InputStream is = getClass().getResourceAsStream(sourcePath.startsWith("/") ? sourcePath : "/" + sourcePath);
        if (is == null) {
            throw new IOException("Resource not found: " + sourcePath);
        }
        return is;
    }

    private void processCharacterAssets(Character character, Path iconDir) throws IOException {
        copyIcon(character.getAvatarImage(), iconDir, character::setAvatarImage);

        List<Skill> skills = character.getSkills();
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

        List<Buff> buffs = character.getBuffs();
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

        for (InventoryItem item : character.getInventory()) {
            copyIcon(item.getIconPath(), iconDir, item::setIconPath);
        }
    }
}

