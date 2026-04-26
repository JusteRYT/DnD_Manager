package com.example.dnd_manager.screen;

import com.example.dnd_manager.lang.I18n;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateCheckButtonPresenterTest {

    @Test
    void showChecking_disablesButtonAndSetsCheckingText() {
        I18n.setLocale(Locale.ENGLISH);
        UpdateCheckButtonPresenter presenter = new UpdateCheckButtonPresenter();
        FakeButtonView view = new FakeButtonView();

        presenter.showChecking(view);

        assertTrue(view.disabled);
        assertEquals("Checking...", view.text);
    }

    @Test
    void showReady_enablesButtonAndSetsReadyText() {
        I18n.setLocale(Locale.ENGLISH);
        UpdateCheckButtonPresenter presenter = new UpdateCheckButtonPresenter();
        FakeButtonView view = new FakeButtonView();

        presenter.showReady(view);

        assertFalse(view.disabled);
        assertEquals("Check Updates", view.text);
    }

    private static final class FakeButtonView implements UpdateCheckButtonView {
        private boolean disabled;
        private String text;

        @Override
        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        @Override
        public void setText(String text) {
            this.text = text;
        }
    }
}
