package com.example.dnd_manager.theme.factory;

import javafx.scene.control.Button;

import java.util.function.Consumer;

/**
 * Backward-compatible facade for application buttons.
 * Implementation is split across focused factories by button type.
 */
public final class AppButtonFactory {

    private AppButtonFactory() {
    }

    public static Button createValueAdjustButton(boolean isPlus, int size, String baseColor, String hoverColor) {
        return GradientButtonFactory.createValueAdjustButton(isPlus, size, baseColor, hoverColor);
    }

    public static Button addEffectButton() {
        return GradientButtonFactory.addEffectButton();
    }

    public static Button hudIconButton(int size, String iconPath) {
        return IconButtonFactory.hudIconButton(size, iconPath);
    }

    public static Button customButton(String text, int width, String primaryColor, String secondaryColor) {
        return OutlineButtonFactory.customButton(text, width, primaryColor, secondaryColor);
    }

    public static Button actionEditIcon(String iconPath, int size) {
        return GradientButtonFactory.actionEditIcon(iconPath, size);
    }

    public static Button deleteButton(int size) {
        return GradientButtonFactory.deleteButton(size);
    }

    public static Button actionSave(String text) {
        return GradientButtonFactory.actionSave(text);
    }

    public static Button addIcon(String text) {
        return GradientButtonFactory.addIcon(text);
    }

    public static Button actionImport(String text, int width) {
        return OutlineButtonFactory.actionImport(text, width);
    }

    public static Button actionExit(String text, int width) {
        return OutlineButtonFactory.actionExit(text, width);
    }

    public static Button primaryButton(String text, int width, int height, int fontSize) {
        return GradientButtonFactory.primaryButton(text, width, height, fontSize);
    }

    public static Button actionAdd(String text, int width) {
        return OutlineButtonFactory.actionAdd(text, width);
    }

    public static Button assetPickerButton() {
        return IconButtonFactory.assetPickerButton();
    }

    public static void attachAssetPicker(Button button, Consumer<String> onPathSelected) {
        IconButtonFactory.attachAssetPicker(button, onPathSelected);
    }
}

