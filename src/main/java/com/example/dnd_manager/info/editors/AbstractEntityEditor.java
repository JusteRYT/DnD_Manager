package com.example.dnd_manager.info.editors;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.Getter;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractEntityEditor<T> extends VBox {

    @Getter
    protected final ObservableList<T> items = FXCollections.observableArrayList();
    protected final Pane itemsContainer;
    protected final Character character;
    protected final Label nameRequiredLabel = new Label(I18n.t("labelField.nameRequired"));

    public AbstractEntityEditor(Character character, String titleKey) {
        this.character = character;
        setSpacing(15);
        setPadding(new Insets(10));
        this.itemsContainer = createItemsContainer();

        // 1. Заголовок
        Label title = new Label(I18n.t(titleKey).toUpperCase());
        title.setStyle("-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 13px; -fx-letter-spacing: 1.5px;");

        // 2. Контейнер ввода (Input Card)
        VBox inputCard = new VBox(12);
        inputCard.setStyle("""
                    -fx-background-color: linear-gradient(to right, #252526, #1e1e1e);
                    -fx-padding: 15;
                    -fx-background-radius: 8;
                    -fx-border-color: #3a3a3a;
                    -fx-border-radius: 8;
                """);

        fillInputCard(inputCard);

        getChildren().addAll(title, inputCard, itemsContainer);

        if (character != null) {
            loadFromCharacter(character);
        }

        for (T item : items) {
            renderItemRow(item);
        }
    }

    /**
     * Метод, который должны реализовать наследники для наполнения формы ввода
     */
    protected abstract void fillInputCard(VBox card);

    /**
     * Метод для создания UI строки элемента списка
     */
    protected abstract Node createItemRow(T item);

    /**
     * Загрузка начальных данных из персонажа
     */
    protected abstract void loadFromCharacter(Character character);

    /**
     * Применение изменений к персонажу
     */
    public abstract void applyTo(Character character);

    // --- Общая логика управления списком ---

    protected void addItem(T item) {
        items.add(item);
        renderItemRow(item);
    }

    private void renderItemRow(T item) {
        itemsContainer.getChildren().add(createItemRow(item));
    }

    protected void removeItem(T item) {
        items.remove(item);
    }

    protected Runnable getOnRemoveAction(T item, Node rowNode) {
        return () -> {
            itemsContainer.getChildren().remove(rowNode);
            items.remove(item);
        };
    }

    protected Pane createItemsContainer() {
        VBox vBox = new VBox(8);
        vBox.setPadding(new Insets(10, 0, 0, 0));
        return vBox;
    }

    // --- Утилиты UI ---

    protected Label createFieldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #666; -fx-font-size: 10px; -fx-font-weight: bold;");
        return l;
    }

    protected void configureNameValidation(AppTextField nameField) {
        nameRequiredLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 10px; -fx-font-weight: bold;");
        nameRequiredLabel.setVisible(false);
        nameRequiredLabel.setManaged(false);
        nameRequiredLabel.setPadding(new Insets(0, 0, 0, 5));

        nameField.getField().textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) {
                nameRequiredLabel.setVisible(false);
                nameRequiredLabel.setManaged(false);
            }
        });
    }

    protected boolean validateName(AppTextField field) {
        boolean valid = !field.getText().isBlank();
        nameRequiredLabel.setVisible(!valid);
        nameRequiredLabel.setManaged(!valid);
        return valid;
    }

    protected String chooseIcon() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(getScene().getWindow());
        return file != null ? file.getAbsolutePath() : null;
    }

    protected String resolveIconPath(AtomicReference<String> iconPath) {
        String icon = iconPath.get();
        if (icon == null || icon.isEmpty()) {
            return getClass().getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm();
        }
        return icon;
    }

    protected void refreshUI() {
        itemsContainer.getChildren().clear();
        for (T item : items) {
            renderItemRow(item);
        }
    }
}
