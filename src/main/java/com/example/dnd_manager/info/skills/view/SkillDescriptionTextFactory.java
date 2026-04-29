package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.skills.model.Skill;
import com.example.dnd_manager.lang.I18n;

public class SkillDescriptionTextFactory {

    public String briefText(Skill skill) {
        return I18n.t("skill.attrActivation") + ": " + skill.activationDisplayName() + "\n" + skill.description();
    }

    public boolean needsScroll(String description) {
        return description != null && description.length() >= 400;
    }
}













