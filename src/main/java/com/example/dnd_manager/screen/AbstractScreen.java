package com.example.dnd_manager.screen;

import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public abstract class AbstractScreen {

    public Parent getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + ";");

        root.setTop(buildTitle());

        VBox form = buildForm();
        form.setPadding(new Insets(10, 10, 10, 10));
        form.setSpacing(15);
        root.setCenter(form);

        return root;
    }

    protected abstract Node buildTitle();

    protected abstract VBox buildForm();
}
