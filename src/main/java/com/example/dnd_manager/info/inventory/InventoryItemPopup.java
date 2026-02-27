package com.example.dnd_manager.info.inventory;

import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Popup view for inventory item details.
 */
public class InventoryItemPopup extends VBox {

    public InventoryItemPopup(InventoryItem item) {
        setSpacing(10);
        setPadding(new Insets(12));
        setMaxWidth(300);

        setStyle("""
            -fx-background-color: #1e1e1e;
            -fx-background-radius: 12;
            -fx-border-color: #444444;
            -fx-border-radius: 12;
            -fx-border-width: 1.5;
        """);

        Label name = new Label(item.getName().toUpperCase());
        name.setStyle("-fx-text-fill: " +
                "#c89b3c; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px;");

        Label count = new Label(I18n.t("textField.showInventoryCount") + " " + item.getCount());
        count.setStyle("""
                    -fx-text-fill: #c89b3c;
                    -fx-font-weight: bold;
                """);

        getChildren().addAll(name, count);

        if (!item.getDescription().isEmpty()) {
            Label description = new Label(item.getDescription());
            description.setWrapText(true);
            description.setStyle("-fx-text-fill: #dddddd;");
            getChildren().addAll(description);
        }

        // Контейнер для скиллов и баффов
        HBox statsBox = new HBox(8);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(5, 0, 0, 0));

        int skillsCount = item.getAttachedSkills() != null ? item.getAttachedSkills().size() : 0;
        int buffsCount = item.getAttachedBuffs() != null ? item.getAttachedBuffs().size() : 0;

        if (skillsCount > 0) {
            statsBox.getChildren().add(createBadge(I18n.t("label.skillsEditor") + ": " + skillsCount, "#3d5afe"));
        }
        if (buffsCount > 0) {
            statsBox.getChildren().add(createBadge(I18n.t("textLabel.buffsItemInventory") + buffsCount, "#00e676"));
        }

        if (!statsBox.getChildren().isEmpty()) {
            getChildren().add(statsBox);
        }

    }

    /**
     * Создает красивый скругленный индикатор с эффектом свечения.
     */
    private Label createBadge(String text, String color) {
        Label badge = new Label(text);

        // Стилизация через CSS
        badge.setStyle(String.format("""
            -fx-background-color: rgba(0, 0, 0, 0.3);
            -fx-text-fill: %1$s;
            -fx-border-color: %1$s;
            -fx-border-radius: 20;
            -fx-background-radius: 20;
            -fx-padding: 2 8 2 8;
            -fx-font-size: 10px;
            -fx-font-weight: bold;
        """, color));

        // Эффект свечения (Glow)
        DropShadow glow = new DropShadow();
        glow.setRadius(10);
        glow.setColor(Color.web(color, 0.6));
        glow.setSpread(0.2);
        badge.setEffect(glow);

        return badge;
    }
}
