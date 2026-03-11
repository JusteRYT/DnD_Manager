package com.example.dnd_manager.updater;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A custom dialog to display the update download progress.
 */
public class AppUpdateProgressDialog {

    private final Stage dialogStage;
    private final ProgressBar progressBar;
    private final Label statusLabel;

    public AppUpdateProgressDialog(Stage owner) {
        dialogStage = new Stage();
        dialogStage.initOwner(owner);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initStyle(StageStyle.UNDECORATED);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(350);
        progressBar.setStyle("-fx-accent: #ffaa00;");

        statusLabel = new Label("Downloading update...");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        VBox root = new VBox(15, statusLabel, progressBar);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1e1e1e; -fx-border-color: #3a3a3a; -fx-border-width: 2;");

        dialogStage.setScene(new Scene(root));
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