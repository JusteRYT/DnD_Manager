package com.example.dnd_manager.screen.selection;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterDeleteConfirmationMessageFactoryTest {

    private final CharacterDeleteConfirmationMessageFactory factory = new CharacterDeleteConfirmationMessageFactory();

    @AfterEach
    void resetLocale() {
        I18n.setLocale(Locale.ENGLISH);
    }

    @Test
    void messageContainsCharacterNameInRussianLocale() {
        I18n.setLocale(Locale.forLanguageTag("ru"));
        Character character = new Character();
        character.setName("Ария");

        assertEquals("Удаление персонажа", factory.title());
        assertEquals("Вы точно хотите удалить [Ария]?", factory.message(character));
    }

    @Test
    void messageUsesFallbackWhenCharacterNameIsBlank() {
        I18n.setLocale(Locale.ENGLISH);
        Character character = new Character();
        character.setName(" ");

        assertTrue(factory.message(character).contains("[Not specified]"));
    }
}
