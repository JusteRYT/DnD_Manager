package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CharacterAssetActiveEffectIconResolver implements ActiveEffectIconResolver {

    @Override
    public ImageView resolve(Character character, String iconPath) {
        if (iconPath == null || iconPath.isBlank()) {
            return null;
        }
        try {
            ImageView icon = new ImageView(new Image(CharacterAssetResolver.resolve(character.getName(), iconPath)));
            icon.setFitWidth(12);
            icon.setFitHeight(12);
            return icon;
        } catch (Exception ignored) {
            return null;
        }
    }
}

