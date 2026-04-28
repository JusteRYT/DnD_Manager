package com.example.dnd_manager.updater.port;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Launches external installer/update script.
 */
public interface UpdateInstallerLauncher {

    void launch(Path installDir, Path zipPath) throws IOException;
}














