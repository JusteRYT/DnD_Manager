package com.example.dnd_manager.application;

import com.example.dnd_manager.application.port.ExternalLinkOpener;
import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.infrastructure.navigation.FxScreenNavigator;
import com.example.dnd_manager.infrastructure.persistence.CharacterPathProvider;
import com.example.dnd_manager.infrastructure.persistence.DefaultCharacterPathProvider;
import com.example.dnd_manager.application.service.CharacterImageIntegrityService;
import com.example.dnd_manager.application.service.CharacterTransferService;
import com.example.dnd_manager.application.service.CharacterTransferServiceImpl;
import com.example.dnd_manager.infrastructure.system.DesktopExternalLinkOpener;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.updater.flow.DefaultUpdateFlowCoordinator;
import com.example.dnd_manager.updater.service.DefaultUpdateService;
import com.example.dnd_manager.updater.runtime.JavaFxUiDispatcher;
import com.example.dnd_manager.updater.runtime.ThreadAsyncRunner;
import com.example.dnd_manager.updater.release.UpdateChecker;
import com.example.dnd_manager.updater.port.UpdateFlowCoordinator;
import com.example.dnd_manager.updater.flow.UpdateManager;
import com.example.dnd_manager.updater.port.UpdateService;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Composition root container for cross-layer dependencies.
 */
public class AppContext {

    private final Stage stage;
    private final ScreenNavigator screenNavigator;
    private final CharacterUseCases characterUseCases;
    private final CharacterTransferService characterTransferService;
    private final CharacterImageIntegrityService characterImageIntegrityService;
    private final UpdateFlowCoordinator updateFlowCoordinator;
    private final ExternalLinkOpener externalLinkOpener;

    private AppContext(Stage stage, StorageService storageService) {
        this.stage = Objects.requireNonNull(stage, "stage must not be null");
        StorageService storage = Objects.requireNonNull(storageService, "storageService must not be null");
        this.screenNavigator = new FxScreenNavigator(stage);
        CharacterPathProvider characterPathProvider = new DefaultCharacterPathProvider();
        this.characterUseCases = new CharacterUseCases(storage);
        this.characterTransferService = new CharacterTransferServiceImpl(characterPathProvider);
        this.characterImageIntegrityService = new CharacterImageIntegrityService(characterUseCases, characterPathProvider);
        UpdateService updateService = new DefaultUpdateService(new UpdateChecker(), new UpdateManager());
        this.updateFlowCoordinator = new DefaultUpdateFlowCoordinator(
                updateService,
                new ThreadAsyncRunner(),
                new JavaFxUiDispatcher()
        );
        this.externalLinkOpener = new DesktopExternalLinkOpener();
    }

    public static AppContext bootstrap(Stage stage, StorageService storageService) {
        return new AppContext(stage, storageService);
    }

    public Stage stage() {
        return stage;
    }

    public ScreenNavigator screenNavigator() {
        return screenNavigator;
    }

    public CharacterUseCases characterUseCases() {
        return characterUseCases;
    }

    public CharacterTransferService characterTransferService() {
        return characterTransferService;
    }

    public CharacterImageIntegrityService characterImageIntegrityService() {
        return characterImageIntegrityService;
    }

    public UpdateFlowCoordinator updateFlowCoordinator() {
        return updateFlowCoordinator;
    }

    public ExternalLinkOpener externalLinkOpener() {
        return externalLinkOpener;
    }
}












