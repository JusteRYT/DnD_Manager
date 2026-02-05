package com.example.dnd_manager.overview;


import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.tooltip.BuffsView;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

/**
 * Right panel: Buffs/Debuffs + Inventory
 */
public class BuffsInventoryPanel extends VBox {

    /**
     * Creates right-side panel with buffs and inventory.
     *
     * @param character character instance
     */
    public BuffsInventoryPanel(Character character, StorageService storageService) {
        setSpacing(15);

        VBox buffs = new VBox(new BuffsView(character));
        buffs.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");
        buffs.setPadding(new Insets(8));

        InventoryPanel inventoryPanel = new InventoryPanel(character, storageService::saveCharacter);

        getChildren().addAll(buffs, inventoryPanel);
    }
}
