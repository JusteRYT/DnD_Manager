package com.example.dnd_manager.screen.selection;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.infrastructure.assets.CharacterAssetResolver;
import com.example.dnd_manager.lang.I18n;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class CharacterSelectionCard extends VBox {

    private static final Logger log = LoggerFactory.getLogger(CharacterSelectionCard.class);
    private static final double CARD_WIDTH = 410;
    private static final double PORTRAIT_WIDTH = 118;
    private static final double PORTRAIT_HEIGHT = 150;

    private final CharacterSelectionStyleProvider styles;

    public CharacterSelectionCard(
            Character character,
            Consumer<Character> onSelected,
            boolean isEdit,
            Runnable onDelete,
            CharacterSelectionStyleProvider styles
    ) {
        this.styles = styles;
        setMinWidth(CARD_WIDTH);
        setPrefWidth(CARD_WIDTH);
        setMaxWidth(CARD_WIDTH);
        setSpacing(12);
        setAlignment(Pos.TOP_LEFT);
        setCursor(Cursor.HAND);
        setStyle(styles.cardStyle(false));
        setOnMouseEntered(e -> setStyle(styles.cardStyle(true)));
        setOnMouseExited(e -> setStyle(styles.cardStyle(false)));
        setOnMouseClicked(e -> onSelected.accept(character));

        HBox body = new HBox(12, createPortrait(character), createInfo(character));
        body.setAlignment(Pos.TOP_LEFT);

        getChildren().add(body);
        if (isEdit) {
            getChildren().add(createEditModeActions(onDelete));
        } else {
            getChildren().add(createLoadHint());
        }
    }

    private StackPane createPortrait(Character character) {
        ImageView avatar = new ImageView();
        try {
            Image image = CharacterAssetResolver.getImage(character, character.getAvatarImage(), 120, 150);
            avatar.setImage(image);
        } catch (Exception ex) {
            log.debug("Failed to load avatar for character selection card '{}'", character.getName(), ex);
        }

        avatar.setFitWidth(PORTRAIT_WIDTH);
        avatar.setFitHeight(PORTRAIT_HEIGHT);
        avatar.setPreserveRatio(false);
        avatar.setSmooth(true);

        Rectangle clip = new Rectangle(PORTRAIT_WIDTH, PORTRAIT_HEIGHT);
        clip.setArcWidth(22);
        clip.setArcHeight(22);
        avatar.setClip(clip);

        StackPane frame = new StackPane(avatar);
        frame.setMinSize(PORTRAIT_WIDTH + 12, PORTRAIT_HEIGHT + 12);
        frame.setPrefSize(PORTRAIT_WIDTH + 12, PORTRAIT_HEIGHT + 12);
        frame.setMaxSize(PORTRAIT_WIDTH + 12, PORTRAIT_HEIGHT + 12);
        frame.setStyle(styles.portraitFrameStyle());
        return frame;
    }

    private VBox createInfo(Character character) {
        VBox info = new VBox(8);
        info.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label name = new Label(valueOrFallback(character.getName()).toUpperCase());
        name.setStyle(styles.cardNameStyle());
        name.setWrapText(true);
        name.setTextAlignment(TextAlignment.LEFT);
        name.setMaxWidth(250);

        FlowPane identity = new FlowPane(6, 6);
        identity.getChildren().addAll(
                chip(I18n.t("levelField.name") + " " + character.getLevel()),
                chip(valueOrFallback(character.getRace())),
                chip(valueOrFallback(character.getCharacterClass()))
        );

        HBox resources = new HBox(6,
                metric(I18n.t("hpField.name"), healthText(character)),
                metric(I18n.t("armorField.name"), String.valueOf(character.getArmor())),
                metric(I18n.t("manaField.name"), manaText(character))
        );
        resources.setAlignment(Pos.CENTER_LEFT);

        FlowPane content = new FlowPane(6, 6);
        content.getChildren().addAll(
                chip(I18n.t("selection.skillsCount").formatted(character.getSkills().size())),
                chip(I18n.t("selection.buffsCount").formatted(character.getBuffs().size())),
                chip(I18n.t("selection.itemsCount").formatted(character.getInventory().size())),
                chip(I18n.t("selection.familiarsCount").formatted(character.getFamiliars().size()))
        );

        info.getChildren().addAll(name, identity, resources, content);
        return info;
    }

    private HBox createEditModeActions(Runnable onDelete) {
        Label hint = new Label(I18n.t("selection.editOpenHint"));
        hint.setStyle(styles.openHintStyle());
        hint.setMaxWidth(Double.MAX_VALUE);

        Button deleteButton = new Button(I18n.t("button.delete"));
        deleteButton.setMinHeight(34);
        deleteButton.setPrefHeight(34);
        deleteButton.setMinWidth(118);
        deleteButton.setOnAction(e -> {
            e.consume();
            onDelete.run();
        });
        deleteButton.setOnMouseClicked(Event::consume);
        styles.applyDangerAction(deleteButton);

        HBox actions = new HBox(10, hint, deleteButton);
        actions.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(hint, Priority.ALWAYS);
        return actions;
    }

    private Label createLoadHint() {
        Label hint = new Label(I18n.t("selection.openHint"));
        hint.setMaxWidth(Double.MAX_VALUE);
        hint.setAlignment(Pos.CENTER_RIGHT);
        hint.setStyle(styles.openHintStyle());
        return hint;
    }

    private Label chip(String text) {
        Label label = new Label(text);
        label.setStyle(styles.chipStyle());
        return label;
    }

    private VBox metric(String captionText, String valueText) {
        Label caption = new Label(captionText.toUpperCase());
        caption.setStyle(styles.metricCaptionStyle());

        Label value = new Label(valueText);
        value.setStyle(styles.metricValueStyle());

        VBox box = new VBox(2, caption, value);
        box.setMinWidth(68);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle(styles.metricStyle());
        return box;
    }

    private String healthText(Character character) {
        return character.getCurrentHp() + "/" + character.getMaxHp();
    }

    private String manaText(Character character) {
        return character.getCurrentMana() + "/" + character.getMaxMana();
    }

    private String valueOrFallback(String value) {
        return value == null || value.isBlank() ? I18n.t("selection.unknown") : value;
    }
}
