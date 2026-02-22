package com.example.dnd_manager.repository;

import com.example.dnd_manager.domain.Character;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class CharacterAssetResolver {

    private static final Logger log = LoggerFactory.getLogger(CharacterAssetResolver.class);
    private static final String DEFAULT_ICON = "/com/example/dnd_manager/icon/no_image.png";

    private CharacterAssetResolver() {}

    public static String resolve(String characterName, String relativePath) {
        return CharacterStoragePathResolver
                .getCharacterDir(characterName)
                .resolve(relativePath)
                .toUri()
                .toString();
    }

    public static Image getImage(Character character, String iconPath) {
        String name = (character != null) ? character.getName() : "";

        if (iconPath == null || iconPath.isBlank() || iconPath.contains("no_image.png")) {
            return getDefaultImage();
        }

        try {
            Path targetPath = null;

            if (iconPath.startsWith("file:")) {
                targetPath = Path.of(java.net.URI.create(iconPath));
            } else {
                Path path = Path.of(iconPath);
                if (
                        path.isAbsolute()) {
                    targetPath = path;
                } else if (!name.isBlank()) {
                    targetPath = CharacterStoragePathResolver.getCharacterDir(name).resolve(iconPath);
                }
            }

            if (targetPath != null && Files.exists(targetPath)) {
                try (InputStream is = Files.newInputStream(targetPath)) {
                    return new Image(is);
                }
            }

            if (iconPath.contains(":/")) {
                return new Image(iconPath, true);
            }

        } catch (Exception e) {
            log.error("Failed to load image safely: {} (Character: {})", iconPath, name, e);
        }

        return getDefaultImage();
    }

    private static Image getDefaultImage() {
        return new Image(Objects.requireNonNull(
                CharacterAssetResolver.class.getResource(DEFAULT_ICON)).toExternalForm());
    }
}