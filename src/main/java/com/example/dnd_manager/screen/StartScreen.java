    package com.example.dnd_manager.screen;

    import com.example.dnd_manager.info.version.AppInfo;
    import com.example.dnd_manager.lang.I18n;
    import com.example.dnd_manager.overview.dialogs.AppConfirmDialog;
    import com.example.dnd_manager.overview.dialogs.AppErrorDialog;
    import com.example.dnd_manager.service.CharacterImageIntegrityService;
    import com.example.dnd_manager.service.CharacterTransferServiceImpl;
    import com.example.dnd_manager.store.StorageService;
    import com.example.dnd_manager.theme.AppTheme;
    import com.example.dnd_manager.theme.ButtonSizeConfigurer;
    import com.example.dnd_manager.theme.factory.AppButtonFactory;
    import com.example.dnd_manager.updater.UpdateChecker;
    import com.example.dnd_manager.updater.UpdateManager;
    import javafx.application.Platform;
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
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import java.util.List;
    import java.util.Locale;
    import java.util.Objects;


    /**
     * Start screen with main navigation actions.
     */
    public class StartScreen {

        private static final double SCALE = 2;
        private static final Logger log = LoggerFactory.getLogger(StartScreen.class);

        private final Stage stage;
        private final StorageService storageService;
        private final int BUTTON_WIDTH = 400;
        private final int BUTTON_HEIGHT = 100;
        private final int FONT_SIZE = 20;

        public StartScreen(Stage stage, StorageService storageService) {
            this.stage = stage;
            this.storageService = storageService;
            log.debug("Initializing StartScreen and running integrity checks...");
            CharacterImageIntegrityService imageService = new CharacterImageIntegrityService(storageService);
            imageService.validateAndRepairAllCharacters();
        }

        /**
         * Builds and returns the start screen view.
         *
         * @return root UI node
         */
        public Parent getView() {
            log.debug("Building StartScreen UI nodes...");
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

            Button createButton = AppButtonFactory.primaryButton(
                    I18n.t("button.create"), BUTTON_WIDTH, BUTTON_HEIGHT, FONT_SIZE
            );
            Button editButton = AppButtonFactory.primaryButton(
                    I18n.t("button.edit"), BUTTON_WIDTH, BUTTON_HEIGHT, FONT_SIZE
            );
            Button loadButton = AppButtonFactory.primaryButton(
                    I18n.t("button.load"), BUTTON_WIDTH, BUTTON_HEIGHT, FONT_SIZE
            );
            Button transferButton = AppButtonFactory.primaryButton(
                    I18n.t("button.importExport"), BUTTON_WIDTH, BUTTON_HEIGHT, FONT_SIZE
            );

            Button languageBtn = AppButtonFactory.primaryButton(
                    I18n.t("button.language"), 120, 40, 14
            );

            Button updateBtn = AppButtonFactory.primaryButton(
                    I18n.t("button.checkUpdate"), 120, 40, 14
            );

            ButtonSizeConfigurer.applyFixedSize(createButton, 400, 50);
            ButtonSizeConfigurer.applyFixedSize(editButton, 400, 50);
            ButtonSizeConfigurer.applyFixedSize(loadButton, 400, 50);
            ButtonSizeConfigurer.applyFixedSize(transferButton, 400, 50);
            ButtonSizeConfigurer.applyFixedSize(languageBtn, 150, 40);
            ButtonSizeConfigurer.applyFixedSize(updateBtn, 150, 40);

            languageBtn.setOnAction(e -> changeLanguage());
            updateBtn.setOnAction(e -> handleUpdateCheck(updateBtn));
            languageBtn.setFocusTraversable(false);
            updateBtn.setFocusTraversable(false);

            createButton.setOnAction(e -> openCharacterCreate());
            editButton.setOnAction(e -> openCharacterEdit());
            loadButton.setOnAction(e -> openCharacterLoad());
            transferButton.setOnAction(e -> openCharacterTransfer());

            HBox bottomButtons = new HBox(15);
            bottomButtons.setAlignment(Pos.CENTER);
            bottomButtons.getChildren().addAll(languageBtn, updateBtn);

            content.getChildren().addAll(
                    title,
                    diceImage,
                    createButton,
                    editButton,
                    loadButton,
                    transferButton,
                    bottomButtons
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
            log.info("Navigation: Opening Character Creation screen");
            CharacterCreateScreen createScreen = new CharacterCreateScreen(stage, storageService);
            ScreenManager.setScreen(stage, createScreen.getView());
        }

        private void openCharacterEdit() {
            log.info("Navigation: Opening Character Selection (Edit mode)");
            checkCharacter();
        }

        private void openCharacterLoad() {
            log.info("Navigation: Opening Character Selection (Load/View mode)");
            checkCharacter();
        }

        private void checkCharacter() {
            List<String> names = storageService.listCharacterNames();
            if (names.isEmpty()) {
                log.warn("Navigation failed: No characters available for loading");
                showError(I18n.t("error.no_characters_title"), I18n.t("error.no_characters_msg"));
                return;
            }

            CharacterSelectionScreen selectionScreen = new CharacterSelectionScreen(stage, storageService,
                    character -> {
                        log.info("Character selected for view: {}", character.getName());
                        CharacterOverviewScreen overviewScreen = new CharacterOverviewScreen(stage, character, storageService);
                        ScreenManager.setScreen(stage, overviewScreen);
                    }, false);

            ScreenManager.setScreen(stage, selectionScreen);
        }

        private void openCharacterTransfer() {
            log.info("Navigation: Opening Import/Export screen");
            CharacterImportExportScreen screen = new CharacterImportExportScreen(
                    stage, storageService, new CharacterTransferServiceImpl()
            );
            ScreenManager.setScreen(stage, screen);
        }

        private void changeLanguage() {
            Locale newLocale = I18n.isEnglish() ? Locale.forLanguageTag("ru") : Locale.ENGLISH;
            log.info("UI: Changing language to {}", newLocale);
            I18n.setLocale(newLocale);

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

        private void showError(String title, String message) {
            AppErrorDialog dialog = new AppErrorDialog(stage, title, message);
            dialog.show();
        }

        private void handleUpdateCheck(Button btn) {
            log.info("Update: Manual update check requested");
            btn.setDisable(true);
            btn.setText(I18n.t("button.checking"));

            new Thread(() -> {
                try {
                    UpdateChecker checker = new UpdateChecker();
                    var releaseOpt = checker.check();

                    Platform.runLater(() -> {
                        btn.setDisable(false);
                        btn.setText(I18n.t("button.checkUpdate"));

                        if (releaseOpt.isPresent()) {
                            var release = releaseOpt.get();
                            log.info("Update found: {}", release.tagName);

                            // ИСПОЛЬЗУЕМ КАСТОМНЫЙ ДИАЛОГ ПОДТВЕРЖДЕНИЯ
                            AppConfirmDialog confirmDialog = new AppConfirmDialog(
                                    stage,
                                    I18n.t("update.title"),
                                    java.text.MessageFormat.format(I18n.t("update.header"), release.tagName) + "\n" + I18n.t("update.content"),
                                    true
                            );
                            confirmDialog.show();

                            if (confirmDialog.isConfirmed()) {
                                log.info("User accepted update: {}", release.tagName);
                                try {
                                    new UpdateManager().applyUpdate(release);
                                } catch (Exception e) {
                                    log.error("Update application failed", e);
                                    showError(I18n.t("update.error_title"),
                                            java.text.MessageFormat.format(I18n.t("update.error_apply"), e.getMessage()));
                                }
                            }
                        } else {
                            log.info("Update: Current version is up to date");
                            // ИСПОЛЬЗУЕМ КАСТОМНЫЙ ДИАЛОГ ИНФОРМАЦИИ
                            AppConfirmDialog infoDialog = new AppConfirmDialog(
                                    stage,
                                    I18n.t("update.no_updates_title"),
                                    I18n.t("update.no_updates_content"),
                                    false
                            );
                            infoDialog.show();
                        }
                    });
                } catch (Exception e) {
                    log.error("Update check failed", e);
                    Platform.runLater(() -> {
                        btn.setDisable(false);
                        btn.setText(I18n.t("button.checkUpdate"));
                        showError(I18n.t("update.error_connection_title"), I18n.t("update.error_connection_content"));
                    });
                }
            }).start();
        }
    }