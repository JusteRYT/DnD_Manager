package com.example.dnd_manager.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.stream.Stream;

public final class CharacterStoragePathResolver {

    private static final String APP_FOLDER_NAME = "DnD_Manager";
    private static final String ROOT_DIR_NAME = "Characters"; // Новое (множественное)
    private static final String LEGACY_DIR_NAME = "Character"; // Старое (единственное)
    private static final Logger log = LoggerFactory.getLogger(CharacterStoragePathResolver.class);

    private CharacterStoragePathResolver() {
    }

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

    public static void migrateIfNeeded() {
        log.info("Starting migration check...");
        Path legacyPath = Paths.get(LEGACY_DIR_NAME).toAbsolutePath();
        Path newRoot = getRoot();

        if (!Files.exists(legacyPath) || !Files.isDirectory(legacyPath)) return;

        try {
            Files.createDirectories(newRoot);
            try (Stream<Path> stream = Files.list(legacyPath)) {
                for (Path sourceDir : stream.filter(Files::isDirectory).toList()) {
                    String charName = sourceDir.getFileName().toString();
                    Path targetDir = newRoot.resolve(charName);

                    if (shouldReplace(sourceDir, targetDir, charName)) {
                        log.warn("Version conflict detected for {}. Legacy is newer. Migrating...", charName);
                        moveContentsRecursive(sourceDir, targetDir);
                    } else {
                        // Если в AppData версия новее, просто удаляем старую
                        deleteDirectoryRecursive(sourceDir);
                    }
                }
            }
            // Удаляем корень старой папки, если она пуста
            if (isDirEmpty(legacyPath)) Files.delete(legacyPath);

        } catch (IOException e) {
            System.err.println("CRITICAL: Migration failed! Check permissions. " + e.getMessage());
        }
    }

    private static boolean shouldReplace(Path sourceDir, Path targetDir, String charName) throws IOException {
        if (!Files.exists(targetDir)) return true;

        Path sourceJson = sourceDir.resolve(charName + ".json");
        Path targetJson = targetDir.resolve(charName + ".json");

        if (!Files.exists(targetJson)) return true;
        if (!Files.exists(sourceJson)) return false;

        FileTime sourceTime = Files.getLastModifiedTime(sourceJson);
        FileTime targetTime = Files.getLastModifiedTime(targetJson);

        // Возвращаем true, если файл в старой папке новее
        return sourceTime.compareTo(targetTime) > 0;
    }

    private static void moveContentsRecursive(Path source, Path target) throws IOException {
        if (!Files.exists(target)) Files.createDirectories(target);
        try (Stream<Path> stream = Files.list(source)) {
            for (Path file : stream.toList()) {
                Path dest = target.resolve(file.getFileName());
                if (Files.isDirectory(file)) {
                    moveContentsRecursive(file, dest);
                } else {
                    Files.move(file, dest, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        Files.deleteIfExists(source);
    }

    private static void deleteDirectoryRecursive(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> stream = Files.list(path)) {
                for (Path p : stream.toList()) deleteDirectoryRecursive(p);
            }
        }
        Files.deleteIfExists(path);
    }

    private static boolean isDirEmpty(Path path) throws IOException {
        try (Stream<Path> stream = Files.list(path)) {
            return stream.findAny().isEmpty();
        }
    }

    public static void ensureRootExists() throws IOException {
        Files.createDirectories(getRoot());
    }
}