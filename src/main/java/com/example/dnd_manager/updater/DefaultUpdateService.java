package com.example.dnd_manager.updater;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Default update service implementation backed by checker/manager components.
 */
public class DefaultUpdateService implements UpdateService {

    private final UpdateChecker updateChecker;
    private final UpdateManager updateManager;

    public DefaultUpdateService(UpdateChecker updateChecker, UpdateManager updateManager) {
        this.updateChecker = Objects.requireNonNull(updateChecker, "updateChecker must not be null");
        this.updateManager = Objects.requireNonNull(updateManager, "updateManager must not be null");
    }

    @Override
    public Optional<GitHubRelease> checkForUpdate() {
        return updateChecker.check();
    }

    @Override
    public void applyUpdate(GitHubRelease release, BiConsumer<Long, Long> progressCallback) throws Exception {
        updateManager.applyUpdate(release, progressCallback);
    }
}

