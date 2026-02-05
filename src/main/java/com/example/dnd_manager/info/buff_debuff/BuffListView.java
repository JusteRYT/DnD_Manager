package com.example.dnd_manager.info.buff_debuff;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * One side of buffs or debuffs list.
 */
public class BuffListView extends VBox {

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
        icons.setPrefWrapLength(260);

        if (buffs.isEmpty()) {
            Label empty = new Label("No active effects");
            empty.setStyle("-fx-text-fill: #777;");
            icons.getChildren().add(empty);
        } else {
            buffs.forEach(buff ->
                    icons.getChildren().add(
                            BuffIconViewFactory.create(buff, style, 48, characterName)
                    )
            );
        }

        getChildren().addAll(title, icons);
    }
}
