package com.example.dnd_manager.screen.form;

import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class MagicalBorderDecorator {

    private final CharacterFormStyleProvider styleProvider;

    public MagicalBorderDecorator(CharacterFormStyleProvider styleProvider) {
        this.styleProvider = styleProvider;
    }

    public void apply(Node node) {
        node.setStyle(node.getStyle() + styleProvider.magicalBorderStyle());
        DropShadow softGlow = new DropShadow(BlurType.THREE_PASS_BOX, Color.web("#ffffff", 0.08), 15, 0, 0, 0);
        node.setEffect(softGlow);
    }
}












