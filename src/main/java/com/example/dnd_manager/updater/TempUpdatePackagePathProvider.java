package com.example.dnd_manager.updater;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Default temp path provider for update package zip.
 */
public class TempUpdatePackagePathProvider implements UpdatePackagePathProvider {

    @Override
    public Path resolvePath() {
        return Paths.get(System.getProperty("java.io.tmpdir"), "dnd_update.zip");
    }
}

