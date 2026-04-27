package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryPanelControllerTest {

    @Test
    void removeItem_updatesCharacterAndInvokesCallbacks() {
        Character character = new Character();
        InventoryItem item = new InventoryItem("Sword", "desc", "icon/sword.png");
        character.getInventory().add(item);
        AtomicInteger updateCalls = new AtomicInteger();
        InventoryPanelController controller = new InventoryPanelController(
                character,
                c -> updateCalls.incrementAndGet(),
                new FakeLauncher()
        );
        AtomicBoolean removedCallbackCalled = new AtomicBoolean(false);

        controller.removeItem(item, () -> removedCallbackCalled.set(true));

        assertTrue(character.getInventory().isEmpty());
        assertTrue(removedCallbackCalled.get());
        assertEquals(1, updateCalls.get());
    }

    @Test
    void openCreateDialog_forwardsToLauncherAndTriggersUpdate() {
        Character character = new Character();
        AtomicInteger updateCalls = new AtomicInteger();
        FakeLauncher launcher = new FakeLauncher();
        InventoryPanelController controller = new InventoryPanelController(
                character,
                c -> updateCalls.incrementAndGet(),
                launcher
        );
        AtomicBoolean createdCallback = new AtomicBoolean(false);

        controller.openCreateDialog(null, item -> createdCallback.set(true));

        assertTrue(launcher.createCalled);
        assertTrue(createdCallback.get());
        assertEquals(1, updateCalls.get());
    }

    @Test
    void openEditDialog_forwardsToLauncherAndTriggersUpdate() {
        Character character = new Character();
        AtomicInteger updateCalls = new AtomicInteger();
        FakeLauncher launcher = new FakeLauncher();
        InventoryPanelController controller = new InventoryPanelController(
                character,
                c -> updateCalls.incrementAndGet(),
                launcher
        );
        AtomicBoolean editCallback = new AtomicBoolean(false);

        controller.openEditDialog(null, new InventoryItem("Sword", "desc", "icon/sword.png"), () -> editCallback.set(true));

        assertTrue(launcher.editCalled);
        assertTrue(editCallback.get());
        assertEquals(1, updateCalls.get());
    }

    private static final class FakeLauncher implements InventoryItemDialogLauncher {
        private boolean createCalled;
        private boolean editCalled;

        @Override
        public void openCreate(javafx.stage.Stage owner, Character character, java.util.function.Consumer<InventoryItem> onComplete) {
            createCalled = true;
            onComplete.accept(new InventoryItem("Created", "desc", "icon/created.png"));
        }

        @Override
        public void openEdit(javafx.stage.Stage owner, Character character, InventoryItem item, java.util.function.Consumer<InventoryItem> onComplete) {
            editCalled = true;
            onComplete.accept(item);
        }
    }
}
