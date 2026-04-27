package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.scene.image.Image;

public class CharacterAssetFamiliarAvatarImageResolver implements FamiliarAvatarImageResolver {

    private static final String FALLBACK_IMAGE = "/com/example/dnd_manager/icon/no_image.png";

    @Override
    public Image resolve(Character familiar, Character owner) {
        try {
            return new Image(CharacterAssetResolver.resolve(owner.getName(), familiar.getAvatarImage()));
        } catch (Exception ignored) {
            return new Image(getClass().getResource(FALLBACK_IMAGE).toExternalForm());
        }
    }
}

