package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.panel.BuffsInventoryPanel;
import com.example.dnd_manager.overview.panel.CurrencyPanel;
import com.example.dnd_manager.overview.ui.ManaBar;
import com.example.dnd_manager.overview.ui.TopBar;
import com.example.dnd_manager.overview.utils.StatsPanel;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.tooltip.SkillsView;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import lombok.Getter;

/**
 * Main character overview screen.
 * Combines top bar, stats, buffs/inventory and skills bar.
 */
@Getter
public class CharacterOverviewScreen extends BorderPane {

    private final StorageService storageService;
    private final ManaBar manaBar;

    public CharacterOverviewScreen(String name, StorageService storageService) {
        this.storageService = storageService;

        Character character = storageService.loadCharacter(name)
                .orElseThrow(() -> new RuntimeException("Character not found: " + name));

        setPadding(new Insets(15));
        setStyle("-fx-background-color: #1e1e1e;");

        // Top bar
        TopBar topBar = new TopBar(character, this, storageService);
        setTop(topBar);

        // Main content grid (Stats слева, Buffs+Inventory справа)
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(15);
        mainGrid.setVgap(15);
        mainGrid.setPadding(new Insets(10));

        BuffsInventoryPanel buffsInventoryPanel = new BuffsInventoryPanel(character, storageService);
        StatsPanel statsPanel = new StatsPanel(character);

        mainGrid.add(statsPanel, 0, 0);
        mainGrid.add(buffsInventoryPanel, 1, 0);

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPercentWidth(50);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(leftCol, rightCol);

        CurrencyPanel currencyPanel = new CurrencyPanel(character, storageService);
        manaBar = currencyPanel.getManaBar();

        mainGrid.add(currencyPanel, 0, 1);

        // Главный VBox: сверху mainGrid, снизу bottomPanels
        VBox centerVBox = new VBox(15); // 15px между grid и нижними панелями
        centerVBox.getChildren().addAll(mainGrid);

        setCenter(centerVBox);

        // Skills bar
        HBox skillsBar = new HBox();
        skillsBar.setPadding(new Insets(10));
        skillsBar.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");
        skillsBar.getChildren().add(new SkillsView(character));
        setBottom(skillsBar);
    }
}