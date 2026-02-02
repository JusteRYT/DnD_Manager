package com.example.dnd_manager.info.stats;

/**
 * Represents base character statistics.
 */
public class Stats {

    private int strength = 0;
    private int dexterity = 0;
    private int constitution = 0;
    private int intelligence = 0;
    private int wisdom = 0;
    private int charisma = 0;

    public int getStrength() {
        return strength;
    }

    public void increaseStrength() {
        strength++;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void increaseDexterity() {
        dexterity++;
    }

    public int getConstitution() {
        return constitution;
    }

    public void increaseConstitution() {
        constitution++;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void increaseIntelligence() {
        intelligence++;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void increaseWisdom() {
        wisdom++;
    }

    public int getCharisma() {
        return charisma;
    }

    public void increaseCharisma() {
        charisma++;
    }
    public Stats copyForm(Stats stats) {
        Stats newStats = new Stats();
        newStats.strength = stats.strength;
        newStats.dexterity = stats.dexterity;
        newStats.constitution = stats.constitution;
        newStats.intelligence = stats.intelligence;
        newStats.wisdom = stats.wisdom;
        newStats.charisma = stats.charisma;
        return newStats;
    }
}
