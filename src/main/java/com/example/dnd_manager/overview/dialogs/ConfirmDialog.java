package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConfirmDialog extends BaseDialog {

    private final String message;
    private final Runnable onConfirm;

    public ConfirmDialog(Stage owner, String title, String message, Runnable onConfirm) {
        super(owner, title, 400, 200); // Компактный размер для подтверждения
        this.message = message;
        this.onConfirm = onConfirm;
    }

    @Override
    protected void setupContent() {
        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-text-alignment: center;");
        msgLabel.setWrapText(true);

        // Кнопки
        var confirmBtn = AppButtonFactory.actionAdd("Confirm", 100);
        var cancelBtn = AppButtonFactory.actionExit(I18n.t("button.exit"), 100);

        confirmBtn.setOnAction(e -> {
            onConfirm.run();
            close();
        });
        cancelBtn.setOnAction(e -> close());

        HBox buttonBox = new HBox(20, cancelBtn, confirmBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        contentArea.getChildren().addAll(msgLabel, buttonBox);
        contentArea.setAlignment(Pos.CENTER);
    }
}