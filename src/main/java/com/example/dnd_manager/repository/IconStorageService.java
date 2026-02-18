package com.example.dnd_manager.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class IconStorageService {

    private static final String ICON_DIR = "icon";

    public String storeIcon(String characterName, File sourceFile) throws IOException {
        Path targetDir = CharacterStoragePathResolver.getCharacterDir(characterName).resolve(ICON_DIR);
        Files.createDirectories(targetDir);

        Path targetFile = targetDir.resolve(sourceFile.getName());
        Files.copy(sourceFile.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);

        return ICON_DIR + "/" + sourceFile.getName();
    }
}