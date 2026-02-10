package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppScrollPaneFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

/**
 * Screen for selecting a character.
 * Supports any action on character selection via callback.
 */
public class CharacterSelectionScreen extends VBox {

    public CharacterSelectionScreen(Stage stage, StorageService storageService, Consumer<Character> onCharacterSelected) {
        setSpacing(10);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #1e1e1e;");
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Select Character");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #ffffff; -fx-font-weight: bold;");
        getChildren().add(title);

        List<String> names = storageService.listCharacterNames();
        if (names.isEmpty()) {
            Label emptyLabel = new Label("No characters found");
            emptyLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px;");
            getChildren().add(emptyLabel);
            return;
        }

        VBox charactersList = new VBox(8);
        charactersList.setAlignment(Pos.CENTER);

        for (String name : names) {
            Character character = storageService.loadCharacter(name).orElse(null);
            if (character == null) {
                continue;
            }

            // --- Avatar ---
            ImageView avatar = new ImageView(new Image(
                    CharacterAssetResolver.resolve(name, character.getAvatarImage())
            ));
            avatar.setFitWidth(48);
            avatar.setFitHeight(48);

            // --- Name ---
            Label nameLabel = new Label(name);
            nameLabel.setStyle("""
                    -fx-text-fill: #ffffff;
                    -fx-font-size: 16px;
                    -fx-font-weight: bold;
                    """);

            // --- LEFT: avatar + name ---
            HBox leftBox = new HBox(10, avatar, nameLabel);
            leftBox.setAlignment(Pos.CENTER_LEFT);

            // --- CENTER: reserved for future info ---
            HBox centerBox = new HBox();
            centerBox.setAlignment(Pos.CENTER);

            // --- RIGHT: delete button ---
            Button deleteBtn = AppButtonFactory.customButton(
                    "Delete",
                    70,
                    AppTheme.BUTTON_DANGER,
                    AppTheme.BUTTON_DANGER_HOVER
            );
            deleteBtn.setFocusTraversable(false);
            deleteBtn.setOnAction(e -> {
                e.consume();
                storageService.deleteCharacter(character);
                charactersList.getChildren().removeIf(
                        node -> node.getUserData() == character
                );
            });

            // --- ROW LAYOUT ---
            javafx.scene.layout.BorderPane characterRow = new javafx.scene.layout.BorderPane();
            characterRow.setLeft(leftBox);
            characterRow.setCenter(centerBox);
            characterRow.setRight(deleteBtn);

            javafx.scene.layout.BorderPane.setAlignment(deleteBtn, Pos.CENTER_RIGHT);
            javafx.scene.layout.BorderPane.setMargin(deleteBtn, new Insets(0, 4, 0, 10));

            characterRow.setPadding(new Insets(8));
            characterRow.setUserData(character);

            characterRow.setStyle("""
                    -fx-background-color: #2e2e2e;
                    -fx-background-radius: 6;
                    """);

            characterRow.setOnMouseEntered(e ->
                    characterRow.setStyle("""
                            -fx-background-color: #3e3e3e;
                            -fx-background-radius: 6;
                            """)
            );

            characterRow.setOnMouseExited(e ->
                    characterRow.setStyle("""
                            -fx-background-color: #2e2e2e;
                            -fx-background-radius: 6;
                            """)
            );

            characterRow.setOnMouseClicked(e ->
                    onCharacterSelected.accept(character)
            );

            charactersList.getChildren().add(characterRow);
        }

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(charactersList);

        getChildren().add(scrollPane);

        // --- Кнопка назад на стартовое окно ---
        Button backBtn = AppButtonFactory.customButton("Back", 70);
        backBtn.setOnAction(e -> stage.getScene().setRoot(new StartScreen(stage, storageService).getView()));

        getChildren().add(backBtn);
    }
}