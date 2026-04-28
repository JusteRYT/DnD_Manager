package com.example.dnd_manager.overview.ui.effects;

import com.example.dnd_manager.domain.Character;
import javafx.scene.image.ImageView;

public interface ActiveEffectIconResolver {

    ImageView resolve(Character character, String iconPath);
}













