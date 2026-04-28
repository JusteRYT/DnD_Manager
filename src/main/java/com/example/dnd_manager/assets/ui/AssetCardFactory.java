package com.example.dnd_manager.assets.ui;

import com.example.dnd_manager.assets.logic.AssetActionHandler;
import com.example.dnd_manager.assets.logic.AssetDnDManager;
import com.example.dnd_manager.assets.logic.AssetSelectionModel;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.function.Consumer;

public class AssetCardFactory {

    public AssetCard create(
            Path path,
            Image image,
            AssetSelectionModel selectionModel,
            AssetActionHandler actionHandler,
            AssetDnDManager dndManager,
            Runnable refreshGallery,
            Consumer<Path> selectionCallback
    ) {
        return new AssetCard(
                path,
                image,
                selectionModel,
                actionHandler,
                dndManager,
                refreshGallery,
                selectionCallback
        );
    }
}












