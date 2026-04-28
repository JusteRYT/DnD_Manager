package com.example.dnd_manager.overview.ui.topbar;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class TopBarVrcSavePaneBuilder {

    public VBox build(Character character, TopBarController controller) {
        Label vrcLabel = new Label(I18n.t("label.textFieldVRCHATSave"));
        vrcLabel.setStyle("-fx-text-fill: #c89b3c; -fx-font-size: 11px; -fx-font-weight: bold;");

        AppTextField vrcSaveAppField = new AppTextField(character.getSaveString(), false);
        TextField field = vrcSaveAppField.getField();
        field.setPromptText(I18n.t("prompt.saveVRCString"));

        field.setStyle(field.getStyle() + """
                -fx-font-family: 'Consolas', 'Monospace';
                -fx-font-size: 12px;
                -fx-opacity: 0.9;
                -fx-padding: 8 12 8 12;
                -fx-text-fill: #eee;
                """);

        field.setPrefWidth(410);
        field.setMaxWidth(410);
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                controller.persistSaveString(field.getText());
            }
        });

        Tooltip vrcTooltip = new Tooltip(I18n.t("popup.saveVRCString"));
        vrcTooltip.setShowDelay(Duration.millis(200));
        vrcTooltip.setStyle("""
                -fx-font-size: 15px;
                -fx-font-weight: bold;
                -fx-border-width: 1;
                -fx-padding: 10 15 10 15;
                """);
        Tooltip.install(field, vrcTooltip);

        VBox container = new VBox(5, vrcLabel, field);
        container.setAlignment(Pos.TOP_RIGHT);
        return container;
    }
}












