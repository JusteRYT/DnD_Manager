package com.example.dnd_manager.info.editors.common;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.infrastructure.assets.CharacterAssetResolver;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public abstract class AbstractEntityRow<T> extends HBox {

    protected final T item;
    protected final Character character;
    protected final EntityEditorStyleProvider styleProvider = new EntityEditorStyleProvider();
    private StackPane iconFrame;

    public AbstractEntityRow(T item, Runnable onRemove, Runnable onEdit, Character character) {
        this.item = item;
        this.character = character;

        setSpacing(14);
        setAlignment(Pos.CENTER_LEFT);
        setMinHeight(Region.USE_PREF_SIZE);
        applyRowAccent(
                "rgba(175, 196, 216, 0.14)",
                "rgba(75, 93, 127, 0.38)",
                "rgba(175, 196, 216, 0.58)"
        );

        // --- Icon ---
        ImageView iconView = new ImageView();
        iconView.setFitWidth(42);
        iconView.setFitHeight(42);
        iconView.setPreserveRatio(true);
        iconView.setImage(resolveIcon(item, character));
        iconView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0, 0, 0);");
        iconView.setSmooth(true);

        iconFrame = new StackPane(iconView);
        iconFrame.setMinSize(58, 58);
        iconFrame.setPrefSize(58, 58);
        iconFrame.setMaxSize(58, 58);
        iconFrame.setStyle(styleProvider.entityIconFrameStyle(
                "rgba(175, 196, 216, 0.16)",
                "rgba(75, 93, 127, 0.42)"
        ));

        // --- Info Box (Content) ---
        VBox infoBox = new VBox(5);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        fillContent(infoBox, item);

        HBox actionButtons = new HBox(8);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        actionButtons.setMinWidth(Region.USE_PREF_SIZE);

        Button editButton = EntityEditorButtonFactory.secondary(I18n.t("button.editEditor"), 118);
        editButton.setMinWidth(Region.USE_PREF_SIZE);
        editButton.setOnAction(e -> onEdit.run());

        Button removeButton = EntityEditorButtonFactory.danger("x");
        removeButton.setOnAction(e -> onRemove.run());

        actionButtons.getChildren().addAll(editButton, removeButton);

        getChildren().addAll(iconFrame, infoBox, actionButtons);
    }

    protected final void applyRowAccent(String accentGlow, String idleBorder, String hoverBorder) {
        String idleStyle = styleProvider.entityRowStyle(false, accentGlow, idleBorder);
        String hoverStyle = styleProvider.entityRowStyle(true, accentGlow, hoverBorder);

        setStyle(idleStyle);
        setOnMouseEntered(e -> setStyle(hoverStyle));
        setOnMouseExited(e -> setStyle(idleStyle));

        if (iconFrame != null) {
            iconFrame.setStyle(styleProvider.entityIconFrameStyle(accentGlow, hoverBorder));
        }
    }

    protected final Label createTitleLabel(String text, String color, String glow) {
        Label label = new Label(text);
        label.setStyle(styleProvider.entityTitleStyle(color, glow));
        label.setWrapText(true);
        return label;
    }

    protected final Label createMetaLabel(String text) {
        Label label = new Label(text);
        label.setStyle(styleProvider.entityMetaStyle());
        label.setWrapText(true);
        return label;
    }

    protected final Label createDescriptionLabel(String text) {
        Label label = new Label(text);
        label.setStyle(styleProvider.entityDescriptionStyle());
        label.setWrapText(true);
        return label;
    }

    protected final Label createChip(String text, String background, String borderColor, String textColor) {
        Label chip = new Label(text);
        chip.setStyle(styleProvider.entityChipStyle(background, borderColor, textColor));
        return chip;
    }

    /**
     * Наследник должен наполнить VBox метками (Label)
     */
    protected abstract void fillContent(VBox container, T item);

    /**
     * Наследник должен вернуть путь к иконке
     */
    protected abstract String getIconPath(T item);

    // Общая логика загрузки картинки
    private Image resolveIcon(T item, Character character) {
        return CharacterAssetResolver.getImage(character, getIconPath(item), 32, 32);
    }
}












