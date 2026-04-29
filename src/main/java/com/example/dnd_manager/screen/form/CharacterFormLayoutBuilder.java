package com.example.dnd_manager.screen.form;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

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
        VBox form = new VBox(22);
        form.setPadding(new Insets(24, 28, 28, 28));
        form.setStyle(styleProvider.formStyle());

        heroCard.setEffect(borderDecorator.softGlow());
        actionButtons.setPadding(new Insets(14, 16, 14, 16));
        actionButtons.setStyle(styleProvider.actionBarStyle());

        VBox sectionDeck = buildSectionDeck(components);
        VBox.setVgrow(sectionDeck, Priority.ALWAYS);

        form.getChildren().addAll(
                heroCard,
                sectionDeck,
                actionButtons
        );
        return form;
    }

    private VBox buildSectionDeck(CharacterFormComponents components) {
        List<SectionEntry> sections = List.of(
                new SectionEntry("characterForm.tab.lore", components.descriptionSection()),
                new SectionEntry("characterForm.tab.effects", components.buffEditor()),
                new SectionEntry("characterForm.tab.inventory", components.inventoryEditor()),
                new SectionEntry("characterForm.tab.skills", components.skillsEditor()),
                new SectionEntry("characterForm.tab.familiars", components.familiarsSection())
        );

        HBox switcher = new HBox(10);
        switcher.setAlignment(Pos.CENTER_LEFT);
        switcher.setPadding(new Insets(10));
        switcher.setStyle(styleProvider.sectionSwitchStyle());

        StackPane contentStack = new StackPane();
        contentStack.setStyle(styleProvider.tabContentStyle());
        contentStack.setPadding(new Insets(10));

        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i < sections.size(); i++) {
            SectionEntry section = sections.get(i);
            Node content = section.content();
            content.setVisible(i == 0);
            content.setManaged(i == 0);
            contentStack.getChildren().add(content);

            Button button = sectionButton(section.titleKey(), i == 0);
            int index = i;
            button.setOnAction(e -> selectSection(index, buttons, contentStack.getChildren()));
            buttons.add(button);
            switcher.getChildren().add(button);
        }

        VBox deck = new VBox(14, switcher, contentStack);
        VBox.setVgrow(contentStack, Priority.ALWAYS);
        return deck;
    }

    private Button sectionButton(String titleKey, boolean selected) {
        Button button = new Button(com.example.dnd_manager.lang.I18n.t(titleKey));
        button.setMinHeight(40);
        button.setPrefHeight(40);
        button.setMaxHeight(40);
        button.setMinWidth(170);
        button.setPrefWidth(170);
        button.setMaxWidth(170);
        button.setFocusTraversable(false);
        button.setStyle(styleProvider.sectionSwitchButtonStyle(selected, false));
        button.setOnMouseEntered(e -> {
            if (!Boolean.TRUE.equals(button.getUserData())) {
                button.setStyle(styleProvider.sectionSwitchButtonStyle(false, true));
            }
        });
        button.setOnMouseExited(e -> {
            if (!Boolean.TRUE.equals(button.getUserData())) {
                button.setStyle(styleProvider.sectionSwitchButtonStyle(false, false));
            }
        });
        button.setUserData(selected);
        return button;
    }

    private void selectSection(int selectedIndex, List<Button> buttons, List<Node> contents) {
        for (int i = 0; i < buttons.size(); i++) {
            boolean selected = i == selectedIndex;
            Button button = buttons.get(i);
            button.setUserData(selected);
            button.setStyle(styleProvider.sectionSwitchButtonStyle(selected, false));

            Node content = contents.get(i);
            content.setVisible(selected);
            content.setManaged(selected);
        }
    }

    private record SectionEntry(String titleKey, Node content) {
    }

}












