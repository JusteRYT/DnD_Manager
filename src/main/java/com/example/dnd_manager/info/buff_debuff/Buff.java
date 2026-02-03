package com.example.dnd_manager.info.buff_debuff;

/**
 * Represents a buff or debuff applied to a character.
 */
public record Buff(String name, String description, BuffType type, String iconPath) {

}
