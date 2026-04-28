package com.example.dnd_manager.updater.ui;

public class UpdateProgressCalculator {

    public double calculate(long downloadedBytes, long totalBytes) {
        if (totalBytes <= 0) {
            return 0.0;
        }

        double raw = (double) downloadedBytes / totalBytes;
        if (raw < 0.0) {
            return 0.0;
        }
        return Math.min(raw, 1.0);
    }
}













