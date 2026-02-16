package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.store.StorageService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CurrencyBox extends VBox {
    private final Label goldText = new Label();
    private final Label silverText = new Label();
    private final Label copperText = new Label();

    private final String pathGoldIcon = "/com/example/dnd_manager/icon/icon_gold.png";
    private final String pathSilverIcon = "/com/example/dnd_manager/icon/icon_silver.png";
    private final String pathCooperIcon = "/com/example/dnd_manager/icon/icon_cooper.png";

    public CurrencyBox(Character character, StorageService storageService) {
        setSpacing(10);
        setPadding(new Insets(12));

        Label title = new Label(I18n.t("currencyPanel.title"));
        title.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 16px; -fx-font-weight: bold;");

        VBox coinsList = new VBox(8,
                new CoinRow(pathGoldIcon, goldText, () -> update(character, storageService, 10), () -> update(character, storageService, -10)),
                new CoinRow(pathSilverIcon, silverText, () -> update(character, storageService, 5), () -> update(character, storageService, -5)),
                new CoinRow(pathCooperIcon, copperText, () -> update(character, storageService, 1), () -> update(character, storageService, -1))
        );

        String commonStyle = """
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
                -fx-background-radius: 8;
                -fx-border-color: #c89b3c;
                -fx-border-radius: 8;
                """;

        String idleStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.2), 10, 0, 0, 0);";
        String hoverStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, #c89b3c, 10, 0.3, 0, 0);";

        setStyle(idleStyle);

        setOnMouseEntered(e -> setStyle(hoverStyle));
        setOnMouseExited(e -> setStyle(idleStyle));

        getChildren().addAll(title, coinsList);
        refresh(character);
    }

    private void update(Character c, StorageService s, int delta) {
        c.setTotalCooper(Math.max(0, c.getTotalCooper() + delta));
        s.saveCharacter(c);
        refresh(c);
    }

    private void refresh(Character c) {
        goldText.setText(String.valueOf(c.getTotalCooper() / 10));
        silverText.setText(String.valueOf(c.getTotalCooper() / 5));
        copperText.setText(String.valueOf(c.getTotalCooper()));
    }
}
