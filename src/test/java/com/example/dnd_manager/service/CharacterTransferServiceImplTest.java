package com.example.dnd_manager.service;

import com.example.dnd_manager.repository.CharacterPathProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterTransferServiceImplTest {

    @TempDir
    Path tempDir;

    @Test
    void importCharacter_extractsZipIntoProviderRoot() throws Exception {
        Path root = tempDir.resolve("Characters");
        CharacterPathProvider pathProvider = new FakePathProvider(root);
        CharacterTransferServiceImpl service = new CharacterTransferServiceImpl(pathProvider);

        Path zipPath = tempDir.resolve("Hero.zip");
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            zos.putNextEntry(new ZipEntry("Hero/Hero.json"));
            zos.write("{\"name\":\"Hero\"}".getBytes());
            zos.closeEntry();
        }

        service.importCharacter(zipPath.toFile());

        assertTrue(Files.exists(root.resolve("Hero").resolve("Hero.json")));
    }

    @Test
    void exportCharacter_writesZipForCharacterDirectory() throws Exception {
        Path root = tempDir.resolve("Characters");
        CharacterPathProvider pathProvider = new FakePathProvider(root);
        CharacterTransferServiceImpl service = new CharacterTransferServiceImpl(pathProvider);

        Path charDir = root.resolve("Hero");
        Files.createDirectories(charDir);
        Files.writeString(charDir.resolve("Hero.json"), "{\"name\":\"Hero\"}");

        File zip = tempDir.resolve("out.zip").toFile();
        service.exportCharacter("Hero", zip);

        assertTrue(zip.exists());
        assertTrue(zip.length() > 0);
    }

    private static final class FakePathProvider implements CharacterPathProvider {
        private final Path root;

        private FakePathProvider(Path root) {
            this.root = root;
        }

        @Override
        public Path getRoot() {
            return root;
        }

        @Override
        public Path getCharacterDir(String characterName) {
            return root.resolve(characterName);
        }

        @Override
        public void ensureRootExists() throws java.io.IOException {
            Files.createDirectories(root);
        }

        @Override
        public void migrateIfNeeded() {
        }
    }
}
