package com.example.dnd_manager.screen.form;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.avatar.AvatarPicker;
import com.example.dnd_manager.info.editors.buff.BuffEditor;
import com.example.dnd_manager.info.editors.inventory.InventoryEditor;
import com.example.dnd_manager.info.editors.skills.SkillsEditor;
import com.example.dnd_manager.info.section.FamiliarsSection;
import com.example.dnd_manager.info.stats.editor.StatsEditor;
import com.example.dnd_manager.info.text.BaseInfoForm;
import com.example.dnd_manager.info.text.CharacterDescriptionSection;
import com.example.dnd_manager.info.text.dto.BaseInfoData;
import com.example.dnd_manager.info.text.dto.CharacterDescriptionData;
import com.example.dnd_manager.screen.FormMode;
import javafx.stage.Stage;

public class CharacterFormComponentFactory {

    public CharacterFormComponents create(Stage stage, Character character, FormMode mode) {
        if (mode == FormMode.CREATE) {
            return createModeComponents(stage, character);
        }
        return editModeComponents(stage, character);
    }

    private CharacterFormComponents createModeComponents(Stage stage, Character character) {
        return new CharacterFormComponents(
                new AvatarPicker(),
                new BaseInfoForm(),
                new StatsEditor(character.getStats(), FormMode.CREATE),
                new CharacterDescriptionSection(),
                new BuffEditor(null),
                new InventoryEditor(null),
                new SkillsEditor(null),
                new FamiliarsSection(stage, null)
        );
    }

    private CharacterFormComponents editModeComponents(Stage stage, Character character) {
        FamiliarsSection familiarsSection = new FamiliarsSection(stage, character);
        familiarsSection.getItems().addAll(character.getFamiliars());
        familiarsSection.refreshList();

        return new CharacterFormComponents(
                new AvatarPicker(character),
                new BaseInfoForm(FormMode.EDIT, baseInfoData(character)),
                new StatsEditor(character.getStats(), FormMode.EDIT),
                new CharacterDescriptionSection(FormMode.EDIT, descriptionData(character)),
                new BuffEditor(character),
                new InventoryEditor(character),
                new SkillsEditor(character),
                familiarsSection
        );
    }

    private BaseInfoData baseInfoData(Character character) {
        return new BaseInfoData(
                character.getName(),
                character.getRace(),
                character.getCharacterClass(),
                character.getCurrentHp(),
                character.getArmor(),
                character.getMaxMana(),
                character.getLevel()
        );
    }

    private CharacterDescriptionData descriptionData(Character character) {
        return new CharacterDescriptionData(
                character.getDescription(),
                character.getPersonality(),
                character.getBackstory()
        );
    }
}












