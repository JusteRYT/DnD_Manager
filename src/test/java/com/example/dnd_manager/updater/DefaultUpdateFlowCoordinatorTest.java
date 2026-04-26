package com.example.dnd_manager.updater;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultUpdateFlowCoordinatorTest {

    @Test
    void checkForUpdate_deliversResultToCallback() {
        FakeUpdateService updateService = new FakeUpdateService();
        GitHubRelease release = new GitHubRelease();
        release.tagName = "v1.0.1";
        updateService.checkResult = Optional.of(release);

        DefaultUpdateFlowCoordinator coordinator = new DefaultUpdateFlowCoordinator(
                updateService,
                new DirectAsyncRunner(),
                new DirectUiDispatcher()
        );

        AtomicReference<Optional<GitHubRelease>> resultRef = new AtomicReference<>();
        AtomicReference<Exception> errorRef = new AtomicReference<>();

        coordinator.checkForUpdate(resultRef::set, errorRef::set);

        assertTrue(resultRef.get().isPresent());
        assertSame(release, resultRef.get().get());
        assertTrue(errorRef.get() == null);
    }

    @Test
    void checkForUpdate_whenServiceThrows_routesErrorCallback() {
        FakeUpdateService updateService = new FakeUpdateService();
        RuntimeException expected = new RuntimeException("network down");
        updateService.checkError = expected;

        DefaultUpdateFlowCoordinator coordinator = new DefaultUpdateFlowCoordinator(
                updateService,
                new DirectAsyncRunner(),
                new DirectUiDispatcher()
        );

        AtomicReference<Optional<GitHubRelease>> resultRef = new AtomicReference<>();
        AtomicReference<Exception> errorRef = new AtomicReference<>();

        coordinator.checkForUpdate(resultRef::set, errorRef::set);

        assertTrue(resultRef.get() == null);
        assertSame(expected, errorRef.get());
    }

    @Test
    void applyUpdate_passesReleaseAndProgressToService() {
        FakeUpdateService updateService = new FakeUpdateService();
        GitHubRelease release = new GitHubRelease();
        release.tagName = "v2.0.0";

        DefaultUpdateFlowCoordinator coordinator = new DefaultUpdateFlowCoordinator(
                updateService,
                new DirectAsyncRunner(),
                new DirectUiDispatcher()
        );

        AtomicBoolean progressed = new AtomicBoolean(false);
        AtomicReference<Exception> errorRef = new AtomicReference<>();

        coordinator.applyUpdate(
                release,
                (downloaded, total) -> progressed.set(downloaded == 50L && total == 100L),
                errorRef::set
        );

        assertSame(release, updateService.lastAppliedRelease);
        assertTrue(progressed.get());
        assertTrue(errorRef.get() == null);
    }

    @Test
    void applyUpdate_whenServiceThrows_routesErrorCallback() {
        FakeUpdateService updateService = new FakeUpdateService();
        RuntimeException expected = new RuntimeException("disk full");
        updateService.applyError = expected;
        GitHubRelease release = new GitHubRelease();

        DefaultUpdateFlowCoordinator coordinator = new DefaultUpdateFlowCoordinator(
                updateService,
                new DirectAsyncRunner(),
                new DirectUiDispatcher()
        );

        AtomicReference<Exception> errorRef = new AtomicReference<>();

        coordinator.applyUpdate(release, (d, t) -> {}, errorRef::set);

        assertSame(expected, errorRef.get());
    }

    private static final class DirectAsyncRunner implements AsyncRunner {
        @Override
        public void run(String threadName, Runnable task) {
            task.run();
        }
    }

    private static final class DirectUiDispatcher implements UiDispatcher {
        @Override
        public void dispatch(Runnable action) {
            action.run();
        }
    }

    private static final class FakeUpdateService implements UpdateService {
        private Optional<GitHubRelease> checkResult = Optional.empty();
        private RuntimeException checkError;
        private RuntimeException applyError;
        private GitHubRelease lastAppliedRelease;

        @Override
        public Optional<GitHubRelease> checkForUpdate() {
            if (checkError != null) {
                throw checkError;
            }
            return checkResult;
        }

        @Override
        public void applyUpdate(GitHubRelease release, java.util.function.BiConsumer<Long, Long> progressCallback) {
            if (applyError != null) {
                throw applyError;
            }
            this.lastAppliedRelease = release;
            progressCallback.accept(50L, 100L);
        }
    }
}

