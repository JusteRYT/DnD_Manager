package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class FamiliarLoreSectionAppender {

    private final FamiliarSectionStyleProvider styleProvider;

    public FamiliarLoreSectionAppender() {
        this(new FamiliarSectionStyleProvider());
    }

    FamiliarLoreSectionAppender(FamiliarSectionStyleProvider styleProvider) {
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
    }

    public void append(VBox container, Character familiar) {
        addTextSection(container, I18n.t("label.textSection.description"), familiar.getDescription());
        addTextSection(container, I18n.t("label.textSection.personality"), familiar.getPersonality());
    }

    private void addTextSection(VBox container, String title, String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        VBox box = new VBox(2, createHeaderLabel(title.toUpperCase()), new Label(text));
        ((Label) box.getChildren().get(1)).setWrapText(true);
        box.getChildren().get(1).setStyle(styleProvider.loreTextStyle());
        container.getChildren().add(box);
    }

    private Label createHeaderLabel(String text) {
        Label label = new Label(text);
        label.setStyle(styleProvider.iconHeaderStyle());
        return label;
    }
}
