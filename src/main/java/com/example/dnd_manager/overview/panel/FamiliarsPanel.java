package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.FamiliarInfoDialog;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.store.StorageService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Objects;

public class FamiliarsPanel extends VBox {
    private final Character character;

    private final Stage parentStage;
    private final StorageService storageService;

    public FamiliarsPanel(Character character, Stage parentStage, StorageService storageService) {
        this.parentStage = parentStage;
        this.character = character;
        this.storageService = storageService;
        // Мистический фиолетовый цвет для фамильяров
        String accentColor = "#9c27b0";

        // Заголовок
        Label title = new Label(I18n.t("label.familiars"));
        title.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 16px; -fx-font-weight: bold; -fx-letter-spacing: 1.5;", accentColor));
        title.setPadding(new Insets(0, 0, 10, 0));

        // Контейнер для списка
        VBox listContainer = new VBox(8);

        if (character.getFamiliars().isEmpty()) {
            Label emptyLabel = new Label(I18n.t("label.noFamiliars"));
            emptyLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");
            listContainer.getChildren().add(emptyLabel);
        } else {
            for (Character familiar : character.getFamiliars()) {
                listContainer.getChildren().add(createFamiliarCard(familiar));
            }
        }

        ScrollPane scrollPane = new ScrollPane(listContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("edge-to-edge");

        getChildren().addAll(title, scrollPane);

        // --- Стилизация Панели (Магическое свечение) ---
        String commonStyle = """
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
                -fx-background-radius: 10;
                -fx-border-color: %s;
                -fx-border-radius: 10;
                -fx-border-width: 1;
                -fx-padding: 12;
                """.formatted(accentColor);

        String idleStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, rgba(156, 39, 176, 0.15), 15, 0, 0, 0);";
        String hoverStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, %s, 10, 0.2, 0, 0);".formatted(accentColor);

        setStyle(idleStyle);
        setOnMouseEntered(e -> setStyle(hoverStyle));
        setOnMouseExited(e -> setStyle(idleStyle));
    }

    private HBox createFamiliarCard(Character familiar) {
        HBox card = getHBox(familiar);

        // --- Содержимое (без изменений, но теперь оно не будет светиться) ---
        ImageView avatar = new ImageView();
        avatar.setFitWidth(40);
        avatar.setFitHeight(40);
        try {
            String path = familiar.getAvatarImage();
            avatar.setImage(new Image((path != null && !path.isBlank())
                    ? CharacterAssetResolver.resolve(character.getName(), path)
                    : Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/no_image.png")).toExternalForm()));
        } catch (Exception e) { /* ignore */ }
        avatar.setClip(new Circle(20, 20, 20));

        Label nameLabel = new Label(familiar.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label raceClassLabel = new Label((familiar.getRace() != null ? familiar.getRace() : "") + " " + (familiar.getCharacterClass() != null ? familiar.getCharacterClass() : ""));
        raceClassLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 11px;");

        VBox infoBox = new VBox(2, nameLabel, raceClassLabel);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        VBox statsBox = new VBox(2);
        statsBox.setAlignment(Pos.CENTER_RIGHT);
        Label hpLabel = new Label(I18n.t("label.familiarsHP")+ ": " + familiar.getMaxHp());
        hpLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 11px; -fx-font-weight: bold;");
        Label acLabel = new Label(I18n.t("label.familiarsAC")+ ": " + familiar.getArmor());
        acLabel.setStyle("-fx-text-fill: #74c0fc; -fx-font-size: 11px; -fx-font-weight: bold;");
        statsBox.getChildren().addAll(hpLabel, acLabel);

        card.getChildren().addAll(avatar, infoBox, statsBox);
        return card;
    }

    private HBox getHBox(Character familiar) {
        HBox card = new HBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(8));

        String accentColor = "#9c27b0";
        // Цвет тени с прозрачностью для мягкости
        String glowColor = "rgba(156, 39, 176, 0.5)";

        // Общие параметры скругления
        String radii = "-fx-background-radius: 6; -fx-border-radius: 6;";

        String baseStyle = radii + """
                -fx-border-width: 1;
                -fx-cursor: hand;
                """;

        // Обычное состояние
        String idleStyle = baseStyle + """
                -fx-background-color: rgba(255, 255, 255, 0.05);
                -fx-border-color: rgba(156, 39, 176, 0.2);
                -fx-effect: null;
                """;

        // Состояние наведения
        String hoverStyle = baseStyle + String.format("""
                -fx-border-color: %1$s;
                /* Два слоя фона: 1-й (внешний) имитирует основу для тени, 2-й (внутренний) - тело карты */
                -fx-background-color: %1$s, #2b2b2b;
                -fx-background-insets: 0, 1; 
                /* Эффект: размытие 15px, spread 0.2 делает края мягче */
                -fx-effect: dropshadow(three-pass-box, %2$s, 15, 0.2, 0, 0);
                """, accentColor, glowColor);

        card.setStyle(idleStyle);

        // Плавное переключение стилей
        card.setOnMouseEntered(e -> card.setStyle(hoverStyle));
        card.setOnMouseExited(e -> card.setStyle(idleStyle));

        card.setOnMouseClicked(e -> new FamiliarInfoDialog(parentStage, familiar, character, storageService).show());
        return card;
    }
}