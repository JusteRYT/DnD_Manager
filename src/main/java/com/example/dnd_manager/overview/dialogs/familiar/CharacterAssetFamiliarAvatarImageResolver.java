package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.infrastructure.assets.CharacterAssetResolver;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CharacterAssetFamiliarAvatarImageResolver implements FamiliarAvatarImageResolver {

    private static final Logger log = LoggerFactory.getLogger(CharacterAssetFamiliarAvatarImageResolver.class);
    private static final String FALLBACK_IMAGE = "/com/example/dnd_manager/icon/no_image.png";

    @Override
    public Image resolve(Character familiar, Character owner) {
        try {
            return new Image(CharacterAssetResolver.resolve(owner.getName(), familiar.getAvatarImage()));
        } catch (Exception ex) {
            log.debug("Failed to resolve familiar avatar '{}' for owner '{}', using fallback",
                    familiar.getAvatarImage(), owner.getName(), ex);
            return new Image(Objects.requireNonNull(getClass().getResource(FALLBACK_IMAGE)).toExternalForm());
        }
    }
}












