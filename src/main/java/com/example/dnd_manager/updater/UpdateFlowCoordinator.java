package com.example.dnd_manager.updater;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Coordinates async update flow steps and marshals callbacks.
 */
public interface UpdateFlowCoordinator {

    void checkForUpdate(Consumer<Optional<GitHubRelease>> onResult, Consumer<Exception> onError);

    void applyUpdate(
            GitHubRelease release,
            BiConsumer<Long, Long> onProgress,
            Consumer<Exception> onError
    );
}

