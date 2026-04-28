package com.example.dnd_manager.info.skills.popup;

import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.info.skills.model.Skill;
import com.example.dnd_manager.info.skills.view.SkillCardStyleProvider;
import com.example.dnd_manager.info.skills.view.SkillDescriptionTextFactory;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.scroll.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SkillPopupContentBuilder {

    private static final double POPUP_WIDTH = 380;
    private static final double TEXT_WIDTH = 340;

    private final SkillCardStyleProvider styleProvider;
    private final SkillDescriptionTextFactory descriptionTextFactory;

    public SkillPopupContentBuilder(
            SkillCardStyleProvider styleProvider,
            SkillDescriptionTextFactory descriptionTextFactory
    ) {
        this.styleProvider = styleProvider;
        this.descriptionTextFactory = descriptionTextFactory;
    }

    public VBox build(
            Skill skill,
            InventoryItem sourceItem,
            Runnable onMouseEntered,
            Runnable onMouseExited
    ) {
        VBox popupContent = new VBox(0);
        popupContent.setPadding(new Insets(18));
        popupContent.setStyle(styleProvider.popupContainerStyle());
        popupContent.setPrefWidth(POPUP_WIDTH);
        popupContent.setMaxWidth(POPUP_WIDTH);
        popupContent.setEffect(new javafx.scene.effect.DropShadow(25, Color.BLACK));
        popupContent.setOnMouseEntered(e -> onMouseEntered.run());
        popupContent.setOnMouseExited(e -> onMouseExited.run());

        if (sourceItem != null) {
            popupContent.getChildren().add(sourceInfo(sourceItem));
        }
        popupContent.getChildren().add(descriptionNode(skill.description()));
        return popupContent;
    }

    private Label sourceInfo(InventoryItem sourceItem) {
        Label itemSourceInfo = new Label(I18n.t("skill.provided_by") + ": " + sourceItem.getName());
        itemSourceInfo.setStyle(styleProvider.sourceInfoStyle());
        VBox.setMargin(itemSourceInfo, new Insets(0, 0, 10, 0));
        return itemSourceInfo;
    }

    private javafx.scene.Node descriptionNode(String description) {
        Label fullDesc = new Label(description);
        fullDesc.setStyle(styleProvider.popupDescriptionStyle());
        fullDesc.setWrapText(true);
        fullDesc.setPrefWidth(TEXT_WIDTH);
        fullDesc.setMaxWidth(TEXT_WIDTH);

        if (!descriptionTextFactory.needsScroll(description)) {
            return fullDesc;
        }

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(fullDesc);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(600);
        scrollPane.setMaxHeight(750);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");
        return scrollPane;
    }
}












