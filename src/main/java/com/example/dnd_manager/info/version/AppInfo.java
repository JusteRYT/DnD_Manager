package com.example.dnd_manager.info.version;

/**
 * Provides application metadata such as version.
 * <p>
 * This class follows Single Responsibility Principle.
 */
public final class AppInfo {

    private static final String VERSION = "1.0.3";

    private AppInfo() {
    }

    /**
     * Returns application version.
     *
     * @return version string
     */
    public static String getVersion() {
        return VERSION;
    }
}
