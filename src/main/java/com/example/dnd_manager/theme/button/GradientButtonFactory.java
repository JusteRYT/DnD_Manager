package com.example.dnd_manager.theme.button;

import javafx.scene.control.Button;

/**
 * Facade for gradient and emphasis-style buttons.
 */
final class GradientButtonFactory {
    private static final GradientButtonStyleProvider STYLE_PROVIDER = new GradientButtonStyleProvider();
    private static final ValueAdjustGradientButtonBuilder VALUE_ADJUST_BUILDER = new ValueAdjustGradientButtonBuilder(STYLE_PROVIDER);
    private static final AddEffectGradientButtonBuilder ADD_EFFECT_BUILDER = new AddEffectGradientButtonBuilder();
    private static final EditIconGradientButtonBuilder EDIT_ICON_BUILDER = new EditIconGradientButtonBuilder(STYLE_PROVIDER);
    private static final DeleteGradientButtonBuilder DELETE_BUILDER = new DeleteGradientButtonBuilder(STYLE_PROVIDER);
    private static final PrimaryGradientButtonBuilder PRIMARY_BUILDER = new PrimaryGradientButtonBuilder(STYLE_PROVIDER);

    private GradientButtonFactory() {
    }

    static Button createValueAdjustButton(boolean isPlus, int size, String baseColor, String hoverColor) {
        return VALUE_ADJUST_BUILDER.build(isPlus, size, baseColor, hoverColor);
    }

    static Button addEffectButton() {
        return ADD_EFFECT_BUILDER.build();
    }

    static Button actionEditIcon(String iconPath, int size) {
        return EDIT_ICON_BUILDER.build(iconPath, size);
    }

    static Button deleteButton(int size) {
        return DELETE_BUILDER.build(size);
    }

    static Button actionSave(String text) {
        return addIcon(text);
    }

    static Button addIcon(String text) {
        return PRIMARY_BUILDER.buildIconButton(text);
    }

    static Button primaryButton(String text, int width, int height, int fontSize) {
        return PRIMARY_BUILDER.buildPrimaryButton(text, width, height, fontSize);
    }
}












