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
        node.setEffect(softGlow());
    }

    public DropShadow softGlow() {
        return new DropShadow(BlurType.THREE_PASS_BOX, Color.web("rgba(175, 196, 216, 0.18)"), 22, 0.22, 0, 8);
    }
}












