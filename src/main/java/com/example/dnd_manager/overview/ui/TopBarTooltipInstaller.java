package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.utils.ButtonPopupInstaller;
import com.example.dnd_manager.overview.utils.PopupFactory;
import javafx.scene.control.Button;

public class TopBarTooltipInstaller {

    public void install(
            Button exportBtn,
            Button showDescBtn,
            Button notesBtn,
            Button editBtn,
            Button increaseLevelBtn,
            Button backBtn
    ) {
        ButtonPopupInstaller.install(exportBtn, PopupFactory.tooltip(I18n.t("button.showExport")));
        ButtonPopupInstaller.install(showDescBtn, PopupFactory.tooltip(I18n.t("button.showDescription")));
        ButtonPopupInstaller.install(editBtn, PopupFactory.tooltip(I18n.t("button.editStatsPopup")));
        ButtonPopupInstaller.install(backBtn, PopupFactory.tooltip(I18n.t("button.showExitPopup")));
        ButtonPopupInstaller.install(increaseLevelBtn, PopupFactory.tooltip(I18n.t("button.levelIncrease")));
        ButtonPopupInstaller.install(notesBtn, PopupFactory.tooltip(I18n.t("button.showNotesPopup")));
    }
}
