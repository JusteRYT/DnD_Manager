package com.example.dnd_manager.info.skills;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Objects;

/**
 * Represents a single effect of a skill.
 *
 * <p>
 * Effect can be predefined (DAMAGE, HEAL, etc.) or custom-defined by the user.
 * For {@link TypeEffects#CUSTOM}, {@code customName} must be provided.
 * </p>
 */
@Getter
public final class SkillEffect {

    private final String type;
    private final String customName;
    private final String value;

    /**
     * Constructor used by Jackson and internal factory.
     */
    @JsonCreator
    public SkillEffect(
            @JsonProperty("type") String type,
            @JsonProperty("customName") String customName,
            @JsonProperty("value") String value
    ) {
        if (type == null) {
            throw new IllegalArgumentException("Effect type must not be null");
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Effect value must not be empty");
        }
        if (type.equals(TypeEffects.CUSTOM.getName()) && (customName == null || customName.isBlank())) {
            throw new IllegalArgumentException("Custom effect requires customName");
        }

        this.type = type;
        this.customName = type.equals(TypeEffects.CUSTOM.getName()) ? customName : null;
        this.value = value;
    }

    /**
     * Factory method for unified creation.
     */
    public static SkillEffect of(String type, String name, String value) {
        return new SkillEffect(type, name, value);
    }

    /**
     * Returns display name for UI.
     */
    @JsonIgnore
    public String getDisplayName() {
        return Objects.equals(type, TypeEffects.CUSTOM.getName()) ? customName : type;
    }

    @Override
    public String toString() {
        return getDisplayName() + ": " + value;
    }
}