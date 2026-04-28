package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.infrastructure.assets.CharacterAssetResolver;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CharacterAssetFamiliarAvatarResolver implements FamiliarAvatarResolver {

    private static final Logger log = LoggerFactory.getLogger(CharacterAssetFamiliarAvatarResolver.class);

    @Override
    public Image resolve(String ownerName, String familiarAvatarPath) {
        try {
            String resolvedPath = familiarAvatarPath != null && !familiarAvatarPath.isBlank()
                    ? CharacterAssetResolver.resolve(ownerName, familiarAvatarPath)
                    : Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/no_image.png")).toExternalForm();
            return new Image(resolvedPath);
        } catch (Exception ex) {
            log.debug("Failed to resolve familiar card avatar '{}' for owner '{}', using fallback",
                    familiarAvatarPath, ownerName, ex);
            return new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/no_image.png")).toExternalForm());
        }
    }
}












