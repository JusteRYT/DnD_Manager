package com.example.dnd_manager.overview.dialogs.common;

import com.example.dnd_manager.lang.I18n;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class RenameDialog extends BaseDialog {
    private final String oldName;
    private final Consumer<String> onConfirm;
    private TextField inputField;

    public RenameDialog(Stage owner, String oldName, Consumer<String> onConfirm) {
        super(owner, I18n.t("dialog.renameAsset.title"), 400, 200);
        this.oldName = oldName;
        this.onConfirm = onConfirm;
    }

    @Override
    protected void setupContent() {
        Label label = new Label(I18n.t("dialog.renameAsset.inputLabel"));
        label.setStyle(dialogStyles.labelStyle());

        inputField = new TextField(oldName);
        dialogStyles.applyTextInput(inputField);

        Button saveBtn = new Button(I18n.t("button.save"));
        saveBtn.setPrefSize(120, 36);
        dialogStyles.applyPrimaryButton(saveBtn);
        saveBtn.setOnAction(e -> {
            onConfirm.accept(inputField.getText());
            close();
        });

        Button cancelBtn = new Button(I18n.t("button.cancel"));
        cancelBtn.setPrefSize(100, 36);
        dialogStyles.applySecondaryButton(cancelBtn);
        cancelBtn.setOnAction(e -> close());

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        buttons.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        contentArea.setSpacing(15);
        contentArea.getChildren().addAll(label, inputField, buttons);
    }
}












