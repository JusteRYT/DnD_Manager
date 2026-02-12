package com.example.dnd_manager.screen;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.service.CharacterTransferServiceImpl;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Locale;
import java.util.Objects;


/**
 * Start screen with main navigation actions.
 */
public class StartScreen {

    private static final double SCALE = 2;

    private final Stage stage;
    private final StorageService storageService;

    public StartScreen(Stage stage, StorageService storageService) {
        this.stage = stage;
        this.storageService = storageService;
    }

    /**
     * Builds and returns the start screen view.
     *
     * @return root UI node
     */
    public Parent getView() {
        VBox root = new VBox(18 * SCALE);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40 * SCALE));
        root.setStyle("""
                    -fx-background-color: %s;
                """.formatted(AppTheme.BACKGROUND_PRIMARY));

        Label title = buildTitle();
        ImageView diceImage = buildDiceImage();

        Button createButton = AppButtonFactory.customButton(
                I18n.t("button.create"), (int) (220 * SCALE), (int) (100 * SCALE), 25
        );
        Button editButton = AppButtonFactory.customButton(
                I18n.t("button.edit"), (int) (220 * SCALE), (int) (100 * SCALE), 25
        );
        Button loadButton = AppButtonFactory.customButton(
                I18n.t("button.load"), (int) (220 * SCALE), (int) (100 * SCALE), 25
        );
        Button transferButton = AppButtonFactory.customButton(
                I18n.t("button.importExport"), (int) (220 * SCALE), (int) (100 * SCALE), 25
        );
        Button languageBtn = AppButtonFactory.customButton(
                I18n.t("button.language"), 100
        );

        languageBtn.setOnAction(e -> changeLanguage());


        createButton.setOnAction(e -> openCharacterCreate());
        editButton.setOnAction(e -> openCharacterEdit());
        loadButton.setOnAction(e -> openCharacterLoad());
        transferButton.setOnAction(e -> openCharacterTransfer());

        root.getChildren().addAll(
                title,
                diceImage,
                createButton,
                editButton,
                loadButton,
                transferButton,
                languageBtn
        );

        return root;
    }

    private Label buildTitle() {
        Label title = new Label(I18n.t("title.start"));
        title.setStyle("""
                    -fx-text-fill: %s;
                    -fx-font-size: %dpx;
                    -fx-font-weight: bold;
                """.formatted(
                AppTheme.TEXT_ACCENT,
                (int) (36 * SCALE)
        ));
        return title;
    }

    private ImageView buildDiceImage() {
        ImageView imageView = new ImageView(
                new Image(
                        Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_dice.png")).toExternalForm()
                )
        );
        imageView.setFitWidth(120 * SCALE);
        imageView.setFitHeight(120 * SCALE);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private void openCharacterCreate() {
        CharacterCreateScreen createScreen =
                new CharacterCreateScreen(stage, storageService);
        stage.getScene().setRoot(createScreen.getView());
    }

    private void openCharacterEdit() {
        List<String> names = storageService.listCharacterNames();

        if (names.isEmpty()) {
            System.out.println("No characters found");
            return;
        }

        CharacterSelectionScreen selectionScreen = new CharacterSelectionScreen(
                stage,
                storageService,
                character -> {
                    CharacterEditScreen editScreen = new CharacterEditScreen(stage, character, storageService);
                    stage.getScene().setRoot(editScreen.getView());
                }, true
        );

        stage.getScene().setRoot(selectionScreen);
    }

    private void openCharacterLoad() {
        List<String> names = storageService.listCharacterNames();

        if (names.isEmpty()) {
            // TODO: Показать уведомление
            System.out.println("No characters found");
            return;
        }

        CharacterSelectionScreen selectionScreen = new CharacterSelectionScreen(stage,
                storageService,
                character -> {
                    CharacterOverviewScreen editScreen = new CharacterOverviewScreen(character.getName(), storageService);
                    stage.getScene().setRoot(editScreen);
                }, false);
        stage.getScene().setRoot(selectionScreen);
    }

    private void openCharacterTransfer() {
        CharacterImportExportScreen screen =
                new CharacterImportExportScreen(
                        stage,
                        storageService,
                        new CharacterTransferServiceImpl()
                );

        stage.getScene().setRoot(screen);
    }

    private void changeLanguage() {
        if (I18n.isEnglish()) {
            I18n.setLocale(Locale.forLanguageTag("ru"));
        } else {
            I18n.setLocale(Locale.ENGLISH);
        }
        stage.getScene().setRoot(new StartScreen(stage, storageService).getView());
    }
}