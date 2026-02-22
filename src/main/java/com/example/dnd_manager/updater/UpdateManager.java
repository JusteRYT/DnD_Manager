package com.example.dnd_manager.updater;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Optional;

public class UpdateManager {

    public void applyUpdate(GitHubRelease release) throws IOException {

        Optional<GitHubRelease.Asset> zipAsset = release.assets.stream()
                .filter(a -> a.name.endsWith(".zip"))
                .findFirst();

        if (zipAsset.isEmpty()) {
            throw new IOException("Update failed: No ZIP asset found in the latest release.");
        }

        Path tempZip = Paths.get(System.getProperty("java.io.tmpdir"), "dnd_update.zip");

        try (InputStream in = new URL(zipAsset.get().downloadUrl).openStream()) {
            Files.copy(in, tempZip, StandardCopyOption.REPLACE_EXISTING);
        }

        String currentDir = System.getProperty("user.dir");
        createWindowsUpdater(currentDir, tempZip.toString());

        System.exit(0);
    }

    private void createWindowsUpdater(String installDir, String zipPath) throws IOException {
        File batchFile = new File("update_script.bat");

        try (PrintWriter writer = new PrintWriter(batchFile)) {
            writer.println("@echo off");
            writer.println("echo Waiting for application to close...");
            writer.println("timeout /t 3 /nobreak > nul");

            writer.println("echo Updating files in: " + installDir);

            writer.println("tar -xf \"" + zipPath + "\" -C \"" + installDir + "\"");

            writer.println("echo Cleaning up...");
            writer.println("del \"" + zipPath + "\"");

            writer.println("start \"\" \"run.bat\"");

            writer.println("del \"%~f0\"");
        }

        new ProcessBuilder("cmd", "/c", "start", "/min", batchFile.getName()).start();
    }
}