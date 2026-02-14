package com.example.dnd_manager.info.text;

import com.example.dnd_manager.info.text.dto.BaseInfoData;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.screen.FormMode;
import com.example.dnd_manager.theme.AppTextField;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BaseInfoForm extends VBox {

    private final AppTextField nameField = new AppTextField(I18n.t("nameField.name"));
    private final AppTextField raceField = new AppTextField(I18n.t("raceField.name"));
    private final AppTextField classField = new AppTextField(I18n.t("classField.name"));
    private final AppTextField hpField = new AppTextField(I18n.t("hpField.name"));
    private final AppTextField armorField = new AppTextField(I18n.t("armorField.name"));
    private final AppTextField manaField = new AppTextField(I18n.t("manaField.name"));
    private final AppTextField levelField = new AppTextField(I18n.t("levelField.name"));

    private final FormMode mode;

    public BaseInfoForm() {
        this(FormMode.CREATE, null);
    }

    public BaseInfoForm(FormMode mode, BaseInfoData data) {
        this.mode = mode;
        setPadding(new Insets(20));
        setSpacing(15);

        Label sectionTitle = new Label("BASIC INFORMATION");
        sectionTitle.setStyle("-fx-font-size: 18px; " +
                "-fx-text-fill: #c89b3c; " +
                "-fx-font-weight: bold; " +
                "-fx-letter-spacing: 1px;");
        getChildren().add(sectionTitle);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        // Layout
        // Name: Row 0, spans 2 cols
        add(grid, I18n.t("nameField.name"), nameField, 0, 0, 2);
        add(grid, I18n.t("raceField.name"), raceField, 0, 1, 1);
        add(grid, I18n.t("classField.name"), classField, 1, 1, 1);
        add(grid, I18n.t("levelField.name"), levelField, 0, 2, 1);
        add(grid, I18n.t("hpField.name"), hpField, 1, 2, 1);
        add(grid, I18n.t("armorField.name"), armorField, 0, 3, 1);
        add(grid, I18n.t("manaField.name"), manaField, 1, 3, 1);

        GridPane.setHgrow(nameField.getField(), Priority.ALWAYS);
        GridPane.setHgrow(raceField.getField(), Priority.ALWAYS);
        GridPane.setHgrow(classField.getField(), Priority.ALWAYS);

        getChildren().add(grid);

        if (mode == FormMode.EDIT && data != null) applyEditData(data);
        if (mode == FormMode.EDIT) nameField.getField().setDisable(true);
    }

    private void add(GridPane grid, String labelText, AppTextField appField, int col, int row, int colSpan) {
        VBox container = new VBox(6);
        Label label = new Label(labelText.toUpperCase());
        label.setStyle("-fx-text-fill: #c89b3c; -fx-font-size: 12px; -fx-font-weight: bold;");

        container.getChildren().addAll(label, appField.getField());
        grid.add(container, col, row, colSpan, 1);
    }



    public BaseInfoData getData() {
        return new BaseInfoData(
                nameField.getText().trim(),
                raceField.getText().trim(),
                classField.getText().trim(),
                hpField.getText().trim(),
                armorField.getText().trim(),
                manaField.getText().trim(),
                levelField.getText().trim()
        );
    }

    private void applyEditData(BaseInfoData data) {
        nameField.setText(data.name());
        raceField.setText(data.race());
        classField.setText(data.characterClass());
        hpField.setText(data.hp());
        armorField.setText(data.armor());
        manaField.setText(data.mana());
        levelField.setText(data.level());
    }
}