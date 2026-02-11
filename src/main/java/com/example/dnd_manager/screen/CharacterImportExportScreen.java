package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.service.CharacterTransferService;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Screen for importing and exporting characters.
 * Styled similarly to CharacterSelectionScreen.
 */
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

        initialize();
    }

    /**
     * Initializes UI components.
     */
    private void initialize() {
        setSpacing(10);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #1e1e1e;");

        Label title = new Label(I18n.t("title.import_export"));
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #ffffff; -fx-font-weight: bold;");
        getChildren().add(title);

        // --- Import button aligned to right ---
        Button importBtn = AppButtonFactory.customButton(
                I18n.t("button.import"),
                100,
                AppTheme.BUTTON_PRIMARY,
                AppTheme.BUTTON_PRIMARY_HOVER
        );
        importBtn.setOnAction(e -> handleImport());

        HBox importContainer = new HBox(importBtn);
        importContainer.setAlignment(Pos.CENTER_RIGHT);
        importContainer.setMaxWidth(Double.MAX_VALUE);
        getChildren().add(importContainer);

        // --- Character list ---
        VBox charactersList = buildCharacterList();
        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(charactersList);
        getChildren().add(scrollPane);

        // --- Back button ---
        Button backBtn = AppButtonFactory.customButton(I18n.t("button.back"), 70);
        backBtn.setOnAction(e ->
                stage.getScene().setRoot(
                        new StartScreen(stage, storageService).getView()
                )
        );
        getChildren().add(backBtn);
    }

    /**
     * Builds list of characters styled as cards.
     */
    private VBox buildCharacterList() {
        VBox charactersList = new VBox(8);
        charactersList.setAlignment(Pos.CENTER);

        List<String> names = storageService.listCharacterNames();

        if (names.isEmpty()) {
            Label emptyLabel = new Label(I18n.t("label.no_characters"));
            emptyLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px;");
            charactersList.getChildren().add(emptyLabel);
            return charactersList;
        }

        for (String name : names) {
            storageService.loadCharacter(name).ifPresent(character -> {
                BorderPane row = createCharacterRow(character);
                charactersList.getChildren().add(row);
            });
        }

        return charactersList;
    }

    /**
     * Creates styled row for character with export button centered vertically.
     */
    private BorderPane createCharacterRow(Character character) {

        ImageView avatar = new ImageView(
                new Image(CharacterAssetResolver.resolve(
                        character.getName(),
                        character.getAvatarImage()
                ))
        );
        avatar.setFitWidth(48);
        avatar.setFitHeight(48);

        Label nameLabel = new Label(character.getName());
        nameLabel.setStyle("""
                -fx-text-fill: #ffffff;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                """);

        HBox leftBox = new HBox(10, avatar, nameLabel);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        Button exportBtn = AppButtonFactory.customButton(
                I18n.t("button.export"),
                80,
                AppTheme.BUTTON_PRIMARY,
                AppTheme.BUTTON_PRIMARY_HOVER
        );
        exportBtn.setFocusTraversable(false);
        exportBtn.setOnAction(e -> {
            e.consume();
            handleExport(character.getName());
        });

        BorderPane row = new BorderPane();
        row.setLeft(leftBox);
        row.setRight(exportBtn);
        row.setPadding(new Insets(8));
        row.setUserData(character);

        // --- align export button vertically center ---
        BorderPane.setAlignment(leftBox, Pos.CENTER_LEFT);
        BorderPane.setAlignment(exportBtn, Pos.CENTER_RIGHT);
        BorderPane.setMargin(exportBtn, new Insets(0, 4, 0, 10));

        row.setStyle("""
                -fx-background-color: #2e2e2e;
                -fx-background-radius: 6;
                """);

        row.setOnMouseEntered(e ->
                row.setStyle("""
                        -fx-background-color: #3e3e3e;
                        -fx-background-radius: 6;
                        """)
        );

        row.setOnMouseExited(e ->
                row.setStyle("""
                        -fx-background-color: #2e2e2e;
                        -fx-background-radius: 6;
                        """)
        );

        return row;
    }

    /**
     * Handles export action.
     */
    private void handleExport(String characterName) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName(characterName + ".zip");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Zip files", "*.zip")
        );

        File file = chooser.showSaveDialog(stage);
        if (file == null) return;

        try {
            transferService.exportCharacter(characterName, file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles import action.
     */
    private void handleImport() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Zip files", "*.zip")
        );

        File file = chooser.showOpenDialog(stage);
        if (file == null) return;

        try {
            transferService.importCharacter(file);
            refresh();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Refreshes character list after import.
     */
    private void refresh() {
        getChildren().clear();
        initialize();
    }
}
