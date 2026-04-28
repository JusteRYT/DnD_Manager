package com.example.dnd_manager.overview.dialogs.inventory;

import com.example.dnd_manager.assets.service.GlobalAssetService;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class EditInventoryItemDialog extends AddInventoryItemDialog {

    public EditInventoryItemDialog(Stage owner, Character character, InventoryItem item, Consumer<InventoryItem> onItemEdited) {
        super(owner, character, item, onItemEdited);
    }

    public EditInventoryItemDialog(
            Stage owner,
            Character character,
            InventoryItem item,
            Consumer<InventoryItem> onItemEdited,
            GlobalAssetService globalAssetService
    ) {
        super(owner, character, item, onItemEdited, globalAssetService);
    }

}












