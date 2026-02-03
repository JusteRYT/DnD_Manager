package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.buff_debuff.BuffColumnStyle;
import com.example.dnd_manager.info.buff_debuff.BuffColumnView;
import com.example.dnd_manager.info.buff_debuff.BuffType;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * Displays buffs and debuffs in two separate columns.
 */
public class BuffsView extends HBox {

    public BuffsView(List<Buff> buffs) {
        setSpacing(16);
        setPadding(new Insets(8));

        BuffColumnView buffsColumn = new BuffColumnView(
                "Buffs",
                buffs.stream()
                        .filter(b -> b.type() == BuffType.BUFF)
                        .toList(),
                BuffColumnStyle.BUFF
        );

        BuffColumnView debuffsColumn = new BuffColumnView(
                "Debuffs",
                buffs.stream()
                        .filter(b -> b.type() == BuffType.DEBUFF)
                        .toList(),
                BuffColumnStyle.DEBUFF
        );

        getChildren().addAll(buffsColumn, debuffsColumn);
    }
}