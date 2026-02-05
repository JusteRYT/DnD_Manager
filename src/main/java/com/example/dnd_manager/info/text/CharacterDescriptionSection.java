package com.example.dnd_manager.info.text;

import com.example.dnd_manager.theme.AppTextSection;
import javafx.scene.layout.VBox;

/**
 * Section containing character description fields.
 */
public class CharacterDescriptionSection extends VBox {

    private final AppTextSection description;
    private final AppTextSection personality;
    private final AppTextSection backstory;

    public CharacterDescriptionSection() {
        setSpacing(15);

        description = new AppTextSection("Description");
        personality = new AppTextSection("Personality");
        backstory = new AppTextSection("Backstory");

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
