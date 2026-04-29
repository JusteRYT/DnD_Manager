package com.example.dnd_manager.infrastructure.system;

import com.example.dnd_manager.application.port.ExternalLinkOpener;

import java.awt.Desktop;
import java.net.URI;

/**
 * Desktop-backed implementation for opening links in the user's browser.
 */
public class DesktopExternalLinkOpener implements ExternalLinkOpener {

    @Override
    public void open(String url) {
        try {
            if (!Desktop.isDesktopSupported()) {
                throw new IllegalStateException("Desktop API is not supported");
            }

            Desktop.getDesktop().browse(URI.create(url));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to open external link: " + url, ex);
        }
    }
}
