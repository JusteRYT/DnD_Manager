package com.example.dnd_manager.updater.flow;

import com.example.dnd_manager.updater.port.UpdatePackagePathProvider;

import com.example.dnd_manager.updater.port.UpdatePackageDownloader;

import com.example.dnd_manager.updater.port.UpdateInstallerLauncher;

import com.example.dnd_manager.updater.port.ApplicationTerminator;

import com.example.dnd_manager.updater.port.ApplicationDirectoryResolver;

import com.example.dnd_manager.updater.model.GitHubRelease;

import com.example.dnd_manager.updater.install.WindowsBatchUpdateInstallerLauncher;

import com.example.dnd_manager.updater.install.SystemExitTerminator;

import com.example.dnd_manager.updater.install.JarApplicationDirectoryResolver;

import com.example.dnd_manager.updater.download.TempUpdatePackagePathProvider;

import com.example.dnd_manager.updater.download.HttpUpdatePackageDownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public class UpdateManager {
    private final UpdatePackageDownloader packageDownloader;
    private final ApplicationDirectoryResolver directoryResolver;
    private final UpdatePackagePathProvider packagePathProvider;
    private final UpdateInstallerLauncher installerLauncher;
    private final ApplicationTerminator terminator;

    public UpdateManager() {
        this(
                new HttpUpdatePackageDownloader(),
                new JarApplicationDirectoryResolver(),
                new TempUpdatePackagePathProvider(),
                new WindowsBatchUpdateInstallerLauncher(),
                new SystemExitTerminator()
        );
    }

    public UpdateManager(
            UpdatePackageDownloader packageDownloader,
            ApplicationDirectoryResolver directoryResolver,
            UpdatePackagePathProvider packagePathProvider,
            UpdateInstallerLauncher installerLauncher,
            ApplicationTerminator terminator
    ) {
        this.packageDownloader = Objects.requireNonNull(packageDownloader, "packageDownloader must not be null");
        this.directoryResolver = Objects.requireNonNull(directoryResolver, "directoryResolver must not be null");
        this.packagePathProvider = Objects.requireNonNull(packagePathProvider, "packagePathProvider must not be null");
        this.installerLauncher = Objects.requireNonNull(installerLauncher, "installerLauncher must not be null");
        this.terminator = Objects.requireNonNull(terminator, "terminator must not be null");
    }

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

        Path packagePath = packagePathProvider.resolvePath();
        packageDownloader.download(zipAsset.get().downloadUrl, packagePath, progressCallback);
        Path installDir = directoryResolver.resolveInstallDirectory();
        installerLauncher.launch(installDir, packagePath);
        terminator.terminate();
    }
}
























