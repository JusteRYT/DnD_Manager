package com.example.dnd_manager.updater;

public interface UpdateProgressTextFormatter {

    String format(long downloadedBytes, long totalBytes);
}
