package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FamiliarCardViewModelMapperTest {

    private final FamiliarCardViewModelMapper mapper = new FamiliarCardViewModelMapper();

    @Test
    void map_buildsViewModelWithLocalizedStats() {
        I18n.setLocale(Locale.ENGLISH);
        Character familiar = new Character();
        familiar.setName("Pixie");
        familiar.setRace("Fey");
        familiar.setCharacterClass("Scout");
        familiar.setCurrentHp(12);
        familiar.setArmor(15);

        FamiliarCardViewModel vm = mapper.map(familiar);

        assertEquals("Pixie", vm.name());
        assertEquals("Fey Scout", vm.raceClass());
        assertEquals("HP: 12", vm.hpText());
        assertEquals("AC: 15", vm.acText());
    }

    @Test
    void map_handlesNullRaceAndClass() {
        I18n.setLocale(Locale.ENGLISH);
        Character familiar = new Character();
        familiar.setName("Sprite");
        familiar.setCurrentHp(5);
        familiar.setArmor(10);

        FamiliarCardViewModel vm = mapper.map(familiar);

        assertEquals("Sprite", vm.name());
        assertEquals("", vm.raceClass());
    }
}












