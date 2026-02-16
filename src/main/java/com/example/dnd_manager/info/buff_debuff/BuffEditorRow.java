package com.example.dnd_manager.info.buff_debuff;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.AbstractEntityRow;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BuffEditorRow extends AbstractEntityRow<Buff> {

    public BuffEditorRow(Buff buff, Runnable onRemove, Character character) {
        super(buff, onRemove, character);
    }

    @Override
    protected String getIconPath(Buff item) {
        return item.iconPath();
    }

    @Override
    protected void fillContent(VBox container, Buff buff) {
        Label title = new Label(buff.name() + " (" + buff.type() + ")");
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #c89b3c;");

        Label description = new Label(buff.description());
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: #e6e6e6;");

        container.getChildren().addAll(title, description);
    }
}