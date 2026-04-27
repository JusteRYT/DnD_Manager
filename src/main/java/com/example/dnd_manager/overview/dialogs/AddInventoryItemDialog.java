package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.assets.service.GlobalAssetService;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.function.Consumer;

public class AddInventoryItemDialog extends BaseDialog {

    private static final String DEFAULT_ICON_PATH = "icon/no_image.png";

    private final Character character;
    private final Consumer<InventoryItem> onItemAddedOrEdited;
    private final InventoryItemIconChooser iconChooser;
    private final InventoryItemDialogPresenter presenter;
    private final InventoryItemFormState formState;
    private final InventoryItemFormBuilder formBuilder;
    private final InventoryItemDialogActionFactory actionFactory;

    public AddInventoryItemDialog(Stage owner, Character character, InventoryItem itemToEdit, Consumer<InventoryItem> onComplete) {
        this(
                owner,
                character,
                itemToEdit,
                onComplete,
                new GlobalAssetService(),
                new InventoryItemMutationService(),
                new InventoryItemCountResolver(),
                new InventoryItemFormValidator(),
                new InventoryItemFormBuilder(),
                new InventoryItemDialogActionFactory()
        );
    }

    public AddInventoryItemDialog(
            Stage owner,
            Character character,
            InventoryItem itemToEdit,
            Consumer<InventoryItem> onComplete,
            GlobalAssetService globalAssetService
    ) {
        this(
                owner,
                character,
                itemToEdit,
                onComplete,
                globalAssetService,
                new InventoryItemMutationService(),
                new InventoryItemCountResolver(),
                new InventoryItemFormValidator(),
                new InventoryItemFormBuilder(),
                new InventoryItemDialogActionFactory()
        );
    }

    AddInventoryItemDialog(
            Stage owner,
            Character character,
            InventoryItem itemToEdit,
            Consumer<InventoryItem> onComplete,
            GlobalAssetService globalAssetService,
            InventoryItemMutationService mutationService,
            InventoryItemCountResolver countResolver,
            InventoryItemFormValidator validator,
            InventoryItemFormBuilder formBuilder,
            InventoryItemDialogActionFactory actionFactory
    ) {
        super(owner,
                itemToEdit == null ? I18n.t("dialog.inventory.add.title") : I18n.t("dialog.inventory.edit.title"),
                450, 550);

        this.character = character;
        this.onItemAddedOrEdited = onComplete;
        this.iconChooser = new InventoryItemIconChooser(Objects.requireNonNull(globalAssetService));
        this.formState = new InventoryItemFormState(itemToEdit);
        this.presenter = new InventoryItemDialogPresenter(
                Objects.requireNonNull(validator, "validator must not be null"),
                Objects.requireNonNull(countResolver, "countResolver must not be null"),
                new InventoryItemDialogSubmitService(
                        Objects.requireNonNull(mutationService, "mutationService must not be null"),
                        DEFAULT_ICON_PATH
                )
        );
        this.formBuilder = Objects.requireNonNull(formBuilder, "formBuilder must not be null");
        this.actionFactory = Objects.requireNonNull(actionFactory, "actionFactory must not be null");
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(0);
        VBox scrollContent = new VBox(15);
        InventoryItemFormView formView = formBuilder.build(stage, character, formState, scrollContent);

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(scrollContent);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        contentArea.getChildren().addAll(
                scrollPane,
                actionFactory.build(
                        stage,
                        character,
                        presenter,
                        formState,
                        formView,
                        iconChooser,
                        onItemAddedOrEdited,
                        this::close
                )
        );
    }
}
