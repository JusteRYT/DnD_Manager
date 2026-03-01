package com.example.dnd_manager.assets.logic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import lombok.Getter;

import java.nio.file.Path;

@Getter
public class AssetSelectionModel {
    private final ObservableSet<Path> selectedPaths = FXCollections.observableSet();

    public void toggle(Path path) {
        if (selectedPaths.contains(path)) selectedPaths.remove(path);
        else selectedPaths.add(path);
    }

    public void clearAndSelect(Path path) {
        selectedPaths.clear();
        selectedPaths.add(path);
    }

    public void clear() {
        selectedPaths.clear();
    }

    public boolean isSelected(Path path) {
        return selectedPaths.contains(path);
    }

}