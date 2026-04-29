package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.lang.I18n;

public class SkillSourceBadgeViewModelFactory {

    public SkillSourceBadgeViewModel create(InventoryItem sourceItem) {
        boolean fromItem = sourceItem != null;
        return new SkillSourceBadgeViewModel(
                fromItem ? "\uD83D\uDCE6" : "\uD83D\uDC64",
                fromItem ? "#8fb8c9" : "#20283c",
                fromItem ? "#10172a" : "#dfe6ec",
                fromItem ? sourceItem.getName() : I18n.t("skill.source.innate")
        );
    }
}












