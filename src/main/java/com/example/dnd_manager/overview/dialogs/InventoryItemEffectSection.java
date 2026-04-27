package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.theme.AppCheckBox;
import com.example.dnd_manager.theme.AppTextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InventoryItemEffectSection {

    private final AppCheckBox equippedCheckBox;
    private final AppTextField effectDisplayField;
    private final VBox container;

    public InventoryItemEffectSection(
            String checkboxTitle,
            String effectLabel,
            String effectPrompt,
            boolean equipped,
            String initialValue
    ) {
        this.equippedCheckBox = new AppCheckBox(checkboxTitle);
        this.equippedCheckBox.setSelected(equipped);

        this.effectDisplayField = new AppTextField(initialValue, false);
        this.effectDisplayField.getField().setPromptText(effectPrompt);

        this.container = new VBox(
                5,
                new Label(effectLabel),
                this.effectDisplayField.getField()
        );
        this.container.setVisible(equipped);

        this.equippedCheckBox.setOnAction(() -> container.setVisible(equippedCheckBox.isSelected()));
    }

    public AppCheckBox getEquippedCheckBox() {
        return equippedCheckBox;
    }

    public VBox getContainer() {
        return container;
    }

    public boolean isEquipped() {
        return equippedCheckBox.isSelected();
    }

    public String getEffectName() {
        return effectDisplayField.getText();
    }
}

