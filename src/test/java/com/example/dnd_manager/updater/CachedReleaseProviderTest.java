package com.example.dnd_manager.updater.release;

import com.example.dnd_manager.updater.model.GitHubRelease;
import com.example.dnd_manager.updater.port.ReleaseCacheStore;
import com.example.dnd_manager.updater.port.ReleaseProvider;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CachedReleaseProviderTest {

    private static final Instant NOW = Instant.parse("2026-04-29T00:00:00Z");
    private static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

    @Test
    void fetchLatestRelease_returnsFreshCacheWithoutRemoteRequest() {
        GitHubRelease cached = release("v1.0.5", "Latest");
        InMemoryCacheStore cacheStore = new InMemoryCacheStore(new CachedRelease(cached, NOW.minus(Duration.ofHours(1))));
        AtomicInteger remoteCalls = new AtomicInteger();
        ReleaseProvider remote = () -> {
            remoteCalls.incrementAndGet();
            return Optional.of(release("v1.0.6", "Remote"));
        };

        CachedReleaseProvider provider = new CachedReleaseProvider(remote, cacheStore, Duration.ofHours(6), CLOCK);

        Optional<GitHubRelease> result = provider.fetchLatestRelease();

        assertTrue(result.isPresent());
        assertSame(cached, result.get());
        assertEquals(0, remoteCalls.get());
    }

    @Test
    void fetchLatestRelease_savesRemoteReleaseWhenStaleCacheChanged() {
        GitHubRelease cached = release("v1.0.5", "Old");
        GitHubRelease remoteRelease = release("v1.0.6", "New");
        InMemoryCacheStore cacheStore = new InMemoryCacheStore(new CachedRelease(cached, NOW.minus(Duration.ofHours(8))));
        ReleaseProvider remote = () -> Optional.of(remoteRelease);

        CachedReleaseProvider provider = new CachedReleaseProvider(remote, cacheStore, Duration.ofHours(6), CLOCK);

        Optional<GitHubRelease> result = provider.fetchLatestRelease();

        assertTrue(result.isPresent());
        assertSame(remoteRelease, result.get());
        assertSame(remoteRelease, cacheStore.saved.release());
        assertEquals(NOW, cacheStore.saved.cachedAt());
    }

    @Test
    void fetchLatestRelease_keepsCachedReleaseWhenRemoteFails() {
        GitHubRelease cached = release("v1.0.5", "Cached");
        InMemoryCacheStore cacheStore = new InMemoryCacheStore(new CachedRelease(cached, NOW.minus(Duration.ofHours(8))));
        ReleaseProvider remote = Optional::empty;

        CachedReleaseProvider provider = new CachedReleaseProvider(remote, cacheStore, Duration.ofHours(6), CLOCK);

        Optional<GitHubRelease> result = provider.fetchLatestRelease();

        assertTrue(result.isPresent());
        assertSame(cached, result.get());
    }

    @Test
    void fetchRecentReleases_savesTwoRemoteReleasesWhenCacheIsStale() {
        GitHubRelease cached = release("v1.0.4", "Cached");
        GitHubRelease latest = release("v1.0.6", "Latest");
        GitHubRelease previous = release("v1.0.5", "Previous");
        InMemoryCacheStore cacheStore = new InMemoryCacheStore(new CachedRelease(cached, NOW.minus(Duration.ofHours(8))));
        ReleaseProvider remote = new ReleaseProvider() {
            @Override
            public Optional<GitHubRelease> fetchLatestRelease() {
                return Optional.of(latest);
            }

            @Override
            public List<GitHubRelease> fetchRecentReleases(int limit) {
                return List.of(latest, previous).stream().limit(limit).toList();
            }
        };

        CachedReleaseProvider provider = new CachedReleaseProvider(remote, cacheStore, Duration.ofHours(6), CLOCK);

        List<GitHubRelease> result = provider.fetchRecentReleases(2);

        assertEquals(List.of(latest, previous), result);
        assertEquals(List.of(latest, previous), cacheStore.saved.releases());
    }

    private static GitHubRelease release(String tagName, String name) {
        GitHubRelease release = new GitHubRelease();
        release.tagName = tagName;
        release.name = name;
        release.body = "Release body";
        release.publishedAt = NOW.toString();
        return release;
    }

    private static class InMemoryCacheStore implements ReleaseCacheStore {
        private CachedRelease cached;
        private CachedRelease saved;

        private InMemoryCacheStore(CachedRelease cached) {
            this.cached = cached;
        }

        @Override
        public Optional<CachedRelease> load() {
            return Optional.ofNullable(cached);
        }

        @Override
        public void save(CachedRelease release) {
            this.cached = release;
            this.saved = release;
        }
    }
}
