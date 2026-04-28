package com.example.dnd_manager.info.editors.familiar;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.buff.BuffEditor;
import com.example.dnd_manager.info.editors.common.EditorFormLayoutBuilder;
import com.example.dnd_manager.info.editors.common.IconPathDisplayFormatter;
import com.example.dnd_manager.info.editors.inventory.InventoryEditor;
import com.example.dnd_manager.info.editors.skills.SkillsEditor;
import com.example.dnd_manager.info.stats.editor.StatsEditor;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.screen.FormMode;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import com.example.dnd_manager.theme.scroll.AppScrollPaneFactory;
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
    private StatsEditor statsEditor;
    private BuffEditor buffEditor;
    private InventoryEditor inventoryEditor;
    private SkillsEditor skillsEditor;
    private final FamiliarCoreDataApplier coreDataApplier = new FamiliarCoreDataApplier();
    private final IconPathDisplayFormatter iconPathDisplayFormatter = new IconPathDisplayFormatter();
    private final FamiliarEditorStyleProvider styleProvider = new FamiliarEditorStyleProvider();
    private final EditorFormLayoutBuilder layoutBuilder = new EditorFormLayoutBuilder(this::createLabel);

    public FamiliarEditor(Character familiar) {
        this.familiar = familiar;
        setSpacing(15);
        setPadding(new Insets(10));

        setupUI();
        loadData();
    }

    private void setupUI() {
        VBox baseCard = new VBox(12);
        baseCard.setStyle(styleProvider.baseCardStyle());

        nameField = new AppTextField(I18n.t("familiar.prompt.name"), true);

        HBox typeRow = layoutBuilder.row(10,
                layoutBuilder.compactField(I18n.t("raceField.name"), (raceField = new AppTextField("", true)).getField()),
                layoutBuilder.compactField(I18n.t("classField.name"), (classField = new AppTextField("", true)).getField())
        );

        HBox statsRow = layoutBuilder.row(10,
                layoutBuilder.compactField(I18n.t("label.familiarsHP"), (hpField = new IntegerField("", true)).getField()),
                layoutBuilder.compactField(I18n.t("label.familiarsAC"), (armorField = new IntegerField("", true)).getField()),
                layoutBuilder.compactField(I18n.t("label.familiarsMP"), (manaField = new IntegerField("", true)).getField())
        );
        hpField.getField().setPrefWidth(80);
        armorField.getField().setPrefWidth(80);
        manaField.getField().setPrefWidth(80);

        iconPathLabel = layoutBuilder.iconPathLabel();
        Button btnIcon = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        btnIcon.setOnAction(e -> chooseAvatar());

        Button btnAssetsIcon = AppButtonFactory.assetPickerButton();
        AppButtonFactory.attachAssetPicker(btnAssetsIcon, path -> {
            avatarPath.set(path);
            iconPathLabel.setText(iconPathDisplayFormatter.fileNameOrEmpty(path));
        });

        HBox buttonBox = layoutBuilder.row(10, btnIcon, btnAssetsIcon);
        baseCard.getChildren().addAll(
                layoutBuilder.compactField(I18n.t("textFieldLabel.name"), nameField.getField()),
                typeRow, statsRow, iconPathLabel, buttonBox
        );

        // 2. Добавляем вложенные редакторы
        statsEditor = new StatsEditor(familiar.getStats(), FormMode.EDIT);
        buffEditor = new BuffEditor(familiar);
        inventoryEditor = new InventoryEditor(familiar);
        skillsEditor = new SkillsEditor(familiar);

        VBox content = new VBox(20, baseCard, statsEditor, skillsEditor, inventoryEditor, buffEditor);
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
            iconPathLabel.setText(iconPathDisplayFormatter.fileNameOrEmpty(familiar.getAvatarImage()));
        }
    }

    public void applyChanges() {
        coreDataApplier.apply(familiar, new FamiliarCoreData(
                nameField.getText(),
                raceField.getText(),
                classField.getText(),
                hpField.getText(),
                armorField.getText(),
                manaField.getText(),
                avatarPath.get()
        ));

        statsEditor.applyTo(familiar);
        buffEditor.applyTo(familiar);
        inventoryEditor.applyTo(familiar);
        skillsEditor.applyTo(familiar);
    }

    private void chooseAvatar() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            avatarPath.set(file.getAbsolutePath());
            iconPathLabel.setText(iconPathDisplayFormatter.fileNameOrEmpty(file.getAbsolutePath()));
        }
    }

    private Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle(styleProvider.fieldLabelStyle());
        return l;
    }

}












