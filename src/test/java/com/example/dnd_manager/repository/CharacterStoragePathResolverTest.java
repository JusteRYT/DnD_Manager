package com.example.dnd_manager.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterStoragePathResolverTest {

    @TempDir
    Path tempDir;

    private String originalAppdata;
    private String originalUserHome;

    @BeforeEach
    void setup() {
        originalAppdata = System.getenv("APPDATA");
        originalUserHome = System.getProperty("user.home");

        System.setProperty("user.home", tempDir.toString());
    }

    @Test
    @DisplayName("Миграция: Перенос данных, если в AppData пусто")
    void shouldMigrateWhenAppDataIsEmpty() throws IOException {
        // Создаем старую папку "Character" в корне запуска (tempDir)
        Path legacyBase = Paths.get("Character").toAbsolutePath();
        Files.createDirectories(legacyBase.resolve("Gimli"));
        Files.writeString(legacyBase.resolve("Gimli/Gimli.json"), "{\"name\":\"Gimli\"}");

        CharacterStoragePathResolver.migrateIfNeeded();

        Path newPath = CharacterStoragePathResolver.getCharacterDir("Gimli");
        assertThat(newPath.resolve("Gimli.json")).exists();
        assertThat(legacyBase).doesNotExist(); // Старая папка должна удалиться
    }

    @Test
    @DisplayName("Миграция: Выбор более свежего файла (Legacy новее)")
    void shouldPreferLegacyWhenItIsNewer() throws IOException, InterruptedException {
        String charName = "Legolas";
        Path newRoot = CharacterStoragePathResolver.getRoot();
        Path targetDir = newRoot.resolve(charName);
        Files.createDirectories(targetDir);
        Path appDataJson = targetDir.resolve(charName + ".json");
        Files.writeString(appDataJson, "Version: Old AppData");

        // Ставим время в прошлом
        Files.setLastModifiedTime(appDataJson, FileTime.from(Instant.now().minusSeconds(1000)));

        Path legacyDir = Paths.get("Character").resolve(charName).toAbsolutePath();
        Files.createDirectories(legacyDir);
        Path legacyJson = legacyDir.resolve(charName + ".json");
        Files.writeString(legacyJson, "Version: Fresh Legacy");
        // Ставим время — сейчас (новее)
        Files.setLastModifiedTime(legacyJson, FileTime.from(Instant.now()));

        CharacterStoragePathResolver.migrateIfNeeded();

        assertThat(Files.readString(appDataJson)).contains("Fresh Legacy");
    }

    @Test
    @DisplayName("Миграция: Удаление старой папки, если в AppData файл новее")
    void shouldDeleteLegacyWhenAppDataIsNewer() throws IOException {
        String charName = "Aragorn";
        Path targetDir = CharacterStoragePathResolver.getCharacterDir(charName);
        Files.createDirectories(targetDir);
        Path appDataJson = targetDir.resolve(charName + ".json");
        Files.writeString(appDataJson, "Modern Data");

        Path legacyDir = Paths.get("Character").resolve(charName).toAbsolutePath();
        Files.createDirectories(legacyDir);
        Path legacyJson = legacyDir.resolve(charName + ".json");
        Files.writeString(legacyJson, "Outdated Data");

        Files.setLastModifiedTime(legacyJson, FileTime.from(Instant.now().minusSeconds(5000)));

        CharacterStoragePathResolver.migrateIfNeeded();

        assertThat(Files.readString(appDataJson)).isEqualTo("Modern Data");
        assertThat(legacyDir).doesNotExist();
    }
}