package com.example.dnd_manager.info.editors.common;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public record EntityEditorShell(
        Label title,
        VBox inputCard,
        Pane itemsContainer
) {
}












