package com.example.dnd_manager.info.text;

import com.example.dnd_manager.info.text.dto.CharacterDescriptionData;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.screen.FormMode;
import com.example.dnd_manager.theme.AppTextSection;
import javafx.scene.layout.VBox;

/**
 * Section containing character description fields.
 * <p>
 * Supports CREATE and EDIT modes.
 */
public class CharacterDescriptionSection extends VBox {

    private final AppTextSection description;
    private final AppTextSection personality;
    private final AppTextSection backstory;

    /**
     * Creates section in CREATE mode.
     */
    public CharacterDescriptionSection() {
        this(FormMode.CREATE, null);
    }

    /**
     * Creates section with given mode and initial data.
     *
     * @param mode        section mode
     * @param initialData initial description data (used in EDIT mode)
     */
    public CharacterDescriptionSection(
            FormMode mode,
            CharacterDescriptionData initialData
    ) {
        setSpacing(15);

        description = new AppTextSection(I18n.t("label.textSection.description"));
        personality = new AppTextSection(I18n.t("label.textSection.personality"));
        backstory = new AppTextSection(I18n.t("label.textSection.backstory"));

        if (mode == FormMode.EDIT && initialData != null) {
            description.setText(initialData.description());
            personality.setText(initialData.personality());
            backstory.setText(initialData.backstory());
        }

        getChildren().addAll(description, personality, backstory);
    }

    /**
     * Returns description data from UI.
     *
     * @return immutable description DTO
     */
    public CharacterDescriptionData getData() {
        return new CharacterDescriptionData(
                description.getText(),
                personality.getText(),
                backstory.getText()
        );
    }
}
