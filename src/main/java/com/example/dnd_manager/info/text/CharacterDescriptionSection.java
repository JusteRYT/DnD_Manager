package com.example.dnd_manager.info.text;

import javafx.scene.layout.VBox;

/**
 * Section containing character description fields.
 */
public class CharacterDescriptionSection extends VBox {

    private final TextSection description;
    private final TextSection personality;
    private final TextSection backstory;

    public CharacterDescriptionSection() {
        setSpacing(15);

        description = new TextSection("Description", 3);
        personality = new TextSection("Personality", 3);
        backstory = new TextSection("Backstory", 5);

        getChildren().addAll(description, personality, backstory);
    }

    public String getDescription() {
        return description.getText();
    }

    public String getPersonality() {
        return personality.getText();
    }

    public String getBackstory() {
        return backstory.getText();
    }
}
