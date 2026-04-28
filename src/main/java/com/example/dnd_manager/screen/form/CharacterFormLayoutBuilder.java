package com.example.dnd_manager.screen.form;

import com.example.dnd_manager.theme.SectionBox;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CharacterFormLayoutBuilder {

    private final CharacterFormStyleProvider styleProvider;
    private final MagicalBorderDecorator borderDecorator;

    public CharacterFormLayoutBuilder(
            CharacterFormStyleProvider styleProvider,
            MagicalBorderDecorator borderDecorator
    ) {
        this.styleProvider = styleProvider;
        this.borderDecorator = borderDecorator;
    }

    public VBox build(CharacterFormComponents components, HBox heroCard, HBox actionButtons) {
        VBox form = new VBox(30);
        form.setPadding(new Insets(30));
        form.setStyle(styleProvider.formStyle());

        borderDecorator.apply(heroCard);
        form.getChildren().addAll(
                heroCard,
                wrapInPanel(components.descriptionSection()),
                wrapInPanel(components.familiarsSection()),
                wrapInPanel(components.buffEditor()),
                wrapInPanel(components.inventoryEditor()),
                wrapInPanel(components.skillsEditor()),
                actionButtons
        );
        return form;
    }

    public Pane wrapInPanel(Node content) {
        SectionBox box = new SectionBox(content);
        borderDecorator.apply(box);
        return box;
    }
}












