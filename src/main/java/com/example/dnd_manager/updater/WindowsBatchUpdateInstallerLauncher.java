package com.example.dnd_manager.updater;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Windows batch-based installer launcher.
 */
public class WindowsBatchUpdateInstallerLauncher implements UpdateInstallerLauncher {

    @Override
    public void launch(Path installDir, Path zipPath) throws IOException {
        Path batchFile = Files.createTempFile("dnd_updater", ".bat");
        String exeName = "DnD_Manager.exe";

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(batchFile))) {
            writer.println("@echo off");
            writer.println("echo Waiting for DnD_Manager to close...");
            writer.println("timeout /t 3 /nobreak > nul");
            writer.println("cd /d \"" + installDir + "\"");
            writer.println("echo Extracting update files...");
            writer.println("tar -xf \"" + zipPath + "\" -C \"" + installDir + "\"");
            writer.println("echo Cleaning up...");
            writer.println("del \"" + zipPath + "\"");
            writer.println("echo Restarting application...");
            writer.println("start \"\" \"" + exeName + "\"");
            writer.println("del \"%~f0\"");
        }

        new ProcessBuilder("cmd", "/c", "start", "/min", "cmd", "/c", batchFile.toString()).start();
    }
}

