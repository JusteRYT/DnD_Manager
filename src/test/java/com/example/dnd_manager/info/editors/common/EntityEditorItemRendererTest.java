package com.example.dnd_manager.info.editors.common;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityEditorItemRendererTest {

    @Test
    void refresh_stretchesEmptyStateToContainerWidth() {
        FlowPane container = new FlowPane();
        EntityEditorItemRenderer<String> renderer = new EntityEditorItemRenderer<>(
                container,
                Label::new,
                VBox::new
        );

        renderer.refresh(List.of());

        assertEquals(1, container.getChildren().size());
        Region emptyState = (Region) container.getChildren().getFirst();
        assertTrue(emptyState.prefWidthProperty().isBound());
    }
}
