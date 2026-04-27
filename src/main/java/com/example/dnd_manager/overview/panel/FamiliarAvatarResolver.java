package com.example.dnd_manager.overview.panel;

import javafx.scene.image.Image;

public interface FamiliarAvatarResolver {

    Image resolve(String ownerName, String familiarAvatarPath);
}

