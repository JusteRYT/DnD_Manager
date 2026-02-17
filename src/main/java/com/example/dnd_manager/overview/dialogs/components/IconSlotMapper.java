package com.example.dnd_manager.overview.dialogs.components;

import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.lang.I18n;

import java.util.Map;

/**
 * Maps domain objects to IconSlotViewModel.
 */
public class IconSlotMapper {

    /**
     * Maps Skill to view model.
     */
    public static IconSlotViewModel fromSkill(Skill skill) {
        return new IconSlotViewModel(
                skill.name(),
                skill.iconPath(),
                "#4CAF50",
                "",
                I18n.t("label.familiarsSKILL"),
                Map.of(
                        I18n.t("skill.attrActivation"), skill.activationType(),
                        I18n.t("skill.attrEffects"), skill.effectsSummary()
                ),
                skill.description()
        );
    }

    public static IconSlotViewModel fromBuff(Buff buff) {
        return new IconSlotViewModel(
                buff.name(),
                buff.iconPath(),
                getAccentColor(buff.type()),
                "",
                buff.type().toUpperCase(),
                Map.of(),
                buff.description()
        );
    }

    public static IconSlotViewModel fromInventoryItem(InventoryItem item) {
        return new IconSlotViewModel(
                item.getName(),
                item.getIconPath(),
                "#ffd43b",
                item.getCount() > 1 ? "x" + item.getCount() : null,
                I18n.t("label.familiarITEM"),
                Map.of(
                        I18n.t("textField.inventoryCountPrompt"), String.valueOf(item.getCount())
                ),
                item.getDescription()
        );
    }

    private static String getAccentColor(String type) {
        return "BUFF".equalsIgnoreCase(type) ? "#69db7c" : "#ff6b6b";
    }

}
