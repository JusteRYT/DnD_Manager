package com.example.dnd_manager.updater;

import com.example.dnd_manager.updater.version.SemanticVersionComparator;

import com.example.dnd_manager.updater.release.UpdateChecker;

import com.example.dnd_manager.updater.port.ReleaseProvider;

import com.example.dnd_manager.updater.model.GitHubRelease;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateCheckerTest {

    @Test
    void check_returnsReleaseWhenRemoteVersionIsNewer() {
        GitHubRelease release = new GitHubRelease();
        release.tagName = "1.0.4";

        UpdateChecker checker = new UpdateChecker(
                () -> Optional.of(release),
                new SemanticVersionComparator(),
                () -> "1.0.3"
        );

        Optional<GitHubRelease> result = checker.check();

        assertTrue(result.isPresent());
        assertSame(release, result.get());
    }

    @Test
    void check_returnsEmptyWhenRemoteVersionIsNotNewer() {
        GitHubRelease release = new GitHubRelease();
        release.tagName = "1.0.3";

        UpdateChecker checker = new UpdateChecker(
                () -> Optional.of(release),
                new SemanticVersionComparator(),
                () -> "1.0.3"
        );

        Optional<GitHubRelease> result = checker.check();

        assertTrue(result.isEmpty());
    }

    @Test
    void fetchLatestRelease_delegatesToProvider() {
        GitHubRelease release = new GitHubRelease();
        ReleaseProvider provider = () -> Optional.of(release);

        UpdateChecker checker = new UpdateChecker(
                provider,
                new SemanticVersionComparator(),
                () -> "1.0.0"
        );

        Optional<GitHubRelease> fetched = checker.fetchLatestRelease();

        assertTrue(fetched.isPresent());
        assertSame(release, fetched.get());
    }
}

















