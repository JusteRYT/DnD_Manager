package com.example.dnd_manager.application.service;
import com.example.dnd_manager.infrastructure.persistence.CharacterPathProvider;
import com.example.dnd_manager.infrastructure.persistence.DefaultCharacterPathProvider;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import java.util.zip.*;

/**
 * Default implementation of character import/export logic.
 */
public class CharacterTransferServiceImpl implements CharacterTransferService {

    private final CharacterPathProvider pathProvider;

    public CharacterTransferServiceImpl() {
        this(new DefaultCharacterPathProvider());
    }

    public CharacterTransferServiceImpl(CharacterPathProvider pathProvider) {
        this.pathProvider = Objects.requireNonNull(pathProvider, "pathProvider must not be null");
    }

    @Override
    public void exportCharacter(String characterName, File targetZipFile) throws IOException {

        pathProvider.ensureRootExists();

        Path sourceDir = pathProvider.getCharacterDir(characterName);

        if (Files.notExists(sourceDir)) {
            throw new IOException("Character folder not found: " + sourceDir);
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetZipFile));
             var paths = Files.walk(sourceDir)) {

            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Path relativePath = sourceDir.relativize(path);
                            ZipEntry entry = new ZipEntry(relativePath.toString().replace("\\", "/"));

                            zos.putNextEntry(entry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
    }

    @Override
    public void importCharacter(File zipFile) throws IOException {

        pathProvider.ensureRootExists();
        Path root = pathProvider.getRoot();

        String fileName = zipFile.getName();
        String characterName = fileName.endsWith(".zip")
                ? fileName.substring(0, fileName.length() - 4)
                : fileName;

        Path characterDir = root.resolve(characterName);
        Files.createDirectories(characterDir);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                Path entryPath = Paths.get(entryName);
                if (entryPath.startsWith(characterName)) {

                    if (entryPath.getNameCount() == 1) {
                        continue;
                    }
                    entryPath = entryPath.subpath(1, entryPath.getNameCount());
                }
                Path newFile = characterDir.resolve(entryPath).normalize();

                if (!newFile.startsWith(characterDir)) {
                    throw new IOException("Invalid zip entry: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(newFile);
                } else {
                    if (newFile.getParent() != null) {
                        Files.createDirectories(newFile.getParent());
                    }
                    Files.copy(zis, newFile, StandardCopyOption.REPLACE_EXISTING);
                }

                zis.closeEntry();
            }
        }
    }
}












