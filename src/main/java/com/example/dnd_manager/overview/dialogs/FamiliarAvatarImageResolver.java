package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import javafx.scene.image.Image;

public interface FamiliarAvatarImageResolver {

    Image resolve(Character familiar, Character owner);
}

