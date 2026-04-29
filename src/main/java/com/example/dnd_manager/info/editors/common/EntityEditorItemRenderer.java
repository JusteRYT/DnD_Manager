package com.example.dnd_manager.info.editors.common;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityEditorItemRenderer<T> {

    private final Pane container;
    private final Function<T, Node> rowFactory;
    private final Supplier<Node> emptyNodeFactory;

    public EntityEditorItemRenderer(Pane container, Function<T, Node> rowFactory) {
        this(container, rowFactory, null);
    }

    public EntityEditorItemRenderer(Pane container, Function<T, Node> rowFactory, Supplier<Node> emptyNodeFactory) {
        this.container = container;
        this.rowFactory = rowFactory;
        this.emptyNodeFactory = emptyNodeFactory;
    }

    public void render(T item) {
        container.getChildren().add(rowFactory.apply(item));
    }

    public void refresh(Collection<T> items) {
        container.getChildren().clear();
        if (items.isEmpty() && emptyNodeFactory != null) {
            Node emptyNode = emptyNodeFactory.get();
            stretchEmptyNode(emptyNode);
            container.getChildren().add(emptyNode);
            return;
        }
        for (T item : items) {
            render(item);
        }
    }

    private void stretchEmptyNode(Node emptyNode) {
        if (emptyNode instanceof Region emptyRegion && container instanceof Region containerRegion) {
            emptyRegion.prefWidthProperty().bind(containerRegion.widthProperty());
        }
    }
}












