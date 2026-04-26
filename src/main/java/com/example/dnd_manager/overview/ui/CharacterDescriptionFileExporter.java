package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.service.CharacterExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CharacterDescriptionFileExporter {

    private static final Logger log = LoggerFactory.getLogger(CharacterDescriptionFileExporter.class);

    private final CharacterDescriptionSaveChooser saveChooser;

    public CharacterDescriptionFileExporter() {
        this(new JavaFxCharacterDescriptionSaveChooser());
    }

    CharacterDescriptionFileExporter(CharacterDescriptionSaveChooser saveChooser) {
        this.saveChooser = Objects.requireNonNull(saveChooser, "saveChooser must not be null");
    }

    public void export(Character character, Stage owner) {
        File file = saveChooser.choose(owner, character.getName());
        if (file == null) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) {
            writer.print(CharacterExporter.generateFullDescription(character));
        } catch (IOException ex) {
            log.error("Failed to export character description to {}", file, ex);
        }
    }
}
