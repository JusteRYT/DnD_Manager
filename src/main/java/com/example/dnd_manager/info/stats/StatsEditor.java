package com.example.dnd_manager.info.stats;

import javafx.scene.layout.VBox;

/**
 * Editor component for character statistics.
 */
public class StatsEditor extends VBox {

    private final Stats stats;

    public StatsEditor(Stats stats) {
        this.stats = stats;
        setSpacing(8);

        StatRow strength = new StatRow(
                "Strength",
                stats.getStrength(),
                stats::increaseStrength
        );
        strength.updateValue(stats.getStrength());

        StatRow dexterity = new StatRow(
                "Dexterity",
                stats.getDexterity(),
                stats::increaseDexterity
        );

        StatRow constitution = new StatRow(
                "Constitution",
                stats.getConstitution(),
                stats::increaseConstitution
        );
        constitution.updateValue(stats.getConstitution());

        StatRow intelligence = new StatRow(
                "Intelligence",
                stats.getIntelligence(),
                stats::increaseIntelligence
        );
        intelligence.updateValue(stats.getIntelligence());

        StatRow wisdom = new StatRow(
                "Wisdom",
                stats.getWisdom(),
                stats::increaseWisdom
        );
        wisdom.updateValue(stats.getWisdom());

        StatRow charisma = new StatRow(
                "Charisma",
                stats.getCharisma(),
                stats::increaseCharisma
        );
        charisma.updateValue(stats.getCharisma());

        getChildren().addAll(
                strength,
                dexterity,
                constitution,
                intelligence,
                wisdom,
                charisma
        );
    }
}
