package com.example.dnd_manager.screen.start;

import com.example.dnd_manager.application.CharacterUseCases;
import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.service.CharacterImageIntegrityService;
import com.example.dnd_manager.application.service.CharacterTransferService;
import com.example.dnd_manager.updater.port.UpdateFlowCoordinator;
import javafx.stage.Stage;

/**
 * Shared helpers for opening the start screen from feature flows.
 */
public final class StartScreenNavigation {

    private StartScreenNavigation() {
    }

    public static Runnable backToStartAction(
            Stage stage,
            ScreenNavigator screenNavigator,
            CharacterUseCases characterUseCases,
            CharacterImageIntegrityService characterImageIntegrityService,
            CharacterTransferService characterTransferService,
            UpdateFlowCoordinator updateFlowCoordinator
    ) {
        return () -> screenNavigator.open(
                new StartScreen(
                        stage,
                        screenNavigator,
                        characterUseCases,
                        characterImageIntegrityService,
                        characterTransferService,
                        updateFlowCoordinator
                ).getView()
        );
    }
}













