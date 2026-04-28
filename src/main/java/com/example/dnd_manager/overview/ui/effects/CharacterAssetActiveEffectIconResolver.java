package com.example.dnd_manager.overview.ui.effects;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.infrastructure.assets.CharacterAssetResolver;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharacterAssetActiveEffectIconResolver implements ActiveEffectIconResolver {

    private static final Logger log = LoggerFactory.getLogger(CharacterAssetActiveEffectIconResolver.class);

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
        } catch (Exception ex) {
            log.debug("Failed to resolve active effect icon '{}' for character '{}'", iconPath, character.getName(), ex);
            return null;
        }
    }
}












