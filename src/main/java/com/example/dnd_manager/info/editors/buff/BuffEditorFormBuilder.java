package com.example.dnd_manager.info.editors.buff;

import com.example.dnd_manager.info.editors.common.EditorFormLayoutBuilder;
import com.example.dnd_manager.info.buff_debuff.model.BuffType;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BuffEditorFormBuilder {

    private final EditorFormLayoutBuilder layoutBuilder;

    public BuffEditorFormBuilder(EditorFormLayoutBuilder layoutBuilder) {
        this.layoutBuilder = layoutBuilder;
    }

    public BuffEditorFormControls build(VBox inputCard, Label nameRequiredLabel) {
        AppTextField nameField = new AppTextField(I18n.t("buff.promptText.name"), true);
        AppTextSection descriptionField = new AppTextSection("", 3, I18n.t("textSection.promptText.descriptionBuffs"));
        AppComboBox<String> typeBox = createTypeBox();
        Label iconPathLabel = layoutBuilder.iconPathLabel();
        Button iconButton = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        Button saveButton = AppButtonFactory.actionSave(I18n.t("button.addBuff"));
        saveButton.setPrefWidth(150);
        Button assetPickerButton = AppButtonFactory.assetPickerButton();

        inputCard.getChildren().addAll(
                layoutBuilder.label(I18n.t("textFieldLabel.name")),
                layoutBuilder.validatedNameField(nameField.getField(), nameRequiredLabel),
                layoutBuilder.label(I18n.t("textFieldLabel.description")),
                descriptionField,
                layoutBuilder.row(15,
                        layoutBuilder.field(I18n.t("textFieldLabel.type"), typeBox),
                        layoutBuilder.field(I18n.t("textFieldLabel.iconName"), iconPathLabel)),
                layoutBuilder.row(15, saveButton, iconButton, assetPickerButton)
        );

        return new BuffEditorFormControls(
                nameField,
                descriptionField,
                typeBox,
                iconPathLabel,
                iconButton,
                saveButton,
                assetPickerButton
        );
    }

    private AppComboBox<String> createTypeBox() {
        AppComboBox<String> typeBox = new AppComboBox<>();
        for (BuffType type : BuffType.values()) {
            typeBox.getItems().add(type.getName());
        }
        typeBox.setValue(BuffType.BUFF.getName());
        typeBox.setPrefWidth(150);
        return typeBox;
    }
}












