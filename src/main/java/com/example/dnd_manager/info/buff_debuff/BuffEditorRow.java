package com.example.dnd_manager.info.buff_debuff;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * UI component representing a buff or debuff entry.
 */
public class BuffEditorRow extends VBox {

    public BuffEditorRow(Buff buff) {
        setSpacing(2);
        setStyle("-fx-border-color: gray; -fx-padding: 5;");

        Label title = new Label(buff.name() + " (" + buff.type() + ")");
        title.setStyle("-fx-font-weight: bold;");

        Label description = new Label(buff.description());
        description.setWrapText(true);

        setAlignment(Pos.TOP_LEFT);
        getChildren().addAll(title, description);
    }
}
