package com.example.dnd_manager.info.buff_debuff;


/**
 * Visual style for buff/debuff columns.
 */
public enum BuffColumnStyle {

    BUFF("#3fb950"),     // green
    DEBUFF("#f85149");   // red

    private final String accentColor;

    BuffColumnStyle(String accentColor) {
        this.accentColor = accentColor;
    }

    public String accentColor() {
        return accentColor;
    }
}
