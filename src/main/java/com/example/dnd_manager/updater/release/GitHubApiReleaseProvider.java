package com.example.dnd_manager.updater.release;

import com.example.dnd_manager.updater.port.ReleaseProvider;

import com.example.dnd_manager.updater.model.GitHubRelease;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class GitHubApiReleaseProvider implements ReleaseProvider {

    private static final String API_URL = "https://api.github.com/repos/JusteRYT/DnD_Manager/releases/latest";
    private static final String RECENT_RELEASES_API_URL = "https://api.github.com/repos/JusteRYT/DnD_Manager/releases?per_page=%d";

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();

    @Override
    public Optional<GitHubRelease> fetchLatestRelease() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("User-Agent", "DnD-Manager-App")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Optional.of(mapper.readValue(response.body(), GitHubRelease.class));
            }
        } catch (Exception e) {
            log.error("Error while fetching latest release", e);
        }
        return Optional.empty();
    }

    @Override
    public List<GitHubRelease> fetchRecentReleases(int limit) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RECENT_RELEASES_API_URL.formatted(Math.max(1, limit))))
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("User-Agent", "DnD-Manager-App")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Arrays.stream(mapper.readValue(response.body(), GitHubRelease[].class))
                        .limit(limit)
                        .toList();
            }
        } catch (Exception e) {
            log.error("Error while fetching recent releases", e);
        }
        return List.of();
    }
}
















