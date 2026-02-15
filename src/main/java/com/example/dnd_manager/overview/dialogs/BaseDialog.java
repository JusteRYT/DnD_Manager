package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.theme.CustomTitleBar;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Базовый класс для всех диалогов, копирующий стиль MainApp.
 */
public abstract class BaseDialog {

    protected final Stage stage;
    protected final VBox root;
    protected final VBox contentArea;

    public BaseDialog(Stage owner, String title, double width, double height) {
        this.stage = new Stage();
        this.stage.initOwner(owner);
        this.stage.initModality(Modality.APPLICATION_MODAL);
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.setTitle(title);

        // Главный контейнер с рамкой, как в MainApp
        this.root = new VBox();
        this.root.setStyle("-fx-border-color: #3a3a3a; -fx-border-width: 1; -fx-background-color: #1e1e1e;");
        this.root.setPadding(new Insets(0, 1, 1, 1));

        // Добавляем твой кастомный TitleBar
        CustomTitleBar titleBar = new CustomTitleBar(stage);

        // Область для контента конкретного диалога
        this.contentArea = new VBox();
        this.contentArea.setPadding(new Insets(20));
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        this.root.getChildren().addAll(titleBar, contentArea);

        Scene scene = new Scene(root, width, height);
        scene.setFill(null);
        this.stage.setScene(scene);
    }

    /**
     * Метод для наполнения контентом (реализуется в наследниках)
     */
    protected abstract void setupContent();

    public void show() {
        setupContent();
        stage.showAndWait();
    }

    protected void close() {
        stage.close();
    }
}