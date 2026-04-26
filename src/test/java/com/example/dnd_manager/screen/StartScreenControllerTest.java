package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.CharacterUseCases;
import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.domain.Character;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartScreenControllerTest {

    @Test
    void openCharacterCreate_usesFactoryAndNavigator() {
        FakeCharacterGateway gateway = new FakeCharacterGateway(List.of("Hero"));
        CharacterUseCases useCases = new CharacterUseCases(gateway);
        FakeScreenNavigator navigator = new FakeScreenNavigator();
        FakeStartScreenFlowFactory factory = new FakeStartScreenFlowFactory();
        OpenStartSpy openStartSpy = new OpenStartSpy();
        ErrorSpy errorSpy = new ErrorSpy();

        StartScreenController controller = new StartScreenController(
                navigator,
                useCases,
                factory,
                openStartSpy,
                errorSpy
        );

        controller.openCharacterCreate();

        assertEquals(1, factory.createCalls);
        assertSame(factory.createScreen, navigator.lastOpened);
        assertEquals(1, navigator.openCount);
        assertEquals(0, errorSpy.calls);
    }

    @Test
    void openCharacterEdit_whenNoCharacters_reportsErrorAndDoesNotNavigate() {
        FakeCharacterGateway gateway = new FakeCharacterGateway(List.of());
        CharacterUseCases useCases = new CharacterUseCases(gateway);
        FakeScreenNavigator navigator = new FakeScreenNavigator();
        FakeStartScreenFlowFactory factory = new FakeStartScreenFlowFactory();
        OpenStartSpy openStartSpy = new OpenStartSpy();
        ErrorSpy errorSpy = new ErrorSpy();

        StartScreenController controller = new StartScreenController(
                navigator,
                useCases,
                factory,
                openStartSpy,
                errorSpy
        );

        controller.openCharacterEdit();

        assertEquals(1, errorSpy.calls);
        assertEquals(0, navigator.openCount);
        assertEquals(0, factory.selectionCalls);
    }

    @Test
    void openCharacterLoad_whenCharactersExist_opensSelectionFromFactory() {
        FakeCharacterGateway gateway = new FakeCharacterGateway(List.of("Hero"));
        CharacterUseCases useCases = new CharacterUseCases(gateway);
        FakeScreenNavigator navigator = new FakeScreenNavigator();
        FakeStartScreenFlowFactory factory = new FakeStartScreenFlowFactory();
        OpenStartSpy openStartSpy = new OpenStartSpy();
        ErrorSpy errorSpy = new ErrorSpy();

        StartScreenController controller = new StartScreenController(
                navigator,
                useCases,
                factory,
                openStartSpy,
                errorSpy
        );

        controller.openCharacterLoad();

        assertEquals(1, factory.selectionCalls);
        assertEquals(1, navigator.openCount);
        assertSame(factory.selectionScreen, navigator.lastOpened);
        assertEquals(0, errorSpy.calls);
        assertEquals(Boolean.FALSE, factory.lastSelectionMode);
        assertSame(openStartSpy, factory.lastBackAction);
        assertTrue(factory.lastSelectionConsumer != null);
    }

    @Test
    void changeLanguageAndReload_runsOpenStartAction() {
        FakeCharacterGateway gateway = new FakeCharacterGateway(List.of("Hero"));
        CharacterUseCases useCases = new CharacterUseCases(gateway);
        FakeScreenNavigator navigator = new FakeScreenNavigator();
        FakeStartScreenFlowFactory factory = new FakeStartScreenFlowFactory();
        OpenStartSpy openStartSpy = new OpenStartSpy();
        ErrorSpy errorSpy = new ErrorSpy();

        StartScreenController controller = new StartScreenController(
                navigator,
                useCases,
                factory,
                openStartSpy,
                errorSpy
        );

        controller.changeLanguageAndReload();

        assertEquals(1, openStartSpy.calls);
        assertNull(navigator.lastOpened);
    }

    private static final class FakeScreenNavigator implements ScreenNavigator {
        private Node lastOpened;
        private int openCount;

        @Override
        public void open(Node view) {
            this.lastOpened = view;
            this.openCount++;
        }
    }

    private static final class OpenStartSpy implements Runnable {
        private int calls;

        @Override
        public void run() {
            calls++;
        }
    }

    private static final class ErrorSpy implements BiConsumer<String, String> {
        private int calls;

        @Override
        public void accept(String title, String message) {
            calls++;
        }
    }

    private static final class FakeStartScreenFlowFactory implements StartScreenFlowFactory {
        private final Parent createScreen = new VBox();
        private final Parent editScreen = new VBox();
        private final Parent overviewScreen = new VBox();
        private final Parent selectionScreen = new VBox();
        private final Parent transferScreen = new VBox();
        private final Parent assetScreen = new VBox();

        private int createCalls;
        private int selectionCalls;
        private Boolean lastSelectionMode;
        private Runnable lastBackAction;
        private Consumer<Character> lastSelectionConsumer;

        @Override
        public Parent createCharacterCreate(Runnable backToStartAction) {
            createCalls++;
            lastBackAction = backToStartAction;
            return createScreen;
        }

        @Override
        public Parent createCharacterEdit(Character character, Runnable backToStartAction) {
            lastBackAction = backToStartAction;
            return editScreen;
        }

        @Override
        public Parent createCharacterOverview(Character character, Runnable backToStartAction) {
            lastBackAction = backToStartAction;
            return overviewScreen;
        }

        @Override
        public Parent createCharacterSelection(
                boolean isEdit,
                Consumer<Character> onCharacterSelected,
                Runnable backToStartAction
        ) {
            selectionCalls++;
            lastSelectionMode = isEdit;
            lastSelectionConsumer = onCharacterSelected;
            lastBackAction = backToStartAction;
            return selectionScreen;
        }

        @Override
        public Parent createCharacterTransfer(Runnable backToStartAction) {
            lastBackAction = backToStartAction;
            return transferScreen;
        }

        @Override
        public Parent createAssetManager(Runnable backToStartAction) {
            lastBackAction = backToStartAction;
            return assetScreen;
        }
    }

    private static final class FakeCharacterGateway implements CharacterGateway {
        private final List<String> names;

        private FakeCharacterGateway(List<String> names) {
            this.names = new ArrayList<>(names);
        }

        @Override
        public void saveCharacter(Character character) {
            if (character != null && character.getName() != null && !character.getName().isBlank() && !names.contains(character.getName())) {
                names.add(character.getName());
            }
        }

        @Override
        public Optional<Character> loadCharacter(String name) {
            if (names.contains(name)) {
                Character character = new Character();
                character.setName(name);
                return Optional.of(character);
            }
            return Optional.empty();
        }

        @Override
        public List<String> listCharacterNames() {
            return new ArrayList<>(names);
        }

        @Override
        public void deleteCharacter(Character character) {
            if (character != null) {
                names.remove(character.getName());
            }
        }
    }
}

