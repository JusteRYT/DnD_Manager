package com.example.dnd_manager.overview.dialogs.inventory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryDialogStyleProviderTest {

    private final InventoryDialogStyleProvider provider = new InventoryDialogStyleProvider();

    @Test
    void styleTokens_areProvidedForAttachments() {
        assertTrue(provider.attachmentsContainerStyle().contains("-fx-background-color"));
        assertTrue(provider.attachmentsTitleStyle().contains("-fx-font-weight"));
    }
}













