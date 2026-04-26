package com.example.dnd_manager.screen;

import com.example.dnd_manager.lang.I18n;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Handles mode-dependent actions for AssetManagerScreen.
 */
public class AssetManagerController {

    private final boolean pickerMode;
    private final Runnable backToStartAction;

    public AssetManagerController(boolean pickerMode, Runnable backToStartAction) {
        this.pickerMode = pickerMode;
        this.backToStartAction = Objects.requireNonNull(backToStartAction, "backToStartAction must not be null");
    }

    public String resolveTitle() {
        return pickerMode ? I18n.t("title.selectAsset").toUpperCase() : I18n.t("title.assetManager").toUpperCase();
    }

    public String resolveExitButtonLabel() {
        return pickerMode ? I18n.t("button.cancel") : I18n.t("button.exit");
    }

    public void handleExit(Stage currentStage) {
        if (pickerMode) {
            currentStage.close();
            return;
        }
        backToStartAction.run();
    }
}

