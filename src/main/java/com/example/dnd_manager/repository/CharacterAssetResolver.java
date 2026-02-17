package com.example.dnd_manager.repository;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.skills.SkillCard;
import javafx.scene.image.Image;

import java.nio.file.Path;
/**
 * Resolves character asset paths to file URLs.
 */
public final class CharacterAssetResolver {

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
        Path fullPath = CharacterStoragePathResolver
                .getCharacterDir(characterName)
                .resolve(relativePath);

        return fullPath.toUri().toString();
    }

    public static Image getImage(Character character, String iconPath) {
        if (iconPath == null || iconPath.isEmpty()) {
            return new Image(SkillCard.class.getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm());
        }

        try {
            if (iconPath.startsWith("file:") || iconPath.startsWith("jar:") || iconPath.contains("://")) {
                return new Image(iconPath);
            }

            Path path = Path.of(iconPath);
            if (path.isAbsolute()) {
                return new Image(path.toUri().toString());
            }

            if (character != null) {
                return new Image(CharacterAssetResolver.resolve(character.getName(), iconPath));
            }

        } catch (Exception e) {
            System.err.println("Error loading image: " + iconPath + " -> " + e.getMessage());
        }

        return new Image(SkillCard.class.getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm());
    }
}
