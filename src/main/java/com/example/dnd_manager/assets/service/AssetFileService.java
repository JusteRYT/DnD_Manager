package com.example.dnd_manager.assets.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/**
 * File operations for asset rename/delete use-cases.
 */
public class AssetFileService {

    public Path buildRenamedPath(Path target, String userInputName) {
        String fileName = target.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        String ext = dot == -1 ? "" : fileName.substring(dot);
        String finalName = userInputName.endsWith(ext) ? userInputName : userInputName + ext;
        return target.resolveSibling(finalName);
    }

    public void rename(Path target, String newName) throws IOException {
        Path renamedPath = buildRenamedPath(target, newName);
        java.nio.file.Files.move(target, renamedPath);
    }

    public void deleteAll(Set<Path> targets) {
        if (targets == null || targets.isEmpty()) {
            return;
        }
        for (Path path : targets) {
            try {
                java.nio.file.Files.deleteIfExists(path);
            } catch (IOException ignored) {
                // caller decides logging policy
            }
        }
    }
}

