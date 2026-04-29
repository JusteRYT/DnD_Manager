package com.example.dnd_manager.info.buff_debuff.view;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.buff_debuff.model.BuffType;
import com.example.dnd_manager.info.editors.common.AbstractEntityRow;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BuffEditorRow extends AbstractEntityRow<Buff> {

    public BuffEditorRow(Buff buff, Runnable onRemove, Runnable onEdit, Character character) {
        super(buff, onRemove, onEdit, character);
        boolean debuff = BuffType.DEBUFF.name().equals(BuffType.canonical(buff.type()));
        applyRowAccent(
                debuff ? "rgba(206, 93, 117, 0.20)" : "rgba(94, 173, 190, 0.18)",
                debuff ? "rgba(206, 93, 117, 0.34)" : "rgba(94, 173, 190, 0.32)",
                debuff ? "rgba(235, 126, 148, 0.58)" : "rgba(136, 204, 218, 0.54)"
        );
    }

    @Override
    protected String getIconPath(Buff item) {
        return item.iconPath();
    }

    @Override
    protected void fillContent(VBox container, Buff buff) {
        boolean debuff = BuffType.DEBUFF.name().equals(BuffType.canonical(buff.type()));
        String accent = debuff ? "#f0a0b0" : "#9dd6df";
        String glow = debuff ? "rgba(206, 93, 117, 0.24)" : "rgba(94, 173, 190, 0.24)";

        Label title = createTitleLabel(buff.name(), accent, glow);
        Label typeChip = createChip(
                BuffType.displayName(buff.type()),
                debuff ? "rgba(93, 31, 47, 0.58)" : "rgba(25, 60, 76, 0.58)",
                debuff ? "rgba(206, 93, 117, 0.48)" : "rgba(94, 173, 190, 0.44)",
                debuff ? "#ffdce3" : "#d7f5fb"
        );
        HBox titleRow = new HBox(8, title, typeChip);

        Label description = createDescriptionLabel(buff.description());

        container.getChildren().addAll(titleRow, description);
    }
}











