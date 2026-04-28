package com.example.dnd_manager.updater.ui;

import com.example.dnd_manager.updater.port.UpdateProgressTextFormatter;

import com.example.dnd_manager.lang.I18n;

import java.text.MessageFormat;

public class MegabytesProgressTextFormatter implements UpdateProgressTextFormatter {

    @Override
    public String format(long downloadedBytes, long totalBytes) {
        double downloadedMb = downloadedBytes / (1024.0 * 1024.0);
        double totalMb = totalBytes / (1024.0 * 1024.0);
        return MessageFormat.format(
                I18n.t("update.progress.downloading"),
                String.format("%.2f", downloadedMb),
                String.format("%.2f", totalMb)
        );
    }
}














