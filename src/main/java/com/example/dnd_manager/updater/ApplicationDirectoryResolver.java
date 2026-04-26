package com.example.dnd_manager.updater;

import java.nio.file.Path;

/**
 * Resolves application installation directory.
 */
public interface ApplicationDirectoryResolver {

    Path resolveInstallDirectory();
}

