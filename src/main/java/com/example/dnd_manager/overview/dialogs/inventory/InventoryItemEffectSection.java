package com.example.dnd_manager.overview.dialogs.inventory;

import com.example.dnd_manager.theme.AppCheckBox;
import com.example.dnd_manager.theme.AppTextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class InventoryItemEffectSection {

    @Getter
    private final AppCheckBox equippedCheckBox;
    private final AppTextField effectDisplayField;
    @Getter
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

        InventoryDialogStyleProvider styles = new InventoryDialogStyleProvider();
        Label label = new Label(effectLabel);
        label.setStyle(styles.effectLabelStyle());

        this.container = new VBox(
                5,
                label,
                this.effectDisplayField.getField()
        );
        this.container.setVisible(equipped);

        this.equippedCheckBox.setOnAction(() -> container.setVisible(equippedCheckBox.isSelected()));
    }

    public boolean isEquipped() {
        return equippedCheckBox.isSelected();
    }

    public String getEffectName() {
        return effectDisplayField.getText();
    }
}













