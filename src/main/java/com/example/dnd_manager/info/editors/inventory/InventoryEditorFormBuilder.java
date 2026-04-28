package com.example.dnd_manager.info.editors.inventory;

import com.example.dnd_manager.info.editors.common.EditorFormLayoutBuilder;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InventoryEditorFormBuilder {

    private final EditorFormLayoutBuilder layoutBuilder;
    private final InventoryEditorEffectsSummaryFormatter effectsSummaryFormatter;

    public InventoryEditorFormBuilder(
            EditorFormLayoutBuilder layoutBuilder,
            InventoryEditorEffectsSummaryFormatter effectsSummaryFormatter
    ) {
        this.layoutBuilder = layoutBuilder;
        this.effectsSummaryFormatter = effectsSummaryFormatter;
    }

    public InventoryEditorFormControls build(VBox inputCard, Label nameRequiredLabel) {
        AppTextField nameField = new AppTextField(I18n.t("textField.inventoryName"), true);
        AppTextSection descriptionField = new AppTextSection("", 3, I18n.t("textSection.inventoryDescription"));
        IntegerField countField = new IntegerField(I18n.t("textField.inventoryCountPrompt"), true);
        Label iconPathLabel = layoutBuilder.iconPathLabel();

        Button addBuffButton = AppButtonFactory.addIcon(I18n.t("dialog.inventory.buffs.short"));
        Button addSkillButton = AppButtonFactory.addIcon(I18n.t("dialog.inventory.skills.short"));

        Label effectsInfoLabel = new Label(effectsSummaryFormatter.emptyText());
        effectsInfoLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11px; -fx-font-style: italic;");

        Button iconButton = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        Button saveButton = AppButtonFactory.actionSave(I18n.t("button.addItem"));
        saveButton.setPrefWidth(150);
        Button assetPickerButton = AppButtonFactory.assetPickerButton();

        HBox effectsRow = layoutBuilder.alignedRow(
                10,
                Pos.CENTER_LEFT,
                addBuffButton,
                addSkillButton,
                effectsInfoLabel
        );
        HBox settingsRow = layoutBuilder.alignedRow(
                15,
                Pos.BOTTOM_LEFT,
                layoutBuilder.field(I18n.t("textFieldLabel.iconName"), iconPathLabel)
        );
        HBox buttonsRow = layoutBuilder.row(15, saveButton, iconButton, assetPickerButton);

        inputCard.getChildren().addAll(
                layoutBuilder.label(I18n.t("textFieldLabel.itemName")),
                layoutBuilder.validatedNameField(nameField.getField(), nameRequiredLabel),
                layoutBuilder.label(I18n.t("textFieldLabel.description")),
                descriptionField,
                layoutBuilder.label(I18n.t("textField.inventoryCount")),
                countField.getField(),
                layoutBuilder.label(I18n.t("label.editDialog")),
                effectsRow,
                settingsRow,
                buttonsRow
        );

        return new InventoryEditorFormControls(
                nameField,
                descriptionField,
                countField,
                iconPathLabel,
                effectsInfoLabel,
                addBuffButton,
                addSkillButton,
                iconButton,
                saveButton,
                assetPickerButton
        );
    }
}












