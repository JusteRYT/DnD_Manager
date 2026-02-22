package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Темный диалог ошибок: текст слева направо, кнопка внизу.
 */
public class AppErrorDialog extends BaseDialog {

    private final String message;

    public AppErrorDialog(Stage owner, String title, String message) {
        super(owner, title, 450, 250);
        this.message = message;
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(15);
        contentArea.setAlignment(Pos.TOP_LEFT);
        contentArea.setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + ";");
        contentArea.setPadding(new Insets(25));

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(400);
        messageLabel.setAlignment(Pos.TOP_LEFT);

        messageLabel.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-alignment: left;",
                AppTheme.TEXT_PRIMARY
        ));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);

        Button okButton = AppButtonFactory.actionSave(I18n.t("error.buttonOk"));
        okButton.setPrefWidth(120);
        okButton.setOnAction(e -> close());

        buttonContainer.getChildren().add(okButton);

        contentArea.getChildren().addAll(messageLabel, spacer, buttonContainer);
    }
}