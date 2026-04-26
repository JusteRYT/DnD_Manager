package com.example.dnd_manager.repository;

import java.io.IOException;
import java.nio.file.Path;

public class DefaultCharacterPathProvider implements CharacterPathProvider {

    @Override
    public Path getRoot() {
        return CharacterStoragePathResolver.getRoot();
    }

    @Override
    public Path getCharacterDir(String characterName) {
        return CharacterStoragePathResolver.getCharacterDir(characterName);
    }

    @Override
    public void ensureRootExists() throws IOException {
        CharacterStoragePathResolver.ensureRootExists();
    }

    @Override
    public void migrateIfNeeded() {
        CharacterStoragePathResolver.migrateIfNeeded();
    }
}
