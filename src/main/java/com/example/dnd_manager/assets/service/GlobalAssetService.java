package com.example.dnd_manager.assets.service;

import com.example.dnd_manager.assets.AssetCategory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Service for managing global application assets.
 * Responsible for importing external files into the project's Assets directory
 * while maintaining a clean, category-based folder structure.
 */
@Slf4j
public class GlobalAssetService {

    /**
     * Root directory for all shared assets.
     */
    private final Path rootAssets;

    public GlobalAssetService() {
        this(Paths.get("Assets"));
    }

    GlobalAssetService(Path rootAssets) {
        this.rootAssets = rootAssets;
    }

    /**
     * Imports an external file into the specified asset category folder.
     *
     * @param sourceFile The external file selected by the user.
     * @param category   The category (folder) where the file should be placed.
     * @return The path relative to the project root (e.g., "Assets/Buffs/icon.png"),
     * or null if the import process failed.
     */
    public String importAsset(File sourceFile, AssetCategory category) {
        if (sourceFile == null || category == null) {
            log.warn("Attempted to import asset with null file or category");
            return null;
        }

        try {
            // 1. Resolve the target directory (e.g., Assets/Skills)
            Path targetDir = rootAssets.resolve(category.getFolderName());

            // 2. Ensure directories exist
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
                log.info("Created new asset directory: {}", targetDir);
            }

            // 3. Define target file path
            Path targetPath = targetDir.resolve(sourceFile.getName());

            // 4. Copy the file to the managed Assets folder
            Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Asset imported successfully: {} -> {}", sourceFile.getName(), targetPath);

            // Return the string path to be stored in the character data
            return targetPath.toString();

        } catch (IOException e) {
            log.error("Failed to import asset file: {}", sourceFile.getAbsolutePath(), e);
            return null;
        }
    }
}












