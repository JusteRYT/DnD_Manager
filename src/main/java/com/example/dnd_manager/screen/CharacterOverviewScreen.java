package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.panel.BuffsInventoryPanel;
import com.example.dnd_manager.overview.panel.CurrencyPanel;
import com.example.dnd_manager.overview.ui.ManaBar;
import com.example.dnd_manager.overview.ui.TopBar;
import com.example.dnd_manager.overview.utils.StatsPanel;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppScrollPaneFactory;
import com.example.dnd_manager.tooltip.SkillsView;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import lombok.Getter;

@Getter
public class CharacterOverviewScreen extends BorderPane {

    private final StorageService storageService;
    private final ManaBar manaBar;

    public CharacterOverviewScreen(String name, StorageService storageService) {
        this.storageService = storageService;

        Character character = storageService.loadCharacter(name)
                .orElseThrow(() -> new RuntimeException("Character not found: " + name));

        setStyle("-fx-background-color: #1e1e1e;");

        // --- Top Bar (Всегда сверху) ---
        TopBar topBar = new TopBar(character, this, storageService);
        setTop(topBar);

        // --- Основной контент ---
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(15);
        mainGrid.setVgap(15);
        mainGrid.setPadding(new Insets(10));

        BuffsInventoryPanel buffsInventoryPanel = new BuffsInventoryPanel(character, storageService);
        StatsPanel statsPanel = new StatsPanel(character);
        CurrencyPanel currencyPanel = new CurrencyPanel(character, storageService);
        manaBar = currencyPanel.getManaBar();

        mainGrid.add(statsPanel, 0, 0);
        mainGrid.add(buffsInventoryPanel, 1, 0, 1, 2);
        mainGrid.add(currencyPanel, 0, 1);

        mainGrid.getRowConstraints().clear();

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPercentWidth(50);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(50);
        mainGrid.getColumnConstraints().setAll(leftCol, rightCol);

        // Панель скиллов
        HBox skillsBar = new HBox();
        skillsBar.setPadding(new Insets(10));
        skillsBar.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");
        skillsBar.getChildren().add(new SkillsView(character));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        // Собираем всё в VBox, который будет внутри ScrollPane
        VBox contentContainer = new VBox(15);
        contentContainer.setPadding(new Insets(15));
        contentContainer.getChildren().addAll(mainGrid, spacer, skillsBar);

        // Позволяем контенту внутри VBox занимать всё свободное место


        // --- Обертка в ScrollPane ---
        // Используем твою фабрику для сохранения стиля
        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(contentContainer);

        // Эти настройки заставляют контент растягиваться под ширину окна,
        // но позволяют появляться скроллу, если высота окна слишком мала.
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        setCenter(scrollPane);
    }
}