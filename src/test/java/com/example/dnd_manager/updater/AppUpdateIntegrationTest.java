package com.example.dnd_manager.updater;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class AppUpdateIntegrationTest {

    private final UpdateChecker checker = new UpdateChecker();

    @Test
    @DisplayName("Verify GitHub API Connectivity and Data Structure")
    void testGitHubConnectionAndParsing() {
        Optional<GitHubRelease> releaseOpt = checker.fetchLatestRelease();

        assertTrue(releaseOpt.isPresent(), "Connection failed or repository not found");

        GitHubRelease release = releaseOpt.get();

        assertAll("Validate GitHub Release Object",
                () -> assertNotNull(release.tagName, "Missing tag_name"),
                () -> assertFalse(release.assets.isEmpty(), "No assets found in release"),
                () -> {
                    boolean hasZip = release.assets.stream()
                            .anyMatch(a -> a.name.endsWith(".zip"));
                    assertTrue(hasZip, "Release must contain a .zip file");
                }
        );

        System.out.println("Latest Release: " + release.tagName);
        System.out.println("Download URL: " + release.assets.get(0).downloadUrl);
    }
}