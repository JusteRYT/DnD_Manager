package com.example.dnd_manager.info.buff_debuff;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Column view for buffs or debuffs.
 */
public class BuffColumnView extends VBox {

    public BuffColumnView(
            String titleText,
            List<Buff> buffs,
            BuffColumnStyle style
    ) {
        setSpacing(6);
        setPadding(new Insets(6));
        setPrefWidth(180);

        Label title = new Label(titleText);
        title.setStyle("""
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-text-fill: %s;
        """.formatted(style.accentColor()));

        FlowPane iconsPane = new FlowPane(6, 6);
        iconsPane.setPrefWrapLength(160);

        buffs.forEach(buff ->
                iconsPane.getChildren().add(
                        BuffIconViewFactory.create(buff, style)
                )
        );

        setStyle("""
            -fx-background-color: #1e1e1e;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            -fx-border-color: %s;
        """.formatted(style.accentColor()));

        getChildren().addAll(title, iconsPane);
    }
}
