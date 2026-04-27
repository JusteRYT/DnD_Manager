package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class InventoryItemFormBuilder {

    public InventoryItemFormView build(
            Stage stage,
            Character character,
            InventoryItemFormState state,
            VBox scrollContent
    ) {
        Objects.requireNonNull(stage, "stage must not be null");
        Objects.requireNonNull(character, "character must not be null");
        Objects.requireNonNull(state, "state must not be null");
        Objects.requireNonNull(scrollContent, "scrollContent must not be null");

        scrollContent.setSpacing(15);
        scrollContent.setPadding(new Insets(0, 15, 10, 0));

        InventoryItem existingItem = state.existingItem();

        AppTextField nameField = new AppTextField(
                existingItem != null ? existingItem.getName() : I18n.t("textField.inventoryName"), true
        );
        AppTextSection descriptionField = new AppTextSection(
                existingItem != null ? existingItem.getDescription() : "", 3, I18n.t("dialog.inventory.description.label")
        );
        IntegerField countField = new IntegerField(
                existingItem != null ? String.valueOf(existingItem.getCount()) : "1", true
        );

        InventoryItemEffectSection effectSection = new InventoryItemEffectSection(
                I18n.t("dialog.inventory.equipped"),
                I18n.t("dialog.inventory.effectDisplay.label"),
                I18n.t("dialog.inventory.effectDisplay.prompt"),
                existingItem != null && existingItem.isEquipped(),
                existingItem != null && existingItem.getCustomEffectName() != null
                        ? existingItem.getCustomEffectName()
                        : I18n.t("textField.inventoryName")
        );

        InventoryItemAttachmentsSection attachmentsSection = new InventoryItemAttachmentsSection(
                stage,
                character,
                state.attachedBuffs(),
                state.attachedSkills()
        );

        scrollContent.getChildren().addAll(
                nameField.getField(),
                descriptionField,
                countField.getField(),
                effectSection.getEquippedCheckBox(),
                effectSection.getContainer(),
                attachmentsSection.buildNode()
        );

        return new InventoryItemFormView(nameField, descriptionField, countField, effectSection);
    }
}

