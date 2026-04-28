package com.example.dnd_manager.overview.dialogs.inventory;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.assets.service.GlobalAssetService;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class InventoryItemIconChooser {

    private final GlobalAssetService globalAssetService;
    private final ItemIconFilePicker filePicker;

    public InventoryItemIconChooser(GlobalAssetService globalAssetService) {
        this(globalAssetService, new JavaFxItemIconFilePicker());
    }

    InventoryItemIconChooser(GlobalAssetService globalAssetService, ItemIconFilePicker filePicker) {
        this.globalAssetService = Objects.requireNonNull(globalAssetService);
        this.filePicker = Objects.requireNonNull(filePicker);
    }

    public String chooseItemIcon(Stage owner, String currentIconPath) {
        File file = filePicker.pick(owner);
        if (file == null) {
            return currentIconPath;
        }

        String importedPath = globalAssetService.importAsset(file, AssetCategory.ITEMS);
        return importedPath != null ? importedPath : currentIconPath;
    }
}












