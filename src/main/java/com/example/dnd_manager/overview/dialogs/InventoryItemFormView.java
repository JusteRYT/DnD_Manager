package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;

public record InventoryItemFormView(
        AppTextField nameField,
        AppTextSection descriptionField,
        IntegerField countField,
        InventoryItemEffectSection effectSection
) {
}

