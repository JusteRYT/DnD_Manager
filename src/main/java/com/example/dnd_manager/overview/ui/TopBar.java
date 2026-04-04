package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.CharacterNotesDialog;
import com.example.dnd_manager.overview.dialogs.EditStatsDialog;
import com.example.dnd_manager.overview.dialogs.FullDescriptionDialog;
import com.example.dnd_manager.overview.dialogs.LevelUpDialog;
import com.example.dnd_manager.overview.utils.ButtonPopupInstaller;
import com.example.dnd_manager.overview.utils.PopupFactory;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.screen.CharacterOverviewScreen;
import com.example.dnd_manager.screen.ScreenManager;
import com.example.dnd_manager.screen.StartScreen;
import com.example.dnd_manager.service.CharacterExporter;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

/**
 * Top bar with avatar, name/race/class, level and action buttons.
 * Level styled as recessed card with white "Level:" and orange number.
 * Includes a button to increment level in the right block with confirmation dialog.
 */
public class TopBar extends HBox {

    private final VBox infoBox;
    private VBox activeEffectsBox;

    public TopBar(Character character, CharacterOverviewScreen parentScreen, StorageService storageService) {
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: transparent;");

        // --- Avatar ---
        ImageView avatar = new ImageView(new Image(CharacterAssetResolver.resolve(character.getName(), character.getAvatarImage())));
        avatar.setFitWidth(160);
        avatar.setFitHeight(220);
        avatar.setPreserveRatio(true);

        // Обертка для аватара с рамкой
        StackPane avatarContainer = getStackPane(avatar);

        // --- Name ---
        Label nameLabel = new Label(character.getName());
        nameLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        // --- Level card ---
        Label levelText = new Label(I18n.t("topBar.level"));
        levelText.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 18px;");

        Label levelValue = new Label(String.valueOf(character.getLevel()));
        levelValue.setStyle("-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 18px;");

        HBox levelBox = new HBox(4, levelText, levelValue);
        levelBox.setAlignment(Pos.CENTER);
        levelBox.setPadding(new Insets(4, 8, 4, 8));
        levelBox.setStyle("""
                    -fx-background-color: #2b2b2b;
                    -fx-background-radius: 6;
                    -fx-border-color: #1a1a1a;
                    -fx-border-radius: 6;
                    -fx-border-width: 2;
                """);

        HBox nameLevelBox = new HBox(10, nameLabel, levelBox);
        nameLevelBox.setAlignment(Pos.CENTER_LEFT);

        // --- Meta info: race + class ---
        Label metaLabel = new Label(character.getRace() + " • " + character.getCharacterClass());
        metaLabel.setStyle("""
                    -fx-font-size: 20px; 
                    -fx-text-fill: #c89b3c; 
                    -fx-background-color: rgba(200, 155, 60, 0.1); 
                    -fx-padding: 2 8 2 8; 
                    -fx-background-radius: 4;
                """);

        // --- HP ---
        ImageView hpIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_heart.png")).toExternalForm()));
        hpIcon.setFitWidth(25);
        hpIcon.setFitHeight(25);
        Label hpLabel = new Label(String.valueOf(character.getCurrentHp()));
        hpLabel.setStyle("-fx-text-fill: #ff5555; -fx-font-weight: bold; -fx-font-size: 18px;");
        HBox hpBox = new HBox(6, hpIcon, hpLabel);
        hpBox.setAlignment(Pos.CENTER_LEFT);

        // --- Armor ---
        ImageView armorIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_shield.png")).toExternalForm()));
        armorIcon.setFitWidth(25);
        armorIcon.setFitHeight(25);
        Label armorLabel = new Label(String.valueOf(character.getArmor()));
        armorLabel.setStyle("-fx-text-fill: #55aaff; -fx-font-weight: bold; -fx-font-size: 18px;");
        HBox armorBox = new HBox(6, armorIcon, armorLabel);
        armorBox.setAlignment(Pos.CENTER_LEFT);

        HBox statsBox = new HBox(12, hpBox, armorBox);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(8, 0, 0, 0));

        this.activeEffectsBox = buildActiveEffectsBox(character);

        this.infoBox = new VBox(8, nameLevelBox, metaLabel, statsBox, activeEffectsBox);
        this.infoBox.setAlignment(Pos.TOP_LEFT);
        this.infoBox.setPadding(new Insets(10, 0, 0, 0));

