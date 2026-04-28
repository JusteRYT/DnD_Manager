package com.example.dnd_manager.info.editors.common;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.Collection;
import java.util.function.Function;

public class EntityEditorItemRenderer<T> {

    private final Pane container;
    private final Function<T, Node> rowFactory;

    public EntityEditorItemRenderer(Pane container, Function<T, Node> rowFactory) {
        this.container = container;
        this.rowFactory = rowFactory;
    }

    public void render(T item) {
        container.getChildren().add(rowFactory.apply(item));
    }

    public void refresh(Collection<T> items) {
        container.getChildren().clear();
        for (T item : items) {
            render(item);
        }
    }
}












