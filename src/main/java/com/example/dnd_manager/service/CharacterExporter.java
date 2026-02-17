package com.example.dnd_manager.service;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.stats.StatEnum;

import java.util.stream.Collectors;

public class CharacterExporter {

    public static String generateFullDescription(Character c) {
        StringBuilder sb = new StringBuilder();

        sb.append("Имя: ").append(c.getName()).append("\n\n");
        sb.append(c.getDescription()).append("\n\n");

        sb.append("Раса/Класс: ").append(c.getRace()).append("-").append(c.getCharacterClass()).append("\n");
        sb.append("Уровень: ").append(c.getLevel()).append("\n");
        sb.append("Здоровье: ").append(c.getCurrentHp()).append("/").append(c.getMaxHp()).append("\n");
        sb.append("Броня: ").append(c.getArmor()).append("\n\n");

        sb.append("--- ХАРАКТЕРИСТИКИ ---\n");
        sb.append(String.format("%-15s %s%n", "Сила:", formatStat(c.getStats().get(StatEnum.STRANGE))));
        sb.append(String.format("%-15s %s%n", "Ловкость:", formatStat(c.getStats().get(StatEnum.AGILITY))));
        sb.append(String.format("%-15s %s%n", "Выносливость:", formatStat(c.getStats().get(StatEnum.ENDURANCE))));
        sb.append(String.format("%-15s %s%n", "Интеллект:", formatStat(c.getStats().get(StatEnum.INTELLIGENCE))));
        sb.append(String.format("%-15s %s%n", "Мудрость:", formatStat(c.getStats().get(StatEnum.WISDOM))));
        sb.append(String.format("%-15s %s%n", "Харизма:", formatStat(c.getStats().get(StatEnum.CHARISMA))));
        sb.append("\n");

        sb.append("--- ВНЕШНОСТЬ ---\n");
        sb.append(c.getPersonality()).append("\n\n");

        sb.append("--- ПРЕДЫСТОРИЯ ---\n");
        sb.append(c.getBackstory()).append("\n\n");

        sb.append("--- БАФЫ / ДЕБАФЫ ---\n");
        for (Buff buff : c.getBuffs()) {
            sb.append("[").append(buff.type()).append("] ").append(buff.name()).append("\n");
            sb.append(buff.description()).append("\n\n");
        }

        sb.append("--- ИНВЕНТАРЬ ---\n");
        for (InventoryItem item : c.getInventory()) {
            sb.append("- ").append(item.getName()).append(": ").append(item.getDescription()).append("\n");
        }

        sb.append("--- СПОСОБНОСТИ ---\n");
        for (Skill skill : c.getSkills()) {
            sb.append("• ").append(skill.name()).append(" (").append(skill.activationType()).append(")\n");
            sb.append(skill.description()).append("\n\n");
        }

        return sb.toString();
    }

    private static String formatStat(int value) {
        return value >= 0 ? "+" + value : String.valueOf(value);
    }
}
