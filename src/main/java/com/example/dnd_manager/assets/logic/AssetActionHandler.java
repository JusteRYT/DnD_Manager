package com.example.dnd_manager.assets.logic;

import com.example.dnd_manager.assets.service.AssetFileService;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.ConfirmDialog;
import com.example.dnd_manager.overview.dialogs.RenameDialog;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class AssetActionHandler {
    private final Runnable refreshCallback;
    private final Stage currentStage;
    private final AssetFileService fileService;

    public AssetActionHandler(Runnable refreshCallback, Stage currentStage) {
        this(refreshCallback, currentStage, new AssetFileService());
    }

    public AssetActionHandler(Runnable refreshCallback, Stage currentStage, AssetFileService fileService) {
        this.refreshCallback = refreshCallback;
        this.currentStage = currentStage;
        this.fileService = Objects.requireNonNull(fileService, "fileService must not be null");
    }

    public void rename(Path target) {
        String fileName = target.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        String baseName = (dot == -1) ? fileName : fileName.substring(0, dot);

        new RenameDialog(currentStage, baseName, newName -> {
            try {
                fileService.rename(target, newName);
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
            message = String.format(
                    I18n.t("asset.delete.confirm.single"),
                    fileName
            );
        } else {
            message = String.format(
                    I18n.t("asset.delete.confirm.multiple"),
                    targets.size()
            );
        }

        new ConfirmDialog(currentStage, I18n.t("asset.delete.confirm.title"), message, () -> {
            fileService.deleteAll(targets);
            refreshCallback.run();
        }).show();
    }
}
