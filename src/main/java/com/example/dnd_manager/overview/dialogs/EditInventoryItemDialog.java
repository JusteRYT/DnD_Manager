package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.assets.service.GlobalAssetService;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.AbstractEntityEditor;
import com.example.dnd_manager.info.editors.BuffEditor;
import com.example.dnd_manager.info.editors.SkillsEditor;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.utils.SubEditorManager;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppCheckBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class EditInventoryItemDialog extends BaseDialog {

    private static final String DEFAULT_ICON_PATH = "icon/no_image.png";

    private final Character character;
    private final InventoryItem item;
    private final Consumer<InventoryItem> onItemEdited;
    private final InventoryItemIconChooser iconChooser;
    private final InventoryItemMutationService mutationService;
    private String iconPath;

    private Label buffsCountLabel;
    private Label skillsCountLabel;
    private AppCheckBox equippedCheckBox;
    private AppTextField effectDisplayField;

    public EditInventoryItemDialog(Stage owner, Character character, InventoryItem item, Consumer<InventoryItem> onItemEdited) {
        this(owner, character, item, onItemEdited, new GlobalAssetService(), new InventoryItemMutationService());
    }

    public EditInventoryItemDialog(
            Stage owner,
            Character character,
            InventoryItem item,
            Consumer<InventoryItem> onItemEdited,
            GlobalAssetService globalAssetService
    ) {
        this(owner, character, item, onItemEdited, globalAssetService, new InventoryItemMutationService());
    }

    EditInventoryItemDialog(
            Stage owner,
            Character character,
            InventoryItem item,
            Consumer<InventoryItem> onItemEdited,
            GlobalAssetService globalAssetService,
            InventoryItemMutationService mutationService
    ) {
        super(owner, I18n.t("title.editDialog") + item.getName(), 450, 550);
        this.character = character;
        this.item = item;
        this.onItemEdited = onItemEdited;
        this.iconChooser = new InventoryItemIconChooser(Objects.requireNonNull(globalAssetService));
        this.mutationService = Objects.requireNonNull(mutationService, "mutationService must not be null");
        this.iconPath = item.getIconPath();
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(0);

        VBox scrollContent = new VBox(15);
        scrollContent.setPadding(new Insets(0, 15, 10, 0));

        AppTextField nameField = new AppTextField(item.getName(), true);
        nameField.setText(item.getName());
        AppTextSection descriptionField = new AppTextSection(item.getDescription(), 4, I18n.t("dialog.inventory.description.label"));
        IntegerField count = new IntegerField(String.valueOf(item.getCount()), false);

        // --- Секция баффов и скиллов ---
        VBox attachmentsBox = new VBox(10);
        attachmentsBox.setStyle("-fx-padding: 10; -fx-background-color: #252525; -fx-background-radius: 5; -fx-border-color: #3a3a3a; -fx-border-radius: 5;");

        Label itemSectionLabel = new Label(I18n.t("label.editDialog"));
        itemSectionLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px; -fx-font-weight: bold;");

        buffsCountLabel = new Label();
        skillsCountLabel = new Label();
        updateLabels();

        Button editBuffsBtn = AppButtonFactory.addIcon(I18n.t("buffsView.titleBuff"));
        editBuffsBtn.setOnAction(e -> openSubEditor(new BuffEditor(character), item.getAttachedBuffs(), I18n.t("dialog.inventory.buffs.editEditorTitle")));

        Button editSkillsBtn = AppButtonFactory.addIcon(I18n.t("label.familiarsSKILLS"));
        editSkillsBtn.setOnAction(e -> openSubEditor(new SkillsEditor(character), item.getAttachedSkills(), I18n.t("dialog.inventory.skills.editEditorTitle")));

        HBox buffRow = new HBox(15, buffsCountLabel, editBuffsBtn);
        buffRow.setAlignment(Pos.CENTER_LEFT);
        HBox skillRow = new HBox(15, skillsCountLabel, editSkillsBtn);
        skillRow.setAlignment(Pos.CENTER_LEFT);

        attachmentsBox.getChildren().addAll(itemSectionLabel, buffRow, skillRow);

        effectDisplayField = new AppTextField(item.getCustomEffectName() != null ?
                item.getCustomEffectName() : item.getName(), false);
        effectDisplayField.getField().setPromptText(I18n.t("dialog.inventory.effectDisplay.prompt"));

        VBox effectFieldContainer = new VBox(5, new Label(I18n.t("dialog.inventory.effectDisplay.label")), effectDisplayField.getField());
        effectFieldContainer.setVisible(item.isEquipped());

        equippedCheckBox = new AppCheckBox(I18n.t("dialog.inventory.equipThisItem"));
        equippedCheckBox.setSelected(item.isEquipped());
        equippedCheckBox.setOnAction(() -> effectFieldContainer.setVisible(equippedCheckBox.isSelected()));

        scrollContent.getChildren().addAll(
                nameField.getField(),
                descriptionField,
                count.getField(),
                equippedCheckBox,
                effectFieldContainer,
                attachmentsBox
        );

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(scrollContent);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // --- Кнопки сохранения ---
        Button iconBtn = AppButtonFactory.actionSave(I18n.t("editDialog.changeIcon"));
        iconBtn.setOnAction(e -> iconPath = iconChooser.chooseItemIcon(stage, iconPath));

        Button chooseButton = AppButtonFactory.assetPickerButton();
        AppButtonFactory.attachAssetPicker(chooseButton, path -> iconPath = path);

        Button saveBtn = AppButtonFactory.actionSave(I18n.t("button.save"));
        saveBtn.setPrefWidth(120);
        saveBtn.setOnAction(e -> {
            if (nameField.getText().isBlank()) return;

            mutationService.applyToExisting(
                    item,
                    nameField.getText(),
                    descriptionField.getText(),
                    count.getValue(),
                    iconPath,
                    DEFAULT_ICON_PATH,
                    equippedCheckBox.isSelected(),
                    effectDisplayField.getText(),
                    item.getAttachedBuffs(),
                    item.getAttachedSkills()
            );

            onItemEdited.accept(item);
            close();
        });

        HBox buttonBox = new HBox(15, iconBtn, saveBtn, chooseButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        contentArea.getChildren().addAll(scrollPane, buttonBox);
    }

    /**
     * Логика открытия вложенного редактора (как в InventoryEditor)
     */
    private <E> void openSubEditor(AbstractEntityEditor<E> editor, List<E> targetList, String title) {
        SubEditorManager.open(this.stage, editor, targetList, title, this::updateLabels);
    }

    private void updateLabels() {
        buffsCountLabel.setText(I18n.t("buffsView.titleBuff") + ": " + item.getAttachedBuffs().size());
        buffsCountLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11px; -fx-font-style: italic;");
        skillsCountLabel.setText(I18n.t("dialog.inventory.skills.count") + " " + item.getAttachedSkills().size());
        skillsCountLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11px; -fx-font-style: italic;");
    }
}
