package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.editors.common.EntityEditorButtonFactory;
import com.example.dnd_manager.info.editors.common.EntityEditorStyleProvider;
import com.example.dnd_manager.info.skills.model.SkillEffect;
import com.example.dnd_manager.info.skills.model.TypeEffects;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
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

public class EffectsBuilderField extends VBox {

    @Getter
    private final ObservableList<SkillEffect> effects = FXCollections.observableArrayList();
    private final EntityEditorStyleProvider styleProvider = new EntityEditorStyleProvider();
    private final FlowPane tagsPane = new FlowPane(10, 10);
    private final Label errorLabel;

    private final AppTextField valueField;
    private final AppTextField customField;
    private final AppComboBox<String> typeBox;

    public EffectsBuilderField() {
        setSpacing(8);
        setStyle(styleProvider.effectsBuilderStyle());

        errorLabel = new Label(I18n.t("labelField.effectRequired"));
        errorLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 10px; -fx-font-weight: bold;");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        typeBox = new AppComboBox<>();
        for (TypeEffects type : TypeEffects.values()) typeBox.getItems().add(type.getName());
        typeBox.setValue(TypeEffects.DAMAGE.getName());

        valueField = new AppTextField(I18n.t("textField.promptText.effectValue"), true);
        customField = new AppTextField(I18n.t("textField.promptText.effectType"), true);
        customField.getField().setVisible(false);
        customField.getField().setManaged(false);

        typeBox.valueProperty().addListener((obs, old, newVal) -> {
            boolean isCustom = TypeEffects.CUSTOM.matches(newVal);
            customField.getField().setVisible(isCustom);
            customField.getField().setManaged(isCustom);
        });

        Button addBtn = EntityEditorButtonFactory.statControl("+");
        addBtn.setOnAction(e -> addCurrentEffect());

        Label sectionLabel = new Label(I18n.t("textFieldLabel.effectsBuilder"));
        sectionLabel.setStyle("-fx-text-fill: #b7c9dd; -fx-font-size: 10px; -fx-font-weight: bold;");

        HBox inputs = new HBox(8, typeBox, customField.getField(),
                new VBox(0, valueField.getField(), errorLabel), addBtn);
        inputs.setAlignment(Pos.TOP_LEFT);
        tagsPane.setMinHeight(34);

        getChildren().addAll(sectionLabel, inputs, tagsPane);
    }

    public void addCurrentEffect() {
        if (valueField.getText().isBlank()) {
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }

        String typeName = TypeEffects.CUSTOM.matches(typeBox.getValue())
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

        tag.setStyle(effectTagStyle(effect.getType()));

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
        if (TypeEffects.DAMAGE.matches(typeName)) return "#c56f82";
        if (TypeEffects.HEAL.matches(typeName)) return "#7ebd9b";
        if (TypeEffects.INCREASE_ARMOR.matches(typeName) || TypeEffects.DECREASE_ARMOR.matches(typeName)) return "#8fb3d8";
        if (TypeEffects.DICE_INCREASE.matches(typeName) || TypeEffects.DICE_DECREASE.matches(typeName)) return "#b5a1d8";
        return "#9fb2c8";
    }

    private String effectTagStyle(String typeName) {
        if (TypeEffects.DAMAGE.matches(typeName)) {
            return styleProvider.entityChipStyle("rgba(83, 32, 48, 0.70)", "rgba(197, 111, 130, 0.52)", "#ffdce3")
                    + "-fx-cursor: hand;";
        }
        if (TypeEffects.HEAL.matches(typeName)) {
            return styleProvider.entityChipStyle("rgba(28, 66, 55, 0.70)", "rgba(126, 189, 155, 0.48)", "#dbf5e7")
                    + "-fx-cursor: hand;";
        }
        if (TypeEffects.INCREASE_ARMOR.matches(typeName) || TypeEffects.DECREASE_ARMOR.matches(typeName)) {
            return styleProvider.entityChipStyle("rgba(31, 52, 78, 0.70)", "rgba(143, 179, 216, 0.48)", "#dcecff")
                    + "-fx-cursor: hand;";
        }
        if (TypeEffects.DICE_INCREASE.matches(typeName) || TypeEffects.DICE_DECREASE.matches(typeName)) {
            return styleProvider.entityChipStyle("rgba(48, 41, 76, 0.70)", "rgba(181, 161, 216, 0.48)", "#eee5ff")
                    + "-fx-cursor: hand;";
        }
        return styleProvider.entityChipStyle("rgba(35, 47, 72, 0.70)", "rgba(159, 178, 200, 0.42)", "#e4edf6")
                + "-fx-cursor: hand;";
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











