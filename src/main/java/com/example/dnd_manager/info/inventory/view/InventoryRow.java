package com.example.dnd_manager.info.inventory.view;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.common.AbstractEntityRow;
import com.example.dnd_manager.info.editors.common.EntityEditorButtonFactory;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.lang.I18n;
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
        applyRowAccent(
                "rgba(169, 185, 205, 0.18)",
                "rgba(96, 112, 142, 0.36)",
                "rgba(176, 190, 210, 0.56)"
        );
    }

    @Override
    protected String getIconPath(InventoryItem item) {
        return item.getIconPath();
    }

    @Override
    protected void fillContent(VBox container, InventoryItem item) {
        Label nameLabel = createTitleLabel(item.getName(), "#e9edf3", "rgba(175, 196, 216, 0.22)");

        Label countLabel = createChip(
                I18n.t("textField.showInventoryCount") + " " + item.getCount(),
                "rgba(35, 47, 72, 0.66)",
                "rgba(126, 143, 172, 0.42)",
                "#dbe4ee"
        );
        HBox titleRow = new HBox(8, nameLabel, countLabel);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label descLabel = createDescriptionLabel(item.getDescription());

        Button buffBtn = EntityEditorButtonFactory.arcaneBuff("Buffs (" + item.getAttachedBuffs().size() + ")", 120);
        Button skillBtn = EntityEditorButtonFactory.arcaneSkill("Skills (" + item.getAttachedSkills().size() + ")", 120);

        buffBtn.setOnAction(e -> onEditBuffs.run());
        skillBtn.setOnAction(e -> onEditSkills.run());

        HBox effectsBox = new HBox(8, buffBtn, skillBtn);
        effectsBox.setAlignment(Pos.CENTER_LEFT);
        effectsBox.setPadding(new javafx.geometry.Insets(5, 0, 0, 0));

        container.getChildren().addAll(titleRow, descLabel, effectsBox);
    }
}











