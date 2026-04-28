package com.example.dnd_manager.overview.ui.launchers;

import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterDescriptionFileExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void export_writesCharacterDescriptionToSelectedFile() throws Exception {
        Path outPath = tempDir.resolve("hero.txt");
        CharacterDescriptionSaveChooser chooser = (owner, characterName) -> outPath.toFile();
        CharacterDescriptionFileExporter exporter = new CharacterDescriptionFileExporter(chooser);

        Character character = new Character();
        character.setName("Hero");
        character.setDescription("Brave adventurer");
        character.setPersonality("Calm");
        character.setBackstory("From village");

        exporter.export(character, null);

        String content = Files.readString(outPath);
        assertTrue(content.toUpperCase().contains("HERO"));
        assertTrue(content.contains("Brave adventurer"));
    }

    @Test
    void export_doesNothingWhenChooserCancelled() {
        CharacterDescriptionSaveChooser chooser = (owner, characterName) -> null;
        CharacterDescriptionFileExporter exporter = new CharacterDescriptionFileExporter(chooser);

        Character character = new Character();
        character.setName("Hero");

        exporter.export(character, null);

        File[] files = tempDir.toFile().listFiles();
        assertTrue(files == null || files.length == 0);
    }
}












