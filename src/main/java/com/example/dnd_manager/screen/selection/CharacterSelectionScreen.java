package com.example.dnd_manager.screen.selection;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.common.AppConfirmDialog;
import com.example.dnd_manager.theme.scroll.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CharacterSelectionScreen extends VBox {

    private static final double SCREEN_PADDING = 28;
    private static final double CONTENT_SPACING = 18;

    private final CharacterSelectionStyleProvider styles = new CharacterSelectionStyleProvider();
    private final CharacterDeleteConfirmationMessageFactory deleteConfirmationMessages =
            new CharacterDeleteConfirmationMessageFactory();
    private Label countChip;

    public CharacterSelectionScreen(
            Consumer<Character> onCharacterSelected,
            boolean isEdit,
            CharacterSelectionController controller
    ) {
        CharacterSelectionController finalController = Objects.requireNonNull(controller, "controller must not be null");

        setSpacing(CONTENT_SPACING);
        setPadding(new Insets(SCREEN_PADDING));
        setStyle(styles.rootStyle());
        setAlignment(Pos.TOP_CENTER);

        List<Character> characters = new ArrayList<>(finalController.loadCharacters());
        VBox contentPanel = createContentPanel();

        getChildren().addAll(
                createHeader(isEdit, characters.size()),
                contentPanel,
                createFooter(finalController)
        );
        VBox.setVgrow(contentPanel, Priority.ALWAYS);

        if (characters.isEmpty()) {
            renderEmptyState(contentPanel);
        } else {
            renderCards(contentPanel, characters, onCharacterSelected, isEdit, finalController);
        }
    }

    private HBox createHeader(boolean isEdit, int characterCount) {
        VBox copy = new VBox(6);
        copy.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(isEdit ? I18n.t("title.selectionEditScreen") : I18n.t("title.selectionLoadScreen"));
        title.setStyle(styles.titleStyle());

        Label subtitle = new Label(isEdit ? I18n.t("selection.editSubtitle") : I18n.t("selection.loadSubtitle"));
        subtitle.setStyle(styles.subtitleStyle());
        subtitle.setWrapText(true);

        copy.getChildren().addAll(title, subtitle);

        countChip = new Label(I18n.t("selection.count").formatted(characterCount));
        countChip.setStyle(styles.countChipStyle());

        Region spacer = new Region();
        HBox header = new HBox(18, copy, spacer, countChip);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 20, 18, 20));
        header.setStyle(styles.headerStyle());
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return header;
    }

    private VBox createContentPanel() {
        VBox panel = new VBox();
        panel.setPadding(new Insets(18));
        panel.setStyle(styles.contentPanelStyle());
        return panel;
    }

    private void renderCards(
            VBox contentPanel,
            List<Character> characters,
            Consumer<Character> onCharacterSelected,
            boolean isEdit,
            CharacterSelectionController controller
    ) {
        FlowPane cardsGrid = new FlowPane(16, 16);
        cardsGrid.setAlignment(Pos.TOP_LEFT);
        cardsGrid.setStyle(styles.gridStyle());
        cardsGrid.prefWrapLengthProperty().bind(widthProperty().subtract(96));

        for (Character character : characters) {
            CharacterSelectionCard card = new CharacterSelectionCard(
                    character,
                    onCharacterSelected,
                    isEdit,
                    () -> {
                        if (!confirmDelete(character)) {
                            return;
                        }
                        controller.deleteCharacter(character);
                        characters.remove(character);
                        updateCount(characters.size());
                        cardsGrid.getChildren().removeIf(node -> node.getUserData() == character);
                        if (cardsGrid.getChildren().isEmpty()) {
                            renderEmptyState(contentPanel);
                        }
                    },
                    styles
            );
            card.setUserData(character);
            cardsGrid.getChildren().add(card);
        }

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(cardsGrid);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        contentPanel.getChildren().setAll(scrollPane);
    }

    private void renderEmptyState(VBox contentPanel) {
        Label title = new Label(I18n.t("selection.emptyTitle"));
        title.setStyle(styles.emptyTitleStyle());

        Label hint = new Label(I18n.t("selection.emptyHint"));
        hint.setStyle(styles.emptyHintStyle());
        hint.setWrapText(true);

        VBox emptyBox = new VBox(10, title, hint);
        emptyBox.setAlignment(Pos.CENTER);
        emptyBox.setMaxWidth(Double.MAX_VALUE);
        emptyBox.setStyle(styles.emptyStateStyle());

        Region topSpacer = new Region();
        Region bottomSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);
        contentPanel.getChildren().setAll(topSpacer, emptyBox, bottomSpacer);
    }

    private HBox createFooter(CharacterSelectionController controller) {
        Button backButton = new Button(I18n.t("button.exit"));
        backButton.setMinHeight(38);
        backButton.setPrefWidth(180);
        backButton.setOnAction(e -> controller.goBack());
        styles.applySecondaryAction(backButton);

        HBox footer = new HBox(backButton);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(12, 14, 12, 14));
        footer.setStyle(styles.footerStyle());
        return footer;
    }

    private void updateCount(int characterCount) {
        if (countChip != null) {
            countChip.setText(I18n.t("selection.count").formatted(characterCount));
        }
    }

    private boolean confirmDelete(Character character) {
        Stage owner = getScene() != null && getScene().getWindow() instanceof Stage stage ? stage : null;
        AppConfirmDialog confirmDialog = new AppConfirmDialog(
                owner,
                deleteConfirmationMessages.title(),
                deleteConfirmationMessages.message(character),
                true
        );
        confirmDialog.show();
        return confirmDialog.isConfirmed();
    }
}













