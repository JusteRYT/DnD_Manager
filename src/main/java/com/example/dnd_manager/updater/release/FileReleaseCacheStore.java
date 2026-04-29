package com.example.dnd_manager.updater.release;

import com.example.dnd_manager.updater.model.GitHubRelease;
import com.example.dnd_manager.updater.port.ReleaseCacheStore;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FileReleaseCacheStore implements ReleaseCacheStore {

    private static final String APP_FOLDER_NAME = "DnD_Manager";
    private static final String CACHE_DIR_NAME = "cache";
    private static final String RELEASE_CACHE_FILE_NAME = "latest-release.json";

    private final ObjectMapper mapper;
    private final Path cacheFile;

    public FileReleaseCacheStore() {
        this(defaultCacheFile(), new ObjectMapper());
    }

    public FileReleaseCacheStore(Path cacheFile) {
        this(cacheFile, new ObjectMapper());
    }

    FileReleaseCacheStore(Path cacheFile, ObjectMapper mapper) {
        this.cacheFile = cacheFile;
        this.mapper = mapper;
    }

    @Override
    public Optional<CachedRelease> load() {
        if (!Files.exists(cacheFile)) {
            return Optional.empty();
        }

        try {
            ReleaseCacheDto dto = mapper.readValue(cacheFile.toFile(), ReleaseCacheDto.class);
            return Optional.of(new CachedRelease(dto.normalizedReleases(), Instant.parse(dto.cachedAt())));
        } catch (Exception ex) {
            log.warn("Failed to read release news cache: {}", cacheFile, ex);
            return Optional.empty();
        }
    }

    @Override
    public void save(CachedRelease release) {
        try {
            Files.createDirectories(cacheFile.getParent());
            ReleaseCacheDto dto = ReleaseCacheDto.from(release);
            mapper.writerWithDefaultPrettyPrinter().writeValue(cacheFile.toFile(), dto);
        } catch (Exception ex) {
            log.warn("Failed to write release news cache: {}", cacheFile, ex);
        }
    }

    private static Path defaultCacheFile() {
        String os = System.getProperty("os.name").toLowerCase();
        Path appRoot;
        if (os.contains("win")) {
            appRoot = Paths.get(System.getenv("APPDATA"), APP_FOLDER_NAME);
        } else {
            appRoot = Paths.get(System.getProperty("user.home"), "." + APP_FOLDER_NAME.toLowerCase());
        }
        return appRoot.resolve(CACHE_DIR_NAME).resolve(RELEASE_CACHE_FILE_NAME).toAbsolutePath();
    }

    private record ReleaseCacheDto(List<GitHubRelease> releases, GitHubRelease release, String cachedAt) {

        @JsonCreator
        private ReleaseCacheDto(
                @JsonProperty("releases") List<GitHubRelease> releases,
                @JsonProperty("release") GitHubRelease release,
                @JsonProperty("cachedAt") String cachedAt
        ) {
            this.release = release;
            this.releases = releases;
            this.cachedAt = cachedAt;
        }

        private static ReleaseCacheDto from(CachedRelease release) {
            return new ReleaseCacheDto(release.releases(), null, release.cachedAt().toString());
        }

        private List<GitHubRelease> normalizedReleases() {
            if (releases != null && !releases.isEmpty()) {
                return releases;
            }
            if (release != null) {
                return List.of(release);
            }
            return List.of();
        }
    }
}
