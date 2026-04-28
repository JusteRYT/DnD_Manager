package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.panel.BuffsInventoryPanel;
import com.example.dnd_manager.overview.panel.ResourcePanel;
import com.example.dnd_manager.overview.ui.resources.ManaBar;
import com.example.dnd_manager.overview.ui.topbar.TopBar;
import com.example.dnd_manager.overview.utils.StatsPanel;
import com.example.dnd_manager.tooltip.view.SkillsView;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CharacterOverviewScreen extends BorderPane {

    private final ManaBar manaBar;
    private final Stage stage;
    private final BuffsInventoryPanel buffsInventoryPanel;
    private final SkillsView skillsView;
    private final Character character;
    private final TopBar topBar;
    private final ScreenNavigator screenNavigator;
    private final SaveCharacterUseCase saveCharacterUseCase;

    public CharacterOverviewScreen(
            Stage stage,
            Character character,
            ScreenNavigator screenNavigator,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction
    ) {
        this.stage = stage;
        this.character = character;
        this.screenNavigator = Objects.requireNonNull(screenNavigator, "screenNavigator must not be null");
        this.saveCharacterUseCase = Objects.requireNonNull(saveCharacterUseCase, "saveCharacterUseCase must not be null");
        setStyle("-fx-background-color: #1e1e1e;");

        // --- Top Bar (Всегда сверху) ---
        this.topBar = new TopBar(
                character,
                this,
                saveCharacterUseCase,
                backToStartAction
        );
        this.topBar.setPadding(new Insets(0, 35, 0, 25));
        setTop(topBar);

        // --- Основной контент ---
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(15);
        mainGrid.setVgap(15);
        mainGrid.setPadding(new Insets(10));

        this.buffsInventoryPanel = new BuffsInventoryPanel(
                character,
                stage,
                this::refreshUI,
                saveCharacterUseCase
        );
        StatsPanel statsPanel = new StatsPanel(character);
        ResourcePanel resourcePanel = new ResourcePanel(character, saveCharacterUseCase);
        manaBar = resourcePanel.getManaBar();

        mainGrid.add(statsPanel, 0, 0);
        mainGrid.add(buffsInventoryPanel, 1, 0, 1, 2);
        mainGrid.add(resourcePanel, 0, 1);

        mainGrid.getRowConstraints().clear();

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPercentWidth(50);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(50);
        mainGrid.getColumnConstraints().setAll(leftCol, rightCol);

        this.skillsView = new SkillsView(character);
        // Панель скиллов
        VBox skillsBar = new VBox();
        skillsBar.setPadding(new Insets(10));
        skillsBar.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");

        HBox.setHgrow(skillsView, Priority.ALWAYS);
        skillsBar.getChildren().add(skillsView);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox contentContainer = new VBox(15);
        contentContainer.setPadding(new Insets(15));
        contentContainer.getChildren().addAll(mainGrid, spacer, skillsBar);

        setCenter(contentContainer);
    }

    public void refreshUI() {
        // 1. Сохраняем данные
        saveCharacterUseCase.execute(character);

        // 2. Обновляем визуальные компоненты
        skillsView.refresh(character);
        buffsInventoryPanel.refreshBuffs();

        topBar.refresh(character);
    }
}












