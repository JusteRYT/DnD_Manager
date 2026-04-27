package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.function.Consumer;

public class InventoryItemDialogActionFactory {

    public HBox build(
            Stage stage,
            Character character,
            InventoryItemDialogPresenter presenter,
            InventoryItemFormState state,
            InventoryItemFormView formView,
            InventoryItemIconChooser iconChooser,
            Consumer<InventoryItem> onComplete,
            Runnable onSubmitSuccess
    ) {
        Objects.requireNonNull(stage, "stage must not be null");
        Objects.requireNonNull(character, "character must not be null");
        Objects.requireNonNull(presenter, "presenter must not be null");
        Objects.requireNonNull(state, "state must not be null");
        Objects.requireNonNull(formView, "formView must not be null");
        Objects.requireNonNull(iconChooser, "iconChooser must not be null");
        Objects.requireNonNull(onComplete, "onComplete must not be null");
        Objects.requireNonNull(onSubmitSuccess, "onSubmitSuccess must not be null");

        InventoryItem existingItem = state.existingItem();

        Button iconBtn = AppButtonFactory.addIcon(I18n.t("buttonText.icon"));
        iconBtn.setOnAction(e -> state.setIconPath(iconChooser.chooseItemIcon(stage, state.iconPath())));

        Button chooseAssets = AppButtonFactory.assetPickerButton();
        AppButtonFactory.attachAssetPicker(chooseAssets, state::setIconPath);

        Button saveBtn = AppButtonFactory.actionSave(existingItem == null ? I18n.t("button.addItem") : I18n.t("button.save"));
        saveBtn.setOnAction(e -> {
            boolean submitted = presenter.submit(
                    character,
                    existingItem,
                    formView.nameField().getText(),
                    formView.descriptionField().getText(),
                    formView.countField().getText(),
                    state.iconPath(),
                    formView.effectSection().isEquipped(),
                    formView.effectSection().getEffectName(),
                    state.attachedBuffs(),
                    state.attachedSkills(),
                    onComplete
            );
            if (submitted) {
                onSubmitSuccess.run();
            }
        });

        HBox actions = new HBox(10, iconBtn, chooseAssets, saveBtn);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(15, 0, 0, 0));
        return actions;
    }
}

