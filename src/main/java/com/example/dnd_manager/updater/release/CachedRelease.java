package com.example.dnd_manager.updater.release;

import com.example.dnd_manager.updater.model.GitHubRelease;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record CachedRelease(List<GitHubRelease> releases, Instant cachedAt) {

    public CachedRelease {
        Objects.requireNonNull(releases, "releases must not be null");
        if (releases.isEmpty()) {
            throw new IllegalArgumentException("releases must not be empty");
        }
        Objects.requireNonNull(cachedAt, "cachedAt must not be null");
    }

    public CachedRelease(GitHubRelease release, Instant cachedAt) {
        this(List.of(Objects.requireNonNull(release, "release must not be null")), cachedAt);
    }

    public GitHubRelease release() {
        return releases.getFirst();
    }
}
