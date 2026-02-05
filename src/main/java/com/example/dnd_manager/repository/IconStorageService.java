package com.example.dnd_manager.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Service responsible for storing character icons.
 */
public class IconStorageService {

    private static final String ICON_DIR = "icon";

    /**
     * Copies selected icon into character icon directory.
     *
     * @param characterName character directory name
     * @param sourceFile selected image file
     * @return relative path to stored icon (e.g. icon/sword.png)
     * @throws IOException if copy fails
     */
    public String storeIcon(String characterName, File sourceFile) throws IOException {
        Path targetDir = Path.of("Character", characterName, ICON_DIR);
        Files.createDirectories(targetDir);

        Path targetFile = targetDir.resolve(sourceFile.getName());

        Files.copy(
                sourceFile.toPath(),
                targetFile,
                StandardCopyOption.REPLACE_EXISTING
        );

        return ICON_DIR + "/" + sourceFile.getName();
    }
}