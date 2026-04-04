package com.example.dnd_manager.info.buff_debuff;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Popup content view for displaying detailed buff information.
 */
public class BuffPopupView extends VBox {

    private static final double MAX_POPUP_WIDTH = 250;
    public BuffPopupView(Buff buff) {
        setSpacing(6);
        setPadding(new Insets(12));
        setAlignment(Pos.TOP_LEFT);

        setPrefWidth(MAX_POPUP_WIDTH);
        setMaxWidth(MAX_POPUP_WIDTH);

        setStyle("""
            -fx-background-color: #252526;
            -fx-background-radius: 8;
            -fx-border-color: #3c3c3c;
            -fx-border-radius: 8;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 10, 0, 0, 5);
        """);

        Label name = new Label(buff.name());
        name.setStyle("""
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-text-fill: #c89b3c;
        """);

        name.setWrapText(true);
        name.setMaxWidth(MAX_POPUP_WIDTH - 24);
        name.setMinHeight(VBox.USE_PREF_SIZE);

        Label type = new Label(buff.type());
        type.setStyle("""
            -fx-text-fill: #9cdcfe;
            -fx-font-size: 12px;
        """);

        Label description = new Label(buff.description());
        description.setWrapText(true);
        description.setMaxWidth(220);
        description.setStyle("""
            -fx-text-fill: #d4d4d4;
            -fx-font-size: 12px;
            -fx-line-spacing: 2px;
        """);

        description.setMinHeight(VBox.USE_PREF_SIZE);
        getChildren().addAll(name, type, description);
    }
}
