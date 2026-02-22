package com.example.dnd_manager.service;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.repository.CharacterStoragePathResolver;
import com.example.dnd_manager.store.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class CharacterImageIntegrityService {

    private static final Logger log = LoggerFactory.getLogger(CharacterImageIntegrityService.class);
    private final StorageService storageService;

    private static final String RESOURCE_PATH_NO_IMAGE = "/com/example/dnd_manager/icon/no_image.png";
    private static final String RESOURCE_PATH_USER = "/com/example/dnd_manager/icon/user.png";
    private static final String DEFAULT_ICON_PATH = "icon/no_image.png";

    public CharacterImageIntegrityService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void validateAndRepairAllCharacters() {
        log.info("Starting image integrity check for all characters...");
        List<String> characterNames = storageService.listCharacterNames();

        for (String name : characterNames) {
            repairCharacterImages(name);
        }
    }

    private void repairCharacterImages(String charName) {
        try {
            // КЛЮЧЕВОЕ ИСПРАВЛЕНИЕ: Берем путь из Resolver, а не из константы "Character"
            Path charDir = CharacterStoragePathResolver.getCharacterDir(charName);
            Path iconDir = charDir.resolve("icon");

            if (!Files.exists(iconDir)) {
                Files.createDirectories(iconDir);
            }

            // Гарантируем наличие базовых ресурсов в AppData
            ensureFileExists(iconDir.resolve("no_image.png"), RESOURCE_PATH_NO_IMAGE);
            ensureFileExists(iconDir.resolve("user.png"), RESOURCE_PATH_USER);

            Optional<Character> characterOpt = storageService.loadCharacter(charName);
            if (characterOpt.isEmpty()) return;

            Character character = characterOpt.get();
            boolean needsSave = false;

            // Проверка путей (Inventory)
            if (character.getInventory() != null) {
                for (InventoryItem item : character.getInventory()) {
                    if (isPathInvalid(item.getIconPath())) {
                        item.setIconPath(DEFAULT_ICON_PATH);
                        needsSave = true;
                    }
                }
            }

            // Проверка путей (Buffs)
            if (character.getBuffs() != null) {
                for (int i = 0; i < character.getBuffs().size(); i++) {
                    Buff buff = character.getBuffs().get(i);
                    if (isPathInvalid(buff.iconPath())) {
                        character.getBuffs().set(i, new Buff(buff.name(), buff.description(), buff.type(), DEFAULT_ICON_PATH));
                        needsSave = true;
                    }
                }
            }

            // Проверка путей (Skills)
            if (character.getSkills() != null) {
                for (int i = 0; i < character.getSkills().size(); i++) {
                    Skill skill = character.getSkills().get(i);
                    if (isPathInvalid(skill.iconPath())) {
                        character.getSkills().set(i, new Skill(skill.name(), skill.description(), skill.effects(), skill.activationType(), DEFAULT_ICON_PATH));
                        needsSave = true;
                    }
                }
            }

            if (needsSave) {
                storageService.saveCharacter(character);
                log.info("Repaired and migrated image references for: {}", charName);
            }

        } catch (Exception e) {
            log.error("Failed to repair images for character: {}", charName, e);
        }
    }

    private boolean isPathInvalid(String path) {
        return path == null || path.isBlank();
    }

    private void ensureFileExists(Path targetPath, String resourcePath) {
        if (!Files.exists(targetPath)) {
            try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
                if (in != null) {
                    Files.copy(in, targetPath);
                    log.debug("Restored missing resource: {}", targetPath.getFileName());
                }
            } catch (IOException e) {
                log.error("Could not restore resource to {}", targetPath, e);
            }
        }
    }
}