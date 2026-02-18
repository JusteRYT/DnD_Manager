package com.example.dnd_manager.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class CharacterStoragePathResolver {

    private static final String APP_FOLDER_NAME = "DnD_Manager";
    private static final String ROOT_DIR_NAME = "Characters";
    private static final String LEGACY_DIR_NAME = "Character"; // Старое имя папки

    private CharacterStoragePathResolver() {}

    public static Path getRoot() {
        String os = System.getProperty("os.name").toLowerCase();
        Path basePath;

        if (os.contains("win")) {
            basePath = Paths.get(System.getenv("APPDATA"), APP_FOLDER_NAME);
        } else {
            basePath = Paths.get(System.getProperty("user.home"), "." + APP_FOLDER_NAME.toLowerCase());
        }
        return basePath.resolve(ROOT_DIR_NAME).toAbsolutePath();
    }

    public static Path getCharacterDir(String characterName) {
        return getRoot().resolve(characterName);
    }

    /**
     * Проверяет наличие старой папки и переносит данные в новую локацию.
     */
    public static void migrateIfNeeded() {
        Path legacyPath = Paths.get(LEGACY_DIR_NAME).toAbsolutePath();
        Path newPath = getRoot();

        if (Files.exists(legacyPath) && Files.isDirectory(legacyPath)) {
            try {
                // Создаем родительскую папку в AppData
                Files.createDirectories(newPath);

                // Переносим все содержимое из старой папки в новую
                try (var stream = Files.list(legacyPath)) {
                    for (Path source : stream.toList()) {
                        Path target = newPath.resolve(source.getFileName());
                        // Если персонаж с таким именем уже есть, заменяем (обновляем)
                        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                }

                // Удаляем пустую старую папку
                Files.deleteIfExists(legacyPath);
                System.out.println("Migration successful: moved characters to " + newPath);

            } catch (IOException e) {
                System.err.println("Migration failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void ensureRootExists() throws IOException {
        Files.createDirectories(getRoot());
    }
}