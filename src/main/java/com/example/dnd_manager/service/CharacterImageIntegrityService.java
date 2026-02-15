package com.example.dnd_manager.service;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.store.StorageService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class CharacterImageIntegrityService {

    private final StorageService storageService;

    // Пути внутри ресурсов твоего проекта (JAR)
    private static final String RESOURCE_PATH_NO_IMAGE = "/com/example/dnd_manager/icon/no_image.png";
    private static final String RESOURCE_PATH_USER = "/com/example/dnd_manager/icon/user.png";

    // Путь к папке персонажей (дублируем из репозитория или передаем в конфиге)
    private static final String CHARACTERS_ROOT_DIR = "Character";
    private static final String DEFAULT_ICON_PATH = "icon/no_image.png";

    public CharacterImageIntegrityService(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Основной метод, который запускаем при старте.
     */
    public void validateAndRepairAllCharacters() {
        List<String> characterNames = storageService.listCharacterNames();

        for (String name : characterNames) {
            repairCharacterImages(name);
        }
    }

    private void repairCharacterImages(String charName) {
        try {
            // 1. Убеждаемся, что физические файлы (no_image.png, user.png) существуют в папке
            Path charDir = Paths.get(CHARACTERS_ROOT_DIR, charName);
            Path iconDir = charDir.resolve("icon");

            if (!Files.exists(iconDir)) {
                Files.createDirectories(iconDir);
            }

            ensureFileExists(iconDir.resolve("no_image.png"), RESOURCE_PATH_NO_IMAGE);
            ensureFileExists(iconDir.resolve("user.png"), RESOURCE_PATH_USER);

            // 2. Загружаем персонажа для проверки путей в JSON
            Optional<Character> characterOpt = storageService.loadCharacter(charName);
            if (characterOpt.isEmpty()) return;

            Character character = characterOpt.get();
            boolean needsSave = false;

            // --- Проверка Inventory ---
            if (character.getInventory() != null) {
                for (InventoryItem item : character.getInventory()) {
                    if (isPathInvalid(item.getIconPath())) {
                        item.setIconPath(DEFAULT_ICON_PATH);
                        needsSave = true;
                    }
                }
            }

            // --- Проверка Buffs (предполагаем, что Buff - это Record или иммутабельный класс) ---
            if (character.getBuffs() != null) {
                for (int i = 0; i < character.getBuffs().size(); i++) {
                    Buff buff = character.getBuffs().get(i);
                    if (isPathInvalid(buff.iconPath())) {
                        Buff fixedBuff = new Buff(buff.name(), buff.description(), buff.type(), DEFAULT_ICON_PATH);
                        character.getBuffs().set(i, fixedBuff);
                        needsSave = true;
                    }
                }
            }

            // --- Проверка Skills ---
            if (character.getSkills() != null) {
                for (int i = 0; i < character.getSkills().size(); i++) {
                    Skill skill = character.getSkills().get(i);
                    if (isPathInvalid(skill.iconPath())) {
                        Skill fixedSkill = new Skill(skill.name(), skill.description(), skill.effects(), skill.activationType(), DEFAULT_ICON_PATH);
                        character.getSkills().set(i, fixedSkill);
                        needsSave = true;
                    }
                }
            }

            // Если были изменения в путях, сохраняем JSON обратно
            if (needsSave) {
                storageService.saveCharacter(character);
                System.out.println("Repaired image paths for character: " + charName);
            }

        } catch (Exception e) {
            System.err.println("Failed to validate images for character: " + charName);
            e.printStackTrace();
        }
    }

    /**
     * Проверяет, пустой ли путь или null.
     */
    private boolean isPathInvalid(String path) {
        return path == null || path.isBlank();
    }

    /**
     * Копирует файл из ресурсов JAR в папку, если файла там еще нет.
     */
    private void ensureFileExists(Path targetPath, String resourcePath) {
        if (!Files.exists(targetPath)) {
            try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
                if (in == null) {
                    System.err.println("Resource not found: " + resourcePath);
                    return;
                }
                Files.copy(in, targetPath);
            } catch (IOException e) {
                System.err.println("Could not copy default image to: " + targetPath);
            }
        }
    }
}