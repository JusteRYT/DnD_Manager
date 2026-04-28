package com.example.dnd_manager.screen.form;

import com.example.dnd_manager.info.avatar.AvatarPicker;
import com.example.dnd_manager.info.editors.buff.BuffEditor;
import com.example.dnd_manager.info.editors.inventory.InventoryEditor;
import com.example.dnd_manager.info.editors.skills.SkillsEditor;
import com.example.dnd_manager.info.section.FamiliarsSection;
import com.example.dnd_manager.info.stats.editor.StatsEditor;
import com.example.dnd_manager.info.text.BaseInfoForm;
import com.example.dnd_manager.info.text.CharacterDescriptionSection;

public record CharacterFormComponents(
        AvatarPicker avatarPicker,
        BaseInfoForm baseInfoForm,
        StatsEditor statsEditor,
        CharacterDescriptionSection descriptionSection,
        BuffEditor buffEditor,
        InventoryEditor inventoryEditor,
        SkillsEditor skillsEditor,
        FamiliarsSection familiarsSection
) {
}












