package com.example.dnd_manager.screen.form;

import com.example.dnd_manager.info.avatar.AvatarPicker;
import com.example.dnd_manager.info.stats.editor.StatsEditor;
import com.example.dnd_manager.info.text.BaseInfoForm;
import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CharacterHeroCardSectionBuilder {

    private final CharacterFormStyleProvider styleProvider;

    public CharacterHeroCardSectionBuilder(CharacterFormStyleProvider styleProvider) {
        this.styleProvider = styleProvider;
    }

    public HBox build(AvatarPicker avatarPicker, BaseInfoForm baseInfoForm, StatsEditor statsEditor) {
        HBox container = new HBox(18);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(18));
        container.setStyle(styleProvider.heroCardStyle());

        VBox avatarColumn = new VBox(12, avatarPicker);
        avatarColumn.setAlignment(Pos.TOP_CENTER);
        avatarColumn.setPadding(new Insets(12));
        avatarColumn.setMinWidth(304);
        avatarColumn.setPrefWidth(304);
        avatarColumn.setStyle(styleProvider.heroColumnStyle());

        VBox statsSection = new VBox(12);
        statsSection.setPadding(new Insets(18));
        statsSection.setMinWidth(304);
        statsSection.setPrefWidth(304);
        statsSection.setAlignment(Pos.TOP_CENTER);
        statsSection.setStyle(styleProvider.statsSectionStyle());

        Label titleStats = new Label(I18n.t("stats.label"));
        titleStats.setStyle(styleProvider.statsTitleStyle());
        statsSection.getChildren().addAll(titleStats, statsEditor);

        avatarPicker.setMinWidth(260);
        avatarPicker.setMaxWidth(280);

        baseInfoForm.setMinWidth(520);
        baseInfoForm.setPrefWidth(620);
        baseInfoForm.setMaxWidth(700);
        StackPane baseInfoSlot = new StackPane(baseInfoForm);
        baseInfoSlot.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(baseInfoSlot, Priority.ALWAYS);

        container.getChildren().addAll(avatarColumn, baseInfoSlot, statsSection);
        return container;
    }
}












