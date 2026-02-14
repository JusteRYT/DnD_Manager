package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.Objects;

/**
 * Row component representing single currency type with icon, value and +/- buttons.
 */
public class CoinRow extends HBox {

    public CoinRow(String iconPath, Label valueLabel, Runnable onAdd, Runnable onSubtract) {
        setSpacing(12);
        setAlignment(Pos.CENTER_LEFT);

        ImageView icon = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream(iconPath)
                ))
        );
        icon.setFitWidth(26);
        icon.setFitHeight(26);
        icon.setPreserveRatio(true);

        valueLabel.setStyle("""
                -fx-text-fill: #f2f2f2;
                -fx-font-weight: bold;
                -fx-font-family: "Consolas";
                -fx-font-size: 14px;
                """);

        StackPane valueBox = new StackPane(valueLabel);
        valueBox.setMinWidth(60);
        valueBox.setPrefWidth(60);
        valueBox.setMaxWidth(60);
        valueBox.setStyle("""
                -fx-background-color: #1e1e1e;
                -fx-background-radius: 6;
                -fx-padding: 4 10;
                """);

        var addBtn = AppButtonFactory.createValueAdjustButton(true, 28,
                AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);
        addBtn.setOnAction(e -> onAdd.run());

        var removeBtn = AppButtonFactory.createValueAdjustButton(false, 28,
                AppTheme.BUTTON_DANGER, AppTheme.BUTTON_DANGER_HOVER);
        removeBtn.setOnAction(e -> onSubtract.run());

        getChildren().addAll(icon, valueBox, addBtn, removeBtn);
    }
}
