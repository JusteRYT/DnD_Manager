package com.example.dnd_manager.info.stats;

import com.example.dnd_manager.lang.I18n;
import lombok.Getter;

/**
 * Enumeration representing character base stats.
 * Stores only translation key, not translated value.
 */
public enum StatEnum {

    STRANGE("stats.strange"),
    AGILITY("stats.agility"),
    ENDURANCE("stats.endurance"),
    INTELLIGENCE("stats.intelligence"),
    WISDOM("stats.wisdom"),
    CHARISMA("stats.charisma");

    /**
     * Translation key used by I18n service.
     */
    @Getter
    private final String key;

    StatEnum(String key) {
        this.key = key;
    }

    /**
     * Returns localized stat name based on current locale.
     *
     * @return localized name of stat
     */
    public String getName() {
        return I18n.t(key);
    }
}
