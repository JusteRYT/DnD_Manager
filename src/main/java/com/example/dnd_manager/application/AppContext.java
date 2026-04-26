package com.example.dnd_manager.application;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.infrastructure.navigation.FxScreenNavigator;
import com.example.dnd_manager.repository.CharacterPathProvider;
import com.example.dnd_manager.repository.DefaultCharacterPathProvider;
import com.example.dnd_manager.service.CharacterImageIntegrityService;
import com.example.dnd_manager.service.CharacterTransferService;
import com.example.dnd_manager.service.CharacterTransferServiceImpl;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.updater.DefaultUpdateFlowCoordinator;
import com.example.dnd_manager.updater.DefaultUpdateService;
import com.example.dnd_manager.updater.JavaFxUiDispatcher;
import com.example.dnd_manager.updater.ThreadAsyncRunner;
import com.example.dnd_manager.updater.UpdateChecker;
import com.example.dnd_manager.updater.UpdateFlowCoordinator;
import com.example.dnd_manager.updater.UpdateManager;
import com.example.dnd_manager.updater.UpdateService;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Composition root container for cross-layer dependencies.
 */
public class AppContext {

    private final Stage stage;
    private final StorageService storageService;
    private final ScreenNavigator screenNavigator;
    private final CharacterPathProvider characterPathProvider;
    private final CharacterUseCases characterUseCases;
    private final CharacterTransferService characterTransferService;
    private final CharacterImageIntegrityService characterImageIntegrityService;
    private final UpdateService updateService;
    private final UpdateFlowCoordinator updateFlowCoordinator;

    private AppContext(Stage stage, StorageService storageService) {
        this.stage = Objects.requireNonNull(stage, "stage must not be null");
        this.storageService = Objects.requireNonNull(storageService, "storageService must not be null");
        this.screenNavigator = new FxScreenNavigator(stage);
        this.characterPathProvider = new DefaultCharacterPathProvider();
        this.characterUseCases = new CharacterUseCases(storageService);
        this.characterTransferService = new CharacterTransferServiceImpl(characterPathProvider);
        this.characterImageIntegrityService = new CharacterImageIntegrityService(characterUseCases, characterPathProvider);
        this.updateService = new DefaultUpdateService(new UpdateChecker(), new UpdateManager());
        this.updateFlowCoordinator = new DefaultUpdateFlowCoordinator(
                updateService,
                new ThreadAsyncRunner(),
                new JavaFxUiDispatcher()
        );
    }

    public static AppContext bootstrap(Stage stage, StorageService storageService) {
        return new AppContext(stage, storageService);
    }

    public Stage stage() {
        return stage;
    }

    public StorageService storageService() {
        return storageService;
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

    public UpdateService updateService() {
        return updateService;
    }

    public UpdateFlowCoordinator updateFlowCoordinator() {
        return updateFlowCoordinator;
    }
}
