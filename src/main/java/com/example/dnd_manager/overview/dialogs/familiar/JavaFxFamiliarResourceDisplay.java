package com.example.dnd_manager.overview.dialogs.familiar;

import javafx.scene.control.Label;

import java.util.Objects;

public class JavaFxFamiliarResourceDisplay implements FamiliarResourceDisplay {

    private final Label hpLabel;
    private final Label mpLabel;
    private final Label acLabel;
    private final Label levelLabel;

    public JavaFxFamiliarResourceDisplay(Label hpLabel, Label mpLabel, Label acLabel, Label levelLabel) {
        this.hpLabel = Objects.requireNonNull(hpLabel, "hpLabel must not be null");
        this.mpLabel = Objects.requireNonNull(mpLabel, "mpLabel must not be null");
        this.acLabel = Objects.requireNonNull(acLabel, "acLabel must not be null");
        this.levelLabel = Objects.requireNonNull(levelLabel, "levelLabel must not be null");
    }

    @Override
    public void show(FamiliarResourceSnapshot snapshot) {
        hpLabel.setText(snapshot.hpText());
        mpLabel.setText(snapshot.mpText());
        acLabel.setText(snapshot.acText());
        levelLabel.setText(snapshot.levelText());
    }
}













