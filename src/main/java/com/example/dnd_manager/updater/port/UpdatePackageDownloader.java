package com.example.dnd_manager.updater.port;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiConsumer;

/**
 * Downloads update package file.
 */
public interface UpdatePackageDownloader {

    void download(String url, Path destination, BiConsumer<Long, Long> progressCallback) throws IOException, InterruptedException;
}














