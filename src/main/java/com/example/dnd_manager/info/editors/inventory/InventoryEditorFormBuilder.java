package com.example.dnd_manager.info.editors.inventory;

import com.example.dnd_manager.info.editors.common.EditorFormLayoutBuilder;
import com.example.dnd_manager.info.editors.common.EntityEditorButtonFactory;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
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

        Button addBuffButton = EntityEditorButtonFactory.arcaneBuff(I18n.t("dialog.inventory.buffs.short"), 120);
        Button addSkillButton = EntityEditorButtonFactory.arcaneSkill(I18n.t("dialog.inventory.skills.short"), 120);

        Label effectsInfoLabel = new Label(effectsSummaryFormatter.emptyText());
        effectsInfoLabel.setStyle("""
                -fx-text-fill: #b8cbd3;
                -fx-font-size: 11px;
                -fx-font-style: italic;
                -fx-effect: dropshadow(gaussian, rgba(111, 159, 189, 0.12), 7, 0.18, 0, 0);
                """);

        Button iconButton = EntityEditorButtonFactory.iconPicker(I18n.t("button.addIcon"));
        Button saveButton = EntityEditorButtonFactory.primary(I18n.t("button.addItem"), 150);
        Button assetPickerButton = EntityEditorButtonFactory.secondary(I18n.t("button.Assets"), 120);

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
        javafx.scene.layout.FlowPane buttonsRow = layoutBuilder.actionRow(saveButton, iconButton, assetPickerButton);

        inputCard.getChildren().addAll(
                layoutBuilder.section(
                        layoutBuilder.label(I18n.t("textFieldLabel.itemName")),
                        layoutBuilder.validatedNameField(nameField.getField(), nameRequiredLabel),
                        layoutBuilder.field(I18n.t("textFieldLabel.description"), descriptionField),
                        layoutBuilder.field(I18n.t("textField.inventoryCount"), countField.getField())
                ),
                layoutBuilder.section(
                        layoutBuilder.label(I18n.t("label.editDialog")),
                        effectsRow
                ),
                layoutBuilder.section(settingsRow),
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












