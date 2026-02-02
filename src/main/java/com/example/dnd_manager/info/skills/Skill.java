package com.example.dnd_manager.info.skills;

/**
 * Represents a character skill.
 */
public record Skill(String name, String description, String damage, ActivationType activationType, String iconPath) {

}
