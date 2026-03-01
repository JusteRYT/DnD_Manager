package com.example.dnd_manager.assets.logic;

import com.example.dnd_manager.overview.dialogs.RenameDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class AssetActionHandler {
    private static final Logger log = LoggerFactory.getLogger(AssetActionHandler.class);
    private final Runnable refreshCallback;
    private final Stage currentStage;

    public AssetActionHandler(Runnable refreshCallback, Stage currentStage) {
        this.refreshCallback = refreshCallback;
        this.currentStage = currentStage;
    }

    public void rename(Path target) {
        String fileName = target.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        String baseName = (dot == -1) ? fileName : fileName.substring(0, dot);
        String ext = (dot == -1) ? "" : fileName.substring(dot);

        new RenameDialog(currentStage, baseName, newName -> {
            String finalName = newName.endsWith(ext) ? newName : newName + ext;
            try {
                Files.move(target, target.resolveSibling(finalName));
                refreshCallback.run();
            } catch (Exception e) { e.printStackTrace(); }
        }).show();
    }

    public void delete(Set<Path> targets) {
        if (targets.isEmpty()) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Удалить " + targets.size() + " элементов?");
        alert.showAndWait().filter(r -> r == ButtonType.OK).ifPresent(r -> {
            targets.forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    log.error("Failed to delete {}", path, e);
                }
            });
            refreshCallback.run();
        });
    }
}