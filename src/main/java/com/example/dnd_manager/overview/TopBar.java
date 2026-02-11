package com.example.dnd_manager.overview;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.screen.CharacterOverviewScreen;
import com.example.dnd_manager.screen.StartScreen;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Top bar with avatar, name/race/class, level and action buttons
 */
public class TopBar extends HBox {

    public TopBar(Character character, CharacterOverviewScreen parentScreen, StorageService storageService) {
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: #1e1e1e;");

        // Left block: avatar + name/race/class + level
        ImageView avatar = new ImageView(new Image(CharacterAssetResolver.resolve(character.getName(), character.getAvatarImage())));
        avatar.setFitWidth(96);
        avatar.setFitHeight(96);

        Label nameLabel = new Label(character.getName());
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        Label metaLabel = new Label(character.getRace() + " • " + character.getCharacterClass());
        metaLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #c89b3c;");

        // --- Level ---
        Label levelLabel = new Label("Level");
        levelLabel.setStyle("-fx-text-fill: #c89b3c; -fx-font-size: 14px;");

        Label levelValue = new Label(String.valueOf(character.getLevel()));
        levelValue.setStyle("-fx-text-fill: #f2f2f2; -fx-font-weight: bold; -fx-font-family: 'Consolas'; -fx-padding: 2 8; -fx-background-color: #1e1e1e; -fx-background-radius: 6;");

        HBox levelBox = new HBox(4, levelLabel, levelValue);
        levelBox.setAlignment(Pos.CENTER_LEFT);

        // --- HP и Armor блок ---
        ImageView hpIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_heart.png")).toExternalForm()));
        hpIcon.setFitWidth(16);
        hpIcon.setFitHeight(16);
        Label hpLabel = new Label(String.valueOf(character.getHp()));
        hpLabel.setStyle("-fx-text-fill: #ff5555; -fx-font-weight: bold;");
        HBox hpBox = new HBox(4, hpIcon, hpLabel);
        hpBox.setAlignment(Pos.CENTER_LEFT);

        ImageView armorIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_shield.png")).toExternalForm()));
        armorIcon.setFitWidth(16);
        armorIcon.setFitHeight(16);
        Label armorLabel = new Label(String.valueOf(character.getArmor()));
        armorLabel.setStyle("-fx-text-fill: #55aaff; -fx-font-weight: bold;");
        HBox armorBox = new HBox(4, armorIcon, armorLabel);
        armorBox.setAlignment(Pos.CENTER_LEFT);

        HBox statsBox = new HBox(12, hpBox, armorBox, levelBox);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        VBox infoBox = new VBox(4, nameLabel, metaLabel, statsBox);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        HBox leftBox = new HBox(12, avatar, infoBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftBox, Priority.ALWAYS);

        // Right block: buttons
        Button showDescBtn = AppButtonFactory.customButton("", 50, 0);
        ImageView descIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_description.png")).toExternalForm()));
        descIcon.setFitWidth(24);
        descIcon.setFitHeight(24);
        showDescBtn.setGraphic(descIcon);

        Button backBtn = AppButtonFactory.customButton("", 50, 0);
        ImageView backIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_back.png")).toExternalForm()));
        backIcon.setFitWidth(24);
        backIcon.setFitHeight(24);
        backBtn.setGraphic(backIcon);

        // --- Edit button ---
        Button editBtn = AppButtonFactory.customButton("", 50, 0);
        ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_description.png")).toExternalForm()));
        editIcon.setFitWidth(24);
        editIcon.setFitHeight(24);
        editBtn.setGraphic(editIcon);
        editBtn.setOnAction(e -> new EditStatsDialog(character, parentScreen, storageService)
                .show(() -> {
                    hpLabel.setText(String.valueOf(character.getHp()));
                    armorLabel.setText(String.valueOf(character.getArmor()));
                    parentScreen.getManaBar().refresh();
                }));

        HBox rightBox = new HBox(8, showDescBtn, editBtn, backBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(leftBox, rightBox);

        // Back button action
        backBtn.setOnAction(e -> {
            Stage stage = (Stage) parentScreen.getScene().getWindow();
            stage.getScene().setRoot(new StartScreen(stage, parentScreen.getStorageService()).getView());
        });

        // Description tooltip
        installStaticTooltip(showDescBtn, "Show full description", character, parentScreen);
    }

    private void installStaticTooltip(Button button, String text, Character character, CharacterOverviewScreen parentScreen) {
        Popup popup = new Popup();
        Label label = new Label(text);
        label.setStyle("-fx-background-color: #333333; -fx-text-fill: #ffffff; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");
        popup.getContent().add(label);
        popup.setAutoHide(false);
        button.setOnMouseEntered(e -> {
            var bounds = button.localToScreen(button.getBoundsInLocal());
            popup.show(button.getScene().getWindow(), bounds.getMaxX() + 5, bounds.getMinY());
        });
        button.setOnMouseExited(e -> popup.hide()); // Действие кнопки
        if (text.equals("Show full description")) {
            button.setOnAction(e -> new FullDescriptionDialog(character, parentScreen).show());
        }
    }
}
