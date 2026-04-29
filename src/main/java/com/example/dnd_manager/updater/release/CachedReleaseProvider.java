package com.example.dnd_manager.updater.release;

import com.example.dnd_manager.updater.model.GitHubRelease;
import com.example.dnd_manager.updater.port.ReleaseCacheStore;
import com.example.dnd_manager.updater.port.ReleaseProvider;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CachedReleaseProvider implements ReleaseProvider {

    private final ReleaseProvider remoteProvider;
    private final ReleaseCacheStore cacheStore;
    private final Duration refreshInterval;
    private final Clock clock;

    public CachedReleaseProvider(
            ReleaseProvider remoteProvider,
            ReleaseCacheStore cacheStore,
            Duration refreshInterval
    ) {
        this(remoteProvider, cacheStore, refreshInterval, Clock.systemDefaultZone());
    }

    public CachedReleaseProvider(
            ReleaseProvider remoteProvider,
            ReleaseCacheStore cacheStore,
            Duration refreshInterval,
            Clock clock
    ) {
        this.remoteProvider = Objects.requireNonNull(remoteProvider, "remoteProvider must not be null");
        this.cacheStore = Objects.requireNonNull(cacheStore, "cacheStore must not be null");
        this.refreshInterval = Objects.requireNonNull(refreshInterval, "refreshInterval must not be null");
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    @Override
    public Optional<GitHubRelease> fetchLatestRelease() {
        return fetchRecentReleases(1).stream().findFirst();
    }

    @Override
    public List<GitHubRelease> fetchRecentReleases(int limit) {
        Optional<CachedRelease> cachedRelease = cacheStore.load();
        if (cachedRelease.isPresent() && !isRefreshNeeded(cachedRelease.get())) {
            return cachedRelease.get().releases().stream()
                    .limit(limit)
                    .toList();
        }

        List<GitHubRelease> remoteReleases = remoteProvider.fetchRecentReleases(limit);
        if (!remoteReleases.isEmpty()) {
            saveIfChanged(cachedRelease.orElse(null), remoteReleases);
            return remoteReleases;
        }

        return cachedRelease
                .map(CachedRelease::releases)
                .orElseGet(List::of)
                .stream()
                .limit(limit)
                .toList();
    }

    private boolean isRefreshNeeded(CachedRelease cachedRelease) {
        Instant nextRefresh = cachedRelease.cachedAt().plus(refreshInterval);
        return !nextRefresh.isAfter(clock.instant());
    }

    private void saveIfChanged(CachedRelease cachedRelease, List<GitHubRelease> remoteReleases) {
        if (cachedRelease == null || hasChanged(cachedRelease.releases(), remoteReleases)) {
            cacheStore.save(new CachedRelease(remoteReleases, clock.instant()));
        }
    }

    private boolean hasChanged(List<GitHubRelease> cachedReleases, List<GitHubRelease> remoteReleases) {
        if (cachedReleases.size() != remoteReleases.size()) {
            return true;
        }
        for (int i = 0; i < cachedReleases.size(); i++) {
            if (hasChanged(cachedReleases.get(i), remoteReleases.get(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasChanged(GitHubRelease cachedRelease, GitHubRelease remoteRelease) {
        return !Objects.equals(cachedRelease.tagName, remoteRelease.tagName)
                || !Objects.equals(cachedRelease.name, remoteRelease.name)
                || !Objects.equals(cachedRelease.body, remoteRelease.body)
                || !Objects.equals(cachedRelease.publishedAt, remoteRelease.publishedAt);
    }
}
