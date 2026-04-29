package com.example.dnd_manager.overview.dialogs.common;

import com.example.dnd_manager.theme.CustomTitleBar;
import com.example.dnd_manager.theme.dialog.AppDialogStyleProvider;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    protected final AppDialogStyleProvider dialogStyles;

    public BaseDialog(Stage owner, String title, double width, double height) {
        this.dialogStyles = new AppDialogStyleProvider();
        this.stage = new Stage();
        if (owner != null) {
            this.stage.initOwner(owner);
        }
        this.stage.initModality(Modality.APPLICATION_MODAL);
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.setTitle(title);

        // Главный контейнер с рамкой, как в MainApp
        this.root = new VBox();
        this.root.setStyle(dialogStyles.rootStyle());
        this.root.setPadding(new Insets(0, 1, 1, 1));

        // Добавляем твой кастомный TitleBar
        CustomTitleBar titleBar = new CustomTitleBar(stage);

        // Область для контента конкретного диалога
        this.contentArea = new VBox();
        this.contentArea.setPadding(new Insets(20));
        this.contentArea.setStyle(dialogStyles.contentAreaStyle());
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        this.root.getChildren().addAll(titleBar, contentArea);

        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
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











