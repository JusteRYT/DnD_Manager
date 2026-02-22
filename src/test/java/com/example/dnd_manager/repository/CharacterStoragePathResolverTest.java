package com.example.dnd_manager.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterStoragePathResolverTest {

    @TempDir
    Path tempDir;

    private final Logger log = LoggerFactory.getLogger(CharacterStoragePathResolverTest.class);

    private String originalUserDir;
    private String originalUserHome;
    private String originalOsName;

    @BeforeEach
    void setup() {
        originalUserDir = System.getProperty("user.dir");
        originalUserHome = System.getProperty("user.home");
        originalOsName = System.getProperty("os.name");

        // Подменяем всё на tempDir
        String tempDirPath = tempDir.toAbsolutePath().toString();
        System.setProperty("user.dir", tempDirPath);
        System.setProperty("user.home", tempDirPath);
        System.setProperty("os.name", "Linux"); // Чтобы игнорировать системный APPDATA

        log.info("Test sandbox initialized at: {}", tempDirPath);
    }

    @AfterEach
    void tearDown() {
        System.setProperty("user.dir", originalUserDir);
        System.setProperty("user.home", originalUserHome);
        System.setProperty("os.name", originalOsName);
    }

    @Test
    @DisplayName("Миграция: Перенос данных из папки Character в новую структуру")
    void shouldMigrateAndCleanupFiles() throws IOException {
        String charName = "Gimli";

        // ВАЖНО: Используем Paths.get, как и в миграторе, чтобы пути совпали
        Path legacyRoot = Paths.get("Character").toAbsolutePath();
        Path legacyCharDir = legacyRoot.resolve(charName);
        Files.createDirectories(legacyCharDir);

        Path jsonFile = legacyCharDir.resolve(charName + ".json");
        Files.writeString(jsonFile, "{\"name\":\"Gimli\"}");

        log.info("Legacy path created at: {}", legacyRoot);

        CharacterStoragePathResolver.migrateIfNeeded();

        Path newPath = CharacterStoragePathResolver.getCharacterDir(charName);

        // Проверяем, что файл переместился
        assertThat(newPath.resolve(charName + ".json")).exists();
        // Проверяем, что старая папка персонажа и корень удалены
        assertThat(Files.exists(legacyCharDir)).isFalse();
        assertThat(Files.exists(legacyRoot)).isFalse();
    }

    @Test
    @DisplayName("Миграция: Удаление устаревших файлов Aragorn")
    void shouldDeleteLegacyWhenAppDataIsNewer() throws IOException {
        String charName = "Aragorn";

        // 1. Создаем новую структуру
        Path targetDir = CharacterStoragePathResolver.getCharacterDir(charName);
        Files.createDirectories(targetDir);
        Path appDataJson = targetDir.resolve(charName + ".json");
        Files.writeString(appDataJson, "Modern Data");
        Files.setLastModifiedTime(appDataJson, FileTime.from(Instant.now()));

        // 2. Создаем старую структуру через Paths.get
        Path legacyRoot = Paths.get("Character").toAbsolutePath();
        Path legacyCharDir = legacyRoot.resolve(charName);
        Files.createDirectories(legacyCharDir);

        Path legacyJson = legacyCharDir.resolve(charName + ".json");
        Files.writeString(legacyJson, "Outdated Data");
        // Ставим время на час назад
        Files.setLastModifiedTime(legacyJson, FileTime.from(Instant.now().minusSeconds(3600)));

        CharacterStoragePathResolver.migrateIfNeeded();

        // Должен остаться новый файл
        assertThat(Files.readString(appDataJson)).isEqualTo("Modern Data");
        // Старое должно быть удалено
        assertThat(Files.exists(legacyCharDir)).isFalse();
        assertThat(Files.exists(legacyRoot)).isFalse();
    }
}