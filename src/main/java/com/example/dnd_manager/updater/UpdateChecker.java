package com.example.dnd_manager.updater;

import com.example.dnd_manager.info.version.AppInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class UpdateChecker {

    private static final String API_URL = "https://api.github.com/repos/JusteRYT/DnD_Manager/releases/latest";
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();

    public Optional<GitHubRelease> check() {
        return fetchLatestRelease().filter(release -> isNewer(release.tagName));
    }

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
            e.printStackTrace();
        }
        return Optional.empty();
    }

    boolean isNewer(String remoteVersion) {
        String current = AppInfo.getVersion().replace("v", "");
        String remote = remoteVersion.replace("v", "");

        String[] currParts = current.split("\\.");
        String[] remParts = remote.split("\\.");

        int length = Math.max(currParts.length, remParts.length);
        for (int i = 0; i < length; i++) {
            int curr = i < currParts.length ? Integer.parseInt(currParts[i]) : 0;
            int rem = i < remParts.length ? Integer.parseInt(remParts[i]) : 0;

            if (rem > curr) return true;
            if (rem < curr) return false;
        }
        return false;
    }
}
