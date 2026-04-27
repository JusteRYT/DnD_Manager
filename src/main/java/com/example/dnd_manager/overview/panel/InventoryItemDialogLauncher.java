package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import javafx.stage.Stage;

import java.util.function.Consumer;

public interface InventoryItemDialogLauncher {

    void openCreate(Stage owner, Character character, Consumer<InventoryItem> onComplete);

    void openEdit(Stage owner, Character character, InventoryItem item, Consumer<InventoryItem> onComplete);
}
