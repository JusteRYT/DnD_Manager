package com.example.dnd_manager.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class CharacterStoragePathResolver {

    private static final String APP_FOLDER_NAME = "DnD_Manager";
    private static final String ROOT_DIR_NAME = "Characters";
    private static final String LEGACY_DIR_NAME = "Character";

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
        // Получаем абсолютный путь к старой папке в корне проекта
        Path legacyPath = Paths.get(LEGACY_DIR_NAME).toAbsolutePath();
        Path newPath = getRoot();

        if (!Files.exists(legacyPath) || !Files.isDirectory(legacyPath)) {
            return;
        }

        try {
            Files.createDirectories(newPath);

            try (var stream = Files.list(legacyPath)) {
                for (Path sourceFolder : stream.toList()) {
                    if (Files.isDirectory(sourceFolder)) {
                        Path targetFolder = newPath.resolve(sourceFolder.getFileName());
                        moveDirectoryRecursive(sourceFolder, targetFolder);
                    }
                }
            }

            // После того как всё содержимое перемещено, пробуем удалить старый корень
            deleteDirectoryRecursive(legacyPath);
            System.out.println("Migration successful: all characters moved to " + newPath);

        } catch (IOException e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void moveDirectoryRecursive(Path source, Path target) throws IOException {
        Files.createDirectories(target);
        try (var stream = Files.list(source)) {
            for (Path file : stream.toList()) {
                Path targetFile = target.resolve(file.getFileName());
                if (Files.isDirectory(file)) {
                    moveDirectoryRecursive(file, targetFile);
                } else {
                    Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        Files.delete(source);
    }

    private static void deleteDirectoryRecursive(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (var stream = Files.list(path)) {
                for (Path p : stream.toList()) {
                    deleteDirectoryRecursive(p);
                }
            }
        }
        Files.deleteIfExists(path);
    }

    public static void ensureRootExists() throws IOException {
        Files.createDirectories(getRoot());
    }
}