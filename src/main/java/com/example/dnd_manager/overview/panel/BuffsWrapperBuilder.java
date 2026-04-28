package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.tooltip.view.BuffsView;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class BuffsWrapperBuilder {

    private final BuffsInventoryPanelStyleProvider styleProvider;

    public BuffsWrapperBuilder() {
        this(new BuffsInventoryPanelStyleProvider());
    }

    BuffsWrapperBuilder(BuffsInventoryPanelStyleProvider styleProvider) {
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
    }

    public VBox build(BuffsView buffsView) {
        VBox wrapper = new VBox(buffsView);
        wrapper.setStyle(styleProvider.buffsWrapperIdleStyle());
        wrapper.setOnMouseEntered(e -> wrapper.setStyle(styleProvider.buffsWrapperHoverStyle()));
        wrapper.setOnMouseExited(e -> wrapper.setStyle(styleProvider.buffsWrapperIdleStyle()));
        return wrapper;
    }
}













