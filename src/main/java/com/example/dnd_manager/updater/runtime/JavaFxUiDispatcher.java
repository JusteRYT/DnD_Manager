package com.example.dnd_manager.updater.runtime;

import com.example.dnd_manager.updater.port.UiDispatcher;

import javafx.application.Platform;

import java.util.Objects;

/**
 * UI dispatcher implementation for JavaFX.
 */
public class JavaFxUiDispatcher implements UiDispatcher {

    @Override
    public void dispatch(Runnable action) {
        Objects.requireNonNull(action, "action must not be null");
        Platform.runLater(action);
    }
}















