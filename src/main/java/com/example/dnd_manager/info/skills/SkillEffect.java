package com.example.dnd_manager.info.skills;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

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

    private final TypeEffects type;
    private final String customName;
    private final String value;

    /**
     * Constructor used by Jackson and internal factory.
     */
    @JsonCreator
    public SkillEffect(
            @JsonProperty("type") TypeEffects type,
            @JsonProperty("customName") String customName,
            @JsonProperty("value") String value
    ) {
        if (type == null) {
            throw new IllegalArgumentException("Effect type must not be null");
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Effect value must not be empty");
        }
        if (type == TypeEffects.CUSTOM && (customName == null || customName.isBlank())) {
            throw new IllegalArgumentException("Custom effect requires customName");
        }

        this.type = type;
        this.customName = type == TypeEffects.CUSTOM ? customName : null;
        this.value = value;
    }

    /**
     * Factory method for unified creation.
     */
    public static SkillEffect of(TypeEffects type, String name, String value) {
        return new SkillEffect(type, name, value);
    }

    /**
     * Returns display name for UI.
     */
    @JsonIgnore
    public String getDisplayName() {
        return type == TypeEffects.CUSTOM ? customName : type.name();
    }

    @Override
    public String toString() {
        return getDisplayName() + ": " + value;
    }
}