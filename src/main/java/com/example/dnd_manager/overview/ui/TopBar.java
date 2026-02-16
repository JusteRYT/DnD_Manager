package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.EditStatsDialog;
import com.example.dnd_manager.overview.dialogs.FullDescriptionDialog;
import com.example.dnd_manager.overview.dialogs.LevelUpDialog;
import com.example.dnd_manager.overview.utils.ButtonPopupInstaller;
import com.example.dnd_manager.overview.utils.PopupFactory;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.screen.CharacterOverviewScreen;
import com.example.dnd_manager.screen.ScreenManager;
import com.example.dnd_manager.screen.StartScreen;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Top bar with avatar, name/race/class, level and action buttons.
 * Level styled as recessed card with white "Level:" and orange number.
 * Includes a button to increment level in the right block with confirmation dialog.
 */
public class TopBar extends HBox {

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
        Label hpLabel = new Label(String.valueOf(character.getHp()));
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

        VBox infoBox = new VBox(8, nameLevelBox, metaLabel, statsBox);
        infoBox.setAlignment(Pos.TOP_LEFT);
        infoBox.setPadding(new Insets(10, 0, 0, 0));

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
                        hpLabel.setText(String.valueOf(character.getHp()));
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

        HBox rightPanel = new HBox(15,
                showDescBtn,
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
    }

    private static StackPane getStackPane(ImageView avatar) {
        StackPane avatarContainer = new StackPane(avatar);
        avatarContainer.setPadding(new Insets(3));

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

        return avatarContainer;
    }

    private static void showLevelUpDialog(Button sourceButton, Character character, StorageService storageService, Label levelValue) {
        Stage owner = (Stage) sourceButton.getScene().getWindow();

        new LevelUpDialog(owner, character, storageService, () -> {
            levelValue.setText(String.valueOf(character.getLevel()));
        }).show();
    }

    private void closeScreen(Stage stage, StorageService storageService) {
        StartScreen startScreen = new StartScreen(stage, storageService);
        ScreenManager.setScreen(stage, startScreen.getView());
    }
}
