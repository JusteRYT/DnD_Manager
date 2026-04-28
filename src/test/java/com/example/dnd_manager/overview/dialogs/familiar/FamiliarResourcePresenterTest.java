package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FamiliarResourcePresenterTest {

    @Test
    void refresh_pushesSnapshotToDisplay() {
        Character familiar = new Character();
        familiar.setCurrentHp(3);
        familiar.setMaxHp(9);
        familiar.setCurrentMana(1);
        familiar.setMaxMana(5);
        familiar.setArmor(12);
        familiar.setLevel(2);

        TestDisplay display = new TestDisplay();
        FamiliarResourcePresenter presenter = new FamiliarResourcePresenter(new FamiliarResourceSnapshotFactory(), display);

        presenter.refresh(familiar);

        assertEquals("3/9", display.snapshot.hpText());
        assertEquals("1/5", display.snapshot.mpText());
        assertEquals("12", display.snapshot.acText());
        assertEquals("2", display.snapshot.levelText());
    }

    @Test
    void updateHandler_refreshesAndCallsCallback() {
        Character familiar = new Character();
        familiar.setCurrentHp(5);
        familiar.setMaxHp(10);
        familiar.setCurrentMana(4);
        familiar.setMaxMana(6);
        familiar.setArmor(11);
        familiar.setLevel(3);

        TestDisplay display = new TestDisplay();
        FamiliarResourcePresenter presenter = new FamiliarResourcePresenter(new FamiliarResourceSnapshotFactory(), display);

        int[] callbackInvocations = {0};
        Runnable handler = presenter.updateHandler(familiar, () -> callbackInvocations[0]++);

        handler.run();

        assertEquals("5/10", display.snapshot.hpText());
        assertEquals("4/6", display.snapshot.mpText());
        assertEquals(1, callbackInvocations[0]);
    }

    private static class TestDisplay implements FamiliarResourceDisplay {
        private FamiliarResourceSnapshot snapshot;

        @Override
        public void show(FamiliarResourceSnapshot snapshot) {
            this.snapshot = snapshot;
        }
    }
}













