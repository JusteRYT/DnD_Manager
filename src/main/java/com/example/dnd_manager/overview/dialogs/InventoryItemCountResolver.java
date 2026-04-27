package com.example.dnd_manager.overview.dialogs;

public class InventoryItemCountResolver {

    public int resolve(String countText) {
        if (countText == null || countText.isBlank()) {
            return 1;
        }
        try {
            return Integer.parseInt(countText.trim());
        } catch (NumberFormatException ignored) {
            return 1;
        }
    }
}

