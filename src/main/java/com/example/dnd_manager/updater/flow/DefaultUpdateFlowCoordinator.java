package com.example.dnd_manager.updater.flow;

import com.example.dnd_manager.updater.port.UpdateService;

import com.example.dnd_manager.updater.port.UpdateFlowCoordinator;

import com.example.dnd_manager.updater.port.UiDispatcher;

import com.example.dnd_manager.updater.port.AsyncRunner;

import com.example.dnd_manager.updater.model.GitHubRelease;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Default update flow coordinator.
 */
public class DefaultUpdateFlowCoordinator implements UpdateFlowCoordinator {

    private final UpdateService updateService;
    private final AsyncRunner asyncRunner;
    private final UiDispatcher uiDispatcher;

    public DefaultUpdateFlowCoordinator(
            UpdateService updateService,
            AsyncRunner asyncRunner,
            UiDispatcher uiDispatcher
    ) {
        this.updateService = Objects.requireNonNull(updateService, "updateService must not be null");
        this.asyncRunner = Objects.requireNonNull(asyncRunner, "asyncRunner must not be null");
        this.uiDispatcher = Objects.requireNonNull(uiDispatcher, "uiDispatcher must not be null");
    }

    @Override
    public void checkForUpdate(Consumer<Optional<GitHubRelease>> onResult, Consumer<Exception> onError) {
        Objects.requireNonNull(onResult, "onResult must not be null");
        Objects.requireNonNull(onError, "onError must not be null");

        asyncRunner.run("update-check-thread", () -> {
            try {
                Optional<GitHubRelease> releaseOpt = updateService.checkForUpdate();
                uiDispatcher.dispatch(() -> onResult.accept(releaseOpt));
            } catch (Exception e) {
                uiDispatcher.dispatch(() -> onError.accept(e));
            }
        });
    }

    @Override
    public void applyUpdate(
            GitHubRelease release,
            BiConsumer<Long, Long> onProgress,
            Consumer<Exception> onError
    ) {
        Objects.requireNonNull(release, "release must not be null");
        Objects.requireNonNull(onProgress, "onProgress must not be null");
        Objects.requireNonNull(onError, "onError must not be null");

        asyncRunner.run("update-apply-thread", () -> {
            try {
                updateService.applyUpdate(release, onProgress);
            } catch (Exception e) {
                uiDispatcher.dispatch(() -> onError.accept(e));
            }
        });
    }
}



















