package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.inventory.InventoryItemPopup;
import com.example.dnd_manager.info.stats.StatsGridView;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.tooltip.BuffsView;
import com.example.dnd_manager.tooltip.SkillsView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Popup;

/**
 * Main character overview screen.
 * Displays character information, stats, skills, buffs, and inventory.
 */
public class CharacterOverviewScreen extends BorderPane {

    public CharacterOverviewScreen(String name, StorageService storageService) {
        Character character = storageService.loadCharacter(name)
                .orElseThrow(() -> new RuntimeException("Character not found: " + name));

        setPadding(new Insets(15));
        setStyle("-fx-background-color: #1e1e1e;");

        setTop(createHeader(character));
        setCenter(createMainContent(character));
        setBottom(createSkillsBar(character));
    }

    /**
     * Creates header with avatar and character identity panel.
     */
    private HBox createHeader(Character character) {
        ImageView avatar = new ImageView(new Image(character.getAvatarImage()));
        avatar.setFitWidth(96);
        avatar.setFitHeight(96);

        Label name = new Label(character.getName());
        name.setStyle("""
                -fx-font-size: 22px;
                -fx-font-weight: bold;
                -fx-text-fill: #f2f2f2;
                """);

        Label meta = new Label(character.getRace() + " • " + character.getCharacterClass());
        meta.setStyle("""
                -fx-font-size: 14px;
                -fx-text-fill: #c89b3c;
                """);

        VBox info = new VBox(2, name, meta);
        info.setPadding(new Insets(8, 12, 8, 12));
        info.setStyle("""
                -fx-background-color: #252526;
                -fx-background-radius: 8;
                """);

        HBox header = new HBox(15, avatar, info);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 0, 20, 0));

        return header;
    }

    /**
     * Main content with left info, center stats & description, right inventory/buffs
     */
    private GridPane createMainContent(Character character) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));

        // Left column: Description + Stats
        VBox description = createTextBlock(character.getDescription());
        Label titleStats = new Label("Stats");
        titleStats.setStyle("""
                    -fx-text-fill: #ffffff;
                    -fx-font-size: 16px;
                    -fx-font-weight: bold;
                """);
        VBox stats = new VBox(
                titleStats,
                new StatsGridView(character.getStats().asMap())
        );
        stylePanel(stats);
        VBox leftColumn = new VBox(10, description, stats);

        // Right column: Buffs/Debuffs + Inventory
        VBox buffs = new VBox(5,
                new BuffsView(character.getBuffs())
        );
        buffs.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");
        buffs.setPadding(new Insets(8));

        Label titleInventory = new Label("INVENTORY");
        titleInventory.setStyle("""
                    -fx-text-fill: #c89b3c;
                    -fx-font-size: 14px;
                    -fx-font-weight: bold;
                """);
        VBox inventory = new VBox(5,
                titleInventory,
                createInventoryView(character)
        );
        inventory.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");
        inventory.setPadding(new Insets(8));

        VBox rightColumn = new VBox(15, buffs, inventory);

        // Layout in grid
        grid.add(leftColumn, 0, 0);
        grid.add(rightColumn, 1, 0);

        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(50);
        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(50);

        grid.getColumnConstraints().addAll(left, right);

        return grid;
    }

    /**
     * Skills bar at the bottom with icon buttons
     */
    private HBox createSkillsBar(Character character) {
        HBox skillsBar = new HBox();
        skillsBar.setPadding(new Insets(10));
        skillsBar.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");
        skillsBar.setAlignment(Pos.CENTER_LEFT);

        SkillsView skillsView = new SkillsView(character.getSkills());
        skillsBar.getChildren().add(skillsView);

        return skillsBar;
    }

    /**
     * Inventory as icons with hover info
     */
    private FlowPane createInventoryView(Character character) {
        FlowPane inventory = new FlowPane(10, 10);
        inventory.setPadding(new Insets(8));

        for (InventoryItem item : character.getInventory()) {
            ImageView icon = new ImageView(new Image("file:" + item.getIconPath()));
            icon.setFitWidth(48);
            icon.setFitHeight(48);
            icon.setPreserveRatio(true);
            icon.setStyle("-fx-cursor: hand;");

            Popup popup = new Popup();
            popup.setAutoHide(true);
            popup.getContent().add(new InventoryItemPopup(item));

            icon.setOnMouseEntered(e -> {
                var bounds = icon.localToScreen(icon.getBoundsInLocal());
                popup.show(
                        icon.getScene().getWindow(),
                        bounds.getMaxX() + 10,
                        bounds.getMinY()
                );
            });

            icon.setOnMouseExited(e -> popup.hide());
            inventory.getChildren().add(icon);
        }

        return inventory;
    }

    private void stylePanel(Region region) {
        region.setStyle("""
                -fx-background-color: #252526;
                -fx-background-radius: 8;
                -fx-padding: 10;
                """);
    }

    /**
     * Text block with title and content, styled
     */
    private VBox createTextBlock(String content) {
        Label titleLabel = new Label("Description");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff;");

        Label text = new Label(content);
        text.setWrapText(true);
        text.setStyle("-fx-text-fill: #dddddd;");

        VBox box = new VBox(5, titleLabel, text);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");

        return box;
    }
}