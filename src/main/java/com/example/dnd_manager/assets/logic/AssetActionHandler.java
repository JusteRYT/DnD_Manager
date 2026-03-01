package com.example.dnd_manager.assets.logic;

import com.example.dnd_manager.overview.dialogs.ConfirmDialog;
import com.example.dnd_manager.overview.dialogs.RenameDialog;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Slf4j
public class AssetActionHandler {
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
            } catch (Exception e) {
                log.error("Error renaming file", e);
            }
        }).show();
    }

    public void delete(Set<Path> targets) {
        if (targets.isEmpty()) return;

        String message;
        if (targets.size() == 1) {
            String fileName = targets.iterator().next().getFileName().toString();
            message = String.format("Вы действительно хотите удалить файл \"%s\"?\nЭто действие необратимо.", fileName);
        } else {
            message = String.format("Вы действительно хотите удалить %d элементов?\nЭто действие необратимо.", targets.size());
        }

        // Вызываем наш красивый ConfirmDialog
        new ConfirmDialog(currentStage, "Подтверждение удаления", message, () -> {
            targets.forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    log.error("Failed to delete {}", path, e);
                }
            });
            refreshCallback.run();
        }).show();
    }
}