package com.example.dnd_manager.domain;


import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.stats.Stats;

import java.util.ArrayList;
import java.util.List;

/**
 * Root aggregate of character data.
 * Represents a DnD character with stats, skills, buffs and inventory.
 */
public class Character {

    private String name;
    private String race;
    private String characterClass;

    private String description;
    private String personality;
    private String backstory;

    private final Stats stats = new Stats();
    private final List<Skill> skills = new ArrayList<>();
    private final List<Buff> buffs = new ArrayList<>();
    private final List<InventoryItem> inventory = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public Stats getStats() {
        return stats;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getBackstory() {
        return backstory;
    }

    public void setBackstory(String backstory) {
        this.backstory = backstory;
    }
}
