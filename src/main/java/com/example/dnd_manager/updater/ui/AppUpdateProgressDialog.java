package com.example.dnd_manager.updater.ui;

import com.example.dnd_manager.updater.port.UpdateProgressView;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.dialog.AppDialogStyleProvider;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A custom dialog to display the update download progress.
 */
public class AppUpdateProgressDialog implements UpdateProgressView {

    private final Stage dialogStage;
    private final ProgressBar progressBar;
    private final Label statusLabel;
    private final AppDialogStyleProvider dialogStyles = new AppDialogStyleProvider();

    public AppUpdateProgressDialog(Stage owner) {
        dialogStage = new Stage();
        dialogStage.initOwner(owner);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initStyle(StageStyle.UNDECORATED);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(350);
        progressBar.setStyle(dialogStyles.progressBarStyle());

        statusLabel = new Label(I18n.t("update.progress.initial"));
        statusLabel.setStyle(dialogStyles.messageStyle());

        VBox root = new VBox(15, statusLabel, progressBar);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle(dialogStyles.rootStyle());

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialogStage.setScene(scene);
    }

    public void show() {
        dialogStage.show();
    }

    public void close() {
        dialogStage.close();
    }

    /**
     * Updates the progress bar and status text.
     *
     * @param progress value from 0.0 to 1.0
     * @param message  status message
     */

    public void update(double progress, String message) {
        Platform.runLater(() -> {
            progressBar.setProgress(progress);
            statusLabel.setText(message);
        });
    }
}














