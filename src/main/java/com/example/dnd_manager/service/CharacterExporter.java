package com.example.dnd_manager.service;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.stats.StatEnum;
import com.example.dnd_manager.lang.I18n;

import java.util.List;
import java.util.stream.Collectors;

public class CharacterExporter {

    private static final String SEPARATOR = "========================================\n";
    private static final String SUB_SEPARATOR = "----------------------------------------\n";

    public static String generateFullDescription(Character c) {
        StringBuilder sb = new StringBuilder();

        // ШАПКА ПЕРСОНАЖА
        sb.append(SEPARATOR);
        sb.append(String.format("   %s: %s %n", I18n.t("textFieldLabel.name"), c.getName().toUpperCase()));
        sb.append(SEPARATOR);

        sb.append(String.format("%s/%s: %s-%s%n",
                I18n.t("raceField.name"), I18n.t("classField.name"), c.getRace(), c.getCharacterClass()));

        sb.append(String.format("%s: %d | %s: %d/%d | %s: %d/%d%n",
                I18n.t("levelField.name"), c.getLevel(),
                I18n.t("hpField.name"), c.getCurrentHp(), c.getMaxHp(),
                I18n.t("manaField.name"), c.getCurrentMana(), c.getMaxMana()));

        sb.append(String.format("%s: %d%n%n", I18n.t("armorField.name"), c.getArmor()));

        if (c.getDescription() != null && !c.getDescription().isEmpty()) {
            sb.append("📜 ").append(c.getDescription()).append("\n\n");
        }

        // ХАРАКТЕРИСТИКИ
        appendStats(sb, c);

        // БАФФЫ И ЭФФЕКТЫ
        sb.append("✨ ").append(I18n.t("label.buffsEditor")).append("\n");
        sb.append(SUB_SEPARATOR);

        // Для личных баффов используем ключ "Character" или имя персонажа
        if (!c.getBuffs().isEmpty()) {
            appendBuffs(sb, c.getBuffs(), c.getName());
        }
        for (InventoryItem item : c.getInventory()) {
            if (!item.getAttachedBuffs().isEmpty()) {
                appendBuffs(sb, item.getAttachedBuffs(), item.getName());
            }
        }
        sb.append("\n");

        // ИНВЕНТАРЬ
        sb.append("🎒 ").append(I18n.t("label.inventoryEditor")).append("\n");
        sb.append(SUB_SEPARATOR);
        for (InventoryItem item : c.getInventory()) {
            String countSuffix = item.getCount() > 1 ? " (x" + item.getCount() + ")" : "";
            sb.append(String.format(" • %-20s — %s%n", item.getName() + countSuffix, item.getDescription()));
        }
        sb.append("\n");

        // СПОСОБНОСТИ
        sb.append("⚔️ ").append(I18n.t("label.skillsEditor")).append("\n");
        sb.append(SUB_SEPARATOR);

        // Личные навыки
        appendSkills(sb, c.getSkills(), c.getName());

        // Навыки от предметов
        for (InventoryItem item : c.getInventory()) {
            if (!item.getAttachedSkills().isEmpty()) {
                appendSkills(sb, item.getAttachedSkills(), item.getName());
            }
        }

        // ФАМИЛЬЯРЫ
        if (!c.getFamiliars().isEmpty()) {
            sb.append("\n🐾 ").append(I18n.t("label.familiars")).append("\n");
            for (Character familiar : c.getFamiliars()) {
                sb.append(SEPARATOR);
                sb.append(String.format(" >>> %s: %s <<<%n", I18n.t("familiar.prompt.name"), familiar.getName()));
                sb.append(String.format(" %s: %d/%d | %s: %d%n",
                        I18n.t("hpField.name"), familiar.getCurrentHp(), familiar.getMaxHp(),
                        I18n.t("armorField.name"), familiar.getArmor()));
                appendStats(sb, familiar);
                appendSkills(sb, familiar.getSkills(), I18n.t("label.familiarsSKILLS"));
            }
        }

        return sb.toString();
    }

    private static void appendStats(StringBuilder sb, Character c) {
        sb.append(String.format(" [%s] %n", I18n.t("stats.label")));

        // Используем конкретные ключи для каждой характеристики
        sb.append(String.format("  %s: %-4s  %s: %-4s  %s: %-4s%n",
                I18n.t("stats.strange"), formatStat(c.getStats().get(StatEnum.STRANGE)),
                I18n.t("stats.agility"), formatStat(c.getStats().get(StatEnum.AGILITY)),
                I18n.t("stats.endurance"), formatStat(c.getStats().get(StatEnum.ENDURANCE))));

        sb.append(String.format("  %s: %-4s  %s: %-4s  %s: %-4s%n",
                I18n.t("stats.intelligence"), formatStat(c.getStats().get(StatEnum.INTELLIGENCE)),
                I18n.t("stats.wisdom"), formatStat(c.getStats().get(StatEnum.WISDOM)),
                I18n.t("stats.charisma"), formatStat(c.getStats().get(StatEnum.CHARISMA))));
        sb.append("\n");
    }

    private static void appendSkills(StringBuilder sb, List<Skill> skills, String sourceName) {
        if (skills.isEmpty()) return;

        for (Skill skill : skills) {
            String actTypeKey = "skill.activationType." + skill.activationType().toLowerCase();

            // Формат: [ИСТОЧНИК] НАЗВАНИЕ (Тип активации)
            sb.append(String.format(" [%s] %s (%s)%n",
                    sourceName.toUpperCase(),
                    skill.name().toUpperCase(),
                    I18n.t(actTypeKey)));

            sb.append("    └ ").append(skill.description()).append("\n");

            if (skill.effects() != null && !skill.effects().isEmpty()) {
                String effectsStr = skill.effects().stream()
                        .map(eff -> {
                            String typeLabel = (eff.getCustomName() != null)
                                    ? eff.getCustomName()
                                    : I18n.t("skill.effectType." + eff.getType().toLowerCase());
                            return typeLabel + ": " + eff.getValue();
                        })
                        .collect(Collectors.joining(", "));
                sb.append("    ✳ ").append(I18n.t("skill.attrEffects")).append(": ").append(effectsStr).append("\n");
            }
            sb.append("\n");
        }
    }

    private static void appendBuffs(StringBuilder sb, List<Buff> buffs, String sourceName) {
        for (Buff buff : buffs) {
            String typeKey = buff.type().equalsIgnoreCase("BUFF") ? "buffType.buffName" : "buffType.debuffName";
            // Формат: [ТИП] Название: Описание (Источник)
            sb.append(String.format(" [%s] %s: %s (%s)%n",
                    I18n.t(typeKey), buff.name(), buff.description(), sourceName));
        }
    }

    private static String formatStat(int value) {
        return value >= 0 ? "+" + value : String.valueOf(value);
    }
}