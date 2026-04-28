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
import javafx.scene.layout.VBox;

public class CharacterHeroCardSectionBuilder {

    private final CharacterFormStyleProvider styleProvider;

    public CharacterHeroCardSectionBuilder(CharacterFormStyleProvider styleProvider) {
        this.styleProvider = styleProvider;
    }

    public HBox build(AvatarPicker avatarPicker, BaseInfoForm baseInfoForm, StatsEditor statsEditor) {
        HBox container = new HBox(10);
        container.setPadding(new Insets(10));

        VBox statsSection = new VBox(15);
        statsSection.setPadding(new Insets(20));
        statsSection.setMinWidth(180);
        statsSection.setAlignment(Pos.TOP_CENTER);
        statsSection.setStyle(styleProvider.statsSectionStyle());

        Label titleStats = new Label(I18n.t("stats.label"));
        titleStats.setStyle(styleProvider.statsTitleStyle());
        statsSection.getChildren().addAll(titleStats, statsEditor);

        HBox.setHgrow(baseInfoForm, Priority.ALWAYS);
        container.getChildren().addAll(avatarPicker, baseInfoForm, statsSection);
        return container;
    }
}












