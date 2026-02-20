package com.example.dnd_manager.repository;

import com.example.dnd_manager.domain.Character;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Resolves character asset paths to file URLs.
 */
public final class CharacterAssetResolver {

    private static final String DEFAULT_ICON = "/com/example/dnd_manager/icon/no_image.png";

    private CharacterAssetResolver() {
    }

    /**
     * Resolves relative characterName asset path to file URL.
     *
     * @param characterName characterName directory name
     * @param relativePath relative asset path (e.g. icon/avatar.png)
     * @return file URL string
     */
    public static String resolve(String characterName, String relativePath) {
        return CharacterStoragePathResolver
                .getCharacterDir(characterName)
                .resolve(relativePath)
                .toUri()
                .toString();
    }

    public static Image getImage(Character character, String iconPath) {
        String name;
        if (character != null) {
            name = character.getName();
        } else {
            name = "";
        }

        if (iconPath == null || iconPath.isBlank()) {
            return getDefaultImage();
        }

        try {
            if (iconPath.contains(":/")) {
                return new Image(iconPath, true); // true для фоновой загрузки
            }

            Path path = Path.of(iconPath);
            if (path.isAbsolute()) {
                return new Image(path.toUri().toString(), true);
            }

            if (name != null && !name.isBlank()) {
                String resolvedPath = resolve(name, iconPath);
                return new Image(resolvedPath, true);
            }

        } catch (Exception e) {
            System.err.println("Failed to load image: " + iconPath + " - " + e.getMessage());
        }

        return getDefaultImage();
    }

    private static Image getDefaultImage() {
        return new Image(Objects.requireNonNull(
                CharacterAssetResolver.class.getResource(DEFAULT_ICON)).toExternalForm());
    }
}
