package com.example.dnd_manager.infrastructure.persistence;

import java.io.IOException;
import java.nio.file.Path;

public interface CharacterPathProvider {

    Path getRoot();

    Path getCharacterDir(String characterName);

    void ensureRootExists() throws IOException;

    void migrateIfNeeded();
}












