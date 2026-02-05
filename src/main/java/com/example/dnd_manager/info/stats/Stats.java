package com.example.dnd_manager.info.stats;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Character stats with Jackson-friendly serialization
 */
public class Stats {

    private final Map<String, Integer> values = new HashMap<>();

    public Stats() {
        for (StatEnum stat : StatEnum.values()) {
            values.put(stat.name(), 0);
        }
    }

    public int get(StatEnum stat) {
        return values.get(stat.name());
    }

    public void increase(StatEnum stat) {
        values.put(stat.name(), values.get(stat.name()) + 1);
    }

    public void decrease(StatEnum stat) {
        values.put(stat.name(), values.get(stat.name()) - 1);
    }

    public void copyFrom(Stats other) {
        for (StatEnum stat : StatEnum.values()) {
            values.put(stat.name(), other.get(stat));
        }
    }

    @JsonAnyGetter
    public Map<String, Integer> any() {
        return values;
    }

    @JsonAnySetter
    public void set(String key, Integer value) {
        values.put(key, value);
    }

    /**
     * Returns immutable enum-based stats map for UI usage.
     *
     * @return map of StatEnum to value
     */
    public Map<StatEnum, Integer> asMap() {
        Map<StatEnum, Integer> result = new EnumMap<>(StatEnum.class);
        for (StatEnum stat : StatEnum.values()) {
            result.put(stat, get(stat));
        }
        return Map.copyOf(result);
    }
}