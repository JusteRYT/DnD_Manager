package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.ui.CoinRow;
import com.example.dnd_manager.overview.ui.ManaBar;
import com.example.dnd_manager.store.StorageService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import lombok.Getter;

public class CurrencyPanel extends VBox {

    private final Character character;
    private final StorageService storageService;

    private final Label goldText = new Label();
    private final Label silverText = new Label();
    private final Label copperText = new Label();

    private static final int SILVER_PER_GOLD = 2;
    private static final int COPPER_PER_SILVER = 5;
    private static final int COPPER_PER_GOLD = SILVER_PER_GOLD * COPPER_PER_SILVER;

    private final InspirationPanel inspirationPanel;
    @Getter
    private final ManaBar manaBar;

    public CurrencyPanel(Character character, StorageService storageService) {
        this.character = character;
        this.storageService = storageService;

        // Инициализируем панель вдохновения (она содержит и ману)
        this.inspirationPanel = new InspirationPanel(character, storageService);
        this.manaBar = inspirationPanel.getManaBar();

        setSpacing(15); // Чуть больше воздуха между блоками

        // Блок валюты
        VBox currencyBox = createCurrencyBox();

        // Основной контейнер: Слева валюта, Справа вдохновение/мана
        HBox contentBox = new HBox(15, currencyBox, inspirationPanel);
        contentBox.setAlignment(Pos.TOP_LEFT);

        // Растягиваем элементы
        HBox.setHgrow(currencyBox, Priority.ALWAYS);
        HBox.setHgrow(inspirationPanel, Priority.ALWAYS);
        currencyBox.setMaxWidth(Double.MAX_VALUE);
        inspirationPanel.setMaxWidth(Double.MAX_VALUE);

        getChildren().add(contentBox);

        refresh();
    }

    private VBox createCurrencyBox() {
        Label title = new Label(I18n.t("currencyPanel.title"));
        title.setStyle("""
            -fx-text-fill: #ffd700; /* Ярко-золотой текст */
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.8), 2, 0, 0, 1);
            """);

        // Создаем строки с монетами
        VBox coinsList = new VBox(10,
                new CoinRow("/com/example/dnd_manager/icon/icon_gold.png", goldText,
                        () -> changeTotalCopper(COPPER_PER_GOLD), () -> changeTotalCopper(-COPPER_PER_GOLD)),
                new CoinRow("/com/example/dnd_manager/icon/icon_silver.png", silverText,
                        () -> changeTotalCopper(COPPER_PER_SILVER), () -> changeTotalCopper(-COPPER_PER_SILVER)),
                new CoinRow("/com/example/dnd_manager/icon/icon_cooper.png", copperText,
                        () -> changeTotalCopper(1), () -> changeTotalCopper(-1))
        );

        return getVBox(title, coinsList);
    }

    private static VBox getVBox(Label title, VBox coinsList) {
        VBox container = new VBox(10, title, coinsList);
        container.setPadding(new Insets(12));

        // --- ЗОЛОТОЙ СТИЛЬ ---
        container.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
            -fx-background-radius: 8;
            -fx-border-color: #c89b3c; /* Золотая рамка */
            -fx-border-radius: 8;
            -fx-border-width: 1;
            /* Золотое свечение */
            -fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.25), 15, 0, 0, 0);
            """);
        return container;
    }

    private void changeTotalCopper(int delta) {
        int newTotal = Math.max(0, character.getTotalCooper() + delta);
        character.setTotalCooper(newTotal);
        afterChange();
    }

    private void afterChange() {
        refresh();
        storageService.saveCharacter(character);
    }

    private void refresh() {
        int total = character.getTotalCooper();
        int silver = total / COPPER_PER_SILVER;
        int gold = silver / SILVER_PER_GOLD;

        goldText.setText(String.valueOf(gold));
        silverText.setText(String.valueOf(silver));
        copperText.setText(String.valueOf(total));

        inspirationPanel.refresh();
    }
}