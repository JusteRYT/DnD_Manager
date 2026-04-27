package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.scene.image.Image;

import java.util.Objects;

public class CharacterAssetFamiliarAvatarResolver implements FamiliarAvatarResolver {

    @Override
    public Image resolve(String ownerName, String familiarAvatarPath) {
        try {
            String resolvedPath = familiarAvatarPath != null && !familiarAvatarPath.isBlank()
                    ? CharacterAssetResolver.resolve(ownerName, familiarAvatarPath)
                    : Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/no_image.png")).toExternalForm();
            return new Image(resolvedPath);
        } catch (Exception ignored) {
            return new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/no_image.png")).toExternalForm());
        }
    }
}

