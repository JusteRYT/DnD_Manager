package com.example.dnd_manager.info.editors.common;

import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.function.Supplier;

public class EntityEditorShellBuilder {

    private final EntityEditorStyleProvider styleProvider;

    public EntityEditorShellBuilder(EntityEditorStyleProvider styleProvider) {
        this.styleProvider = styleProvider;
    }

    public EntityEditorShell build(String titleKey, Supplier<Pane> itemsContainerFactory) {
        Label title = new Label(I18n.t(titleKey).toUpperCase());
        title.setStyle(styleProvider.titleStyle());

        VBox inputCard = new VBox(12);
        inputCard.setStyle(styleProvider.inputCardStyle());

        return new EntityEditorShell(title, inputCard, itemsContainerFactory.get());
    }

    public VBox defaultItemsContainer() {
        VBox vBox = new VBox(8);
        vBox.setPadding(new Insets(10, 0, 0, 0));
        return vBox;
    }
}












