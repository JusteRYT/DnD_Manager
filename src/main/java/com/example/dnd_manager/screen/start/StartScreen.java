package com.example.dnd_manager.screen.start;

import com.example.dnd_manager.application.AppContext;
import com.example.dnd_manager.application.ApplicationLinks;
import com.example.dnd_manager.application.CharacterUseCases;
import com.example.dnd_manager.application.port.ExternalLinkOpener;
import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.common.AppErrorDialog;
import com.example.dnd_manager.application.service.CharacterImageIntegrityService;
import com.example.dnd_manager.application.service.CharacterTransferService;
import com.example.dnd_manager.screen.update.StartScreenUpdateController;
import com.example.dnd_manager.updater.port.UpdateFlowCoordinator;
import com.example.dnd_manager.updater.release.CachedReleaseProvider;
import com.example.dnd_manager.updater.release.FileReleaseCacheStore;
import com.example.dnd_manager.updater.release.GitHubApiReleaseProvider;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Start screen with main navigation actions.
 */
public class StartScreen {

    private static final Logger log = LoggerFactory.getLogger(StartScreen.class);

    private final Stage stage;
    private final StartScreenController controller;
    private final StartScreenUpdateController updateController;
    private final StartScreenViewBuilder viewBuilder;
    private final ExternalLinkOpener externalLinkOpener;

    public StartScreen(
            Stage stage,
            ScreenNavigator screenNavigator,
            CharacterUseCases characterUseCases,
            CharacterImageIntegrityService characterImageIntegrityService,
            CharacterTransferService characterTransferService,
            UpdateFlowCoordinator updateFlowCoordinator,
            ExternalLinkOpener externalLinkOpener
    ) {
        this.stage = stage;
        this.externalLinkOpener = externalLinkOpener;
        StartScreenFlowFactory screenFactory = new DefaultStartScreenFlowFactory(
                stage,
                screenNavigator,
                characterUseCases,
                characterTransferService
        );
        Runnable openStartAction = StartScreenNavigation.backToStartAction(
                stage,
                screenNavigator,
                characterUseCases,
                characterImageIntegrityService,
                characterTransferService,
                updateFlowCoordinator,
                externalLinkOpener
        );
        this.controller = new StartScreenController(
                screenNavigator,
                characterUseCases,
                screenFactory,
                openStartAction,
                this::showError
        );
        this.updateController = new StartScreenUpdateController(stage, this::showError, updateFlowCoordinator);
        CachedReleaseProvider releaseNewsProvider = new CachedReleaseProvider(
                new GitHubApiReleaseProvider(),
                new FileReleaseCacheStore(),
                Duration.ofHours(6)
        );
        this.viewBuilder = new StartScreenViewBuilder(() -> releaseNewsProvider.fetchRecentReleases(2));
        log.debug("Initializing StartScreen and running integrity checks...");
        characterImageIntegrityService.validateAndRepairAllCharactersOnce();
    }

    public StartScreen(AppContext context) {
        this(
                context.stage(),
                context.screenNavigator(),
                context.characterUseCases(),
                context.characterImageIntegrityService(),
                context.characterTransferService(),
                context.updateFlowCoordinator(),
                context.externalLinkOpener()
        );
    }

    /**
     * Builds and returns the start screen view.
     *
     * @return root UI node
     */
    public Parent getView() {
        log.debug("Building StartScreen UI nodes...");
        StartScreenViewActions actions = new StartScreenViewActions(
                controller::openCharacterCreate,
                controller::openCharacterEdit,
                controller::openCharacterLoad,
                controller::openAssetManager,
                controller::openCharacterTransfer,
                controller::changeLanguageAndReload,
                updateController::handleUpdateCheck,
                () -> openExternalLink(ApplicationLinks.DONATE_URL),
                () -> openExternalLink(ApplicationLinks.DEVELOPER_TELEGRAM_URL),
                () -> openExternalLink(ApplicationLinks.COMMUNITY_BOT_URL)
        );
        return viewBuilder.build(actions);
    }

    private void openExternalLink(String url) {
        try {
            externalLinkOpener.open(url);
        } catch (RuntimeException ex) {
            log.error("Failed to open external link: {}", url, ex);
            showError(I18n.t("update.error_title"), I18n.t("error.open_link"));
        }
    }

    private void showError(String title, String message) {
        AppErrorDialog dialog = new AppErrorDialog(stage, title, message);
        dialog.show();
    }
}













