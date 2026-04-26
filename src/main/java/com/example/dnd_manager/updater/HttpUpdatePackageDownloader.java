package com.example.dnd_manager.updater;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * HTTP downloader for update package.
 */
public class HttpUpdatePackageDownloader implements UpdatePackageDownloader {

    private final HttpClient httpClient;

    public HttpUpdatePackageDownloader() {
        this(HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build());
    }

    HttpUpdatePackageDownloader(HttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
    }

    @Override
    public void download(String url, Path destination, BiConsumer<Long, Long> progressCallback) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

        long totalSize = Long.parseLong(response.headers().firstValue("Content-Length").orElse("-1"));

        try (InputStream input = response.body();
             OutputStream output = Files.newOutputStream(destination)) {

            byte[] buffer = new byte[8192];
            long downloaded = 0;
            int read;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
                downloaded += read;
                if (progressCallback != null) {
                    progressCallback.accept(downloaded, totalSize);
                }
            }
        }
    }
}

