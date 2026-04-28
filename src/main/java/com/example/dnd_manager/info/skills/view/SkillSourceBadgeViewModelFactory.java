package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.lang.I18n;

public class SkillSourceBadgeViewModelFactory {

    public SkillSourceBadgeViewModel create(InventoryItem sourceItem) {
        boolean fromItem = sourceItem != null;
        return new SkillSourceBadgeViewModel(
                fromItem ? "\uD83D\uDCE6" : "\uD83D\uDC64",
                fromItem ? "#55ccff" : "#4a4a4a",
                fromItem ? "#1a1a1a" : "#c89b3c",
                fromItem ? sourceItem.getName() : I18n.t("skill.source.innate")
        );
    }
}












