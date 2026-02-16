package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.panel.BuffsInventoryPanel;
import com.example.dnd_manager.overview.panel.ResourcePanel;
import com.example.dnd_manager.overview.ui.ManaBar;
import com.example.dnd_manager.overview.ui.TopBar;
import com.example.dnd_manager.overview.utils.StatsPanel;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.tooltip.SkillsView;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class CharacterOverviewScreen extends BorderPane {

    private final StorageService storageService;
    private final ManaBar manaBar;
    private final Stage stage;

    public CharacterOverviewScreen(Stage stage, Character character, StorageService storageService) {
        this.storageService = storageService;
        this.stage = stage;
        setStyle("-fx-background-color: #1e1e1e;");

        // --- Top Bar (Всегда сверху) ---
        TopBar topBar = new TopBar(character, this, storageService);
        topBar.setPadding(new Insets(0,35,0,25));
        setTop(topBar);

        // --- Основной контент ---
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(15);
        mainGrid.setVgap(15);
        mainGrid.setPadding(new Insets(10));

        BuffsInventoryPanel buffsInventoryPanel = new BuffsInventoryPanel(character, storageService, stage);
        StatsPanel statsPanel = new StatsPanel(character);
        ResourcePanel resourcePanel = new ResourcePanel(character, storageService);
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

        SkillsView skillsView = new SkillsView(character);
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
}