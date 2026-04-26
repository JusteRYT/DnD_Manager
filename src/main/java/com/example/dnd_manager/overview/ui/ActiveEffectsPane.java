package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Reusable pane for displaying active item effects of a character.
 */
public class ActiveEffectsPane extends VBox {

    public ActiveEffectsPane(Character character) {
        setSpacing(4);
        setPadding(new Insets(4, 0, 0, 0));
        rebuild(character);
    }

    public void rebuild(Character character) {
        getChildren().clear();

        List<InventoryItem> equippedItems = character.getInventory().stream()
                .filter(InventoryItem::isEquipped)
                .toList();

        FlowPane buffsPane = new FlowPane(6, 6);
        for (InventoryItem item : equippedItems) {
            if (item.getCustomEffectName() != null && !item.getCustomEffectName().isBlank()) {
                buffsPane.getChildren().add(createEffectLabel(item.getCustomEffectName(), null, character));
            } else {
                for (Buff buff : item.getAttachedBuffs()) {
                    buffsPane.getChildren().add(createEffectLabel(formatBuffText(buff), buff.iconPath(), character));
                }
            }
        }

        if (buffsPane.getChildren().isEmpty()) {
            return;
        }

        Label title = new Label(I18n.t("label.activeEffects"));
        title.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px; -fx-font-style: italic;");
        getChildren().addAll(title, buffsPane);
    }

    private Label createEffectLabel(String text, String iconPath, Character character) {
        Label label = new Label(text);
        label.setStyle("""
                    -fx-background-color: rgba(200, 155, 60, 0.15);
                    -fx-text-fill: #c89b3c;
                    -fx-padding: 2 6 2 6;
                    -fx-background-radius: 4;
                    -fx-border-color: rgba(200, 155, 60, 0.3);
                    -fx-border-radius: 4;
                    -fx-font-size: 11px;
                    -fx-font-weight: bold;
                """);

        if (iconPath != null && !iconPath.isBlank()) {
            ImageView icon = new ImageView(new Image(CharacterAssetResolver.resolve(character.getName(), iconPath)));
            icon.setFitWidth(12);
            icon.setFitHeight(12);
            label.setGraphic(icon);
        }
        return label;
    }

    private String formatBuffText(Buff buff) {
        return (buff.type() != null && !buff.type().isBlank())
                ? String.format("%s (%s)", buff.name(), buff.type())
                : buff.name();
    }
}

