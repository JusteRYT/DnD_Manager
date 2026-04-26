package com.example.dnd_manager.updater;

/**
 * Compares app versions for update checks.
 */
public interface VersionComparator {

    boolean isRemoteNewer(String currentVersion, String remoteVersion);
}

