package com.example.dnd_manager.info.skills;

import java.util.List;

/**
 * Represents a character skill with combat and activation properties.
 *
 * @param name skill name
 * @param description detailed skill description
 * @param effects effect value
 * @param activationType how the skill is activated
 * @param iconPath path to skill icon
 */
public record Skill(String name, String description, List<SkillEffect> effects, String activationType, String iconPath) {

    public String effectsSummary() {
        StringBuilder sb = new StringBuilder();
        for (SkillEffect effect : effects) {
            if (effects.size() == 1){
                sb.append(effect.toString());
            } else {
                sb.append(effect.toString()).append("; ");
            }
        }
        return sb.toString();
    }
}
