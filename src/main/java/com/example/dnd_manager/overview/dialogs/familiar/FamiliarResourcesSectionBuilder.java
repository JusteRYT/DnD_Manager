package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class FamiliarResourcesSectionBuilder {

    private final FamiliarSectionStyleProvider styleProvider;

    public FamiliarResourcesSectionBuilder() {
        this(new FamiliarSectionStyleProvider());
    }

    FamiliarResourcesSectionBuilder(FamiliarSectionStyleProvider styleProvider) {
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
    }

    public Node build(Label hpVal, Label acVal, Label mpVal, Label lvlVal) {
        FlowPane pane = new FlowPane(15, 10);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle(styleProvider.resourcesContainerStyle());

        pane.getChildren().addAll(
                createResBox("label.familiarsHP", hpVal),
                createResBox("label.familiarsAC", acVal),
                createResBox("label.familiarsMP", mpVal),
                createResBox("label.familiarsLVL", lvlVal)
        );
        return pane;
    }

    private VBox createResBox(String labelKey, Label valueLabel) {
        Label title = new Label(I18n.t(labelKey));
        title.setStyle(styleProvider.resourceTitleStyle());
        VBox box = new VBox(-2, title, valueLabel);
        box.setAlignment(Pos.CENTER);
        box.setMinWidth(55);
        return box;
    }
}












