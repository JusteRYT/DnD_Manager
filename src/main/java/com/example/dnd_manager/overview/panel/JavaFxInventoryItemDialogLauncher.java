package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.overview.dialogs.inventory.AddInventoryItemDialog;
import com.example.dnd_manager.overview.dialogs.inventory.EditInventoryItemDialog;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class JavaFxInventoryItemDialogLauncher implements InventoryItemDialogLauncher {

    @Override
    public void openCreate(Stage owner, Character character, Consumer<InventoryItem> onComplete) {
        new AddInventoryItemDialog(owner, character, null, onComplete).show();
    }

    @Override
    public void openEdit(Stage owner, Character character, InventoryItem item, Consumer<InventoryItem> onComplete) {
        new EditInventoryItemDialog(owner, character, item, onComplete).show();
    }
}












