package com.example.dnd_manager.infrastructure.assets;

import com.example.dnd_manager.infrastructure.persistence.CharacterPathProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IconStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void storeIcon_copiesFileUsingProvidedCharacterPathProvider() throws Exception {
        CharacterPathProvider pathProvider = new FakePathProvider(tempDir.resolve("Characters"));
        IconStorageService service = new IconStorageService(pathProvider);

        Path source = tempDir.resolve("avatar.png");
        Files.writeString(source, "img");

        String relativePath = service.storeIcon("Hero", source.toFile());

        assertEquals("icon/avatar.png", relativePath);
        assertTrue(Files.exists(tempDir.resolve("Characters").resolve("Hero").resolve("icon").resolve("avatar.png")));
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












