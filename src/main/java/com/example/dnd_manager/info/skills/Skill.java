package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.info.familiar.Displayable;
import com.example.dnd_manager.lang.I18n;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a character skill with combat and activation properties.
 *
 * @param name           skill name
 * @param description    detailed skill description
 * @param effects        effect value
 * @param activationType how the skill is activated
 * @param iconPath       path to skill icon
 */
public record Skill(String name, String description, List<SkillEffect> effects, String activationType,
                    String iconPath) implements Displayable {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getIconPath() {
        return iconPath;
    }

    @Override
    public String getTypeName() {
        return "SKILL";
    }

    @Override
    public String getAccentColor() {
        return "#9c27b0";
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = new LinkedHashMap<>();
        attr.put(I18n.t("skill.attrActivation"), activationType);
        attr.put(I18n.t("skill.attrEffects"), effectsSummary());
        return attr;
    }

    public String effectsSummary() {
        StringBuilder sb = new StringBuilder();
        for (SkillEffect effect : effects) {
            if (effects.size() == 1) {
                sb.append(effect.toString());
            } else {
                sb.append(effect.toString()).append("; ");
            }
        }
        return sb.toString();
    }
}
