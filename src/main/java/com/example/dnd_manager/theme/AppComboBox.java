package com.example.dnd_manager.theme;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * ComboBox styled according to the application theme.
 *
 * @param <T> type of items
 */
public class AppComboBox<T> extends ComboBox<T> {

    /**
     * Creates an empty AppComboBox with no items.
     */
    public AppComboBox() {
        super();
        applyStyle();
    }

    /**
     * Creates an AppComboBox with initial items.
     *
     * @param items initial items
     */
    public AppComboBox(javafx.collections.ObservableList<T> items) {
        super(items);
        applyStyle();
    }

    /**
     * Applies color theme and styling to the ComboBox.
     */
    private void applyStyle() {
        // Main combo box style
        setStyle("""
            -fx-background-color: %s;
            -fx-border-color: %s;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
            -fx-text-fill: %s;
        """.formatted(
                AppTheme.BACKGROUND_SECONDARY,
                AppTheme.BORDER_MUTED,
                AppTheme.TEXT_PRIMARY
        ));

        // Dropdown cells styling
        setCellFactory(new Callback<>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item.toString());
                            setStyle("""
                                -fx-background-color: %s;
                                -fx-text-fill: %s;
                            """.formatted(AppTheme.BACKGROUND_SECONDARY, AppTheme.TEXT_PRIMARY));
                        }
                    }
                };
            }
        });

        // Button cell styling (selected item)
        setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setStyle("""
                        -fx-background-color: %s;
                        -fx-text-fill: %s;
                    """.formatted(AppTheme.BACKGROUND_SECONDARY, AppTheme.TEXT_PRIMARY));
                }
            }
        });
    }
}
