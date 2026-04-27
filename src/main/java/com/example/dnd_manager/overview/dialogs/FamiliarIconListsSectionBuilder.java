package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.components.IconSlot;
import com.example.dnd_manager.overview.dialogs.components.IconSlotMapper;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class FamiliarIconListsSectionBuilder {

    private final FamiliarSectionStyleProvider styleProvider;

    public FamiliarIconListsSectionBuilder() {
        this(new FamiliarSectionStyleProvider());
    }

    FamiliarIconListsSectionBuilder(FamiliarSectionStyleProvider styleProvider) {
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
    }

    public Node build(Character familiar, Character owner) {
        VBox container = new VBox(20);

        appendIconRow(container,
                I18n.t("label.familiarsSKILLS"),
                familiar.getSkills(),
                skill -> new IconSlot(IconSlotMapper.fromSkill(skill), owner));

        appendIconRow(container,
                I18n.t("label.familiarsINVENTORY"),
                familiar.getInventory(),
                item -> new IconSlot(IconSlotMapper.fromInventoryItem(item), owner));

        appendIconRow(container,
                I18n.t("label.familiarsEFFECTS"),
                familiar.getBuffs(),
                buff -> new IconSlot(IconSlotMapper.fromBuff(buff), owner));

        return container;
    }

    private <T> void appendIconRow(VBox container, String title, List<T> items, Function<T, Node> mapper) {
        if (items == null || items.isEmpty()) {
            return;
        }
        VBox section = new VBox(8, createHeaderLabel(title));
        FlowPane flow = new FlowPane(10, 10);
        items.forEach(item -> flow.getChildren().add(mapper.apply(item)));
        section.getChildren().add(flow);
        container.getChildren().add(section);
    }

    private Label createHeaderLabel(String text) {
        Label label = new Label(text);
        label.setStyle(styleProvider.iconHeaderStyle());
        return label;
    }
}
