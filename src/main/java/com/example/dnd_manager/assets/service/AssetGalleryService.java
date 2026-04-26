package com.example.dnd_manager.assets.service;

import com.example.dnd_manager.assets.AssetCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * File-system service for asset gallery listing and import operations.
 */
public class AssetGalleryService {

    private static final Logger log = LoggerFactory.getLogger(AssetGalleryService.class);

    public void ensureDirectory(Path directory) throws IOException {
        Files.createDirectories(directory);
    }

    public List<Path> listImageFiles(AssetCategory category, Path baseAssetsPath, Path rootCategoryPath) throws IOException {
        try (Stream<Path> stream = category.isAll()
                ? Files.walk(baseAssetsPath, 2)
                : Files.list(rootCategoryPath)) {
            return stream.filter(Files::isRegularFile)
                    .filter(this::isImageFile)
                    .sorted()
                    .toList();
        }
    }

    public void importFiles(List<File> files, Path targetDirectory) {
        if (files == null || files.isEmpty()) {
            return;
        }
        List<File> copied = new ArrayList<>(files);
        for (File file : copied) {
            try {
                Files.copy(file.toPath(), targetDirectory.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ex) {
                log.error("Copy error for file {}", file, ex);
            }
        }
    }

    private boolean isImageFile(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".png")
                || name.endsWith(".jpg")
                || name.endsWith(".jpeg")
                || name.endsWith(".webp");
    }
}

