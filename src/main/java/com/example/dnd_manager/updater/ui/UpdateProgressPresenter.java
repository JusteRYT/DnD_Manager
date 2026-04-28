package com.example.dnd_manager.updater.ui;

import com.example.dnd_manager.updater.port.UpdateProgressView;

import com.example.dnd_manager.updater.port.UpdateProgressTextFormatter;

import java.util.Objects;

public class UpdateProgressPresenter {

    private final UpdateProgressCalculator progressCalculator;
    private final UpdateProgressTextFormatter progressTextFormatter;

    public UpdateProgressPresenter(
            UpdateProgressCalculator progressCalculator,
            UpdateProgressTextFormatter progressTextFormatter
    ) {
        this.progressCalculator = Objects.requireNonNull(progressCalculator, "progressCalculator must not be null");
        this.progressTextFormatter = Objects.requireNonNull(progressTextFormatter, "progressTextFormatter must not be null");
    }

    public void present(UpdateProgressView view, long downloaded, long total) {
        double progress = progressCalculator.calculate(downloaded, total);
        String message = progressTextFormatter.format(downloaded, total);
        view.update(progress, message);
    }
}















