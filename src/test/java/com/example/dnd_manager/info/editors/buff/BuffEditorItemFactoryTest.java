package com.example.dnd_manager.info.editors.buff;

import com.example.dnd_manager.info.buff_debuff.model.Buff;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuffEditorItemFactoryTest {

    private final BuffEditorItemFactory factory = new BuffEditorItemFactory();

    @Test
    void create_trimsNameAndKeepsEditorValues() {
        Buff buff = factory.create(" Rage ", "desc", "BUFF", "icon.png");

        assertEquals("Rage", buff.name());
        assertEquals("desc", buff.description());
        assertEquals("BUFF", buff.type());
        assertEquals("icon.png", buff.iconPath());
    }
}












