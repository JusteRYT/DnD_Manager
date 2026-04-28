package com.example.dnd_manager.info.editors.inventory;

import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public record InventoryEditorFormControls(
        AppTextField nameField,
        AppTextSection descriptionField,
        IntegerField countField,
        Label iconPathLabel,
        Label effectsInfoLabel,
        Button addBuffButton,
        Button addSkillButton,
        Button iconButton,
        Button saveButton,
        Button assetPickerButton
) {
}