        HBox leftBox = new HBox(20, avatarContainer, infoBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setPadding(new Insets(15, 25, 15, 20));
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        leftBox.setStyle("""
                    -fx-background-color: linear-gradient(to right, #252525, #1e1e1e);
                    -fx-background-radius: 12;
                    -fx-border-width: 0 1 0 0;
                """);

        // --- Right block: buttons ---
        Button exportBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/import-export.png");
        exportBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Сохранить описание персонажа");
            fileChooser.setInitialFileName(character.getName() + "_description.txt");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            Stage stage = (Stage) exportBtn.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
                    String fullText = CharacterExporter.generateFullDescription(character);
                    writer.print(fullText);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Здесь можно вывести Alert с ошибкой
                }
            }
        });

        Button showDescBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/icon_description.png");
        showDescBtn.setOnAction(e -> {
            Stage owner = (Stage) showDescBtn.getScene().getWindow();
            new FullDescriptionDialog(owner, character).show();
        });

        Button editBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/edit_icon.png");
        editBtn.setOnAction(e -> {
            Stage owner = (Stage) editBtn.getScene().getWindow();
            EditStatsDialog dialog = new EditStatsDialog(
                    owner,
                    character,
                    storageService,
                    () -> {
                        hpLabel.setText(String.valueOf(character.getCurrentHp()));
                        armorLabel.setText(String.valueOf(character.getArmor()));
                        parentScreen.getManaBar().refresh();
                        levelValue.setText(String.valueOf(character.getLevel()));
                    }
            );
            dialog.show();
        });

        Button backBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/icon_back.png");

        // --- Increase level button with confirmation ---
        Button increaseLevelBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/level_up_icon.png");
        increaseLevelBtn.setOnAction(e -> showLevelUpDialog(increaseLevelBtn, character, storageService, levelValue));

        Button notesBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/icon_notes.png");
        notesBtn.setOnAction(e -> {
            Stage owner = (Stage) notesBtn.getScene().getWindow();
            new CharacterNotesDialog(owner, character).show();
        });

        HBox rightPanel = new HBox(15,
                exportBtn,
                showDescBtn,
                notesBtn,
                editBtn,
                increaseLevelBtn,
                backBtn
        );

        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setMaxHeight(100);
        rightPanel.setStyle("""
                -fx-background-color: linear-gradient(to bottom, #2d2d2d, #1a1a1a);
                -fx-background-radius: 12;
                -fx-border-color: rgba(200, 155, 60, 0.3); 
                -fx-border-radius: 12;
                -fx-border-width: 1.5;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 15, 0, 0, 5);
                """);

        HBox.setMargin(rightPanel, new Insets(10, 10, 10, 0));


        getChildren().addAll(leftBox, rightPanel);

        backBtn.setOnAction(e -> {
            Stage stage = (Stage) parentScreen.getScene().getWindow();
            closeScreen(stage, storageService);
        });

        ButtonPopupInstaller.install(
                exportBtn,
                PopupFactory.tooltip(I18n.t("button.showExport"))
        );

        ButtonPopupInstaller.install(
                showDescBtn,
                PopupFactory.tooltip(I18n.t("button.showDescription"))
        );

        ButtonPopupInstaller.install(
                editBtn,
                PopupFactory.tooltip(I18n.t("button.editStatsPopup"))
        );

        ButtonPopupInstaller.install(
                backBtn,
                PopupFactory.tooltip(I18n.t("button.showExitPopup"))
        );

        ButtonPopupInstaller.install(
                increaseLevelBtn,
                PopupFactory.tooltip(I18n.t("button.levelIncrease"))
        );

        ButtonPopupInstaller.install(
                notesBtn,
                PopupFactory.tooltip(I18n.t("button.showNotesPopup"))
        );
    }

    private static StackPane getStackPane(ImageView avatar) {
        StackPane avatarContainer = new StackPane(avatar);
        avatarContainer.setPadding(new Insets(3));
        avatarContainer.setCursor(Cursor.HAND);

        String baseStyle = """
                -fx-background-color: #2b2b2b;
                -fx-background-radius: 8;
                -fx-border-color: #c89b3c;
                -fx-border-radius: 8;
                -fx-border-width: 2;
                -fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.3), 15, 0, 0, 0);
                """;

        String hoverStyle = """
                -fx-background-color: #2b2b2b;
                -fx-background-radius: 8;
                -fx-border-color: #f5b741;
                -fx-border-radius: 8;
                -fx-border-width: 2;
                -fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.8), 25, 0, 0, 0);
                """;

        avatarContainer.setStyle(baseStyle);

        avatarContainer.setOnMouseEntered(e -> avatarContainer.setStyle(hoverStyle));
        avatarContainer.setOnMouseExited(e -> avatarContainer.setStyle(baseStyle));

        // --- Click to Copy Logic ---
        avatarContainer.setOnMouseClicked(e -> {
            // 1. Копирование в буфер обмена
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putImage(avatar.getImage());
            clipboard.setContent(content);

            // 2. Визуальное уведомление
            showCopiedNotification(avatarContainer);
        });

        return avatarContainer;
    }

    private static void showLevelUpDialog(Button sourceButton, Character character, StorageService storageService, Label levelValue) {
        Stage owner = (Stage) sourceButton.getScene().getWindow();

        new LevelUpDialog(owner, character, storageService, () ->
                levelValue.setText(String.valueOf(character.getLevel()))).show();
    }

    private void closeScreen(Stage stage, StorageService storageService) {
        StartScreen startScreen = new StartScreen(stage, storageService);
        ScreenManager.setScreen(stage, startScreen.getView());
    }
    /**
     * Refreshes the active effects display by rebuilding the effects container.
     * @param character the character to pull data from
     */
    public void refresh(Character character) {
        // Удаляем старый блок эффектов
        infoBox.getChildren().remove(activeEffectsBox);
        // Создаем новый
        activeEffectsBox = buildActiveEffectsBox(character);
        // Добавляем в конец infoBox
        infoBox.getChildren().add(activeEffectsBox);
    }

    private VBox buildActiveEffectsBox(Character character) {
        VBox container = new VBox(4);
        container.setPadding(new Insets(4, 0, 0, 0));

        List<InventoryItem> equippedItems = character.getInventory().stream()
                .filter(InventoryItem::isEquipped)
                .toList();

        FlowPane buffsPane = new FlowPane(6, 6);

        for (InventoryItem item : equippedItems) {
            // Если есть кастомное имя эффекта - выводим его как основной тег
            if (item.getCustomEffectName() != null && !item.getCustomEffectName().isBlank()) {
                buffsPane.getChildren().add(createEffectLabel(item.getCustomEffectName(), null, character));
            } else {
                // Если кастомного имени нет, выводим все прикрепленные баффы
                for (Buff buff : item.getAttachedBuffs()) {
                    buffsPane.getChildren().add(createEffectLabel(getBuffText(buff), buff.iconPath(), character));
                }
            }
        }

        // Если тегов нет, возвращаем пустой контейнер (чтобы не было надписи "Active Effects:")
        if (buffsPane.getChildren().isEmpty()) {
            return container;
        }

        Label title = new Label("Active Effects:");
        title.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px; -fx-font-style: italic;");

        container.getChildren().addAll(title, buffsPane);
        return container;
    }

    /**
     * Creates a styled label for an effect.
     */
    private Label createEffectLabel(String text, String iconPath, Character character) {
        Label label = new Label(text);
        label.setStyle("""
            -fx-background-color: rgba(200, 155, 60, 0.15);
            -fx-text-fill: #c89b3c;
            -fx-padding: 2 6 2 6;
            -fx-background-radius: 4;
            -fx-border-color: rgba(200, 155, 60, 0.3);
            -fx-border-radius: 4;
            -fx-font-size: 11px;
            -fx-font-weight: bold;
        """);

        if (iconPath != null && !iconPath.isBlank()) {
            ImageView icon = new ImageView(new Image(CharacterAssetResolver.resolve(character.getName(), iconPath)));
            icon.setFitWidth(12);
            icon.setFitHeight(12);
            label.setGraphic(icon);
        }
        return label;
    }

    private String getBuffText(Buff buff) {
        return (buff.type() != null && !buff.type().isBlank())
                ? String.format("%s (%s)", buff.name(), buff.type())
                : buff.name();
    }

    /**
     * Displays a temporary notification label over the avatar.
     * * @param container The StackPane where the notification will be added.
     */
    private static void showCopiedNotification(StackPane container) {
        Label notification = getLabel();

        container.getChildren().add(notification);

        // Анимация появления и исчезновения
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), notification);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), notification);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(actionEvent -> container.getChildren().remove(notification));

        // Запуск последовательности
        fadeIn.setOnFinished(e -> pause.play());
        pause.setOnFinished(e -> fadeOut.play());
        fadeIn.play();
    }

    private static Label getLabel() {
        Label notification = new Label(I18n.t("text.clipboardImage"));
        notification.setStyle("""
            -fx-background-color: rgba(0, 0, 0, 0.7);
            -fx-text-fill: #c89b3c;
            -fx-font-weight: bold;
            -fx-padding: 8 12;
            -fx-background-radius: 4;
            -fx-border-color: #c89b3c;
            -fx-border-radius: 4;
            -fx-font-size: 14px;
        """);

        // Предотвращаем прокликивание сквозь уведомление во время его показа
        notification.setMouseTransparent(true);
        return notification;
    }

}
