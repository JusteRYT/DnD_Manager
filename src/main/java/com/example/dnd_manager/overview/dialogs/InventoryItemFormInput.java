package com.example.dnd_manager.overview.dialogs;

public record InventoryItemFormInput(
        String name,
        String description,
        int count,
        String iconPath,
        boolean equipped,
        String customEffectName
) {
}

