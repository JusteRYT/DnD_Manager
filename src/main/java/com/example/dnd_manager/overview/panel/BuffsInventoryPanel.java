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
    private final BuffsView buffsView;
    private final Character character;

    /**
     * Creates right-side panel with buffs and inventory.
     *
     * @param character character instance
     */
    public BuffsInventoryPanel(Character character, StorageService storageService, Stage stage, Runnable onRefresh) {
        setSpacing(15);
        this.character = character;
        this.buffsView = new BuffsView(character);

        VBox buffsWrapper = new VBox(buffsView);

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

        buffsWrapper.setStyle(idleStyle);

        buffsWrapper.setOnMouseEntered(e -> buffsWrapper.setStyle(hoverStyle));
        buffsWrapper.setOnMouseExited(e -> buffsWrapper.setStyle(idleStyle));

        InventoryPanel inventoryPanel = new InventoryPanel(character, c -> {
            onRefresh.run();
        });

        getChildren().addAll(buffsWrapper, inventoryPanel, new FamiliarsPanel(character, stage, storageService));
    }

    public void refreshBuffs() {
        buffsView.refresh(character);
    }
}
