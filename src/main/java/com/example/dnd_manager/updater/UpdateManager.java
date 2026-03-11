package com.example.dnd_manager.updater;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiConsumer;

@Slf4j
public class UpdateManager {
    private final HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();

    /**
     * Downloads the update and triggers the external script to apply it.
     *
     * @param release          The release to download.
     * @param progressCallback Callback with (downloadedBytes, totalBytes).
     * @throws IOException If download or file operations fail.
     */
    public void applyUpdate(GitHubRelease release, BiConsumer<Long, Long> progressCallback) throws IOException, InterruptedException {
        Optional<GitHubRelease.Asset> zipAsset = release.assets.stream()
                .filter(a -> a.name.endsWith(".zip"))
                .findFirst();

        if (zipAsset.isEmpty()) {
            throw new IOException("No ZIP asset found.");
        }

        Path tempZip = Paths.get(System.getProperty("java.io.tmpdir"), "dnd_update.zip");
        downloadFile(zipAsset.get().downloadUrl, tempZip, progressCallback);

        String currentDir = getAppDirectory().toString();
        createWindowsUpdater(currentDir, tempZip.toString());

        System.exit(0);
    }

    /**
     * Gets the directory where the application executable is located.
     */

    private Path getAppDirectory() {
        try {
            return Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (Exception e) {
            return Paths.get(System.getProperty("user.dir"));
        }
    }

    private void downloadFile(String url, Path dest, BiConsumer<Long, Long> progressCallback) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

        long totalSize = Long.parseLong(response.headers().firstValue("Content-Length").orElse("-1"));

        try (InputStream is = response.body();
             OutputStream os = Files.newOutputStream(dest)) {

            byte[] buffer = new byte[8192];
            long downloaded = 0;
            int read;

            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
                downloaded += read;
                if (progressCallback != null) {
                    progressCallback.accept(downloaded, totalSize);
                }
            }
        }
    }

    /**
     * Creates and executes a temporary batch script to replace application files and restart.
     *
     * @param installDir The absolute path to the application's root directory.
     * @param zipPath    The absolute path to the downloaded update ZIP file.
     * @throws IOException If script creation or execution fails.
     */
    private void createWindowsUpdater(String installDir, String zipPath) throws IOException {
        // Используем временный файл для скрипта
        Path batchFile = Files.createTempFile("dnd_updater", ".bat");
        String exeName = "DnD_Manager.exe";

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(batchFile))) {
            writer.println("@echo off");
            writer.println("echo Waiting for DnD_Manager to close...");
            writer.println("timeout /t 3 /nobreak > nul");

            // Переходим в папку установки
            writer.println("cd /d \"" + installDir + "\"");
            writer.println("echo Extracting update files...");
            // Убрали --strip-components 1, так как в твоем ZIP файлы лежат в корне
            writer.println("tar -xf \"" + zipPath + "\" -C \"" + installDir + "\"");

            writer.println("echo Cleaning up...");
            writer.println("del \"" + zipPath + "\"");

            writer.println("echo Restarting application...");
            // Используем команду start для корректного запуска GUI приложения
            writer.println("start \"\" \"" + exeName + "\"");

            // Скрипт удаляет сам себя
            writer.println("del \"%~f0\"");
        }

        // Запускаем скрытно (минимизированно)
        new ProcessBuilder("cmd", "/c", "start", "/min", "cmd", "/c", batchFile.toString())
                .start();
    }
}