package com.example.dnd_manager.assets.logic;

import javafx.application.Platform;
import javafx.scene.layout.FlowPane;

import java.nio.file.Path;
import java.util.function.Consumer;

public class AssetGalleryLoadingCoordinator {

    private Thread loadingThread;

    public void load(
            AssetGalleryController controller,
            FlowPane galleryPane,
            Runnable clearSelection,
            Consumer<Path> addCard,
            Consumer<Exception> onError
    ) {
        interruptCurrent();

        Platform.runLater(() -> {
            clearSelection.run();
            galleryPane.getChildren().clear();
        });

        loadingThread = controller.startLoadingImages(addCard, onError);
    }

    private void interruptCurrent() {
        if (loadingThread != null && loadingThread.isAlive()) {
            loadingThread.interrupt();
        }
    }
}












