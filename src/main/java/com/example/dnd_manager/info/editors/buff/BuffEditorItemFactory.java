package com.example.dnd_manager.info.editors.buff;

import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.buff_debuff.model.BuffType;

public class BuffEditorItemFactory {

    public Buff create(String name, String description, String type, String iconPath) {
        return new Buff(name.trim(), description, BuffType.canonical(type), iconPath);
    }
}












