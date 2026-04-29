package com.example.dnd_manager.overview.dialogs.inventory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryDialogStyleProviderTest {

    private final InventoryDialogStyleProvider provider = new InventoryDialogStyleProvider();

    @Test
    void styleTokens_areProvidedForAttachments() {
        assertTrue(provider.attachmentsContainerStyle().contains("-fx-background-color"));
        assertTrue(provider.attachmentsTitleStyle().contains("-fx-font-weight"));
        assertTrue(provider.attachmentsCounterStyle().contains("#dbe5ea"));
        assertFalse(provider.attachmentsContainerStyle().contains("#252525"));
        assertFalse(provider.attachmentsContainerStyle().contains("#3a3a3a"));
    }
}













