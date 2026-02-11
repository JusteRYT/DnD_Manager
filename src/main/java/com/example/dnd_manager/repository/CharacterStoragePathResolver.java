package com.example.dnd_manager.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Resolves and manages character storage root directory.
 */
public final class CharacterStoragePathResolver {

    private static final String ROOT_DIR_NAME = "Character";

    private CharacterStoragePathResolver() {
    }

    /**
     * Returns root directory where all characters are stored.
     *
     * @return absolute path to character storage
     */
    public static Path getRoot() {
        return Paths.get(ROOT_DIR_NAME).toAbsolutePath();
    }

    /**
     * Returns path to specific character directory.
     *
     * @param characterName name of character
     * @return absolute path to character folder
     */
    public static Path getCharacterDir(String characterName) {
        return getRoot().resolve(characterName);
    }

    /**
     * Ensures root directory exists.
     *
     * @throws IOException if directory cannot be created
     */
    public static void ensureRootExists() throws IOException {
        Files.createDirectories(getRoot());
    }
}
