package com.example.dnd_manager.overview.panel;


import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.tooltip.view.BuffsView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

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
    public BuffsInventoryPanel(
            Character character,
            Stage stage,
            Runnable onRefresh,
            SaveCharacterUseCase saveCharacterUseCase
    ) {
        this(character, stage, onRefresh, saveCharacterUseCase, new BuffsWrapperBuilder());
    }

    BuffsInventoryPanel(
            Character character,
            Stage stage,
            Runnable onRefresh,
            SaveCharacterUseCase saveCharacterUseCase,
            BuffsWrapperBuilder buffsWrapperBuilder
    ) {
        setSpacing(15);
        this.character = Objects.requireNonNull(character, "character must not be null");
        BuffsWrapperBuilder wrapperBuilder = Objects.requireNonNull(buffsWrapperBuilder, "buffsWrapperBuilder must not be null");
        this.buffsView = new BuffsView(character);

        InventoryPanel inventoryPanel = new InventoryPanel(character, c -> onRefresh.run());

        getChildren().addAll(
                wrapperBuilder.build(buffsView),
                inventoryPanel,
                new FamiliarsPanel(character, stage, saveCharacterUseCase)
        );
    }

    public void refreshBuffs() {
        buffsView.refresh(character);
    }
}












