package com.example.dnd_manager.info.text.dto;

/**
 * Immutable character description data.
 *
 * @param description character description
 * @param personality character personality
 * @param backstory   character backstory
 */
public record CharacterDescriptionData(
        String description,
        String personality,
        String backstory
) {
}
