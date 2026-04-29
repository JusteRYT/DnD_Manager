package com.example.dnd_manager.updater.port;

import com.example.dnd_manager.updater.release.CachedRelease;

import java.util.Optional;

/**
 * Stores the latest known release metadata for low-noise start screen news.
 */
public interface ReleaseCacheStore {

    Optional<CachedRelease> load();

    void save(CachedRelease release);
}
