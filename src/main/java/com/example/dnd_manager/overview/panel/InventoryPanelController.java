package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.function.Consumer;

public class InventoryPanelController {

    private final Character character;
    private final Consumer<Character> onCharacterUpdated;
    private final InventoryItemDialogLauncher dialogLauncher;

    public InventoryPanelController(Character character, Consumer<Character> onCharacterUpdated) {
        this(character, onCharacterUpdated, new JavaFxInventoryItemDialogLauncher());
    }

    InventoryPanelController(
            Character character,
            Consumer<Character> onCharacterUpdated,
            InventoryItemDialogLauncher dialogLauncher
    ) {
        this.character = Objects.requireNonNull(character, "character must not be null");
        this.onCharacterUpdated = Objects.requireNonNull(onCharacterUpdated, "onCharacterUpdated must not be null");
        this.dialogLauncher = Objects.requireNonNull(dialogLauncher, "dialogLauncher must not be null");
    }

    public void openCreateDialog(Stage owner, Consumer<InventoryItem> onItemCreated) {
        dialogLauncher.openCreate(owner, character, item -> {
            onItemCreated.accept(item);
            onCharacterUpdated.accept(character);
        });
    }

    public void openEditDialog(Stage owner, InventoryItem item, Runnable onItemEdited) {
        dialogLauncher.openEdit(owner, character, item, updated -> {
            onItemEdited.run();
            onCharacterUpdated.accept(character);
        });
    }

    public void removeItem(InventoryItem item, Runnable onItemRemoved) {
        character.getInventory().remove(item);
        onItemRemoved.run();
        onCharacterUpdated.accept(character);
    }
}
