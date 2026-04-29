package com.example.dnd_manager.info.familiar;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.common.AbstractEntityRow;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FamiliarEditorRow extends AbstractEntityRow<Character> {

    public FamiliarEditorRow(Character familiar, Runnable onRemove, Runnable onEdit, Character owner) {
        super(familiar, onRemove, onEdit, owner);
        applyRowAccent(
                "rgba(154, 132, 190, 0.20)",
                "rgba(126, 110, 168, 0.34)",
                "rgba(183, 162, 220, 0.56)"
        );
    }

    @Override
    protected void fillContent(VBox container, Character familiar) {
        Label nameLabel = createTitleLabel(familiar.getName(), "#ede7f7", "rgba(154, 132, 190, 0.26)");

        // Описание (Раса / Класс)
        String subText = String.format("%s | %s",
                familiar.getRace() != null ? familiar.getRace() : "---",
                familiar.getCharacterClass() != null ? familiar.getCharacterClass() : "---"
        );
        Label subLabel = createMetaLabel(subText);

        Label hpChip = createChip("HP " + familiar.getMaxHp(), "rgba(30, 50, 74, 0.62)", "rgba(105, 151, 174, 0.44)", "#d9eef6");
        Label armorChip = createChip("AC " + familiar.getArmor(), "rgba(42, 38, 68, 0.62)", "rgba(154, 132, 190, 0.44)", "#ece4fb");
        HBox statsRow = new HBox(8, hpChip, armorChip);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        container.getChildren().addAll(nameLabel, subLabel, statsRow);
    }

    @Override
    protected String getIconPath(Character familiar) {
        return familiar.getAvatarImage();
    }
}











