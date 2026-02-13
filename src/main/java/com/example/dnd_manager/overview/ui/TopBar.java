package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.utils.ButtonPopupInstaller;
import com.example.dnd_manager.overview.utils.PopupFactory;
import com.example.dnd_manager.overview.dialogs.EditStatsDialog;
import com.example.dnd_manager.overview.dialogs.FullDescriptionDialog;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.screen.CharacterOverviewScreen;
import com.example.dnd_manager.screen.StartScreen;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
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
        setStyle("-fx-background-color: #1e1e1e;");

        // --- Avatar ---
        ImageView avatar = new ImageView(new Image(CharacterAssetResolver.resolve(character.getName(), character.getAvatarImage())));
        avatar.setFitWidth(100);
        avatar.setFitHeight(100);

        // --- Name ---
        Label nameLabel = new Label(character.getName());
        nameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        // --- Level card ---
        Label levelText = new Label(I18n.t("topBar.level"));
        levelText.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label levelValue = new Label(String.valueOf(character.getLevel()));
        levelValue.setStyle("-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 14px;");

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
        metaLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #c89b3c;");

        // --- HP ---
        ImageView hpIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_heart.png")).toExternalForm()));
        hpIcon.setFitWidth(24);
        hpIcon.setFitHeight(24);
        Label hpLabel = new Label(String.valueOf(character.getHp()));
        hpLabel.setStyle("-fx-text-fill: #ff5555; -fx-font-weight: bold; -fx-font-size: 16px;");
        HBox hpBox = new HBox(6, hpIcon, hpLabel);
        hpBox.setAlignment(Pos.CENTER_LEFT);

        // --- Armor ---
        ImageView armorIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_shield.png")).toExternalForm()));
        armorIcon.setFitWidth(24);
        armorIcon.setFitHeight(24);
        Label armorLabel = new Label(String.valueOf(character.getArmor()));
        armorLabel.setStyle("-fx-text-fill: #55aaff; -fx-font-weight: bold; -fx-font-size: 16px;");
        HBox armorBox = new HBox(6, armorIcon, armorLabel);
        armorBox.setAlignment(Pos.CENTER_LEFT);

        HBox statsBox = new HBox(12, hpBox, armorBox);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        VBox infoBox = new VBox(6, nameLevelBox, metaLabel, statsBox);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        HBox leftBox = new HBox(12, avatar, infoBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        // --- Right block: buttons ---
        Button showDescBtn = AppButtonFactory.customButton("", 50, 0);
        ImageView descIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_description.png")).toExternalForm()));
        descIcon.setFitWidth(28);
        descIcon.setFitHeight(28);
        showDescBtn.setGraphic(descIcon);
        showDescBtn.setOnAction(e ->
                new FullDescriptionDialog(character, parentScreen).show()
        );

        Button editBtn = AppButtonFactory.customButton("", 50, 0);
        ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/edit_icon.png")).toExternalForm()));
        editIcon.setFitWidth(28);
        editIcon.setFitHeight(28);
        editBtn.setGraphic(editIcon);
        editBtn.setOnAction(e -> new EditStatsDialog(character, parentScreen, storageService)
                .show(() -> {
                    hpLabel.setText(String.valueOf(character.getHp()));
                    armorLabel.setText(String.valueOf(character.getArmor()));
                    parentScreen.getManaBar().refresh();
                    levelValue.setText(String.valueOf(character.getLevel()));
                }));

        Button backBtn = AppButtonFactory.customButton("", 50, 0);
        ImageView backIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_back.png")).toExternalForm()));
        backIcon.setFitWidth(28);
        backIcon.setFitHeight(28);
        backIcon.setPreserveRatio(true);
        backIcon.setSmooth(false);
        backBtn.setGraphic(backIcon);

        // --- Increase level button with confirmation ---
        Button increaseLevelBtn = AppButtonFactory.customButton("", 50, 0);
        ImageView increaseLevelIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/level_up_icon.png")).toExternalForm()));
        increaseLevelIcon.setFitWidth(28);
        increaseLevelIcon.setFitHeight(28);
        increaseLevelBtn.setGraphic(increaseLevelIcon);
        increaseLevelBtn.setOnAction(e -> showLevelUpDialog(character, storageService, levelValue));

        HBox rightBox = new HBox(10, showDescBtn, editBtn, increaseLevelBtn, backBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(leftBox, rightBox);

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

    private static void showLevelUpDialog(Character character, StorageService storageService, Label levelValue) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(I18n.t("dialogLevel.title"));

        Label message = new Label(I18n.t("dialogLevel.message"));
        message.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px;");

        Button yesBtn = AppButtonFactory.primary(I18n.t("button.yes"));
        Button noBtn = AppButtonFactory.primary(I18n.t("button.no"));

        HBox buttons = new HBox(10, yesBtn, noBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, message, buttons);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #1e1e1e;");
        layout.setAlignment(Pos.CENTER);

        dialog.setScene(new Scene(layout, 400, 150));
        dialog.show();

        yesBtn.setOnAction(ev -> {
            int currentLevel;
            try {
                currentLevel = Integer.parseInt(character.getLevel());
            } catch (NumberFormatException ex) {
                currentLevel = 1;
            }
            currentLevel += 1;
            character.setLevel(String.valueOf(currentLevel));
            storageService.saveCharacter(character);
            levelValue.setText(String.valueOf(currentLevel));
            dialog.close();
        });

        noBtn.setOnAction(ev -> dialog.close());
    }

    private void closeScreen(Stage stage, StorageService storageService) {
        StartScreen startScreen = new StartScreen(stage, storageService);
        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(startScreen.getView());
        scrollPane.setFitToHeight(true);
        stage.getScene().setRoot(scrollPane);
    }
}
