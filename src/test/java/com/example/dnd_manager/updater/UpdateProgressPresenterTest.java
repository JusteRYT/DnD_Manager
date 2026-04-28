package com.example.dnd_manager.updater;

import com.example.dnd_manager.updater.ui.UpdateProgressPresenter;

import com.example.dnd_manager.updater.ui.UpdateProgressCalculator;

import com.example.dnd_manager.updater.port.UpdateProgressView;

import com.example.dnd_manager.updater.port.UpdateProgressTextFormatter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateProgressPresenterTest {

    @Test
    void present_updatesViewUsingCalculatorAndFormatter() {
        UpdateProgressCalculator calculator = new UpdateProgressCalculator();
        UpdateProgressTextFormatter formatter = (downloaded, total) -> "d=" + downloaded + ",t=" + total;
        UpdateProgressPresenter presenter = new UpdateProgressPresenter(calculator, formatter);
        FakeView view = new FakeView();

        presenter.present(view, 50, 100);

        assertEquals(0.5, view.progress);
        assertEquals("d=50,t=100", view.message);
    }

    private static final class FakeView implements UpdateProgressView {
        private double progress;
        private String message;

        @Override
        public void update(double progress, String message) {
            this.progress = progress;
            this.message = message;
        }
    }
}
















