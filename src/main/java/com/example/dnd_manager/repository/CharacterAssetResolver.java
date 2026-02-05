package com.example.dnd_manager.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Resolves character asset paths to file URLs.
 */
public final class CharacterAssetResolver {

    private static final String ROOT_DIR = "Character";

    private CharacterAssetResolver() {
    }

    /**
     * Resolves relative character asset path to file URL.
     *
     * @param characterName character directory name
     * @param relativePath relative asset path (e.g. icon/avatar.png)
     * @return file URL string
     */
    public static String resolve(String characterName, String relativePath) {
        Path fullPath = Paths.get(ROOT_DIR, characterName, relativePath);
        return fullPath.toUri().toString();
    }
}
