package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.domain.CharacterCard;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.service.CharacterTransferService;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CharacterImportExportScreen extends VBox {

    private final Stage stage;
    private final StorageService storageService;
    private final CharacterTransferService transferService;

    public CharacterImportExportScreen(Stage stage,
                                       StorageService storageService,
                                       CharacterTransferService transferService) {
        this.stage = stage;
        this.storageService = storageService;
        this.transferService = transferService;

        setupLayout();
        renderContent();
    }

    private void setupLayout() {
        setSpacing(0);
        setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + ";");
        setAlignment(Pos.TOP_LEFT);
    }

    private void renderContent() {
        getChildren().clear();

        // --- 1. ШАПКА (Header) ---
        StackPane headerStack = new StackPane();
        headerStack.setPadding(new Insets(40, 50, 20, 50));
        headerStack.setAlignment(Pos.CENTER);

        // --- СЛОЙ 1: Заголовок (Строго по центру) ---
        Label title = new Label(I18n.t("title.import_export"));
        title.setStyle("-fx-font-size: 32px; -fx-text-fill: #c89b3c; -fx-font-weight: bold; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 2);");

        // --- СЛОЙ 2: Кнопка НАЗАД (Слева) ---
        Button backBtn = AppButtonFactory.actionExit(I18n.t("button.back"), 100);
        backBtn.setOnAction(e -> closeScreen());
        StackPane.setAlignment(backBtn, Pos.CENTER_RIGHT);

        // --- СЛОЙ 3: Кнопка ИМПОРТ (Справа) ---
        Button importBtn = AppButtonFactory.actionImport(I18n.t("button.import"), 160);
        importBtn.setOnAction(e -> handleImport());
        StackPane.setAlignment(importBtn, Pos.CENTER_LEFT);

        headerStack.getChildren().addAll(title, backBtn, importBtn);

        // --- 2. РАЗДЕЛИТЕЛЬ ---
        HBox separatorBox = new HBox(15);
        separatorBox.setAlignment(Pos.CENTER);
        separatorBox.setPadding(new Insets(0, 50, 10, 50));

        Region lineLeft = new Region();
        Region lineRight = new Region();
        HBox.setHgrow(lineLeft, Priority.ALWAYS);
        HBox.setHgrow(lineRight, Priority.ALWAYS);
        lineLeft.setStyle("-fx-background-color: #3a3a3a; -fx-pref-height: 1; -fx-max-height: 1;");
        lineRight.setStyle("-fx-background-color: #3a3a3a; -fx-pref-height: 1; -fx-max-height: 1;");

        Label listLabel = new Label(I18n.t("labelField.characterExport"));
        listLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 13px; -fx-font-weight: bold; -fx-text-transform: uppercase; -fx-letter-spacing: 1;");

        separatorBox.getChildren().addAll(lineLeft, listLabel, lineRight);

        // --- 3. СЕТКА ---
        FlowPane cardsGrid = new FlowPane();
        cardsGrid.setHgap(25);
        cardsGrid.setVgap(25);
        cardsGrid.setAlignment(Pos.TOP_LEFT);
        cardsGrid.setPadding(new Insets(20, 50, 40, 50));

        List<String> names = storageService.listCharacterNames();
        if (names.isEmpty()) {
            renderEmptyState(cardsGrid);
        } else {
            for (String name : names) {
                storageService.loadCharacter(name).ifPresent(character -> {
                    CharacterCard card = new CharacterCard(character, this::handleExportFromCard, false, null);
                    cardsGrid.getChildren().add(card);
                });
            }
        }

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(cardsGrid);
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        getChildren().addAll(headerStack, separatorBox, scrollPane);
    }

    private void renderEmptyState(Pane container) {
        Label emptyLabel = new Label(I18n.t("label.no_characters"));
        emptyLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 18px; -fx-font-style: italic;");
        container.getChildren().add(emptyLabel);
    }

    private void handleExportFromCard(Character character) {
        handleExport(character.getName());
    }

    private void handleExport(String characterName) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName(characterName + ".zip");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Zip files", "*.zip"));
        File file = chooser.showSaveDialog(stage);
        if (file != null) {
            try { transferService.exportCharacter(characterName, file); } catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    private void handleImport() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Zip files", "*.zip"));
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            try { transferService.importCharacter(file); renderContent(); } catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    private void closeScreen() {
        StartScreen startScreen = new StartScreen(stage, storageService);
        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(startScreen.getView());
        scrollPane.setFitToHeight(true);
        stage.getScene().setRoot(scrollPane);
    }
}