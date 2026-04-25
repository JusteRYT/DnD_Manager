package com.example.dnd_manager.infrastructure.navigation;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.screen.ScreenManager;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * JavaFX implementation of screen navigation port.
 */
public class FxScreenNavigator implements ScreenNavigator {

    private final Stage stage;

    public FxScreenNavigator(Stage stage) {
        this.stage = Objects.requireNonNull(stage, "stage must not be null");
    }

    @Override
    public void open(Node view) {
        ScreenManager.setScreen(stage, view);
    }
}

