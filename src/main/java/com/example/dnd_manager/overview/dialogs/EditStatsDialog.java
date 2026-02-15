package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.screen.CharacterOverviewScreen;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import com.example.dnd_manager.theme.AppTextField;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Dialog to edit character's HP, Armor, and Mana.
 * Styled similarly to FullDescriptionDialog, with proper background.
 * Handles empty fields by keeping old values.
 */
public class EditStatsDialog {

    private final Character character;
    private final Stage stage;
    private final StorageService storageService;

    public EditStatsDialog(Character character, CharacterOverviewScreen parentScreen, StorageService storageService) {
        this.character = character;
        this.storageService = storageService;
        this.stage = new Stage();
        stage.initOwner(parentScreen.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(I18n.t("dialogEdit.title") + character.getName());
    }

    public void show(Runnable refreshCallback) {
        VBox content = new VBox(12);
        content.setPadding(new Insets(15));
        content.setStyle("-fx-background-color: #1e1e1e; -fx-background-radius: 8;");

        // HP
        Label hpLabel = new Label(I18n.t("hpField.name") + ":");
        hpLabel.setStyle("-fx-text-fill: #ff5555; -fx-font-weight: bold;");
        IntegerField hpField = new IntegerField(String.valueOf(character.getHp()));
        VBox hpBox = new VBox(4, hpLabel, hpField.getField());

        // Armor
        Label armorLabel = new Label(I18n.t("armorField.name") + ":");
        armorLabel.setStyle("-fx-text-fill: #55aaff; -fx-font-weight: bold;");
        AppTextField armorField = new AppTextField(String.valueOf(character.getArmor()));
        VBox armorBox = new VBox(4, armorLabel, armorField.getField());

        // Mana
        Label manaLabel = new Label(I18n.t("manaField.name") + ":");
        manaLabel.setStyle("-fx-text-fill: #3aa3c3; -fx-font-weight: bold;");
        IntegerField manaField = new IntegerField(String.valueOf(character.getCurrentMana()));
        VBox manaBox = new VBox(4, manaLabel, manaField.getField());

        // level
        Label levelLabel = new Label(I18n.t("levelField.name") + ":");
        manaLabel.setStyle("-fx-text-fill: #3aa3c3; -fx-font-weight: bold;");
        IntegerField levelField = new IntegerField(String.valueOf(character.getLevel()));
        VBox LevelBox = new VBox(4, levelLabel, levelField.getField());

        // Save button
        Button saveBtn = AppButtonFactory.primary(I18n.t("button.save"));
        saveBtn.setOnAction(ev -> {
            if (!hpField.getText().isBlank()) {
                character.setHp(hpField.getInt());
            }
            if (!armorField.getText().isBlank()) {
                character.setArmor(armorField.getInt());
            }
            if (!manaField.getText().isBlank()) {
                character.setMaxMana(manaField.getInt());
            }

            if (!levelField.getText().isBlank()) {
                character.setLevel(levelField.getInt());
            }

            // Сохраняем в storage
            storageService.saveCharacter(character);

            // Обновляем TopBar, ManaBar и любые другие progressBar
            refreshCallback.run();

            stage.close();
        });

        content.getChildren().addAll(hpBox, armorBox, manaBox, LevelBox, saveBtn);

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(content);
        StackPane root = new StackPane(scrollPane);
        Scene scene = new Scene(root, 500, 350);
        scene.setFill(javafx.scene.paint.Color.web("#1e1e1e"));

        stage.setScene(scene);
        stage.showAndWait();
    }
}
