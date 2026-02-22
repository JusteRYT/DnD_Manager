package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.buff_debuff.BuffColumnStyle;
import com.example.dnd_manager.info.buff_debuff.BuffListView;
import com.example.dnd_manager.info.buff_debuff.BuffType;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Panel displaying buffs and debuffs side by side.
 */
public class BuffsView extends HBox {

    public BuffsView(Character character) {
         refresh(character);
    }

    public void refresh(Character character) {
        getChildren().clear();
        List<Buff> buffs = new ArrayList<>(character.getBuffs());
        for (InventoryItem item : character.getInventory()) {
            buffs.addAll(item.getAttachedBuffs());
        }

        BuffListView buffsListView = new BuffListView(
                I18n.t("buffsView.titleBuff"),
                buffs.stream().filter(b -> Objects.equals(b.type(), BuffType.BUFF.getName())).toList(),
                BuffColumnStyle.BUFF, character.getName()
        );

        BuffListView debuffsListView = new BuffListView(
                I18n.t("buffsView.titleDebuff"),
                buffs.stream().filter(b -> Objects.equals(b.type(), BuffType.DEBUFF.getName())).toList(),
                BuffColumnStyle.DEBUFF,
                character.getName()
        );

        HBox.setHgrow(buffsListView, Priority.ALWAYS);
        HBox.setHgrow(debuffsListView, Priority.ALWAYS);
        getChildren().addAll(buffsListView, debuffsListView);
    }
}