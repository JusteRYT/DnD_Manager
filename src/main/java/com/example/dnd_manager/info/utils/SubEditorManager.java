package com.example.dnd_manager.info.utils;

import com.example.dnd_manager.info.editors.common.AbstractEntityEditor;
import com.example.dnd_manager.info.editors.common.EntityEditorButtonFactory;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.CustomTitleBar;
import com.example.dnd_manager.theme.scroll.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class SubEditorManager {

    private static final double RESIZE_MARGIN = 8;

    public static <E> void open(Stage owner,
                                AbstractEntityEditor<E> editor,
                                List<E> targetList,
                                String title,
                                Runnable onApply) {
        openSingleItem(owner, editor, targetList, title, onApply);
    }

    public static <E> void openSingleItem(Stage owner,
                                          AbstractEntityEditor<E> editor,
                                          List<E> targetList,
                                          String title,
                                          Runnable onApply) {
        openInternal(owner, editor, targetList, title, onApply, true);
    }

    public static <E> void openCollection(Stage owner,
                                          AbstractEntityEditor<E> editor,
                                          List<E> targetList,
                                          String title,
                                          Runnable onApply) {
        openInternal(owner, editor, targetList, title, onApply, false);
    }

    private static <E> void openInternal(Stage owner,
                                         AbstractEntityEditor<E> editor,
                                         List<E> targetList,
                                         String title,
                                         Runnable onApply,
                                         boolean singleItemLayout) {
        Stage subStage = new Stage();
        subStage.initModality(Modality.APPLICATION_MODAL);
        if (owner != null) subStage.initOwner(owner);
        subStage.initStyle(StageStyle.UNDECORATED);
        subStage.setResizable(true);
        subStage.setMinWidth(540);
        subStage.setMinHeight(560);
        subStage.setTitle(title);

        if (singleItemLayout) {
            editor.useSingleItemDialogLayout();
        }
        editor.getItems().setAll(targetList);
        editor.refreshUI();

        Button saveBtn = EntityEditorButtonFactory.primary(I18n.t("button.applyToItem"), 220);
        saveBtn.setOnAction(e -> {
            targetList.clear();
            targetList.addAll(editor.getItems());
            if (onApply != null) onApply.run();
            subStage.close();
        });

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(editor);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        javafx.scene.layout.Region footerSpacer = new javafx.scene.layout.Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);
        HBox footer = new HBox(12, footerSpacer, saveBtn);
        footer.setAlignment(Pos.CENTER_RIGHT);

        VBox content = new VBox(15, scrollPane, footer);
        content.setAlignment(Pos.CENTER);
        content.setFillWidth(true);
        content.setPadding(new Insets(15));
        content.setStyle("""
                -fx-background-color:
                    radial-gradient(center 18% 18%, radius 62%, rgba(23, 35, 58, 0.86), transparent 64%),
                    radial-gradient(center 82% 18%, radius 70%, rgba(42, 36, 69, 0.72), transparent 66%),
                    linear-gradient(from 0% 0% to 100% 100%, #070b14, #11172a 52%, #151229);
                """);

        VBox layout = new VBox(new CustomTitleBar(subStage), content);
        layout.setAlignment(Pos.CENTER);
        layout.setFillWidth(true);
        layout.setStyle("""
                -fx-background-color:
                    radial-gradient(center 18% 18%, radius 62%, rgba(23, 35, 58, 0.86), transparent 64%),
                    radial-gradient(center 82% 18%, radius 70%, rgba(42, 36, 69, 0.72), transparent 66%),
                    linear-gradient(from 0% 0% to 100% 100%, #070b14, #11172a 52%, #151229);
                -fx-border-color: rgba(127, 185, 212, 0.46);
                -fx-border-width: 1;
                """);
        VBox.setVgrow(content, Priority.ALWAYS);
        installResizeBehavior(subStage, layout);

        Scene scene = new Scene(layout, singleItemLayout ? 600 : 980, 700);
        scene.setFill(Color.TRANSPARENT);
        subStage.setScene(scene);
        subStage.showAndWait();
    }

    private static void installResizeBehavior(Stage stage, VBox layout) {
        double[] dragStartX = new double[1];
        double[] dragStartY = new double[1];
        double[] dragStartStageX = new double[1];
        double[] dragStartStageY = new double[1];
        double[] dragStartWidth = new double[1];
        double[] dragStartHeight = new double[1];
        ResizeDirection[] direction = new ResizeDirection[1];

        layout.addEventFilter(MouseEvent.MOUSE_MOVED, event ->
                layout.setCursor(resolveResizeDirection(event, layout).cursor()));

        layout.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            if (direction[0] == null || direction[0] == ResizeDirection.NONE) {
                layout.setCursor(Cursor.DEFAULT);
            }
        });

        layout.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            direction[0] = resolveResizeDirection(event, layout);
            if (direction[0] == ResizeDirection.NONE) {
                return;
            }
            dragStartX[0] = event.getScreenX();
            dragStartY[0] = event.getScreenY();
            dragStartStageX[0] = stage.getX();
            dragStartStageY[0] = stage.getY();
            dragStartWidth[0] = stage.getWidth();
            dragStartHeight[0] = stage.getHeight();
            event.consume();
        });

        layout.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (direction[0] == null || direction[0] == ResizeDirection.NONE) {
                return;
            }
            resize(stage, direction[0], dragStartX[0], dragStartY[0], dragStartStageX[0], dragStartStageY[0],
                    dragStartWidth[0], dragStartHeight[0], event.getScreenX(), event.getScreenY());
            event.consume();
        });

        layout.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> direction[0] = ResizeDirection.NONE);
    }

    private static ResizeDirection resolveResizeDirection(MouseEvent event, VBox layout) {
        boolean left = event.getX() <= RESIZE_MARGIN;
        boolean right = event.getX() >= layout.getWidth() - RESIZE_MARGIN;
        boolean top = event.getY() <= RESIZE_MARGIN;
        boolean bottom = event.getY() >= layout.getHeight() - RESIZE_MARGIN;

        if (top && left) return ResizeDirection.NORTH_WEST;
        if (top && right) return ResizeDirection.NORTH_EAST;
        if (bottom && left) return ResizeDirection.SOUTH_WEST;
        if (bottom && right) return ResizeDirection.SOUTH_EAST;
        if (top) return ResizeDirection.NORTH;
        if (right) return ResizeDirection.EAST;
        if (bottom) return ResizeDirection.SOUTH;
        if (left) return ResizeDirection.WEST;
        return ResizeDirection.NONE;
    }

    private static void resize(Stage stage,
                               ResizeDirection direction,
                               double startMouseX,
                               double startMouseY,
                               double startStageX,
                               double startStageY,
                               double startWidth,
                               double startHeight,
                               double mouseX,
                               double mouseY) {
        double deltaX = mouseX - startMouseX;
        double deltaY = mouseY - startMouseY;

        if (direction.resizesEast()) {
            stage.setWidth(Math.max(stage.getMinWidth(), startWidth + deltaX));
        }
        if (direction.resizesSouth()) {
            stage.setHeight(Math.max(stage.getMinHeight(), startHeight + deltaY));
        }
        if (direction.resizesWest()) {
            double targetWidth = Math.max(stage.getMinWidth(), startWidth - deltaX);
            stage.setX(startStageX + startWidth - targetWidth);
            stage.setWidth(targetWidth);
        }
        if (direction.resizesNorth()) {
            double targetHeight = Math.max(stage.getMinHeight(), startHeight - deltaY);
            stage.setY(startStageY + startHeight - targetHeight);
            stage.setHeight(targetHeight);
        }
    }

    private enum ResizeDirection {
        NONE(Cursor.DEFAULT),
        NORTH(Cursor.N_RESIZE),
        EAST(Cursor.E_RESIZE),
        SOUTH(Cursor.S_RESIZE),
        WEST(Cursor.W_RESIZE),
        NORTH_EAST(Cursor.NE_RESIZE),
        NORTH_WEST(Cursor.NW_RESIZE),
        SOUTH_EAST(Cursor.SE_RESIZE),
        SOUTH_WEST(Cursor.SW_RESIZE);

        private final Cursor cursor;

        ResizeDirection(Cursor cursor) {
            this.cursor = cursor;
        }

        Cursor cursor() {
            return cursor;
        }

        boolean resizesNorth() {
            return this == NORTH || this == NORTH_EAST || this == NORTH_WEST;
        }

        boolean resizesEast() {
            return this == EAST || this == NORTH_EAST || this == SOUTH_EAST;
        }

        boolean resizesSouth() {
            return this == SOUTH || this == SOUTH_EAST || this == SOUTH_WEST;
        }

        boolean resizesWest() {
            return this == WEST || this == NORTH_WEST || this == SOUTH_WEST;
        }
    }
}












