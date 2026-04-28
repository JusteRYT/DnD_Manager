package com.example.dnd_manager.assets.logic;

import com.example.dnd_manager.assets.service.AssetFileService;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.common.AppConfirmDialog;
import com.example.dnd_manager.overview.dialogs.common.RenameDialog;
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
    private final AssetDeleteConfirmMessageFactory deleteMessageFactory;
    private final AssetBaseNameResolver baseNameResolver;

    public AssetActionHandler(Runnable refreshCallback, Stage currentStage) {
        this(
                refreshCallback,
                currentStage,
                new AssetFileService(),
                new AssetDeleteConfirmMessageFactory(),
                new AssetBaseNameResolver()
        );
    }

    public AssetActionHandler(Runnable refreshCallback, Stage currentStage, AssetFileService fileService) {
        this(
                refreshCallback,
                currentStage,
                fileService,
                new AssetDeleteConfirmMessageFactory(),
                new AssetBaseNameResolver()
        );
    }

    public AssetActionHandler(
            Runnable refreshCallback,
            Stage currentStage,
            AssetFileService fileService,
            AssetDeleteConfirmMessageFactory deleteMessageFactory,
            AssetBaseNameResolver baseNameResolver
    ) {
        this.refreshCallback = refreshCallback;
        this.currentStage = currentStage;
        this.fileService = Objects.requireNonNull(fileService, "fileService must not be null");
        this.deleteMessageFactory = Objects.requireNonNull(deleteMessageFactory, "deleteMessageFactory must not be null");
        this.baseNameResolver = Objects.requireNonNull(baseNameResolver, "baseNameResolver must not be null");
    }

    public void rename(Path target) {
        String fileName = target.getFileName().toString();
        String baseName = baseNameResolver.resolve(fileName);

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
        String message = deleteMessageFactory.create(targets);

        AppConfirmDialog confirmDialog = new AppConfirmDialog(
                currentStage,
                I18n.t("asset.delete.confirm.title"),
                message,
                true
        );
        confirmDialog.show();
        if (confirmDialog.isConfirmed()) {
            fileService.deleteAll(targets);
            refreshCallback.run();
        }
    }
}












