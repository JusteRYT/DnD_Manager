package com.example.dnd_manager.info.section;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.familiar.FamiliarEditorRow;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.familiar.FamiliarDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

public class FamiliarsSection extends VBox {

    @Getter
    private final ObservableList<Character> items = FXCollections.observableArrayList();
    private final VBox listContainer;
    private final Stage parentStage;
    private final Character dummyOwner;
    private final Label emptyLabel = new Label(I18n.t("label.noFamiliars"));

    public FamiliarsSection(Stage parentStage, Character dummyOwner) {
        this.parentStage = parentStage;
        this.dummyOwner = dummyOwner;
        setSpacing(14);
        setPadding(new Insets(4));

        Label title = new Label(I18n.t("label.familiarsEditor").toUpperCase());
        title.setStyle("-fx-text-fill: #f0f2f7; -fx-font-weight: bold; -fx-font-size: 14px; -fx-letter-spacing: 1.2px; -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.22), 10, 0.28, 0, 0);");

        Label hint = new Label(I18n.t("characterForm.familiarsHint"));
        hint.setWrapText(true);
        hint.setStyle("-fx-text-fill: #b6bed0; -fx-font-size: 12px;");

        Button addButton = new Button(I18n.t("button.addFamiliar"));
        addButton.setPrefSize(190, 38);
        addButton.setStyle(buttonStyle(false));
        addButton.setOnMouseEntered(e -> addButton.setStyle(buttonStyle(true)));
        addButton.setOnMouseExited(e -> addButton.setStyle(buttonStyle(false)));
        addButton.setOnAction(e -> openDialog(null));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(14, new VBox(6, title, hint), spacer, addButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14));
        header.setStyle("""
                -fx-background-color: rgba(11, 19, 35, 0.52);
                -fx-background-radius: 14;
                -fx-border-color: rgba(72, 85, 117, 0.34);
                -fx-border-radius: 14;
                -fx-border-width: 1;
                """);

        listContainer = new VBox(8);
        listContainer.setPadding(new Insets(4, 0, 0, 0));

        emptyLabel.setStyle("-fx-text-fill: #aab8cf; -fx-font-size: 12px; -fx-font-style: italic;");

        getChildren().addAll(header, listContainer);
        refreshList();
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

    public void refreshList() {
        listContainer.getChildren().clear();
        if (items.isEmpty()) {
            listContainer.getChildren().add(emptyLabel);
            return;
        }
        for (Character familiar : items) {
            FamiliarEditorRow row = new FamiliarEditorRow(
                    familiar,
                    () -> { items.remove(familiar); refreshList(); },
                    () -> openDialog(familiar),
                    dummyOwner
            );
            row.setFocusTraversable(false);
            listContainer.getChildren().add(row);
        }
    }

    private String buttonStyle(boolean hover) {
        String background = hover
                ? "linear-gradient(to bottom, #eef3f6, #c7d5df)"
                : "linear-gradient(to bottom, #dfe6ec, #b7c7d3)";
        String border = hover ? "#d8e4eb" : "#b3c4d3";
        String glow = hover ? "rgba(179, 196, 211, 0.48)" : "rgba(179, 196, 211, 0.28)";
        return """
                -fx-background-color: %s;
                -fx-background-radius: 6;
                -fx-border-color: %s;
                -fx-border-radius: 6;
                -fx-text-fill: #0c1018;
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, %s, 14, 0.25, 0, 1);
                """.formatted(background, border, glow);
    }
}











