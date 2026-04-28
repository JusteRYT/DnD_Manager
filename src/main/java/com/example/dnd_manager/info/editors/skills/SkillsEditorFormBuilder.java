package com.example.dnd_manager.info.editors.skills;

import com.example.dnd_manager.info.editors.common.EditorFormLayoutBuilder;
import com.example.dnd_manager.info.skills.model.ActivationType;
import com.example.dnd_manager.info.skills.view.EffectsBuilderField;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SkillsEditorFormBuilder {

    private final EditorFormLayoutBuilder layoutBuilder;

    public SkillsEditorFormBuilder(EditorFormLayoutBuilder layoutBuilder) {
        this.layoutBuilder = layoutBuilder;
    }

    public SkillsEditorFormControls build(VBox inputCard, Label nameRequiredLabel) {
        EffectsBuilderField effectsBuilder = new EffectsBuilderField();
        AppTextField nameField = new AppTextField(I18n.t("textField.skillName"), true);
        AppComboBox<String> activationBox = createActivationBox();
        AppTextSection descriptionSection = new AppTextSection("", 3, I18n.t("textSection.promptText.skillDescription"));
        Label iconPathLabel = layoutBuilder.iconPathLabel();
        Button iconButton = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        Button saveButton = AppButtonFactory.actionSave(I18n.t("button.addSkill"));
        saveButton.setPrefWidth(200);
        Button assetPickerButton = AppButtonFactory.assetPickerButton();

        HBox topRow = layoutBuilder.row(15,
                layoutBuilder.field(I18n.t("textFieldLabel.skillName"), new VBox(0, nameField.getField(), nameRequiredLabel)),
                layoutBuilder.field(I18n.t("textFieldLabel.activation"), activationBox)
        );

        inputCard.getChildren().addAll(
                topRow,
                layoutBuilder.field(I18n.t("textFieldLabel.description"), descriptionSection),
                effectsBuilder,
                layoutBuilder.row(15, layoutBuilder.field(I18n.t("textFieldLabel.iconName"), iconPathLabel)),
                layoutBuilder.row(15, saveButton, iconButton, assetPickerButton)
        );

        return new SkillsEditorFormControls(
                effectsBuilder,
                nameField,
                descriptionSection,
                activationBox,
                iconPathLabel,
                iconButton,
                saveButton,
                assetPickerButton
        );
    }

    private AppComboBox<String> createActivationBox() {
        AppComboBox<String> activationBox = new AppComboBox<>();
        for (ActivationType type : ActivationType.values()) {
            activationBox.getItems().add(type.getName());
        }
        activationBox.setValue(ActivationType.ACTION.getName());
        activationBox.setPrefWidth(180);
        return activationBox;
    }
}












