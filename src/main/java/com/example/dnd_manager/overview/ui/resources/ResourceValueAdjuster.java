package com.example.dnd_manager.overview.ui.resources;

import com.example.dnd_manager.domain.Character;

import java.util.Objects;

public class ResourceValueAdjuster {

    public int change(Character target, int delta, CharacterResourceMetric metric) {
        Objects.requireNonNull(target, "target must not be null");
        Objects.requireNonNull(metric, "metric must not be null");

        int current = Math.max(0, metric.getCurrent(target));
        int max = Math.max(0, metric.getMax(target));
        int next = Math.max(0, Math.min(current + delta, max));
        metric.setCurrent(target, next);
        return next;
    }
}













