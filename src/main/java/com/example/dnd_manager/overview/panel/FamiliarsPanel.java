package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.FamiliarInfoDialog;
import com.example.dnd_manager.repository.CharacterAssetResolver;
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
    private Character character;

    private final Stage parentStage;
    public FamiliarsPanel(Character character, Stage parentStage) {
        this.parentStage = parentStage;
        this.character = character;
        // Мистический фиолетовый цвет для фамильяров
        String accentColor = "#9c27b0";

        // Заголовок
        Label title = new Label(I18n.t("label.familiars"));
        title.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 14px; -fx-font-weight: bold; -fx-letter-spacing: 1.5;", accentColor));

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
        HBox card = new HBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(8));
        card.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-background-radius: 6;");
        card.setOnMouseClicked(e -> {
            new FamiliarInfoDialog(parentStage, familiar, character).show();
        });

        // 1. Аватар
        ImageView avatar = new ImageView();
        avatar.setFitWidth(40);
        avatar.setFitHeight(40);
        avatar.setPreserveRatio(false);

        try {
            String path = familiar.getAvatarImage();
            if (path != null && !path.isBlank()) {
                avatar.setImage(new Image(CharacterAssetResolver.resolve(character.getName(), path)));
            } else {
                avatar.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/no_image.png")).toExternalForm()));
            }
        } catch (Exception e) {
            // Fallback
        }

        // Круглая маска для аватара
        Circle clip = new Circle(20, 20, 20);
        avatar.setClip(clip);

        // 2. Информация
        Label nameLabel = new Label(familiar.getName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label raceClassLabel = new Label((familiar.getRace() != null ? familiar.getRace() : "") + " " + (familiar.getCharacterClass() != null ? familiar.getCharacterClass() : ""));
        raceClassLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 11px;");

        VBox infoBox = new VBox(2, nameLabel, raceClassLabel);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // 3. Статы (HP / AC)
        VBox statsBox = new VBox(2);
        statsBox.setAlignment(Pos.CENTER_RIGHT);

        Label hpLabel = new Label("HP: " + familiar.getHp());
        hpLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 11px; -fx-font-weight: bold;");

        Label acLabel = new Label("AC: " + familiar.getArmor());
        acLabel.setStyle("-fx-text-fill: #74c0fc; -fx-font-size: 11px; -fx-font-weight: bold;");

        statsBox.getChildren().addAll(hpLabel, acLabel);

        card.getChildren().addAll(avatar, infoBox, statsBox);
        return card;
    }
}