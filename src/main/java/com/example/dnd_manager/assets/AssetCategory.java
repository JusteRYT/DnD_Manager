package com.example.dnd_manager.assets;

import lombok.Getter;

@Getter
public enum AssetCategory {
    SKILLS("Skills", "Навыки"),
    BUFFS("Buffs", "Баффы/Дебаффы"),
    ITEMS("Items", "Предметы"),
    CHARACTERS("Characters", "Персонажи");

    private final String folderName;
    private final String displayName;

    AssetCategory(String folderName, String displayName) {
        this.folderName = folderName;
        this.displayName = displayName;
    }
}
