package com.example.dnd_manager.assets.ui;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import com.example.dnd_manager.theme.scroll.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AssetGalleryViewBuilder {

    private final AssetGalleryTabStyleProvider styleProvider;

    public AssetGalleryViewBuilder(AssetGalleryTabStyleProvider styleProvider) {
        this.styleProvider = styleProvider;
    }

    public AssetGalleryView build(AssetCategory category, Runnable clearSelectionAction) {
        Button uploadButton = AppButtonFactory.actionAdd("Add assets", 150);
        if (category.isAll()) {
            uploadButton.setVisible(false);
            uploadButton.setManaged(false);
        }

        HBox controls = new HBox(20, uploadButton);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(0, 10, 0, 10));

        FlowPane galleryPane = new FlowPane(15, 15);
        galleryPane.setAlignment(Pos.TOP_LEFT);
        galleryPane.setPadding(new Insets(10));
        galleryPane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getTarget() == galleryPane) {
                clearSelectionAction.run();
            }
        });

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(galleryPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(styleProvider.scrollPaneStyle());
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        galleryPane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));
        return new AssetGalleryView(uploadButton, controls, galleryPane, scrollPane);
    }
}












