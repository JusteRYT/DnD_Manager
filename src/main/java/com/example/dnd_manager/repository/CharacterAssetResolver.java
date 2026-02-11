package com.example.dnd_manager.repository;

import java.nio.file.Path;
/**
 * Resolves character asset paths to file URLs.
 */
public final class CharacterAssetResolver {

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
        Path fullPath = CharacterStoragePathResolver
                .getCharacterDir(characterName)
                .resolve(relativePath);

        return fullPath.toUri().toString();
    }
}
