package com.example.dnd_manager.overview.ui.topbar;

import com.example.dnd_manager.overview.ui.effects.ActiveEffectsPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public record TopBarInfoComponents(
        HBox leftBox,
        Label hpLabel,
        Label armorLabel,
        Label levelValue,
        ActiveEffectsPane activeEffectsPane
) {
}













