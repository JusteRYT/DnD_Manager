package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.BuffsInventoryPanel;
import com.example.dnd_manager.overview.StatsPanel;
import com.example.dnd_manager.overview.TopBar;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.tooltip.SkillsView;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.Getter;

/**
 * Main character overview screen.
 * Combines top bar, stats, buffs/inventory and skills bar.
 */
@Getter
public class CharacterOverviewScreen extends BorderPane {

    private final StorageService storageService;

    public CharacterOverviewScreen(String name, StorageService storageService) {
        this.storageService = storageService;

        Character character = storageService.loadCharacter(name)
                .orElseThrow(() -> new RuntimeException("Character not found: " + name));

        setPadding(new Insets(15));
        setStyle("-fx-background-color: #1e1e1e;");

        // Top bar
        TopBar topBar = new TopBar(character, this);
        setTop(topBar);

        // Main content
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(15);
        mainGrid.setVgap(15);
        mainGrid.setPadding(new Insets(10));

        BuffsInventoryPanel buffsInventoryPanel = new BuffsInventoryPanel(character, storageService);
        StatsPanel statsPanel = new StatsPanel(character);

        // Добавляем слева Buffs+Inventory, справа Stats
        mainGrid.add(buffsInventoryPanel, 1, 0);
        mainGrid.add(statsPanel, 0, 0);

        // Колонки: Buffs+Inventory = 50%, Stats = 50% ширины
        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPercentWidth(50);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(leftCol, rightCol);

        setCenter(mainGrid);

        // Skills bar
        HBox skillsBar = new HBox();
        skillsBar.setPadding(new Insets(10));
        skillsBar.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");
        skillsBar.getChildren().add(new SkillsView(character));
        setBottom(skillsBar);
    }
}