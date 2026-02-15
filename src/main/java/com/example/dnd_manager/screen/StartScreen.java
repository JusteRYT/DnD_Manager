    package com.example.dnd_manager.screen;

    import com.example.dnd_manager.info.version.AppInfo;
    import com.example.dnd_manager.lang.I18n;
    import com.example.dnd_manager.service.CharacterTransferServiceImpl;
    import com.example.dnd_manager.store.StorageService;
    import com.example.dnd_manager.theme.AppTheme;
    import com.example.dnd_manager.theme.ButtonSizeConfigurer;
    import com.example.dnd_manager.theme.factory.AppButtonFactory;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.Parent;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.BorderPane;
    import javafx.scene.layout.HBox;
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
        private final int BUTTON_WIDTH = 400;
        private final int BUTTON_HEIGHT = 100;
        private final int FONT_SIZE = 20;

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
            BorderPane root = new BorderPane();
            root.setStyle("""
                        -fx-background-color: %s;
                    """.formatted(AppTheme.BACKGROUND_PRIMARY));

            VBox content = new VBox(18 * SCALE);
            content.setAlignment(Pos.CENTER);
            content.setPadding(new Insets(40 * SCALE));
            content.setFillWidth(false);

            Label title = buildTitle();
            ImageView diceImage = buildDiceImage();

            Button createButton = AppButtonFactory.customButton(
                    I18n.t("button.create"), BUTTON_WIDTH, BUTTON_HEIGHT, FONT_SIZE
            );
            Button editButton = AppButtonFactory.customButton(
                    I18n.t("button.edit"), BUTTON_WIDTH, BUTTON_HEIGHT, FONT_SIZE
            );
            Button loadButton = AppButtonFactory.customButton(
                    I18n.t("button.load"), BUTTON_WIDTH, BUTTON_HEIGHT, FONT_SIZE
            );
            Button transferButton = AppButtonFactory.customButton(
                    I18n.t("button.importExport"), BUTTON_WIDTH, BUTTON_HEIGHT, FONT_SIZE
            );
            Button languageBtn = AppButtonFactory.customButton(
                    I18n.t("button.language"), 100
            );

            ButtonSizeConfigurer.applyFixedSize(createButton, 400, 50);
            ButtonSizeConfigurer.applyFixedSize(editButton, 400, 50);
            ButtonSizeConfigurer.applyFixedSize(loadButton, 400, 50);
            ButtonSizeConfigurer.applyFixedSize(transferButton, 400, 50);
            ButtonSizeConfigurer.applyFixedSize(languageBtn, 100, 40);

            languageBtn.setOnAction(e -> changeLanguage());

            createButton.setOnAction(e -> openCharacterCreate());
            editButton.setOnAction(e -> openCharacterEdit());
            loadButton.setOnAction(e -> openCharacterLoad());
            transferButton.setOnAction(e -> openCharacterTransfer());

            content.getChildren().addAll(
                    title,
                    diceImage,
                    createButton,
                    editButton,
                    loadButton,
                    transferButton,
                    languageBtn
            );

            root.setCenter(content);

            root.setBottom(buildVersionFooter());

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
            CharacterCreateScreen createScreen = new CharacterCreateScreen(stage, storageService);
            ScreenManager.setScreen(stage, createScreen.getView());
        }

        private void openCharacterEdit() {
            List<String> names = storageService.listCharacterNames();
            if (names.isEmpty()) return;

            CharacterSelectionScreen selectionScreen = new CharacterSelectionScreen(
                    stage,
                    storageService,
                    character -> {
                        CharacterEditScreen editScreen = new CharacterEditScreen(stage, character, storageService);
                        ScreenManager.setScreen(stage, editScreen.getView());
                    }, true
            );

            ScreenManager.setScreen(stage, selectionScreen);
        }

        private void openCharacterLoad() {
            List<String> names = storageService.listCharacterNames();
            if (names.isEmpty()) return;

            CharacterSelectionScreen selectionScreen = new CharacterSelectionScreen(stage, storageService,
                    character -> {

                        CharacterOverviewScreen overviewScreen = new CharacterOverviewScreen(stage, character, storageService);
                        ScreenManager.setScreen(stage, overviewScreen);
                    }, false);

            ScreenManager.setScreen(stage, selectionScreen);
        }

        private void openCharacterTransfer() {
            CharacterImportExportScreen screen = new CharacterImportExportScreen(
                    stage, storageService, new CharacterTransferServiceImpl()
            );
            ScreenManager.setScreen(stage, screen);
        }

        private void changeLanguage() {
            if (I18n.isEnglish()) {
                I18n.setLocale(Locale.forLanguageTag("ru"));
            } else {
                I18n.setLocale(Locale.ENGLISH);
            }

            StartScreen newScreen = new StartScreen(stage, storageService);
            ScreenManager.setScreen(stage, newScreen.getView());
        }

        /**
         * Builds footer with application version aligned to bottom-right.
         *
         * @return footer node
         */
        private HBox buildVersionFooter() {

            Label versionLabel = new Label("v" + AppInfo.getVersion());
            versionLabel.setStyle("""
                    -fx-text-fill: #777777;
                    -fx-font-size: 16px;
                    """);

            HBox footer = new HBox(versionLabel);
            footer.setAlignment(Pos.CENTER_RIGHT);
            footer.setPadding(new Insets(5, 10, 5, 10));

            return footer;
        }
    }