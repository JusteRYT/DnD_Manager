package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TopBarControllerTest {

    @Test
    void dialogActions_delegateToLaunchers() {
        Character character = new Character();
        character.setName("Hero");
        SaveCounterGateway gateway = new SaveCounterGateway();
        SaveCharacterUseCase saveCharacterUseCase = new SaveCharacterUseCase(gateway);

        TestExporter exporter = new TestExporter();
        TestDescriptionLauncher descriptionLauncher = new TestDescriptionLauncher();
        TestNotesLauncher notesLauncher = new TestNotesLauncher();
        TestEditStatsLauncher editStatsLauncher = new TestEditStatsLauncher();
        TestLevelUpLauncher levelUpLauncher = new TestLevelUpLauncher();

        TopBarController controller = new TopBarController(
                character,
                saveCharacterUseCase,
                () -> {
                },
                exporter,
                new CharacterSaveStringService(saveCharacterUseCase),
                descriptionLauncher,
                notesLauncher,
                editStatsLauncher,
                levelUpLauncher
        );

        controller.exportDescription(null);
        controller.showDescription(null);
        controller.showNotes(null);

        AtomicInteger editUpdates = new AtomicInteger();
        AtomicInteger levelUpdates = new AtomicInteger();
        controller.openEditStats(null, editUpdates::incrementAndGet);
        controller.openLevelUp(null, levelUpdates::incrementAndGet);

        assertSame(character, exporter.characterRef.get());
        assertSame(character, descriptionLauncher.characterRef.get());
        assertSame(character, notesLauncher.characterRef.get());
        assertSame(character, editStatsLauncher.characterRef.get());
        assertSame(character, levelUpLauncher.characterRef.get());

        assertTrue(editStatsLauncher.onUpdatedRef.get() != null);
        assertTrue(levelUpLauncher.onUpdatedRef.get() != null);

        editStatsLauncher.onUpdatedRef.get().run();
        levelUpLauncher.onUpdatedRef.get().run();
        assertEquals(1, editUpdates.get());
        assertEquals(1, levelUpdates.get());
    }

    @Test
    void persistSaveString_andBackToStart_work() {
        Character character = new Character();
        character.setName("Hero");
        SaveCounterGateway gateway = new SaveCounterGateway();
        SaveCharacterUseCase saveCharacterUseCase = new SaveCharacterUseCase(gateway);

        AtomicInteger backCounter = new AtomicInteger();
        TopBarController controller = new TopBarController(
                character,
                saveCharacterUseCase,
                backCounter::incrementAndGet,
                new TestExporter(),
                new CharacterSaveStringService(saveCharacterUseCase),
                new TestDescriptionLauncher(),
                new TestNotesLauncher(),
                new TestEditStatsLauncher(),
                new TestLevelUpLauncher()
        );

        controller.persistSaveString("  ABC  ");
        controller.backToStart();

        assertEquals("ABC", character.getSaveString());
        assertEquals(1, gateway.saveCount.get());
        assertEquals(1, backCounter.get());
    }

    private static class SaveCounterGateway implements CharacterGateway {
        private final AtomicInteger saveCount = new AtomicInteger();

        @Override
        public void saveCharacter(Character character) {
            saveCount.incrementAndGet();
        }

        @Override
        public Optional<Character> loadCharacter(String name) {
            return Optional.empty();
        }

        @Override
        public List<String> listCharacterNames() {
            return List.of();
        }

        @Override
        public void deleteCharacter(Character character) {
        }
    }

    private static class TestExporter extends CharacterDescriptionFileExporter {
        private final AtomicReference<Character> characterRef = new AtomicReference<>();

        TestExporter() {
            super((owner, defaultCharacterName) -> null);
        }

        @Override
        public void export(Character character, Stage owner) {
            characterRef.set(character);
        }
    }

    private static class TestDescriptionLauncher implements DescriptionDialogLauncher {
        private final AtomicReference<Character> characterRef = new AtomicReference<>();

        @Override
        public void show(Stage owner, Character character) {
            characterRef.set(character);
        }
    }

    private static class TestNotesLauncher implements NotesDialogLauncher {
        private final AtomicReference<Character> characterRef = new AtomicReference<>();

        @Override
        public void show(Stage owner, Character character) {
            characterRef.set(character);
        }
    }

    private static class TestEditStatsLauncher implements EditStatsDialogLauncher {
        private final AtomicReference<Character> characterRef = new AtomicReference<>();
        private final AtomicReference<Runnable> onUpdatedRef = new AtomicReference<>();

        @Override
        public void show(Stage owner, Character character, SaveCharacterUseCase saveCharacterUseCase, Runnable onUpdated) {
            characterRef.set(character);
            onUpdatedRef.set(onUpdated);
        }
    }

    private static class TestLevelUpLauncher implements LevelUpDialogLauncher {
        private final AtomicReference<Character> characterRef = new AtomicReference<>();
        private final AtomicReference<Runnable> onUpdatedRef = new AtomicReference<>();

        @Override
        public void show(Stage owner, Character character, SaveCharacterUseCase saveCharacterUseCase, Runnable onLevelUpdated) {
            characterRef.set(character);
            onUpdatedRef.set(onLevelUpdated);
        }
    }
}

