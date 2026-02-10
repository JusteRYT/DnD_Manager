package com.example.dnd_manager.screen;

import com.example.dnd_manager.theme.AppScrollPaneFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
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

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(form);

        scrollPane.setPadding(new Insets(5));

        root.setCenter(scrollPane);

        return root;
    }

    protected abstract Node buildTitle();

    protected abstract VBox buildForm();
}
