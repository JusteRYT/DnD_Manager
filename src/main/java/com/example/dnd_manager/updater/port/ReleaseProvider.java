package com.example.dnd_manager.updater.port;

import com.example.dnd_manager.updater.model.GitHubRelease;

import java.util.List;
import java.util.Optional;

/**
 * Provides latest release metadata from update source.
 */
public interface ReleaseProvider {

    Optional<GitHubRelease> fetchLatestRelease();

    default List<GitHubRelease> fetchRecentReleases(int limit) {
        return fetchLatestRelease()
                .stream()
                .limit(limit)
                .toList();
    }
}















