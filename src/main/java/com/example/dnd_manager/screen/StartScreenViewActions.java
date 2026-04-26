package com.example.dnd_manager.screen;

import javafx.scene.control.Button;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * UI actions used by StartScreenViewBuilder.
 */
public record StartScreenViewActions(
        Runnable onCreate,
        Runnable onEdit,
        Runnable onLoad,
        Runnable onAssets,
        Runnable onTransfer,
        Runnable onLanguageChange,
        Consumer<Button> onUpdateCheck
) {
    public StartScreenViewActions {
        Objects.requireNonNull(onCreate, "onCreate must not be null");
        Objects.requireNonNull(onEdit, "onEdit must not be null");
        Objects.requireNonNull(onLoad, "onLoad must not be null");
        Objects.requireNonNull(onAssets, "onAssets must not be null");
        Objects.requireNonNull(onTransfer, "onTransfer must not be null");
        Objects.requireNonNull(onLanguageChange, "onLanguageChange must not be null");
        Objects.requireNonNull(onUpdateCheck, "onUpdateCheck must not be null");
    }
}

