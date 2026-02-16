package com.example.dnd_manager.info.inventory;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.AbstractEntityRow;
import com.example.dnd_manager.lang.I18n;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InventoryRow extends AbstractEntityRow<InventoryItem> {

    public InventoryRow(InventoryItem item, Runnable onDelete, Character character) {
        super(item, onDelete, character);
    }

    @Override
    protected String getIconPath(InventoryItem item) {
        return item.getIconPath();
    }

    @Override
    protected void fillContent(VBox container, InventoryItem item) {
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #c89b3c;");

        Label countLabel = new Label(I18n.t("textField.showInventoryCount") + item.getCount());
        countLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #a0a0a0;");

        Label descLabel = new Label(item.getDescription());
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #e6e6e6; -fx-font-size: 12px;");

        container.getChildren().addAll(nameLabel, countLabel, descLabel);
    }
}