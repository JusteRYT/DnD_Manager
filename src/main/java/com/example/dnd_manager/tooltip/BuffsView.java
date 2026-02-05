package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.buff_debuff.BuffColumnStyle;
import com.example.dnd_manager.info.buff_debuff.BuffListView;
import com.example.dnd_manager.info.buff_debuff.BuffType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

/**
 * Panel displaying buffs and debuffs side by side.
 */
public class BuffsView extends HBox {

    public BuffsView(Character character) {
              List<Buff> buffs = character.getBuffs();

        BuffListView buffsView = new BuffListView(
                "BUFFS",
                buffs.stream().filter(b -> b.type() == BuffType.BUFF).toList(),
                BuffColumnStyle.BUFF, character.getName()
        );

        BuffListView debuffsView = new BuffListView(
                "DEBUFFS",
                buffs.stream().filter(b -> b.type() == BuffType.DEBUFF).toList(),
                BuffColumnStyle.DEBUFF,
                character.getName()
        );

        HBox.setHgrow(buffsView, Priority.ALWAYS);
        HBox.setHgrow(debuffsView, Priority.ALWAYS);

        getChildren().addAll(buffsView, debuffsView);
    }
}