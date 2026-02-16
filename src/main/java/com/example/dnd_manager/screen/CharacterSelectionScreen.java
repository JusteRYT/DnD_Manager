package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.domain.CharacterCard;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

public class CharacterSelectionScreen extends VBox {

    public CharacterSelectionScreen(Stage stage, StorageService storageService, Consumer<Character> onCharacterSelected, boolean isEdit) {
        setSpacing(25);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + ";");
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label(I18n.t("title.selectionScreen"));
        title.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 2);");
        getChildren().add(title);

        List<String> names = storageService.listCharacterNames();

        if (names.isEmpty()) {
            renderEmptyState();
        } else {
            FlowPane cardsGrid = new FlowPane();
            cardsGrid.setHgap(25);
            cardsGrid.setVgap(25);
            cardsGrid.setAlignment(Pos.TOP_LEFT);
            cardsGrid.setPadding(new Insets(20));
            cardsGrid.prefWidthProperty().bind(this.widthProperty());

            for (String name : names) {
                storageService.loadCharacter(name).ifPresent(character -> {
                    CharacterCard card = new CharacterCard(character, onCharacterSelected, isEdit, () -> {
                        cardsGrid.getChildren().removeIf(node -> node.getUserData() == character);
                        storageService.deleteCharacter(character);
                    });
                    card.setUserData(character);
                    cardsGrid.getChildren().add(card);
                });
            }

            VBox.setVgrow(cardsGrid, Priority.ALWAYS);
            getChildren().add(cardsGrid);
        }

        Button backBtn = AppButtonFactory.actionExit(I18n.t("button.exit"), 80);
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> closeScreen(stage, storageService));
        getChildren().add(backBtn);
    }

    private void renderEmptyState() {
        Label emptyLabel = new Label(I18n.t("label.selectionScreen"));
        emptyLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 18px; -fx-font-style: italic;");
        getChildren().add(emptyLabel);
    }

    private void closeScreen(Stage stage, StorageService storageService) {
        StartScreen startScreen = new StartScreen(stage, storageService);
        ScreenManager.setScreen(stage, startScreen.getView());
    }
}