package com.example.dnd_manager.info.editors;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.assets.service.GlobalAssetService;
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
    protected final GlobalAssetService globalAssetService;
    protected final Label nameRequiredLabel = new Label(I18n.t("labelField.nameRequired"));

    public AbstractEntityEditor(Character character, String titleKey) {
        this(character, titleKey, new GlobalAssetService());
    }

    public AbstractEntityEditor(Character character, String titleKey, GlobalAssetService globalAssetService) {
        this.character = character;
        this.globalAssetService = globalAssetService;
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

    /**
     * Opens a FileChooser, allows the user to select an image, and imports it
     * into the global Assets directory under the specified category.
     *
     * @param category The category determining the target subfolder in Assets.
     * @return String path relative to the project root (e.g., "Assets/Items/sword.png"), or null if cancelled.
     */
    protected String chooseAndImportIcon(AssetCategory category) {
        FileChooser chooser = new FileChooser();
        // Поддержка webp добавлена, так как она часто используется для иконок
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp"));
        File file = chooser.showOpenDialog(getScene().getWindow());

        if (file != null) {
            // Делегируем копирование сервису
            return globalAssetService.importAsset(file, category);
        }
        return null;
    }

    protected String resolveIconPath(AtomicReference<String> iconPath) {
        String icon = iconPath.get();
        return (icon == null || icon.isEmpty()) ? "" : icon;
    }

    public void refreshUI() {
        itemsContainer.getChildren().clear();
        for (T item : items) {
            renderItemRow(item);
        }
    }
}
