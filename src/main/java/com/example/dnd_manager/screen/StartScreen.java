package com.example.dnd_manager.screen;

import com.example.dnd_manager.store.StorageService;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import com.example.dnd_manager.domain.Character;

import java.util.List;

/**
 * Start screen with main navigation actions.
 */
public class StartScreen {

    private final Stage stage;
    private final StorageService storageService;

    public StartScreen(Stage stage, StorageService storageService) {
        this.stage = stage;
        this.storageService = storageService;
    }

    /**
     * Builds and returns the view.
     */
    public Parent getView() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Button createButton = new Button("Create new character");
        Button loadButton = new Button("Load character");

        createButton.setPrefWidth(250);
        loadButton.setPrefWidth(250);

        createButton.setOnAction(event -> openCharacterCreate());
        loadButton.setOnAction(event -> openCharacterLoad());

        root.getChildren().addAll(createButton, loadButton);
        return root;
    }

    private void openCharacterLoad() {
        List<String> names = storageService.listCharacterNames();

        if (names.isEmpty()) {
            System.out.println("No characters found");
            return;
        }

        // Пока просто берем первого для примера
        String name = names.getFirst();

        Character character = storageService.loadCharacter(name).orElseThrow();
        CharacterOverviewScreen overview = new CharacterOverviewScreen(character);

        stage.getScene().setRoot(overview);
    }

    private void openCharacterCreate() {
        CharacterCreateScreen createScreen = new CharacterCreateScreen(stage, storageService);
        stage.getScene().setRoot(createScreen.getView());
    }
}
