package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.Objects;

public class EffectsBuilderField extends VBox {

    @Getter
    private final ObservableList<SkillEffect> effects = FXCollections.observableArrayList();
    private final FlowPane tagsPane = new FlowPane(10, 10);
    private final Label errorLabel;

    private final AppTextField valueField;
    private final AppTextField customField;
    private final AppComboBox<String> typeBox;

    public EffectsBuilderField() {
        setSpacing(8);
        setStyle("-fx-background-color: rgba(0,0,0,0.2); -fx-padding: 15; -fx-background-radius: 5;");

        errorLabel = new Label(I18n.t("labelField.effectRequired"));
        errorLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 10px; -fx-font-weight: bold;");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        typeBox = new AppComboBox<>();
        for (TypeEffects type : TypeEffects.values()) typeBox.getItems().add(type.getName());
        typeBox.setValue(TypeEffects.DAMAGE.getName());

        valueField = new AppTextField(I18n.t("textField.promptText.effectValue"));
        customField = new AppTextField(I18n.t("textField.promptText.effectType"));
        customField.getField().setVisible(false);
        customField.getField().setManaged(false);

        typeBox.valueProperty().addListener((obs, old, newVal) -> {
            boolean isCustom = Objects.equals(newVal, TypeEffects.CUSTOM.getName());
            customField.getField().setVisible(isCustom);
            customField.getField().setManaged(isCustom);
        });

        Button addBtn = AppButtonFactory.addEffectButton();
        addBtn.setOnAction(e -> addCurrentEffect());

        Label sectionLabel = new Label(I18n.t("textFieldLabel.effectsBuilder"));
        sectionLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 10px; -fx-font-weight: bold;");

        HBox inputs = new HBox(8, typeBox, customField.getField(),
                new VBox(0, valueField.getField(), errorLabel), addBtn);
        inputs.setAlignment(Pos.TOP_LEFT);

        getChildren().addAll(sectionLabel, inputs, tagsPane);
    }

    public void addCurrentEffect() {
        if (valueField.getText().isBlank()) {
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }

        String typeName = Objects.equals(typeBox.getValue(), TypeEffects.CUSTOM.getName())
                ? customField.getText().trim()
                : typeBox.getValue();

        SkillEffect effect = SkillEffect.of(typeBox.getValue(), typeName, valueField.getText().trim());
        addEffectToUI(effect);

        valueField.clear();
        customField.clear();
        errorLabel.setVisible(false);
    }

    public void addEffect(SkillEffect effect) {
        if (effect != null) {
            addEffectToUI(effect);
        }
    }

    /**
     * Создает визуальный тег с эффектом свечения
     */
    private void addEffectToUI(SkillEffect effect) {
        effects.add(effect);

        String colorHex = getEffectColor(effect.getType());
        Label tag = new Label(effect.toString());

        tag.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-padding: 4 10; " +
                        "-fx-background-radius: 12; -fx-cursor: hand; -fx-font-weight: bold; -fx-font-size: 11px;",
                colorHex
        ));

        // ДОБАВЛЯЕМ СВЕЧЕНИЕ
        applyGlow(tag, colorHex);

        tag.setOnMouseClicked(e -> {
            effects.remove(effect);
            tagsPane.getChildren().remove(tag);
        });

        tagsPane.getChildren().add(tag);
    }

    private void applyGlow(Label tag, String hexColor) {
        Color color = Color.web(hexColor);

        DropShadow glow = new DropShadow();
        glow.setBlurType(BlurType.THREE_PASS_BOX);
        glow.setColor(color.deriveColor(0, 1.2, 1.2, 0.6));
        glow.setRadius(15);
        glow.setSpread(0.25);
        glow.setOffsetX(0);
        glow.setOffsetY(0);

        tag.setEffect(glow);

        tag.setOnMouseEntered(e -> glow.setRadius(20));
        tag.setOnMouseExited(e -> glow.setRadius(15));
    }

    private String getEffectColor(String typeName) {
        if (typeName.contains(TypeEffects.DAMAGE.getName())) return "#722f37";
        if (typeName.contains(TypeEffects.HEAL.getName())) return "#2e5a1c";
        return "#3b444b";
    }

    public void clear() {
        effects.clear();
        tagsPane.getChildren().clear();
        valueField.clear();
    }

    public boolean validate() {
        boolean valid = !effects.isEmpty();
        errorLabel.setVisible(!valid);
        errorLabel.setManaged(!valid);
        return valid;
    }
}