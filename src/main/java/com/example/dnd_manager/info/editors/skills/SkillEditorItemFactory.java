package com.example.dnd_manager.info.editors.skills;

import com.example.dnd_manager.info.skills.model.Skill;
import com.example.dnd_manager.info.skills.model.SkillEffect;
import com.example.dnd_manager.info.skills.model.ActivationType;

import java.util.ArrayList;
import java.util.List;

public class SkillEditorItemFactory {

    public Skill create(
            String name,
            String description,
            List<SkillEffect> effects,
            String activationType,
            String iconPath
    ) {
        return new Skill(name.trim(), description, new ArrayList<>(effects), ActivationType.canonical(activationType), iconPath);
    }
}












