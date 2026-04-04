package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.*;
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
        setSpacing(10);

        List<BuffWithSource> allBuffsWithSources = new ArrayList<>();

        for (Buff buff : character.getBuffs()) {
            allBuffsWithSources.add(new BuffWithSource(buff, null));
        }

        for (InventoryItem item : character.getInventory()) {
            if (item.getAttachedBuffs() != null) {
                for (Buff buff : item.getAttachedBuffs()) {
                    allBuffsWithSources.add(new BuffWithSource(buff, item));
                }
            }
        }

        var buffsOnly = allBuffsWithSources.stream()
                .filter(b -> Objects.equals(b.buff().type(), BuffType.BUFF.getName()))
                .toList();

        var debuffsOnly = allBuffsWithSources.stream()
                .filter(b -> Objects.equals(b.buff().type(), BuffType.DEBUFF.getName()))
                .toList();

        BuffListView buffsListView = new BuffListView(
                I18n.t("buffsView.titleBuff"),
                buffsOnly,
                BuffColumnStyle.BUFF,
                character.getName()
        );

        BuffListView debuffsListView = new BuffListView(
                I18n.t("buffsView.titleDebuff"),
                debuffsOnly,
                BuffColumnStyle.DEBUFF,
                character.getName()
        );

        HBox.setHgrow(buffsListView, Priority.ALWAYS);
        HBox.setHgrow(debuffsListView, Priority.ALWAYS);
        getChildren().addAll(buffsListView, debuffsListView);
    }
}