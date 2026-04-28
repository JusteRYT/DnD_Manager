package com.example.dnd_manager.updater;

import com.example.dnd_manager.updater.port.UpdatePackageDownloader;

import com.example.dnd_manager.updater.port.UpdateInstallerLauncher;

import com.example.dnd_manager.updater.port.ApplicationTerminator;

import com.example.dnd_manager.updater.model.GitHubRelease;

import com.example.dnd_manager.updater.flow.UpdateManager;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateManagerTest {

    @Test
    void applyUpdate_throwsWhenZipAssetMissing() {
        UpdateManager manager = new UpdateManager(
                new FakeDownloader(),
                () -> Path.of("C:/app"),
                () -> Path.of("C:/temp/update.zip"),
                new FakeLauncher(),
                new FakeTerminator()
        );

        GitHubRelease release = new GitHubRelease();
        release.assets = List.of();

        assertThrows(IOException.class, () -> manager.applyUpdate(release, null));
    }

    @Test
    void applyUpdate_downloadsLaunchesAndTerminates() throws Exception {
        FakeDownloader downloader = new FakeDownloader();
        FakeLauncher launcher = new FakeLauncher();
        FakeTerminator terminator = new FakeTerminator();

        Path installPath = Path.of("C:/DnD_Manager");
        Path packagePath = Path.of("C:/temp/dnd_update.zip");

        UpdateManager manager = new UpdateManager(
                downloader,
                () -> installPath,
                () -> packagePath,
                launcher,
                terminator
        );

        GitHubRelease.Asset asset = new GitHubRelease.Asset();
        asset.name = "DnD_Manager.zip";
        asset.downloadUrl = "https://example.com/update.zip";
        GitHubRelease release = new GitHubRelease();
        release.assets = List.of(asset);

        AtomicBoolean progressed = new AtomicBoolean(false);

        manager.applyUpdate(release, (downloaded, total) -> progressed.set(downloaded == 10L && total == 10L));

        assertEquals("https://example.com/update.zip", downloader.lastUrl);
        assertSame(packagePath, downloader.lastDestination);
        assertTrue(progressed.get());
        assertSame(installPath, launcher.lastInstallPath);
        assertSame(packagePath, launcher.lastZipPath);
        assertTrue(terminator.called);
    }

    private static final class FakeDownloader implements UpdatePackageDownloader {
        private String lastUrl;
        private Path lastDestination;

        @Override
        public void download(String url, Path destination, java.util.function.BiConsumer<Long, Long> progressCallback) {
            this.lastUrl = url;
            this.lastDestination = destination;
            if (progressCallback != null) {
                progressCallback.accept(10L, 10L);
            }
        }
    }

    private static final class FakeLauncher implements UpdateInstallerLauncher {
        private Path lastInstallPath;
        private Path lastZipPath;

        @Override
        public void launch(Path installDir, Path zipPath) {
            this.lastInstallPath = installDir;
            this.lastZipPath = zipPath;
        }
    }

    private static final class FakeTerminator implements ApplicationTerminator {
        private boolean called;

        @Override
        public void terminate() {
            called = true;
        }
    }
}


















