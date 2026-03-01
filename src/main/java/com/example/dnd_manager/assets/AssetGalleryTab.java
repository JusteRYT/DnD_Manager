package com.example.dnd_manager.assets;

import com.example.dnd_manager.assets.logic.AssetActionHandler;
import com.example.dnd_manager.assets.logic.AssetDnDManager;
import com.example.dnd_manager.assets.logic.AssetSelectionModel;
import com.example.dnd_manager.assets.ui.AssetCard;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class AssetGalleryTab extends VBox {
    private static final Logger log = LoggerFactory.getLogger(AssetGalleryTab.class);

    private final Path categoryPath;
    private final FlowPane galleryPane;
    private final Stage stage;
    private final String categoryName;

    // Хранилище выделенных файлов
    private final Set<Path> selectedFiles = new HashSet<>();
    private final AssetSelectionModel selectionModel = new AssetSelectionModel();
    private final AssetActionHandler actionHandler;
    private final AssetDnDManager dndManager;

    public AssetGalleryTab(AssetCategory category, Path basePath, Stage stage, AssetDnDManager dndManager) {
        this.stage = stage;
        this.categoryName = category.getDisplayName();
        this.categoryPath = basePath.resolve(category.getFolderName());
        this.dndManager = dndManager;
        this.actionHandler = new AssetActionHandler(this::loadImages, stage);

        setSpacing(20);
        setPadding(new Insets(20, 0, 0, 0));
        setStyle("-fx-background-color: transparent;");

        try {
            Files.createDirectories(categoryPath);
        } catch (Exception e) {
            log.error("Critical: Could not create asset directory: {}", categoryPath, e);
        }

        Button uploadBtn = AppButtonFactory.actionAdd("+ Add to " + categoryName, 200);
        uploadBtn.setOnAction(e -> handleUpload());

        Button addSubCategoryBtn = AppButtonFactory.actionSubCategory("+ Subcategory", 100);
        addSubCategoryBtn.setDisable(true);

        HBox controls = new HBox(15, uploadBtn, addSubCategoryBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        galleryPane = new FlowPane(20, 20);
        galleryPane.setAlignment(Pos.TOP_LEFT);
        galleryPane.setPadding(new Insets(10));

        // Клик по пустому месту сбрасывает выделение
        galleryPane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getTarget() == galleryPane) {
                clearSelection();
            }
        });

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(galleryPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        getChildren().addAll(controls, scrollPane);

        loadImages();
    }

    public void loadImages() {
        log.debug("Loading images for category: {}", categoryPath);
        Platform.runLater(() -> {
            galleryPane.getChildren().clear();
            selectedFiles.clear();
        });

        new Thread(() -> {
            try (Stream<Path> paths = Files.list(categoryPath)) {
                paths.filter(Files::isRegularFile)
                        .filter(p -> p.toString().toLowerCase().matches(".*\\.(png|jpg|jpeg|webp)$"))
                        .forEach(path -> {
                            Image img = new Image(path.toUri().toString(), 100, 100, true, true, true);
                            Platform.runLater(() -> addImageToGallery(img, path));
                        });
            } catch (Exception e) {
                log.error("Failed to list files in {}", categoryPath, e);
            }
        }).start();
    }

    private void addImageToGallery(Image img, Path path) {
        AssetCard card = new AssetCard(path, img, selectionModel, actionHandler, dndManager, this::loadImages);
        galleryPane.getChildren().add(card);
    }

    private void clearSelection() {
        selectedFiles.clear();
        String idleStyle = "-fx-background-color: #2b2b2b; -fx-background-radius: 8; -fx-border-color: transparent; -fx-border-radius: 8; -fx-border-width: 2;";
        for (Node node : galleryPane.getChildren()) {
            node.setStyle(idleStyle);
        }
    }

    private void handleUpload() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import Assets to " + categoryName);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp"));

        var files = chooser.showOpenMultipleDialog(stage);
        if (files != null && !files.isEmpty()) {
            log.info("Attempting to upload {} files to {}", files.size(), categoryName);
            for (File file : files) {
                try {
                    Path dest = categoryPath.resolve(file.getName());
                    Files.copy(file.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    log.error("Error copying file: {}", file.getName(), ex);
                }
            }
            loadImages();
        }
    }
}