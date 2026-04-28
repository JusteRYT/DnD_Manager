package com.example.dnd_manager.assets.logic;

public class AssetBaseNameResolver {

    public String resolve(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "";
        }

        int dot = fileName.lastIndexOf('.');
        if (dot <= 0) {
            return fileName;
        }
        return fileName.substring(0, dot);
    }
}












