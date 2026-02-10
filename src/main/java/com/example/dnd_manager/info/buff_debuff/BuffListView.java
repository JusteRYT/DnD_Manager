package com.example.dnd_manager.info.buff_debuff;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;


/**
 * One side of buffs or debuffs list.
 * All icons are fixed-size squares for row alignment.
 */
public class BuffListView extends VBox {

    private static final int ICON_SIZE = 60;
    private static final int ICON_CONTAINER_SIZE = 70;

    public BuffListView(
            String titleText,
            List<Buff> buffs,
            BuffColumnStyle style,
            String characterName
    ) {
        setSpacing(8);
        setPadding(new Insets(4));

        Label title = new Label(titleText);
        title.setStyle("""
            -fx-text-fill: %s;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
        """.formatted(style.accentColor()));

        FlowPane icons = new FlowPane(10, 10);
        icons.setPrefWrapLength(300);

        if (buffs.isEmpty()) {
            Label empty = new Label("No active effects");
            empty.setStyle("-fx-text-fill: #777;");
            icons.getChildren().add(empty);
        } else {
            buffs.forEach(buff -> {
                StackPane container = new StackPane();
                container.setPrefSize(ICON_CONTAINER_SIZE, ICON_CONTAINER_SIZE);
                container.setMinSize(ICON_CONTAINER_SIZE, ICON_CONTAINER_SIZE);
                container.setMaxSize(ICON_CONTAINER_SIZE, ICON_CONTAINER_SIZE);
                container.setAlignment(Pos.CENTER);

                ImageView icon = BuffIconViewFactory.create(buff, style, ICON_SIZE, characterName);
                icon.setFitWidth(ICON_SIZE);
                icon.setFitHeight(ICON_SIZE);
                icon.setPreserveRatio(false);
                icon.setSmooth(true);

                container.getChildren().add(icon);
                icons.getChildren().add(container);
            });
        }

        getChildren().addAll(title, icons);
    }
}