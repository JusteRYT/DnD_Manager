package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.domain.Character;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class FamiliarSectionBuilder {

    private final FamiliarResourcesSectionBuilder resourcesSectionBuilder;
    private final FamiliarStatsSectionBuilder statsSectionBuilder;
    private final FamiliarIconListsSectionBuilder iconListsSectionBuilder;
    private final FamiliarLoreSectionAppender loreSectionAppender;

    public FamiliarSectionBuilder() {
        this(
                new FamiliarResourcesSectionBuilder(),
                new FamiliarStatsSectionBuilder(),
                new FamiliarIconListsSectionBuilder(),
                new FamiliarLoreSectionAppender()
        );
    }

    FamiliarSectionBuilder(
            FamiliarResourcesSectionBuilder resourcesSectionBuilder,
            FamiliarStatsSectionBuilder statsSectionBuilder,
            FamiliarIconListsSectionBuilder iconListsSectionBuilder,
            FamiliarLoreSectionAppender loreSectionAppender
    ) {
        this.resourcesSectionBuilder = Objects.requireNonNull(resourcesSectionBuilder, "resourcesSectionBuilder must not be null");
        this.statsSectionBuilder = Objects.requireNonNull(statsSectionBuilder, "statsSectionBuilder must not be null");
        this.iconListsSectionBuilder = Objects.requireNonNull(iconListsSectionBuilder, "iconListsSectionBuilder must not be null");
        this.loreSectionAppender = Objects.requireNonNull(loreSectionAppender, "loreSectionAppender must not be null");
    }

    public Node buildResources(Label hpVal, Label acVal, Label mpVal, Label lvlVal) {
        return resourcesSectionBuilder.build(hpVal, acVal, mpVal, lvlVal);
    }

    public Node buildStats(Character familiar) {
        return statsSectionBuilder.build(familiar);
    }

    public void addLore(VBox container, Character familiar) {
        loreSectionAppender.append(container, familiar);
    }

    public Node buildIconLists(Character familiar, Character character) {
        return iconListsSectionBuilder.build(familiar, character);
    }
}












