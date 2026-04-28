package com.example.dnd_manager.info.editors.common;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Function;

public class EditorFormLayoutBuilder {

    private final Function<String, Label> labelFactory;

    public EditorFormLayoutBuilder(Function<String, Label> labelFactory) {
        this.labelFactory = labelFactory;
    }

    public VBox field(String labelText, Node content) {
        return new VBox(5, label(labelText), content);
    }

    public VBox compactField(String labelText, Node content) {
        return new VBox(2, label(labelText), content);
    }

    public Label label(String labelText) {
        return labelFactory.apply(labelText);
    }

    public VBox validatedNameField(Node field, Label validationLabel) {
        VBox nameBox = new VBox(2, field, validationLabel);
        nameBox.setMinHeight(45);
        nameBox.setAlignment(Pos.TOP_LEFT);
        return nameBox;
    }

    public HBox row(double spacing, Node... nodes) {
        return new HBox(spacing, nodes);
    }

    public HBox alignedRow(double spacing, Pos alignment, Node... nodes) {
        HBox row = row(spacing, nodes);
        row.setAlignment(alignment);
        return row;
    }

    public Label iconPathLabel() {
        Label label = new Label();
        label.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 11px;");
        return label;
    }
}












