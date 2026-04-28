package com.example.dnd_manager.theme.button;

import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.utils.Utils;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

class DeleteGradientButtonBuilder {

    private final GradientButtonStyleProvider styleProvider;

    DeleteGradientButtonBuilder(GradientButtonStyleProvider styleProvider) {
        this.styleProvider = styleProvider;
    }

    Button build(int size) {
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);

        StackPane icon = Utils.createAdjustIcon(false, size);
        button.setGraphic(icon);

        final boolean[] isActive = {false};
        String colorNormal = AppTheme.BUTTON_REMOVE;
        String colorActive = AppTheme.BUTTON_DANGER;
        String colorHover = AppTheme.BUTTON_REMOVE_HOVER;

        button.setStyle(styleProvider.deleteButtonStyle(colorNormal, false));
        button.setOnMouseEntered(e -> {
            if (!isActive[0]) {
                button.setStyle(styleProvider.deleteButtonStyle(colorHover, true));
            }
        });
        button.setOnMouseExited(e ->
                button.setStyle(styleProvider.deleteButtonStyle(isActive[0] ? colorActive : colorNormal, isActive[0])));
        button.setOnAction(e -> {
            isActive[0] = !isActive[0];
            button.setStyle(styleProvider.deleteButtonStyle(isActive[0] ? colorActive : colorHover, isActive[0]));
            button.setUserData(isActive[0]);
            button.setTranslateY(isActive[0] ? 1 : 0);
        });

        return button;
    }
}












