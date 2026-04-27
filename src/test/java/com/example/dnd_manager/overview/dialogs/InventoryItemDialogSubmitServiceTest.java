package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class InventoryItemDialogSubmitServiceTest {

    private final InventoryItemDialogSubmitService submitService =
            new InventoryItemDialogSubmitService(new InventoryItemMutationService(), "icon/no_image.png");

    @Test
    void submit_createsNewItemAndAddsToInventory() {
        Character character = new Character();
        AtomicReference<InventoryItem> callbackItem = new AtomicReference<>();

        InventoryItem result = submitService.submit(
                character,
                null,
                new InventoryItemFormInput("Torch", "Light source", 2, "", true, "Glow"),
                List.of(),
                List.of(),
                callbackItem::set
        );

        assertEquals(1, character.getInventory().size());
        assertSame(result, character.getInventory().get(0));
        assertSame(result, callbackItem.get());
        assertEquals("icon/no_image.png", result.getIconPath());
    }

    @Test
    void submit_updatesExistingItem() {
        Character character = new Character();
        InventoryItem existing = new InventoryItem("Old", "Old", "old.png");
        AtomicReference<InventoryItem> callbackItem = new AtomicReference<>();

        InventoryItem result = submitService.submit(
                character,
                existing,
                new InventoryItemFormInput("New", "Updated", 5, "new.png", false, "None"),
                List.of(),
                List.of(),
                callbackItem::set
        );

        assertSame(existing, result);
        assertSame(existing, callbackItem.get());
        assertEquals(0, character.getInventory().size());
        assertEquals("New", existing.getName());
        assertEquals(5, existing.getCount());
        assertEquals("new.png", existing.getIconPath());
    }
}
