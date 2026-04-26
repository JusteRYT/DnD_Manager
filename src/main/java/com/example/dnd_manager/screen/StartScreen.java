package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.AppContext;
import com.example.dnd_manager.application.CharacterUseCases;
import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.overview.dialogs.AppErrorDialog;
import com.example.dnd_manager.service.CharacterImageIntegrityService;
import com.example.dnd_manager.service.CharacterTransferService;
import com.example.dnd_manager.updater.UpdateFlowCoordinator;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Start screen with main navigation actions.
 */
public class StartScreen {

    private static final Logger log = LoggerFactory.getLogger(StartScreen.class);

    private final Stage stage;
    private final StartScreenController controller;
    private final StartScreenUpdateController updateController;
    private final StartScreenViewBuilder viewBuilder;

    public StartScreen(
            Stage stage,
            ScreenNavigator screenNavigator,
            CharacterUseCases characterUseCases,
            CharacterImageIntegrityService characterImageIntegrityService,
            CharacterTransferService characterTransferService,
            UpdateFlowCoordinator updateFlowCoordinator
    ) {
        this.stage = stage;
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
                updateFlowCoordinator
        );
        this.controller = new StartScreenController(
                screenNavigator,
                characterUseCases,
                screenFactory,
                openStartAction,
                this::showError
        );
        this.updateController = new StartScreenUpdateController(stage, this::showError, updateFlowCoordinator);
        this.viewBuilder = new StartScreenViewBuilder();
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
                context.updateFlowCoordinator()
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
                updateController::handleUpdateCheck
        );
        return viewBuilder.build(actions);
    }

    private void showError(String title, String message) {
        AppErrorDialog dialog = new AppErrorDialog(stage, title, message);
        dialog.show();
    }
}
