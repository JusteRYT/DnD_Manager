package com.example.dnd_manager.overview.ui;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class JavaFxStageResolver implements StageResolver {

    @Override
    public Stage resolve(Button sourceButton) {
        return (Stage) sourceButton.getScene().getWindow();
    }
}

