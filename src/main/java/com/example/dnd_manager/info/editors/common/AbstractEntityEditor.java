package com.example.dnd_manager.info.editors.common;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.assets.service.GlobalAssetService;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.infrastructure.assets.CharacterAssetResolver;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.scroll.AppScrollPaneFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.Getter;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractEntityEditor<T> extends VBox {

    @Getter
    protected final ObservableList<T> items = FXCollections.observableArrayList();
    protected Pane itemsContainer;
    protected final Character character;
    protected final GlobalAssetService globalAssetService;
    protected final Label nameRequiredLabel = new Label(I18n.t("labelField.nameRequired"));
    protected final EntityEditorStyleProvider styleProvider = new EntityEditorStyleProvider();
    private final EntityEditorShellBuilder shellBuilder = new EntityEditorShellBuilder(styleProvider);
    private EntityEditorItemRenderer<T> itemRenderer;
    private VBox inputCard;
    private VBox listPanel;
    private HBox editorBody;

    public AbstractEntityEditor(Character character, String titleKey) {
        this(character, titleKey, new GlobalAssetService());
    }

    public AbstractEntityEditor(Character character, String titleKey, GlobalAssetService globalAssetService) {
        this.character = character;
        this.globalAssetService = globalAssetService;
        setSpacing(15);
        setPadding(Insets.EMPTY);
    }

    protected final void initializeEditor(String titleKey) {
        EntityEditorShell shell = shellBuilder.build(titleKey, this::createItemsContainer);
        this.itemsContainer = shell.itemsContainer();
        this.itemRenderer = new EntityEditorItemRenderer<>(itemsContainer, this::createItemRow, this::createEmptyState);

        this.inputCard = shell.inputCard();
        fillInputCard(inputCard);

        listPanel = new VBox(10);
        listPanel.setPadding(new Insets(15));
        listPanel.setStyle(styleProvider.listPanelStyle());
        Label listTitle = new Label(I18n.t("characterForm.createdItems"));
        listTitle.setStyle(styleProvider.listTitleStyle());
        ScrollPane itemsScrollPane = AppScrollPaneFactory.defaultPane(itemsContainer);
        itemsScrollPane.setPrefViewportHeight(430);
        itemsScrollPane.setMinViewportHeight(260);

        VBox.setVgrow(itemsScrollPane, Priority.ALWAYS);
        listPanel.getChildren().addAll(listTitle, itemsScrollPane);

        inputCard.setMinWidth(440);
        inputCard.setPrefWidth(520);
        inputCard.setMaxWidth(560);
        listPanel.setMinWidth(420);
        editorBody = new HBox(16, inputCard, listPanel);
        editorBody.setAlignment(Pos.TOP_LEFT);
        editorBody.setPadding(new Insets(10, 0, 0, 0));
        editorBody.setStyle(styleProvider.editorBodyStyle());
        HBox.setHgrow(inputCard, Priority.NEVER);
        HBox.setHgrow(listPanel, Priority.ALWAYS);

        getChildren().addAll(shell.title(), editorBody);

        if (character != null) {
            loadFromCharacter(character);
        }

        itemRenderer.refresh(items);
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

    protected Node createEmptyState() {
        Label label = new Label(I18n.t("characterForm.emptyItems"));
        label.setStyle(styleProvider.emptyStateTextStyle());
        label.setWrapText(true);

        VBox box = new VBox(label);
        box.setAlignment(Pos.CENTER);
        box.setMinHeight(96);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setStyle(styleProvider.emptyStateStyle());
        return box;
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

    protected void showIconPreview(Label target, String path) {
        boolean hasIcon = path != null && !path.isBlank();
        ImageView preview = new ImageView(CharacterAssetResolver.getImage(character, path, 46, 46));
        preview.setFitWidth(46);
        preview.setFitHeight(46);
        preview.setPreserveRatio(true);
        preview.setSmooth(true);
        preview.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.36), 8, 0.22, 0, 1);");
        target.setGraphic(preview);
        target.setText(hasIcon ? I18n.t("editor.icon.selected") : I18n.t("inventoryEditor.icon.noneSelected"));
    }

    public void refreshUI() {
        itemRenderer.refresh(items);
    }

    public void useSingleItemDialogLayout() {
        if (listPanel != null) {
            listPanel.setVisible(false);
            listPanel.setManaged(false);
        }
        if (inputCard != null) {
            inputCard.setMinWidth(440);
            inputCard.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(inputCard, Priority.ALWAYS);
        }
        if (editorBody != null) {
            editorBody.setMaxWidth(Double.MAX_VALUE);
            editorBody.setMaxHeight(Double.MAX_VALUE);
        }
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(editorBody, Priority.ALWAYS);
    }
}












