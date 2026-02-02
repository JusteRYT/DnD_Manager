package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.info.buff_debuff.Buff;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.util.List;

/**
 * Displays character buffs and debuffs as icons with tooltips.
 */
public class BuffsView extends FlowPane {

    public BuffsView(List<Buff> buffs) {
        super(8, 8);
        setPrefWrapLength(400);

        for (Buff buff : buffs) {
            ImageView icon = new ImageView();
            icon.setFitWidth(40);
            icon.setFitHeight(40);

            Tooltip tooltip = TooltipFactory.createBuffTooltip(buff);
            Tooltip.install(icon, tooltip);

            icon.setStyle("""
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, black, 4, 0.5, 0, 0);
            """);

            getChildren().add(icon);
        }
    }
}