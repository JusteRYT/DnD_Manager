package com.example.dnd_manager.updater.port;

import com.example.dnd_manager.updater.model.GitHubRelease;

import java.util.Optional;

/**
 * Provides latest release metadata from update source.
 */
public interface ReleaseProvider {

    Optional<GitHubRelease> fetchLatestRelease();
}















