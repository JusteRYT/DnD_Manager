package com.example.dnd_manager.info.buff_debuff;

import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;


/**
 * One side of buffs or debuffs list.
 * Displays buff icons with source indicators (Innate or Item).
 */
public class BuffListView extends VBox {

    private static final int ICON_SIZE = 60;
    private static final int ICON_CONTAINER_SIZE = 70;

    public BuffListView(
            String titleText,
            List<BuffWithSource> buffs,
            BuffColumnStyle style,
            String characterName
    ) {
        setSpacing(8);
        setPadding(new Insets(4));

        Label title = new Label(titleText);
        title.setStyle("""
            -fx-text-fill: %s;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
        """.formatted(style.accentColor()));

        FlowPane icons = new FlowPane(10, 10);
        icons.setPrefWrapLength(300);

        if (buffs.isEmpty()) {
            Label empty = new Label(I18n.t("buffListView.titleEmpty"));
            empty.setStyle("-fx-text-fill: #777;");
            icons.getChildren().add(empty);
        } else {
            buffs.forEach(buffWithSource -> {
                StackPane container = new StackPane();
                container.setPrefSize(ICON_CONTAINER_SIZE, ICON_CONTAINER_SIZE);
                container.setMinSize(ICON_CONTAINER_SIZE, ICON_CONTAINER_SIZE);
                container.setMaxSize(ICON_CONTAINER_SIZE, ICON_CONTAINER_SIZE);
                container.setAlignment(Pos.CENTER);
                container.setStyle("-fx-cursor: hand;");

                String accentColor = style.accentColor();
                String idleStyle = "-fx-background-color: transparent; -fx-effect: null;";
                String hoverStyle = "-fx-effect: dropshadow(three-pass-box, %s, 8, 0.3, 0, 0);".formatted(accentColor);

                // Используем объект баффа для иконки
                ImageView icon = BuffIconViewFactory.create(buffWithSource.buff(), style, ICON_SIZE, characterName);
                icon.setFitWidth(ICON_SIZE);
                icon.setFitHeight(ICON_SIZE);

                // --- Добавляем индикатор источника ---
                Node sourceBadge = createSourceBadge(buffWithSource.sourceItem());
                StackPane.setAlignment(sourceBadge, Pos.TOP_RIGHT);
                // Немного смещаем внутрь, чтобы не обрезалось
                StackPane.setMargin(sourceBadge, new Insets(2, 2, 0, 0));

                container.getChildren().addAll(icon, sourceBadge);

                container.setOnMouseEntered(e -> container.setStyle(hoverStyle));
                container.setOnMouseExited(e -> container.setStyle(idleStyle));

                icons.getChildren().add(container);
            });
        }

        getChildren().addAll(title, icons);
    }

    /**
     * Создает компактный индикатор источника (предмет или врожденное).
     */
    private Node createSourceBadge(InventoryItem sourceItem) {
        boolean isFromItem = sourceItem != null;

        Label badge = getLabel(isFromItem);

        // --- Настройка Tooltip ---
        String tooltipText = isFromItem ? sourceItem.getName() : I18n.t("skill.source.innate");
        Tooltip tooltip = new Tooltip(tooltipText);

        // Увеличиваем шрифт и добавляем отступы для солидности
        tooltip.setStyle("""
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-color: #1a1a1a;
            -fx-text-fill: #55ccff;
            -fx-border-color: #c89b3c;
            -fx-border-width: 1;
            -fx-padding: 5 10 5 10;
        """);

        // Настройка задержек (опционально)
        tooltip.setShowDelay(Duration.millis(300));
        tooltip.setShowDuration(Duration.seconds(10));

        Tooltip.install(badge, tooltip);

        return badge;
    }

    private static Label getLabel(boolean isFromItem) {
        String iconText = isFromItem ? "📦" : "👤";
        String bgColor = isFromItem ? "#55ccff" : "#4a4a4a";
        String textColor = isFromItem ? "#1a1a1a" : "#c89b3c";

        Label badge = new Label(iconText);

        return getLabelForBadge(bgColor, textColor, badge);
    }

    private static Label getLabelForBadge(String bgColor, String textColor, Label badge) {
        double size = 18;
        badge.setMinSize(size, size);
        badge.setMaxSize(size, size);
        badge.setPrefSize(size, size);
        badge.setAlignment(Pos.CENTER);

        badge.setStyle(String.format("""
                    -fx-background-color: %1$s;
                    -fx-text-fill: %2$s;
                    -fx-font-size: 9px;
                    -fx-font-weight: bold;
                    -fx-background-radius: 4;
                    -fx-border-color: #1a1a1a;
                    -fx-border-width: 1;
                    -fx-border-radius: 4;
                """, bgColor, textColor));
        return badge;
    }

}