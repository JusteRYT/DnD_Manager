package com.example.dnd_manager.screen;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.AppConfirmDialog;
import com.example.dnd_manager.updater.AppUpdateProgressDialog;
import com.example.dnd_manager.updater.GitHubRelease;
import com.example.dnd_manager.updater.MegabytesProgressTextFormatter;
import com.example.dnd_manager.updater.UpdateFlowCoordinator;
import com.example.dnd_manager.updater.UpdateProgressCalculator;
import com.example.dnd_manager.updater.UpdateProgressPresenter;
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
    private final UpdateProgressPresenter updateProgressPresenter;
    private final UpdateCheckButtonPresenter updateCheckButtonPresenter;

    public StartScreenUpdateController(
            Stage stage,
            BiConsumer<String, String> errorPresenter,
            UpdateFlowCoordinator updateFlowCoordinator
    ) {
        this(
                stage,
                errorPresenter,
                updateFlowCoordinator,
                new UpdateProgressPresenter(
                        new UpdateProgressCalculator(),
                        new MegabytesProgressTextFormatter()
                ),
                new UpdateCheckButtonPresenter()
        );
    }

    StartScreenUpdateController(
            Stage stage,
            BiConsumer<String, String> errorPresenter,
            UpdateFlowCoordinator updateFlowCoordinator,
            UpdateProgressPresenter updateProgressPresenter,
            UpdateCheckButtonPresenter updateCheckButtonPresenter
    ) {
        this.stage = stage;
        this.errorPresenter = errorPresenter;
        this.updateFlowCoordinator = updateFlowCoordinator;
        this.updateProgressPresenter = updateProgressPresenter;
        this.updateCheckButtonPresenter = updateCheckButtonPresenter;
    }

    public void handleUpdateCheck(Button updateButton) {
        log.info("Update: Manual update check requested");
        UpdateCheckButtonView buttonView = new JavaFxUpdateCheckButtonView(updateButton);
        updateCheckButtonPresenter.showChecking(buttonView);

        updateFlowCoordinator.checkForUpdate(
                releaseOpt -> {
                    updateCheckButtonPresenter.showReady(buttonView);

                    if (releaseOpt.isPresent()) {
                        handleFoundUpdate(releaseOpt.get(), buttonView);
                    } else {
                        showNoUpdatesDialog();
                    }
                },
                e -> {
                    log.error("Update check failed", e);
                    updateCheckButtonPresenter.showReady(buttonView);
                    errorPresenter.accept(
                            I18n.t("update.error_connection_title"),
                            I18n.t("update.error_connection_content")
                    );
                }
        );
    }

    private void handleFoundUpdate(GitHubRelease release, UpdateCheckButtonView buttonView) {
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
                    updateProgressPresenter.present(progressDialog, downloaded, total);
                },
                e -> {
                    log.error("Update failed", e);
                    progressDialog.close();
                    errorPresenter.accept(I18n.t("update.error_title"), e.getMessage());
                    updateCheckButtonPresenter.showReady(buttonView);
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
