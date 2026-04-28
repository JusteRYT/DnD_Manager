package com.example.dnd_manager.assets.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetFileServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void buildRenamedPath_keepsOriginalExtensionWhenMissingInInput() {
        AssetFileService service = new AssetFileService();
        Path source = tempDir.resolve("icon.png");

        Path renamed = service.buildRenamedPath(source, "new_name");

        assertEquals("new_name.png", renamed.getFileName().toString());
    }

    @Test
    void rename_movesFileToNewName() throws Exception {
        AssetFileService service = new AssetFileService();
        Path source = tempDir.resolve("spell.webp");
        Files.writeString(source, "data");

        service.rename(source, "spell_new");

        assertFalse(Files.exists(source));
        assertTrue(Files.exists(tempDir.resolve("spell_new.webp")));
    }

    @Test
    void deleteAll_deletesAllProvidedFiles() throws Exception {
        AssetFileService service = new AssetFileService();
        Path a = tempDir.resolve("a.jpg");
        Path b = tempDir.resolve("b.jpg");
        Files.writeString(a, "x");
        Files.writeString(b, "x");

        service.deleteAll(Set.of(a, b));

        assertFalse(Files.exists(a));
        assertFalse(Files.exists(b));
    }
}













