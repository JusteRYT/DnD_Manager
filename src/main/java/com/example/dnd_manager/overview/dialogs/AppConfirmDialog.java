package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
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

        // Текст сообщения
        Label label = new Label(message);
        label.setStyle("-fx-text-fill: " + AppTheme.TEXT_PRIMARY + "; -fx-font-size: 16px; -fx-text-alignment: center;");
        label.setWrapText(true);

        // Кнопки
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button okBtn = AppButtonFactory.primaryButton(isConfirmation ? I18n.t("error.buttonOk") : I18n.t("button.close"), 120, 35, 14);
        okBtn.setOnAction(e -> {
            confirmed = true;
            close();
        });

        buttonBox.getChildren().add(okBtn);

        if (isConfirmation) {
            Button cancelBtn = AppButtonFactory.primaryButton("Отмена", 120, 35, 14);
            cancelBtn.setStyle(cancelBtn.getStyle() + "-fx-base: #444444;"); // Немного приглушим кнопку отмены
            cancelBtn.setOnAction(e -> {
                confirmed = false;
                close();
            });
            buttonBox.getChildren().add(cancelBtn);
        }

        contentArea.getChildren().addAll(label, buttonBox);
    }
}