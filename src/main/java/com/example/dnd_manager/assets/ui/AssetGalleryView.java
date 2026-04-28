package com.example.dnd_manager.assets.ui;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public record AssetGalleryView(
        Button uploadButton,
        HBox controls,
        FlowPane galleryPane,
        ScrollPane scrollPane
) {
}












