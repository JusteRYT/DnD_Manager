package com.example.dnd_manager.info.skills;

/**
 * Represents a character skill with combat and activation properties.
 *
 * @param name skill name
 * @param description detailed skill description
 * @param damage damage or effect value
 * @param activationType how the skill is activated
 * @param iconPath path to skill icon
 */
public record Skill(String name, String description, String damage, ActivationType activationType, String iconPath) {

}
