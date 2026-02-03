package com.example.dnd_manager.info.stats;

import lombok.Getter;
import lombok.Setter;

public enum StatEnum {
    STRANGE("Strange"),
    AGILITY("Agility"),
    ENDURANCE("Endurance"),
    INTELLIGENCE("Intelligence"),
    WISDOM("Wisdom"),
    CHARISMA("Charisma"),;

    @Getter
    @Setter
    private String name;

    StatEnum(String name) {
        this.name = name;
    }
}
