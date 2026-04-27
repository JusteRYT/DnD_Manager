package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryItemDialogPresenterTest {

    private final InventoryItemDialogPresenter presenter = new InventoryItemDialogPresenter(
            new InventoryItemFormValidator(),
            new InventoryItemCountResolver(),
            new InventoryItemDialogSubmitService(new InventoryItemMutationService(), "icon/no_image.png")
    );

    @Test
    void submit_returnsFalseWhenNameInvalid() {
        Character character = new Character();

        boolean submitted = presenter.submit(
                character,
                null,
                " ",
                "desc",
                "2",
                "icon.png",
                false,
                "",
                List.of(),
                List.of(),
                item -> {
                }
        );

        assertFalse(submitted);
        assertEquals(0, character.getInventory().size());
    }

    @Test
    void submit_returnsTrueWhenDataValid() {
        Character character = new Character();
        AtomicReference<InventoryItem> callback = new AtomicReference<>();

        boolean submitted = presenter.submit(
                character,
                null,
                "Potion",
                "heal",
                "3",
                "",
                true,
                "Heal Aura",
                List.of(),
                List.of(),
                callback::set
        );

        assertTrue(submitted);
        assertEquals(1, character.getInventory().size());
        assertEquals("Potion", callback.get().getName());
    }
}

