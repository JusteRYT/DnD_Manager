package com.example.dnd_manager.info.editors.buff;

import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public record BuffEditorFormControls(
        AppTextField nameField,
        AppTextSection descriptionField,
        AppComboBox<String> typeBox,
        Label iconPathLabel,
        Button iconButton,
        Button saveButton,
        Button assetPickerButton
) {
}












