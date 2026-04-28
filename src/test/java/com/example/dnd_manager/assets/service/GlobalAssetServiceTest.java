package com.example.dnd_manager.assets.service;

import com.example.dnd_manager.assets.AssetCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalAssetServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void importAsset_copiesFileIntoCategoryDirectory() throws Exception {
        Path source = tempDir.resolve("icon.png");
        Files.writeString(source, "img");

        Path assetsRoot = tempDir.resolve("AssetsRoot");
        GlobalAssetService service = new GlobalAssetService(assetsRoot);

        String importedPath = service.importAsset(source.toFile(), AssetCategory.ITEMS);

        assertNotNull(importedPath);
        Path target = assetsRoot.resolve(AssetCategory.ITEMS.getFolderName()).resolve("icon.png");
        assertTrue(Files.exists(target));
    }
}













