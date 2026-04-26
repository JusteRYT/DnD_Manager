package com.example.dnd_manager.screen;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.AppConfirmDialog;
import com.example.dnd_manager.updater.AppUpdateProgressDialog;
import com.example.dnd_manager.updater.GitHubRelease;
import com.example.dnd_manager.updater.UpdateFlowCoordinator;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

/**
 * Controls manual update checks and update application flow for StartScreen.
 */
public class StartScreenUpdateController {

    private static final Logger log = LoggerFactory.getLogger(StartScreenUpdateController.class);

    private final Stage stage;
    private final BiConsumer<String, String> errorPresenter;
    private final UpdateFlowCoordinator updateFlowCoordinator;

    public StartScreenUpdateController(
            Stage stage,
            BiConsumer<String, String> errorPresenter,
            UpdateFlowCoordinator updateFlowCoordinator
    ) {
        this.stage = stage;
        this.errorPresenter = errorPresenter;
        this.updateFlowCoordinator = updateFlowCoordinator;
    }

    public void handleUpdateCheck(Button updateButton) {
        log.info("Update: Manual update check requested");
        updateButton.setDisable(true);
        updateButton.setText(I18n.t("button.checking"));

        updateFlowCoordinator.checkForUpdate(
                releaseOpt -> {
                    updateButton.setDisable(false);
                    updateButton.setText(I18n.t("button.checkUpdate"));

                    if (releaseOpt.isPresent()) {
                        handleFoundUpdate(releaseOpt.get(), updateButton);
                    } else {
                        showNoUpdatesDialog();
                    }
                },
                e -> {
                    log.error("Update check failed", e);
                    updateButton.setDisable(false);
                    updateButton.setText(I18n.t("button.checkUpdate"));
                    errorPresenter.accept(
                            I18n.t("update.error_connection_title"),
                            I18n.t("update.error_connection_content")
                    );
                }
        );
    }

    private void handleFoundUpdate(GitHubRelease release, Button updateButton) {
        log.info("Update found: {}", release.tagName);
        AppConfirmDialog confirmDialog = new AppConfirmDialog(
                stage,
                I18n.t("update.title"),
                java.text.MessageFormat.format(I18n.t("update.header"), release.tagName) + "\n" + I18n.t("update.content"),
                true
        );
        confirmDialog.show();

        if (!confirmDialog.isConfirmed()) {
            return;
        }

        log.info("User accepted update: {}", release.tagName);
        AppUpdateProgressDialog progressDialog = new AppUpdateProgressDialog(stage);
        progressDialog.show();

        updateFlowCoordinator.applyUpdate(
                release,
                (downloaded, total) -> {
                    double progress = (double) downloaded / total;
                    String msg = String.format(
                            "Downloading: %.2f MB / %.2f MB",
                            downloaded / (1024.0 * 1024.0),
                            total / (1024.0 * 1024.0)
                    );
                    progressDialog.update(progress, msg);
                },
                e -> {
                    log.error("Update failed", e);
                    progressDialog.close();
                    errorPresenter.accept(I18n.t("update.error_title"), e.getMessage());
                    updateButton.setDisable(false);
                    updateButton.setText(I18n.t("button.checkUpdate"));
                }
        );
    }

    private void showNoUpdatesDialog() {
        log.info("Update: Current version is up to date");
        AppConfirmDialog infoDialog = new AppConfirmDialog(
                stage,
                I18n.t("update.no_updates_title"),
                I18n.t("update.no_updates_content"),
                false
        );
        infoDialog.show();
    }
}
