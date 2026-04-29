package com.example.dnd_manager.overview.dialogs.common;

import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;

public class AppConfirmDialog extends BaseDialog {

    private final String message;
    private final boolean isConfirmation;
    @Getter
    private boolean confirmed = false;

    public AppConfirmDialog(Stage owner, String title, String message, boolean isConfirmation) {
        super(owner, title, 450, 200);
        this.message = message;
        this.isConfirmation = isConfirmation;
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(25);
        contentArea.setAlignment(Pos.CENTER);

        Label label = new Label(message);
        label.setStyle(dialogStyles.messageStyle() + "-fx-text-alignment: center;");
        label.setWrapText(true);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button okBtn = new Button(isConfirmation ? I18n.t("button.confirm") : I18n.t("button.close"));
        okBtn.setPrefSize(120, 36);
        dialogStyles.applyPrimaryButton(okBtn);
        okBtn.setOnAction(e -> {
            confirmed = isConfirmation;
            close();
        });

        buttonBox.getChildren().add(okBtn);

        if (isConfirmation) {
            Button cancelBtn = new Button(I18n.t("button.cancel"));
            cancelBtn.setPrefSize(120, 36);
            dialogStyles.applySecondaryButton(cancelBtn);
            cancelBtn.setOnAction(e -> {
                confirmed = false;
                close();
            });
            buttonBox.getChildren().add(cancelBtn);
        }

        contentArea.getChildren().addAll(label, buttonBox);
    }
}












