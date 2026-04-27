package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;

public class FamiliarResourceSnapshotFactory {

    public FamiliarResourceSnapshot from(Character familiar) {
        return new FamiliarResourceSnapshot(
                familiar.getCurrentHp() + "/" + familiar.getMaxHp(),
                familiar.getCurrentMana() + "/" + familiar.getMaxMana(),
                String.valueOf(familiar.getArmor()),
                String.valueOf(familiar.getLevel())
        );
    }
}

