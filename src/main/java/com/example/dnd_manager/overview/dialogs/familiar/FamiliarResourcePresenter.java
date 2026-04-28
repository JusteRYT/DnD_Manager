package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.domain.Character;

import java.util.Objects;

public class FamiliarResourcePresenter {

    private final FamiliarResourceSnapshotFactory snapshotFactory;
    private final FamiliarResourceDisplay display;

    public FamiliarResourcePresenter(
            FamiliarResourceSnapshotFactory snapshotFactory,
            FamiliarResourceDisplay display
    ) {
        this.snapshotFactory = Objects.requireNonNull(snapshotFactory, "snapshotFactory must not be null");
        this.display = Objects.requireNonNull(display, "display must not be null");
    }

    public void refresh(Character familiar) {
        display.show(snapshotFactory.from(familiar));
    }

    public Runnable updateHandler(Character familiar, Runnable onAnyUpdate) {
        return () -> {
            refresh(familiar);
            if (onAnyUpdate != null) {
                onAnyUpdate.run();
            }
        };
    }
}













