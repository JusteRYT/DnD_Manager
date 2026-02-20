package com.example.dnd_manager.info.inventory;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.AbstractEntityRow;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InventoryRow extends AbstractEntityRow<InventoryItem> {

    private final Runnable onEditBuffs;
    private final Runnable onEditSkills;

    public InventoryRow(InventoryItem item, Runnable onDelete, Runnable onEdit, Runnable onEditBuffs, Runnable onEditSkills, Character character) {
        super(item, onDelete, onEdit, character);
        this.onEditBuffs = onEditBuffs;
        this.onEditSkills = onEditSkills;
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

        Button buffBtn = AppButtonFactory.addIcon("Buffs (" + item.getAttachedBuffs().size() + ")");
        Button skillBtn = AppButtonFactory.addIcon("Skills (" + item.getAttachedSkills().size() + ")");

        buffBtn.setOnAction(e -> onEditBuffs.run());
        skillBtn.setOnAction(e -> onEditSkills.run());

        HBox effectsBox = new HBox(8, buffBtn, skillBtn);
        effectsBox.setAlignment(Pos.CENTER_LEFT);
        effectsBox.setPadding(new javafx.geometry.Insets(5, 0, 0, 0));

        container.getChildren().addAll(nameLabel, countLabel, descLabel, effectsBox);
    }
}