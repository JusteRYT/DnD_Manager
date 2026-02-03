package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.skills.SkillCardView;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;

import java.util.List;

/**
 * View for displaying a list of skill cards.
 */
public class SkillsView extends FlowPane {

    public SkillsView(List<Skill> skills) {
        setHgap(20);
        setVgap(20);
        setPadding(new Insets(20));
        setPrefWidth(900);
        setMaxWidth(Double.MAX_VALUE);

        skills.stream()
                .map(SkillCardView::new)
                .forEach(getChildren()::add);
    }
}