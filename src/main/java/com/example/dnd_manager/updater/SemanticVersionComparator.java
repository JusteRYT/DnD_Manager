package com.example.dnd_manager.updater;

/**
 * Numeric dot-separated version comparator with optional "v" prefix.
 */
public class SemanticVersionComparator implements VersionComparator {

    @Override
    public boolean isRemoteNewer(String currentVersion, String remoteVersion) {
        String current = sanitize(currentVersion);
        String remote = sanitize(remoteVersion);

        String[] currentParts = current.split("\\.");
        String[] remoteParts = remote.split("\\.");

        int length = Math.max(currentParts.length, remoteParts.length);
        for (int i = 0; i < length; i++) {
            int curr = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int rem = i < remoteParts.length ? Integer.parseInt(remoteParts[i]) : 0;

            if (rem > curr) {
                return true;
            }
            if (rem < curr) {
                return false;
            }
        }
        return false;
    }

    private String sanitize(String version) {
        if (version == null) {
            return "0";
        }
        return version.trim().replace("v", "");
    }
}

