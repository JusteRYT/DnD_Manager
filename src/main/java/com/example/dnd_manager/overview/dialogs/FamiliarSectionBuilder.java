package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.stats.StatEnum;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.components.IconSlot;
import com.example.dnd_manager.overview.dialogs.components.IconSlotMapper;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Function;

public class FamiliarSectionBuilder {

    public static Node buildResources(Label hpVal, Label acVal, Label mpVal, Label lvlVal) {
        FlowPane pane = new FlowPane(15, 10);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: rgba(0,0,0,0.2); -fx-padding: 10; -fx-background-radius: 8;");

        pane.getChildren().addAll(
                createResBox("label.familiarsHP", hpVal),
                createResBox("label.familiarsAC", acVal),
                createResBox("label.familiarsMP", mpVal),
                createResBox("label.familiarsLVL", lvlVal)
        );
        return pane;
    }

    public static Node buildStats(Character familiar) {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);
        StatEnum[] stats = StatEnum.values();
        for (int i = 0; i < stats.length; i++) {
            grid.add(createStatBlock(stats[i], familiar.getStats().get(stats[i])), i % 6, i / 6);
        }
        return grid;
    }

    public static void addLore(VBox container, Character familiar) {
        addTextSection(container, I18n.t("label.textSection.description"), familiar.getDescription());
        addTextSection(container, I18n.t("label.textSection.personality"), familiar.getPersonality());
    }

    public static Node buildIconLists(Character familiar, Character character) {
        VBox container = new VBox(20);

        appendIconRow(container,
                I18n.t("label.familiarsSKILLS"),
                familiar.getSkills(),
                s -> new IconSlot(
                        IconSlotMapper.fromSkill(s),
                        character
                ));

        appendIconRow(container,
                I18n.t("label.familiarsINVENTORY"),
                familiar.getInventory(),
                i -> new IconSlot(
                        IconSlotMapper.fromInventoryItem(i),
                        character));

        appendIconRow(container,
                I18n.t("label.familiarsEFFECTS"),
                familiar.getBuffs(),
                b -> new IconSlot(
                        IconSlotMapper.fromBuff(b), character));

        return container;
    }

    private static <T> void appendIconRow(VBox container, String title, List<T> items, Function<T, Node> mapper) {
        if (items == null || items.isEmpty()) return;
        VBox section = new VBox(8, createHeaderLabel(title));
        FlowPane flow = new FlowPane(10, 10);
        items.forEach(item -> flow.getChildren().add(mapper.apply(item)));
        section.getChildren().add(flow);
        container.getChildren().add(section);
    }

    private static Label createHeaderLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #9c27b0; -fx-font-weight: bold; -fx-font-size: 12px;");
        return l;
    }

    private static VBox createResBox(String labelKey, Label valueLabel) {
        Label title = new Label(I18n.t(labelKey));
        title.setStyle("-fx-text-fill: #666; -fx-font-size: 9px;");
        VBox b = new VBox(-2, title, valueLabel);
        b.setAlignment(Pos.CENTER);
        b.setMinWidth(55);
        return b;
    }

    private static VBox createStatBlock(StatEnum stat, int val) {
        VBox b = new VBox(0);
        b.setAlignment(Pos.CENTER);
        b.setPrefWidth(60);
        b.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 5; -fx-background-radius: 4;");
        Label n = new Label(stat.getName().substring(0, 3).toUpperCase());
        n.setStyle("-fx-text-fill: #888; -fx-font-size: 10px;");
        Label v = new Label(String.valueOf(val));
        v.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        b.getChildren().addAll(n, v);
        return b;
    }

    private static void addTextSection(VBox container, String title, String text) {
        if (text == null || text.isBlank()) return;
        VBox box = new VBox(2, createHeaderLabel(title.toUpperCase()), new Label(text));
        ((Label) box.getChildren().get(1)).setWrapText(true);
        box.getChildren().get(1).setStyle("-fx-text-fill: #ddd; -fx-font-size: 13px;");
        container.getChildren().add(box);
    }
}