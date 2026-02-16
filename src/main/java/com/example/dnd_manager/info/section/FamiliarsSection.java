package com.example.dnd_manager.info.section;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.familiar.FamiliarEditorRow;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.FamiliarDialog;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

public class FamiliarsSection extends VBox {

    @Getter
    private final ObservableList<Character> items = FXCollections.observableArrayList();
    private final VBox listContainer;
    private final Stage parentStage;
    private final Character dummyOwner = new Character();

    public FamiliarsSection(Stage parentStage) {
        this.parentStage = parentStage;
        setSpacing(10);
        setPadding(new Insets(10));

        // 1. Заголовок
        Label title = new Label(I18n.t("label.familiarsEditor").toUpperCase());
        title.setStyle("-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 13px; -fx-letter-spacing: 1.5px;");

        // 2. Контейнер для списка
        listContainer = new VBox(8);

        // 3. Кнопка добавления
        Button addButton = AppButtonFactory.primaryButton(I18n.t("button.addFamiliar"), 100, 400, 14);
        addButton.setOnAction(e -> openDialog(null));

        getChildren().addAll(title, listContainer, addButton);
    }

    private void openDialog(Character existingFamiliar) {
        final boolean isNew = (existingFamiliar == null);
        Character familiarToEdit = isNew ? new Character() : existingFamiliar;

        new FamiliarDialog(parentStage, familiarToEdit, () -> {
            if (isNew) {
                items.add(familiarToEdit);
            }
            refreshList();
        }).show();
    }

    private void refreshList() {
        listContainer.getChildren().clear();
        for (Character familiar : items) {
            FamiliarEditorRow row = new FamiliarEditorRow(
                    familiar,
                    () -> { items.remove(familiar); refreshList(); },
                    () -> openDialog(familiar),
                    dummyOwner
            );
            listContainer.getChildren().add(row);
        }
    }
}