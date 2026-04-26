package com.example.dnd_manager.updater;

import java.util.Optional;

/**
 * Provides latest release metadata from update source.
 */
public interface ReleaseProvider {

    Optional<GitHubRelease> fetchLatestRelease();
}

