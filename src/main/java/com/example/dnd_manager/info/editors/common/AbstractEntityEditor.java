package com.example.dnd_manager.info.editors.common;

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
    protected final EntityEditorStyleProvider styleProvider = new EntityEditorStyleProvider();
    private final EntityEditorShellBuilder shellBuilder = new EntityEditorShellBuilder(styleProvider);
    private final EntityEditorItemRenderer<T> itemRenderer;

    public AbstractEntityEditor(Character character, String titleKey) {
        this(character, titleKey, new GlobalAssetService());
    }

    public AbstractEntityEditor(Character character, String titleKey, GlobalAssetService globalAssetService) {
        this.character = character;
        this.globalAssetService = globalAssetService;
        setSpacing(15);
        setPadding(new Insets(10));

        EntityEditorShell shell = shellBuilder.build(titleKey, this::createItemsContainer);
        this.itemsContainer = shell.itemsContainer();
        this.itemRenderer = new EntityEditorItemRenderer<>(itemsContainer, this::createItemRow);

        fillInputCard(shell.inputCard());

        getChildren().addAll(shell.title(), shell.inputCard(), itemsContainer);

        if (character != null) {
            loadFromCharacter(character);
        }

        for (T item : items) {
            itemRenderer.render(item);
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

    protected Pane createItemsContainer() {
        return shellBuilder.defaultItemsContainer();
    }

    // --- Утилиты UI ---

    protected Label createFieldLabel(String text) {
        Label l = new Label(text);
        l.setStyle(styleProvider.fieldLabelStyle());
        return l;
    }

    protected void configureNameValidation(AppTextField nameField) {
        nameRequiredLabel.setStyle(styleProvider.requiredLabelStyle());
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
        itemRenderer.refresh(items);
    }
}












