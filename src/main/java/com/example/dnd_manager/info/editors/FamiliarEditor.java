package com.example.dnd_manager.info.editors;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class FamiliarEditor extends VBox {

    private final Character familiar;

    private AppTextField nameField;
    private AppTextField raceField;
    private AppTextField classField;
    private IntegerField hpField;
    private IntegerField armorField;
    private IntegerField manaField;

    private final AtomicReference<String> avatarPath = new AtomicReference<>("");
    private Label iconPathLabel;

    // Вложенные редакторы для фамильяра
    private BuffEditor buffEditor;
    private InventoryEditor inventoryEditor;
    private SkillsEditor skillsEditor;

    public FamiliarEditor(Character familiar) {
        this.familiar = familiar;
        setSpacing(15);
        setPadding(new Insets(10));

        setupUI();
        loadData();
    }

    private void setupUI() {
        VBox baseCard = new VBox(12);
        baseCard.setStyle("-fx-background-color: #252526; -fx-padding: 15; -fx-background-radius: 8;");

        nameField = new AppTextField(I18n.t("familiar.prompt.name"));

        HBox typeRow = new HBox(10,
                new VBox(2, createLabel(I18n.t("raceField.name")), (raceField = new AppTextField("")).getField()),
                new VBox(2, createLabel(I18n.t("classField.name")), (classField = new AppTextField("")).getField())
        );

        HBox statsRow = new HBox(10,
                new VBox(2, createLabel(I18n.t("label.familiarsHP")), (hpField = new IntegerField("")).getField()),
                new VBox(2, createLabel(I18n.t("label.familiarsAC")), (armorField = new IntegerField("")).getField()),
                new VBox(2, createLabel(I18n.t("label.familiarsMP")), (manaField = new IntegerField("")).getField())
        );
        hpField.getField().setPrefWidth(80);
        armorField.getField().setPrefWidth(80);
        manaField.getField().setPrefWidth(80);

        iconPathLabel = new Label();
        iconPathLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 11px;");
        Button btnIcon = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        btnIcon.setOnAction(e -> chooseAvatar());

        baseCard.getChildren().addAll(
                new VBox(2, createLabel(I18n.t("textFieldLabel.name")), nameField.getField()),
                typeRow, statsRow, iconPathLabel, btnIcon
        );

        // 2. Добавляем вложенные редакторы
        buffEditor = new BuffEditor(familiar);
        inventoryEditor = new InventoryEditor(familiar);
        skillsEditor = new SkillsEditor(familiar);

        VBox content = new VBox(20, baseCard, skillsEditor, inventoryEditor, buffEditor);
        ScrollPane scroll = AppScrollPaneFactory.defaultPane(content);

        getChildren().add(scroll);
    }

    private void loadData() {
        nameField.setText(familiar.getName());
        raceField.setText(familiar.getRace());
        classField.setText(familiar.getCharacterClass());
        hpField.setText(String.valueOf(familiar.getMaxHp()));
        armorField.setText(String.valueOf(familiar.getArmor()));
        manaField.setText(String.valueOf(familiar.getMaxMana()));
        avatarPath.set(familiar.getAvatarImage());
        if (familiar.getAvatarImage() != null) {
            iconPathLabel.setText(new File(familiar.getAvatarImage()).getName());
        }
    }

    public void applyChanges() {
        familiar.setName(nameField.getText());
        familiar.setRace(raceField.getText());
        familiar.setCharacterClass(classField.getText());
        familiar.setMaxHp(parseSafe(hpField.getText()));
        familiar.setCurrentHp(parseSafe(hpField.getText()));
        familiar.setArmor(parseSafe(armorField.getText()));
        familiar.setMaxMana(parseSafe(manaField.getText()));
        familiar.setCurrentMana(parseSafe(manaField.getText()));
        familiar.setAvatarImage(avatarPath.get());

        buffEditor.applyTo(familiar);
        inventoryEditor.applyTo(familiar);
        skillsEditor.applyTo(familiar);
    }

    private void chooseAvatar() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            avatarPath.set(file.getAbsolutePath());
            iconPathLabel.setText(file.getName());
        }
    }

    private Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #666; -fx-font-size: 10px; -fx-font-weight: bold;");
        return l;
    }

    private int parseSafe(String v) {
        try { return Integer.parseInt(v.replaceAll("\\D", "")); } catch (Exception e) { return 0; }
    }
}