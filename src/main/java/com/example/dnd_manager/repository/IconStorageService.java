package com.example.dnd_manager.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class IconStorageService {

    private static final String ICON_DIR = "icon";
    private final CharacterPathProvider pathProvider;

    public IconStorageService() {
        this(new DefaultCharacterPathProvider());
    }

    public IconStorageService(CharacterPathProvider pathProvider) {
        this.pathProvider = Objects.requireNonNull(pathProvider, "pathProvider must not be null");
    }

    public String storeIcon(String characterName, File sourceFile) throws IOException {
        Path targetDir = pathProvider.getCharacterDir(characterName).resolve(ICON_DIR);
        Files.createDirectories(targetDir);

        Path targetFile = targetDir.resolve(sourceFile.getName());
        Files.copy(sourceFile.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);

        return ICON_DIR + "/" + sourceFile.getName();
    }
}
