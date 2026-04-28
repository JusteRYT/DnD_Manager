package com.example.dnd_manager.assets.service;

import com.example.dnd_manager.assets.AssetCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssetGalleryServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void listImageFiles_filtersOnlySupportedImagesForCategory() throws IOException {
        AssetGalleryService service = new AssetGalleryService();
        Path basePath = tempDir.resolve("Assets");
        Path categoryPath = basePath.resolve("Skills");
        Files.createDirectories(categoryPath);

        Files.writeString(categoryPath.resolve("a.png"), "x");
        Files.writeString(categoryPath.resolve("b.jpg"), "x");
        Files.writeString(categoryPath.resolve("ignore.txt"), "x");

        List<Path> files = service.listImageFiles(AssetCategory.SKILLS, basePath, categoryPath);

        assertEquals(2, files.size());
    }

    @Test
    void listImageFiles_allCategory_readsAcrossFolders() throws IOException {
        AssetGalleryService service = new AssetGalleryService();
        Path basePath = tempDir.resolve("Assets");
        Path skills = basePath.resolve("Skills");
        Path items = basePath.resolve("Items");
        Files.createDirectories(skills);
        Files.createDirectories(items);

        Files.writeString(skills.resolve("s.webp"), "x");
        Files.writeString(items.resolve("i.jpeg"), "x");
        Files.writeString(items.resolve("n.bin"), "x");

        List<Path> files = service.listImageFiles(AssetCategory.ALL, basePath, basePath);

        assertEquals(2, files.size());
    }
}













