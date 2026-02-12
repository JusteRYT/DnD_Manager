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
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * Unified card panel with Currency (left) and Inspiration (right).
 * Each section has its own title and visible border.
 */
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

        this.inspirationPanel = new InspirationPanel(character, storageService);
        this.manaBar = inspirationPanel.getManaBar();

        setSpacing(10);

        VBox currencyBox = createCurrencyBox();

        HBox contentBox = new HBox(12, currencyBox, inspirationPanel);
        contentBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(currencyBox, Priority.ALWAYS);
        HBox.setHgrow(inspirationPanel, Priority.ALWAYS);

        currencyBox.setMaxWidth(Double.MAX_VALUE);
        inspirationPanel.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(contentBox);
        card.setSpacing(12);
        card.setPadding(new Insets(12));
        card.setStyle("""
            -fx-background-color: #252526;
            -fx-background-radius: 8;
            """);

        getChildren().add(card);

        refresh();
    }

    private VBox createCurrencyBox() {
        Label title = new Label(I18n.t("currencyPanel.title"));
        title.setStyle("""
            -fx-text-fill: #c89b3c;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            """);

        VBox box = getVBox(title);
        box.setSpacing(8);
        box.setPadding(new Insets(8));
        box.setStyle("""
            -fx-border-color: #3a3a3a;
            -fx-border-radius: 6;
            -fx-border-width: 1;
            -fx-background-radius: 6;
            -fx-background-color: #252526;
            """);

        return box;
    }

    private VBox getVBox(Label title) {
        VBox coinsBox = new VBox(
                new CoinRow("/com/example/dnd_manager/icon/icon_gold.png", goldText,
                        () -> changeTotalCopper(COPPER_PER_GOLD), () -> changeTotalCopper(-COPPER_PER_GOLD)),
                new CoinRow("/com/example/dnd_manager/icon/icon_silver.png", silverText,
                        () -> changeTotalCopper(COPPER_PER_SILVER), () -> changeTotalCopper(-COPPER_PER_SILVER)),
                new CoinRow("/com/example/dnd_manager/icon/icon_cooper.png", copperText,
                        () -> changeTotalCopper(1), () -> changeTotalCopper(-1))
        );
        coinsBox.setSpacing(10);

        return new VBox(title, coinsBox);
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
