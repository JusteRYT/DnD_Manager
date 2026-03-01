package com.example.dnd_manager.assets.logic;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public class AssetActionHandler {
    private static final Logger log = LoggerFactory.getLogger(AssetActionHandler.class);
    private final Runnable refreshCallback;

    public AssetActionHandler(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    public void rename(Path target) {
        String oldFullPath = target.getFileName().toString();
        // Отделяем расширение
        int dotIndex = oldFullPath.lastIndexOf('.');
        String extension = (dotIndex == -1) ? "" : oldFullPath.substring(dotIndex);
        String oldNameWithoutExt = (dotIndex == -1) ? oldFullPath : oldFullPath.substring(0, dotIndex);

        TextInputDialog dialog = new TextInputDialog(oldNameWithoutExt);
        dialog.setTitle("Rename Asset");
        dialog.setHeaderText("Переименование файла");
        dialog.setContentText("Новое имя:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            if (newName.trim().isEmpty()) return;

            // Если пользователь не ввел расширение сам, добавляем его
            String finalName = newName.endsWith(extension) ? newName : newName + extension;
            Path destination = target.resolveSibling(finalName);

            try {
                if (Files.exists(target)) {
                    Files.move(target, destination);
                    log.info("Renamed: {} -> {}", target.getFileName(), finalName);
                    refreshCallback.run();
                } else {
                    log.error("Rename failed: Source file does not exist at {}", target);
                }
            } catch (IOException e) {
                log.error("Error during rename", e);
            }
        });
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