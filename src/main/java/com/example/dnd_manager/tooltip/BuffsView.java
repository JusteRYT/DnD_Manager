package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.info.buff_debuff.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

/**
 * Panel displaying buffs and debuffs side by side.
 */
public class BuffsView extends HBox {

    public BuffsView(List<Buff> buffs) {
        setSpacing(16);
        setPadding(new Insets(12));
        setAlignment(Pos.TOP_LEFT);

        setStyle("""
            -fx-background-color: #252526;
            -fx-background-radius: 8;
        """);

        BuffListView buffsView = new BuffListView(
                "BUFFS",
                buffs.stream().filter(b -> b.type() == BuffType.BUFF).toList(),
                BuffColumnStyle.BUFF
        );

        BuffListView debuffsView = new BuffListView(
                "DEBUFFS",
                buffs.stream().filter(b -> b.type() == BuffType.DEBUFF).toList(),
                BuffColumnStyle.DEBUFF
        );

        HBox.setHgrow(buffsView, Priority.ALWAYS);
        HBox.setHgrow(debuffsView, Priority.ALWAYS);

        getChildren().addAll(buffsView, debuffsView);
    }
}