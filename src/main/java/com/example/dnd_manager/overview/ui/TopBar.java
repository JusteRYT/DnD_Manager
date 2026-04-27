package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.screen.CharacterOverviewScreen;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

/**
 * Top bar with avatar, name/race/class, level and action buttons.
 * Level styled as recessed card with white "Level:" and orange number.
 * Includes a button to increment level in the right block with confirmation dialog.
 */
public class TopBar extends HBox {

    private final ActiveEffectsPane activeEffectsPane;
    private final TopBarController controller;
    private final TopBarVrcSavePaneBuilder vrcSavePaneBuilder;
    private final TopBarTooltipInstaller tooltipInstaller;
    private final TopBarInfoPaneBuilder infoPaneBuilder;
    private final TopBarActionsRowBuilder actionsRowBuilder;
    private final StageResolver stageResolver;

    public TopBar(
            Character character,
            CharacterOverviewScreen parentScreen,
            ScreenNavigator screenNavigator,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction
    ) {
        this(
                character,
                parentScreen,
                saveCharacterUseCase,
                backToStartAction,
                new TopBarVrcSavePaneBuilder(),
                new TopBarTooltipInstaller(),
                new TopBarInfoPaneBuilder(),
                new TopBarActionsRowBuilder(),
                new JavaFxStageResolver()
        );
    }

    TopBar(
            Character character,
            CharacterOverviewScreen parentScreen,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction,
            TopBarVrcSavePaneBuilder vrcSavePaneBuilder,
            TopBarTooltipInstaller tooltipInstaller,
            TopBarInfoPaneBuilder infoPaneBuilder,
            TopBarActionsRowBuilder actionsRowBuilder,
            StageResolver stageResolver
    ) {
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: transparent;");
        this.controller = new TopBarController(
                character,
                saveCharacterUseCase,
                backToStartAction
        );
        this.vrcSavePaneBuilder = vrcSavePaneBuilder;
        this.tooltipInstaller = tooltipInstaller;
        this.infoPaneBuilder = infoPaneBuilder;
        this.actionsRowBuilder = actionsRowBuilder;
        this.stageResolver = stageResolver;

        TopBarInfoComponents infoComponents = infoPaneBuilder.build(character);
        this.activeEffectsPane = infoComponents.activeEffectsPane();

        TopBarActionButtons actionButtons = actionsRowBuilder.build(
                controller,
                stageResolver,
                stage -> controller.openEditStats(
                        stage,
                        () -> {
                            infoComponents.hpLabel().setText(String.valueOf(character.getCurrentHp()));
                            infoComponents.armorLabel().setText(String.valueOf(character.getArmor()));
                            parentScreen.getManaBar().refresh();
                            infoComponents.levelValue().setText(String.valueOf(character.getLevel()));
                        }
                ),
                stage -> controller.openLevelUp(
                        stage,
                        () -> infoComponents.levelValue().setText(String.valueOf(character.getLevel()))
                )
        );

        // --- VRChat Save String Field (using AppTextField) ---
        VBox vrcContainer = this.vrcSavePaneBuilder.build(character, controller);

        VBox rightLayout = new VBox(12, actionButtons.row(), vrcContainer);
        rightLayout.setAlignment(Pos.TOP_RIGHT);
        HBox.setMargin(rightLayout, new Insets(10, 10, 10, 0));

        getChildren().addAll(infoComponents.leftBox(), rightLayout);

        this.tooltipInstaller.install(
                actionButtons.exportButton(),
                actionButtons.showDescriptionButton(),
                actionButtons.notesButton(),
                actionButtons.editButton(),
                actionButtons.increaseLevelButton(),
                actionButtons.backButton()
        );
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
