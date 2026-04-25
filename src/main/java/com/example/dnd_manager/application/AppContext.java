package com.example.dnd_manager.application;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.infrastructure.navigation.FxScreenNavigator;
import com.example.dnd_manager.store.StorageService;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Composition root container for cross-layer dependencies.
 */
public class AppContext {

    private final Stage stage;
    private final StorageService storageService;
    private final ScreenNavigator screenNavigator;
    private final CharacterUseCases characterUseCases;

    private AppContext(Stage stage, StorageService storageService) {
        this.stage = Objects.requireNonNull(stage, "stage must not be null");
        this.storageService = Objects.requireNonNull(storageService, "storageService must not be null");
        this.screenNavigator = new FxScreenNavigator(stage);
        this.characterUseCases = new CharacterUseCases(storageService);
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
}

