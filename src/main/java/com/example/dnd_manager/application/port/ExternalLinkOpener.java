package com.example.dnd_manager.application.port;

/**
 * Opens external links outside the JavaFX application shell.
 */
public interface ExternalLinkOpener {
    void open(String url);
}
