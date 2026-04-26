package com.example.dnd_manager.assets.logic;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.assets.service.AssetGalleryService;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/**
 * Orchestrates gallery loading and import actions for AssetGalleryTab.
 */
public class AssetGalleryController {

    private static final Logger log = LoggerFactory.getLogger(AssetGalleryController.class);

    private final AssetCategory category;
    private final Path baseAssetsPath;
    private final Path rootCategoryPath;
    private final Stage stage;
    private final AssetGalleryService galleryService;

    public AssetGalleryController(
            AssetCategory category,
            Path baseAssetsPath,
            Stage stage,
            AssetGalleryService galleryService
    ) {
        this.category = category;
        this.baseAssetsPath = baseAssetsPath;
        this.rootCategoryPath = category.isAll() ? baseAssetsPath : baseAssetsPath.resolve(category.getFolderName());
        this.stage = stage;
        this.galleryService = galleryService;
    }

    public void ensureCategoryDirectory() {
        try {
            galleryService.ensureDirectory(rootCategoryPath);
        } catch (Exception e) {
            log.error("Dir error", e);
        }
    }

    public Thread startLoadingImages(Consumer<Path> onImagePath, Consumer<Exception> onError) {
        Thread worker = new Thread(() -> {
            try {
                List<Path> paths = galleryService.listImageFiles(category, baseAssetsPath, rootCategoryPath);
                for (Path path : paths) {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    onImagePath.accept(path);
                }
            } catch (Exception e) {
                onError.accept(e);
            }
        });
        worker.setDaemon(true);
        worker.start();
        return worker;
    }

    public void handleUpload(Runnable onUploaded) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp"));
        List<File> files = chooser.showOpenMultipleDialog(stage);
        galleryService.importFiles(files, rootCategoryPath);
        if (files != null && !files.isEmpty()) {
            onUploaded.run();
        }
    }
}

