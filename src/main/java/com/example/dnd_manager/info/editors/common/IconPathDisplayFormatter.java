package com.example.dnd_manager.info.editors.common;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class IconPathDisplayFormatter {

    public String fileNameOrEmpty(String iconPath) {
        return fileNameOrFallback(iconPath, "");
    }

    public String fileNameOrFallback(String iconPath, String fallback) {
        if (iconPath == null || iconPath.isBlank()) {
            return fallback;
        }

        try {
            Path fileName = Path.of(iconPath).getFileName();
            return fileName == null ? fallback : fileName.toString();
        } catch (InvalidPathException e) {
            return iconPath;
        }
    }
}












