package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.inventory.model.InventoryItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillSourceBadgeViewModelFactoryTest {

    private final SkillSourceBadgeViewModelFactory factory = new SkillSourceBadgeViewModelFactory();

    @Test
    void create_marksItemSource() {
        InventoryItem item = new InventoryItem("Ring", "desc", "icon.png");

        SkillSourceBadgeViewModel viewModel = factory.create(item);

        assertEquals("\uD83D\uDCE6", viewModel.iconText());
        assertEquals("#55ccff", viewModel.backgroundColor());
        assertEquals("Ring", viewModel.tooltipText());
    }

    @Test
    void create_marksInnateSource() {
        SkillSourceBadgeViewModel viewModel = factory.create(null);

        assertEquals("\uD83D\uDC64", viewModel.iconText());
        assertEquals("#4a4a4a", viewModel.backgroundColor());
    }
}













