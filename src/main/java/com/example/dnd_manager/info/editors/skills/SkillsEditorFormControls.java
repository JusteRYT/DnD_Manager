package com.example.dnd_manager.info.editors.skills;

import com.example.dnd_manager.info.skills.view.EffectsBuilderField;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public record SkillsEditorFormControls(
        EffectsBuilderField effectsBuilder,
        AppTextField nameField,
        AppTextSection descriptionSection,
        AppComboBox<String> activationBox,
        Label iconPathLabel,
        Button iconButton,
        Button saveButton,
        Button assetPickerButton
) {
}












