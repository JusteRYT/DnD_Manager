package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.ui.CurrencyBox;
import com.example.dnd_manager.overview.ui.InspirationBox;
import com.example.dnd_manager.overview.ui.ManaBar;
import com.example.dnd_manager.store.StorageService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class ResourcePanel extends HBox {

    @Getter
    private final ManaBar manaBar;

    public ResourcePanel(Character character, StorageService storageService) {
        setSpacing(15);
        setPadding(new Insets(5));
        setAlignment(Pos.TOP_LEFT);
        setFillHeight(false);



        // Убираем прозрачный фон, делаем его стандартным для приложения или прозрачным,
        // чтобы не перекрывать стиль внутренних панелек
        setStyle("""
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1e1e1e);
                -fx-background-radius: 12;
                -fx-border-color: #3a3a3a;
                -fx-border-radius: 12;
                -fx-border-width: 1;
                -fx-padding: 20;
                -fx-effect: dropshadow(three-pass-box, rgba(255, 255, 255, 0.08), 15, 0, 0, 0);
                """);

        // 1. Левая часть: Валюта (делаем шире)
        CurrencyBox currencyBox = new CurrencyBox(character, storageService);
        HBox.setHgrow(currencyBox, Priority.ALWAYS); // Занимает всё свободное место
        currencyBox.setMaxWidth(Double.MAX_VALUE);
        currencyBox.setMinHeight(190); // Чтобы по высоте подходило под правый стек

        // 2. Правая часть: Стек (Вдохновение + Мана)
        VBox rightStack = new VBox(10);
        rightStack.setAlignment(Pos.TOP_CENTER);

        InspirationBox inspirationBox = new InspirationBox(character, storageService);
        this.manaBar = new ManaBar(character, storageService);

        // Чтобы правая колонка не была слишком широкой
        rightStack.setMinWidth(300);
        rightStack.setPrefWidth(450);

        // Растягиваем элементы внутри стека по ширине колонки
        inspirationBox.setMaxWidth(Double.MAX_VALUE);
        this.manaBar.setMaxWidth(Double.MAX_VALUE);

        rightStack.getChildren().addAll(inspirationBox, manaBar);

        getChildren().addAll(currencyBox, rightStack);
    }
}