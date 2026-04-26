package com.example.dnd_manager.updater;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Application service for update flow operations.
 */
public interface UpdateService {

    Optional<GitHubRelease> checkForUpdate();

    void applyUpdate(GitHubRelease release, BiConsumer<Long, Long> progressCallback) throws Exception;
}

