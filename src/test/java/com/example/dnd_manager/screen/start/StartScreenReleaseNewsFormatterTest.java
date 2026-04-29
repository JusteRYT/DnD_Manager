package com.example.dnd_manager.screen.start;

import com.example.dnd_manager.updater.model.GitHubRelease;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartScreenReleaseNewsFormatterTest {

    @Test
    void fromRelease_summarizesSeveralLines() {
        GitHubRelease release = new GitHubRelease();
        release.name = "v1.2.3";
        release.body = "# Заголовок\n\n* Добавлена новая механика\n* Балансирование классов\n\n- Исправлены баги\n- Улучшен интерфейс\n- Дополнительный пункт";

        StartScreenReleaseNewsFormatter formatter = new StartScreenReleaseNewsFormatter();
        StartScreenReleaseNews news = formatter.fromRelease(release);

        String[] lines = news.summary().split("\\R");

        assertEquals("v1.2.3", news.title());
        assertFalse(news.meta().isBlank());
        assertNotNull(news.summary());
        assertTrue(lines.length <= 4);
        for (String line : lines) {
            assertFalse(line.isBlank());
            assertTrue(line.length() <= 165);
        }
    }

    @Test
    void fallback_usesLocalizedDefaults() {
        StartScreenReleaseNewsFormatter formatter = new StartScreenReleaseNewsFormatter();
        StartScreenReleaseNews news = formatter.fallback();

        assertFalse(news.title().isBlank());
        assertFalse(news.summary().isBlank());
        assertFalse(news.meta().isBlank());
    }

    @Test
    void fromReleases_showsTwoLatestReleaseVersions() {
        GitHubRelease latest = release("v1.0.6", "Latest", "* Новая карточка\n* Улучшена верстка");
        GitHubRelease previous = release("v1.0.5", "Previous", "* Исправлен импорт\n* Обновлены стили");

        StartScreenReleaseNewsFormatter formatter = new StartScreenReleaseNewsFormatter();
        StartScreenReleaseNews news = formatter.fromReleases(List.of(latest, previous));

        assertTrue(news.summary().contains("Latest: Новая карточка"));
        assertTrue(news.summary().contains("Previous: Исправлен импорт"));
        assertTrue(news.meta().contains("v1.0.6"));
        assertTrue(news.meta().contains("v1.0.5"));
    }

    private static GitHubRelease release(String tagName, String name, String body) {
        GitHubRelease release = new GitHubRelease();
        release.tagName = tagName;
        release.name = name;
        release.body = body;
        release.publishedAt = "2026-04-29T00:00:00Z";
        return release;
    }
}
