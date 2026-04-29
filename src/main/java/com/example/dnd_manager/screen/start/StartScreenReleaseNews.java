package com.example.dnd_manager.screen.start;

import java.util.List;

public record StartScreenReleaseNews(
        String title,
        String summary,
        String meta,
        List<StartScreenReleaseNewsSection> sections
) {

    public StartScreenReleaseNews(String title, String summary, String meta) {
        this(title, summary, meta, List.of());
    }
}
