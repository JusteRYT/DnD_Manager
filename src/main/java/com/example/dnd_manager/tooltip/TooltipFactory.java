package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.skills.Skill;
import javafx.scene.control.Tooltip;

/**
 * Factory for creating styled tooltips for game entities.
 */
public final class TooltipFactory {

    private TooltipFactory() {
    }

    public static Tooltip createSkillTooltip(Skill skill) {
        Tooltip tooltip = new Tooltip(
                skill.name() + "\n\n" +
                        skill.description() + "\n\n" +
                        "Activation: " + skill.activationType() + "\n" +
                        "Damage: " + skill.damage()
        );

        tooltip.setStyle("""
                -fx-font-size: 13px;
                -fx-background-color: #1e1e1e;
                -fx-text-fill: #ffffff;
                -fx-padding: 10;
                """);

        return tooltip;
    }

    public static Tooltip createBuffTooltip(Buff buff) {
        Tooltip tooltip = new Tooltip(
                buff.name() + " (" + buff.type() + ")\n\n" +
                        buff.description()
        );

        tooltip.setStyle("""
                -fx-font-size: 13px;
                -fx-background-color: #2b2b2b;
                -fx-text-fill: #ffffff;
                -fx-padding: 10;
                """);

        return tooltip;
    }

}
