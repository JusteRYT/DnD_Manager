package com.example.dnd_manager.updater;

import com.example.dnd_manager.info.version.AppInfo;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class UpdateChecker {

    private final ReleaseProvider releaseProvider;
    private final VersionComparator versionComparator;
    private final Supplier<String> currentVersionSupplier;

    public UpdateChecker() {
        this(
                new GitHubApiReleaseProvider(),
                new SemanticVersionComparator(),
                AppInfo::getVersion
        );
    }

    UpdateChecker(
            ReleaseProvider releaseProvider,
            VersionComparator versionComparator,
            Supplier<String> currentVersionSupplier
    ) {
        this.releaseProvider = Objects.requireNonNull(releaseProvider, "releaseProvider must not be null");
        this.versionComparator = Objects.requireNonNull(versionComparator, "versionComparator must not be null");
        this.currentVersionSupplier = Objects.requireNonNull(currentVersionSupplier, "currentVersionSupplier must not be null");
    }

    public Optional<GitHubRelease> check() {
        return fetchLatestRelease().filter(release -> isNewer(release.tagName));
    }

    public Optional<GitHubRelease> fetchLatestRelease() {
        return releaseProvider.fetchLatestRelease();
    }

    boolean isNewer(String remoteVersion) {
        return versionComparator.isRemoteNewer(currentVersionSupplier.get(), remoteVersion);
    }
}
