package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.editors.AbstractEntityEditor;
import com.example.dnd_manager.info.editors.BuffEditor;
import com.example.dnd_manager.info.editors.SkillsEditor;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.utils.SubEditorManager;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class InventoryItemAttachmentsSection {

    private final Stage stage;
    private final Character character;
    private final List<Buff> attachedBuffs;
    private final List<Skill> attachedSkills;
    private final InventoryDialogStyleProvider styleProvider;
    private final Label buffsCountLabel = new Label();
    private final Label skillsCountLabel = new Label();

    public InventoryItemAttachmentsSection(
            Stage stage,
            Character character,
            List<Buff> attachedBuffs,
            List<Skill> attachedSkills
    ) {
        this(stage, character, attachedBuffs, attachedSkills, new InventoryDialogStyleProvider());
    }

    InventoryItemAttachmentsSection(
            Stage stage,
            Character character,
            List<Buff> attachedBuffs,
            List<Skill> attachedSkills,
            InventoryDialogStyleProvider styleProvider
    ) {
        this.stage = Objects.requireNonNull(stage, "stage must not be null");
        this.character = Objects.requireNonNull(character, "character must not be null");
        this.attachedBuffs = Objects.requireNonNull(attachedBuffs, "attachedBuffs must not be null");
        this.attachedSkills = Objects.requireNonNull(attachedSkills, "attachedSkills must not be null");
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
    }

    public VBox buildNode() {
        VBox attachmentsBox = new VBox(10);
        attachmentsBox.setStyle(styleProvider.attachmentsContainerStyle());

        Label sectionLabel = new Label(I18n.t("dialog.inventory.attachments.title"));
        sectionLabel.setStyle(styleProvider.attachmentsTitleStyle());

        updateLabels();

        Button editBuffsBtn = AppButtonFactory.addIcon(I18n.t("dialog.inventory.buffs.short"));
        editBuffsBtn.setOnAction(e -> openSubEditor(new BuffEditor(character), attachedBuffs, I18n.t("dialog.inventory.buffs.editorTitle")));

        Button editSkillsBtn = AppButtonFactory.addIcon(I18n.t("dialog.inventory.skills.short"));
        editSkillsBtn.setOnAction(e -> openSubEditor(new SkillsEditor(character), attachedSkills, I18n.t("dialog.inventory.skills.editorTitle")));

        HBox buffRow = new HBox(15, buffsCountLabel, editBuffsBtn);
        buffRow.setAlignment(Pos.CENTER_LEFT);
        HBox skillRow = new HBox(15, skillsCountLabel, editSkillsBtn);
        skillRow.setAlignment(Pos.CENTER_LEFT);

        attachmentsBox.getChildren().addAll(sectionLabel, buffRow, skillRow);
        return attachmentsBox;
    }

    private void updateLabels() {
        buffsCountLabel.setText(I18n.t("textLabel.buffsItemInventory") + " " + attachedBuffs.size());
        skillsCountLabel.setText(I18n.t("dialog.inventory.skills.count") + " " + attachedSkills.size());
    }

    private <E> void openSubEditor(AbstractEntityEditor<E> editor, List<E> targetList, String title) {
        SubEditorManager.open(stage, editor, targetList, title, this::updateLabels);
    }
}
