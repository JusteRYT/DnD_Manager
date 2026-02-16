package com.example.dnd_manager.domain;


import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.stats.Stats;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Root aggregate of character data.
 * Represents a DnD character with stats, skills, buffs and inventory.
 */
@Getter
@Setter
public class Character {

    private String name;
    private String race;
    private String characterClass;
    private int hp;
    private int armor;
    private int currentMana;
    private int maxMana;
    private int level;

    private String description;
    private String personality;
    private String backstory;

    private final Stats stats = new Stats();
    private final List<Skill> skills = new ArrayList<>();
    private final List<Buff> buffs = new ArrayList<>();
    private final List<InventoryItem> inventory = new ArrayList<>();
    private final List<Character> familiars = new ArrayList<>();

    private String avatarImage;

    private int totalCooper = 0;
    private int inspiration = 0;

}
