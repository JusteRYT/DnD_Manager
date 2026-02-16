package com.example.dnd_manager.overview.panel;


import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.tooltip.BuffsView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Right panel: Buffs/Debuffs + Inventory
 */
public class BuffsInventoryPanel extends VBox {

    /**
     * Creates right-side panel with buffs and inventory.
     *
     * @param character character instance
     */
    public BuffsInventoryPanel(Character character, StorageService storageService, Stage stage) {
        setSpacing(15);

        VBox buffsContainer = new VBox(new BuffsView(character));

        String accentColor = "#3aa3c3";
        String commonStyle = """
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
                -fx-background-radius: 10;
                -fx-border-color: %s;
                -fx-border-radius: 10;
                -fx-border-width: 1;
                -fx-padding: 8;
                """.formatted(accentColor);

        String idleStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, rgba(58, 163, 195, 0.2), 15, 0, 0, 0);";
        String hoverStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, %s, 10, 0.2, 0, 0);".formatted(accentColor);

        buffsContainer.setStyle(idleStyle);

        buffsContainer.setOnMouseEntered(e -> buffsContainer.setStyle(hoverStyle));
        buffsContainer.setOnMouseExited(e -> buffsContainer.setStyle(idleStyle));

        InventoryPanel inventoryPanel = new InventoryPanel(character, storageService::saveCharacter);

        FamiliarsPanel familiarsPanel = new FamiliarsPanel(character, stage);

        getChildren().addAll(buffsContainer, inventoryPanel, familiarsPanel);
    }
}
