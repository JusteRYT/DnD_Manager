package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.screen.CharacterOverviewScreen;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Top bar with avatar, name/race/class, level and action buttons.
 * Level styled as recessed card with white "Level:" and orange number.
 * Includes a button to increment level in the right block with confirmation dialog.
 */
public class TopBar extends HBox {

    private final VBox infoBox;
    private final ActiveEffectsPane activeEffectsPane;
    private final TopBarController controller;
    private final TopBarVrcSavePaneBuilder vrcSavePaneBuilder;
    private final TopBarTooltipInstaller tooltipInstaller;

    public TopBar(
            Character character,
            CharacterOverviewScreen parentScreen,
            ScreenNavigator screenNavigator,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction
    ) {
        this(character, parentScreen, saveCharacterUseCase, backToStartAction, new TopBarVrcSavePaneBuilder(), new TopBarTooltipInstaller());
    }

    TopBar(
            Character character,
            CharacterOverviewScreen parentScreen,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction,
            TopBarVrcSavePaneBuilder vrcSavePaneBuilder,
            TopBarTooltipInstaller tooltipInstaller
    ) {
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: transparent;");
        this.controller = new TopBarController(
                character,
                parentScreen,
                saveCharacterUseCase,
                backToStartAction
        );
        this.vrcSavePaneBuilder = vrcSavePaneBuilder;
        this.tooltipInstaller = tooltipInstaller;

        // --- Avatar ---
        Image avatarImg = CharacterAssetResolver.getAvatarImage(character, character.getAvatarImage(), 400, 600);

        StackPane avatarContainer = AvatarClipboardPane.create(avatarImg);

        avatarContainer.setPrefSize(160, 220);
        avatarContainer.setMinSize(160, 220);
        avatarContainer.setMaxWidth(220);

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
        HBox.setHgrow(avatarContainer, Priority.SOMETIMES);

        this.activeEffectsPane = new ActiveEffectsPane(character);

        this.infoBox = new VBox(8, nameLevelBox, metaLabel, statsBox, activeEffectsPane);
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
        exportBtn.setOnAction(e -> controller.exportDescription((Stage) exportBtn.getScene().getWindow()));

        Button showDescBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/icon_description.png");
        showDescBtn.setOnAction(e -> controller.showDescription((Stage) showDescBtn.getScene().getWindow()));

        Button editBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/edit_icon.png");
        editBtn.setOnAction(e -> controller.openEditStats(
                (Stage) editBtn.getScene().getWindow(),
                hpLabel,
                armorLabel,
                levelValue
        ));

        Button backBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/icon_back.png");

        // --- Increase level button with confirmation ---
        Button increaseLevelBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/level_up_icon.png");
        increaseLevelBtn.setOnAction(e -> controller.openLevelUp(
                (Stage) increaseLevelBtn.getScene().getWindow(),
                levelValue
        ));

        Button notesBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/icon_notes.png");
        notesBtn.setOnAction(e -> controller.showNotes((Stage) notesBtn.getScene().getWindow()));

        HBox buttonsRow = new HBox(15,
                exportBtn,
                showDescBtn,
                notesBtn,
                editBtn,
                increaseLevelBtn,
                backBtn
        );
        buttonsRow.setAlignment(Pos.CENTER);
        buttonsRow.setPadding(new Insets(15, 20, 15, 20));
        buttonsRow.setMaxHeight(100);
        buttonsRow.setStyle("""
                -fx-background-color: linear-gradient(to bottom, #2d2d2d, #1a1a1a);
                -fx-background-radius: 12;
                -fx-border-color: rgba(200, 155, 60, 0.3);
                -fx-border-radius: 12;
                -fx-border-width: 1.5;
                -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 15, 0, 0, 5);
                """);

        // --- VRChat Save String Field (using AppTextField) ---
        VBox vrcContainer = this.vrcSavePaneBuilder.build(character, controller);

        VBox rightLayout = new VBox(12, buttonsRow, vrcContainer);
        rightLayout.setAlignment(Pos.TOP_RIGHT);
        HBox.setMargin(rightLayout, new Insets(10, 10, 10, 0));


        getChildren().addAll(leftBox, rightLayout);

        backBtn.setOnAction(e -> {
            controller.backToStart();
        });

        this.tooltipInstaller.install(exportBtn, showDescBtn, notesBtn, editBtn, increaseLevelBtn, backBtn);
    }

    /**
     * Refreshes the active effects display by rebuilding the effects container.
     *
     * @param character the character to pull data from
     */
    public void refresh(Character character) {
        activeEffectsPane.rebuild(character);
    }
}
