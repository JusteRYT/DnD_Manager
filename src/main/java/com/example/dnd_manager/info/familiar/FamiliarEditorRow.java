package com.example.dnd_manager.info.familiar;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.AbstractEntityRow;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FamiliarEditorRow extends AbstractEntityRow<Character> {

    public FamiliarEditorRow(Character familiar, Runnable onRemove, Runnable onEdit, Character owner) {
        super(familiar, onRemove, onEdit, owner);
    }

    @Override
    protected void fillContent(VBox container, Character familiar) {
        Label nameLabel = new Label(familiar.getName());
        nameLabel.setStyle("-fx-text-fill: #f0f0f0; -fx-font-weight: bold; -fx-font-size: 14px;");

        // Описание (Раса / Класс)
        String subText = String.format("%s | %s",
                familiar.getRace() != null ? familiar.getRace() : "---",
                familiar.getCharacterClass() != null ? familiar.getCharacterClass() : "---"
        );
        Label subLabel = new Label(subText);
        subLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");

        Label statsLabel = new Label(String.format("HP: %d | AC: %d", familiar.getMaxHp(), familiar.getArmor()));
        statsLabel.setStyle("-fx-text-fill: #3aa3c3; -fx-font-size: 11px; -fx-font-family: 'Consolas';");

        container.getChildren().addAll(nameLabel, subLabel, statsLabel);
    }

    @Override
    protected String getIconPath(Character familiar) {
        return familiar.getAvatarImage();
    }
}