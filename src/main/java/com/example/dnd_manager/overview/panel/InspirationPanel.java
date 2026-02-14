package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.ui.ManaBar;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lombok.Getter;

import java.util.Objects;

/**
 * Panel representing Inspiration (Purple Glow) and holding the ManaBar (Blue Glow).
 */
public class InspirationPanel extends VBox {

    private final Character character;
    private final StorageService storageService;

    private final Label inspirationLabel = new Label();
    @Getter
    private final ManaBar manaBar;

    public InspirationPanel(Character character, StorageService storageService) {
        this.character = character;
        this.storageService = storageService;

        setSpacing(15);
        setPadding(new Insets(0));

        // --- Блок Вдохновения ---
        VBox inspirationBox = createInspirationBox();

        // --- Блок Маны (уже имеет свой стиль внутри класса ManaBar) ---
        manaBar = new ManaBar(character, storageService);

        getChildren().addAll(inspirationBox, manaBar);

        refresh();
    }

    private VBox createInspirationBox() {
        Label title = new Label(I18n.t("inspirationPanel.title"));
        title.setStyle("""
                -fx-text-fill: #dba1ff;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.8), 2, 0, 0, 1);
                """);

        HBox contentRow = new HBox(12);
        contentRow.setAlignment(Pos.CENTER_LEFT);

        ImageView icon = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/com/example/dnd_manager/icon/inspiration_icon.png")
                ))
        );
        icon.setFitWidth(28);
        icon.setFitHeight(28);
        icon.setPreserveRatio(true);
        // Добавим иконке легкое свечение
        icon.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(155, 89, 182, 0.6), 5, 0, 0, 0);");

        inspirationLabel.setStyle("""
                        -fx-text-fill: #ffffff;
                        -fx-font-weight: bold;
                        -fx-font-family: "Consolas";
                        -fx-font-size: 18px;
                """);

        StackPane valueBox = new StackPane(inspirationLabel);
        valueBox.setMinWidth(50);
        valueBox.setPrefWidth(50);
        valueBox.setMaxWidth(50);
        valueBox.setMinHeight(30);
        valueBox.setAlignment(Pos.CENTER);
        valueBox.setStyle("""
                        -fx-background-color: #1a1a1a;
                        -fx-background-radius: 6;
                        -fx-border-color: #5e3b6f;
                        -fx-border-radius: 6;
                """);

        var addBtn = AppButtonFactory.customButton("+", 28);
        addBtn.setOnAction(e -> changeInspiration(1));

        var removeBtn = AppButtonFactory.customButton("–", 28,
                AppTheme.BUTTON_REMOVE, AppTheme.BUTTON_REMOVE_HOVER);
        removeBtn.setOnAction(e -> changeInspiration(-1));

        contentRow.getChildren().addAll(icon, valueBox, addBtn, removeBtn);
        HBox.setHgrow(valueBox, Priority.ALWAYS);

        return getVBox(title, contentRow);
    }

    private static VBox getVBox(Label title, HBox contentRow) {
        VBox container = new VBox(8, title, contentRow);
        container.setPadding(new Insets(12));

        // --- ФИОЛЕТОВЫЙ СТИЛЬ (Mystic) ---
        container.setStyle("""
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
                -fx-background-radius: 8;
                -fx-border-color: #9b59b6; /* Фиолетовая рамка */
                -fx-border-radius: 8;
                -fx-border-width: 1;
                /* Фиолетовое свечение */
                -fx-effect: dropshadow(three-pass-box, rgba(155, 89, 182, 0.25), 15, 0, 0, 0);
                """);
        return container;
    }

    private void changeInspiration(int delta) {
        character.setInspiration(Math.max(0, character.getInspiration() + delta));
        refresh();
        storageService.saveCharacter(character);
    }

    public void refresh() {
        inspirationLabel.setText(String.valueOf(character.getInspiration()));
        manaBar.refresh();
    }
}