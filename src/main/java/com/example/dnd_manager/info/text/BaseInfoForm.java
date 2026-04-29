package com.example.dnd_manager.info.text;

import com.example.dnd_manager.info.text.dto.BaseInfoData;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.screen.FormMode;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.IntegerField;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BaseInfoForm extends VBox {

    private final BaseInfoFormStyleProvider styleProvider = new BaseInfoFormStyleProvider();
    private final AppTextField nameField = new AppTextField(I18n.t("nameField.name"), true);
    private final AppTextField raceField = new AppTextField(I18n.t("raceField.name"), true);
    private final AppTextField classField = new AppTextField(I18n.t("classField.name"), true);
    private final IntegerField hpField = new IntegerField(I18n.t("hpField.name"), true);
    private final IntegerField armorField = new IntegerField(I18n.t("armorField.name"), true);
    private final IntegerField manaField = new IntegerField(I18n.t("manaField.name"), true);
    private final IntegerField levelField = new IntegerField(I18n.t("levelField.name"), true);
    private final Label nameRequiredLabel = new Label(I18n.t("labelField.nameRequired"));

    public BaseInfoForm() {
        this(FormMode.CREATE, null);
    }

    public BaseInfoForm(FormMode mode, BaseInfoData data) {
        setPadding(new Insets(20));
        setSpacing(15);
        setStyle(styleProvider.panelStyle());

        Label sectionTitle = new Label(I18n.t("baseInfo.title"));
        sectionTitle.setStyle(styleProvider.sectionTitleStyle());
        getChildren().add(sectionTitle);

        configureNameValidation();

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(50);
        leftColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(50);
        rightColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(leftColumn, rightColumn);

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
        nameField.getField().setMaxWidth(Double.MAX_VALUE);
        raceField.getField().setMaxWidth(Double.MAX_VALUE);
        classField.getField().setMaxWidth(Double.MAX_VALUE);
        levelField.getField().setMaxWidth(Double.MAX_VALUE);
        hpField.getField().setMaxWidth(Double.MAX_VALUE);
        armorField.getField().setMaxWidth(Double.MAX_VALUE);
        manaField.getField().setMaxWidth(Double.MAX_VALUE);

        getChildren().add(grid);

        if (mode == FormMode.EDIT && data != null) applyEditData(data);
    }

    private void add(GridPane grid, String labelText, AppTextField appField, int col, int row, int colSpan) {
        VBox container = new VBox(5);
        container.setStyle(styleProvider.fieldCardStyle());
        Label label = new Label(labelText.toUpperCase());
        label.setStyle(styleProvider.fieldLabelStyle());

        appField.getField().setPromptText("");
        VBox fieldAndErrorBox = new VBox(2, appField.getField());
        if (appField == nameField) {
            fieldAndErrorBox.getChildren().add(nameRequiredLabel);
            fieldAndErrorBox.setMinHeight(45);
        } else {
            fieldAndErrorBox.setMinHeight(45);
        }

        container.getChildren().addAll(label, fieldAndErrorBox);
        grid.add(container, col, row, colSpan, 1);
    }

    private void configureNameValidation() {
        nameRequiredLabel.setStyle(styleProvider.requiredLabelStyle());

        nameRequiredLabel.setVisible(false);
        nameRequiredLabel.setManaged(false);
        nameRequiredLabel.setPadding(new Insets(0, 0, 0, 5));

        nameField.getField().textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) {
                nameRequiredLabel.setVisible(false);
                nameRequiredLabel.setManaged(false);
            }
        });
    }

    public boolean validateName() {
        boolean valid = !nameField.getText().isBlank();
        nameRequiredLabel.setVisible(!valid);
        nameRequiredLabel.setManaged(!valid);
        return valid;
    }

    public boolean validate() {
        return !validateName();
    }

    // --- Остальные методы без изменений ---

    public BaseInfoData getData() {
        return new BaseInfoData(
                nameField.getText().trim(),
                raceField.getText().trim(),
                classField.getText().trim(),
                hpField.getInt(),
                armorField.getInt(),
                manaField.getInt(),
                levelField.getInt()
        );
    }

    private void applyEditData(BaseInfoData data) {
        nameField.setText(data.name());
        raceField.setText(data.race());
        classField.setText(data.characterClass());
        hpField.setValue(data.hp());
        armorField.setValue(data.armor());
        manaField.setValue(data.mana());
        levelField.setValue(data.level());
    }
}












