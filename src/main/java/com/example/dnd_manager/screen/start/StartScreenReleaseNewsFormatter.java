package com.example.dnd_manager.screen.start;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.updater.model.GitHubRelease;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StartScreenReleaseNewsFormatter {

    private static final int SUMMARY_LIMIT = 165;
    private static final int MAX_SUMMARY_LINES = 4;
    private static final int RELEASES_LIMIT = 2;
    private static final int LINES_PER_RELEASE = 2;

    public StartScreenReleaseNews fromRelease(GitHubRelease release) {
        Objects.requireNonNull(release, "release must not be null");

        String title = firstPresent(release.name, release.tagName, I18n.t("news.latestRelease"));
        String summary = summarizeAll(release.body);
        String meta = firstPresent(release.tagName, release.publishedAt, I18n.t("news.githubRelease"));

        if (summary.isBlank()) {
            summary = I18n.t("news.emptyReleaseBody");
        }

        return new StartScreenReleaseNews(title, summary, meta);
    }

    public StartScreenReleaseNews fromReleases(List<GitHubRelease> releases) {
        if (releases == null || releases.isEmpty()) {
            return fallback();
        }

        List<GitHubRelease> visibleReleases = releases.stream()
                .filter(Objects::nonNull)
                .limit(RELEASES_LIMIT)
                .toList();
        if (visibleReleases.isEmpty()) {
            return fallback();
        }

        List<StartScreenReleaseNewsSection> sections = visibleReleases.stream()
                .map(this::toSection)
                .toList();
        String summary = sections.stream()
                .flatMap(section -> section.highlights().stream()
                        .map(highlight -> section.title() + ": " + highlight))
                .collect(Collectors.joining("\n"));
        if (summary.isBlank()) {
            summary = I18n.t("news.emptyReleaseBody");
        }

        String meta = visibleReleases.stream()
                .map(release -> firstPresent(release.tagName, release.publishedAt, ""))
                .filter(value -> !value.isBlank())
                .collect(Collectors.joining(" • "));
        if (meta.isBlank()) {
            meta = I18n.t("news.githubRelease");
        }

        return new StartScreenReleaseNews(I18n.t("news.latestReleases"), summary, meta, sections);
    }

    public StartScreenReleaseNews fallback() {
        return new StartScreenReleaseNews(
                I18n.t("news.futureReleaseTitle"),
                I18n.t("news.futureReleaseSummary"),
                I18n.t("news.futureReleaseMeta")
        );
    }

    private String summarizeAll(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }

        return extractHighlights(markdown).stream()
                .limit(MAX_SUMMARY_LINES)
                .collect(Collectors.joining("\n"));
    }

    private StartScreenReleaseNewsSection toSection(GitHubRelease release) {
        String version = firstPresent(release.name, release.tagName, I18n.t("news.latestRelease"));
        List<String> highlights = extractHighlights(release.body).stream()
                .limit(LINES_PER_RELEASE)
                .toList();
        if (highlights.isEmpty()) {
            highlights = List.of(I18n.t("news.emptyReleaseBody"));
        }
        String meta = firstPresent(release.tagName, release.publishedAt, I18n.t("news.githubRelease"));
        return new StartScreenReleaseNewsSection(version, highlights, meta);
    }

    private List<String> extractHighlights(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return List.of();
        }

        List<String> lines = new ArrayList<>();
        List<String> cleaned = Arrays.stream(markdown.split("\\R"))
                .map(this::cleanMarkdownLine)
                .filter(line -> !line.isBlank())
                .filter(line -> !line.equals("-"))
                .filter(line -> line.length() > 3)
                .toList();

        for (String line : cleaned) {
            if (line.startsWith("#")) {
                continue;
            }
            if (line.length() > SUMMARY_LIMIT) {
                lines.add(line.substring(0, SUMMARY_LIMIT - 3).trim() + "...");
            } else {
                lines.add(line);
            }
            if (lines.size() >= MAX_SUMMARY_LINES) {
                break;
            }
        }

        return lines;
    }

    private String cleanMarkdownLine(String line) {
        return line
                .replaceAll("^#+\\s*", "")
                .replaceAll("^[-*]\\s*", "")
                .replace("**", "")
                .replace("__", "")
                .replace("`", "")
                .trim();
    }

    private String firstPresent(String first, String second, String fallback) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        if (second != null && !second.isBlank()) {
            return second;
        }
        return fallback;
    }
}
