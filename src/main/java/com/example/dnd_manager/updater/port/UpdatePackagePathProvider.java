package com.example.dnd_manager.updater.port;

import java.nio.file.Path;

/**
 * Provides destination path for downloaded update package.
 */
public interface UpdatePackagePathProvider {

    Path resolvePath();
}














