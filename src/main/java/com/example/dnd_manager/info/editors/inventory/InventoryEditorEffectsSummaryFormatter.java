package com.example.dnd_manager.info.editors.inventory;

import com.example.dnd_manager.lang.I18n;

public class InventoryEditorEffectsSummaryFormatter {

    public String emptyText() {
        return I18n.t("inventoryEditor.effects.empty");
    }

    public String format(int buffsCount, int skillsCount) {
        return I18n.t("inventoryEditor.effects.attached")
                .formatted(Math.max(0, buffsCount), Math.max(0, skillsCount));
    }
}













