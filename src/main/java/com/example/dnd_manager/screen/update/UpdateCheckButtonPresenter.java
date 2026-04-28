package com.example.dnd_manager.screen.update;

import com.example.dnd_manager.lang.I18n;

public class UpdateCheckButtonPresenter {

    public void showChecking(UpdateCheckButtonView view) {
        view.setDisabled(true);
        view.setText(I18n.t("button.checking"));
    }

    public void showReady(UpdateCheckButtonView view) {
        view.setDisabled(false);
        view.setText(I18n.t("button.checkUpdate"));
    }
}













