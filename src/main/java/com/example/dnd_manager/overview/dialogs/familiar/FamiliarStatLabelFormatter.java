package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.info.stats.model.StatEnum;

import java.util.Locale;

public class FamiliarStatLabelFormatter {

    public String shortLabel(StatEnum stat) {
        String name = stat.getName();
        if (name.isBlank()) {
            return "";
        }
        return name.substring(0, Math.min(3, name.length())).toUpperCase(Locale.ROOT);
    }
}













