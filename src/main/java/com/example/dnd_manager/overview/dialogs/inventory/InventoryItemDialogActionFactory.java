package com.example.dnd_manager.overview.dialogs.inventory;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import com.example.dnd_manager.theme.dialog.AppDialogStyleProvider;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.function.Consumer;

public class InventoryItemDialogActionFactory {

    private final AppDialogStyleProvider dialogStyles = new AppDialogStyleProvider();

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

        Button iconBtn = new Button(I18n.t("buttonText.icon"));
        iconBtn.setPrefSize(120, 38);
        dialogStyles.applySecondaryButton(iconBtn);
        iconBtn.setOnAction(e -> state.setIconPath(iconChooser.chooseItemIcon(stage, state.iconPath())));

        Button chooseAssets = AppButtonFactory.assetPickerButton();
        chooseAssets.setPrefSize(120, 38);
        dialogStyles.applySecondaryButton(chooseAssets);
        AppButtonFactory.attachAssetPicker(chooseAssets, state::setIconPath);

        Button saveBtn = new Button(existingItem == null ? I18n.t("button.addItem") : I18n.t("button.save"));
        saveBtn.setPrefSize(150, 38);
        dialogStyles.applyPrimaryButton(saveBtn);
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













