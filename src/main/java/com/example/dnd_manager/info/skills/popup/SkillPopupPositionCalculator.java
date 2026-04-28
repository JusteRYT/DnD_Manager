package com.example.dnd_manager.info.skills.popup;

public class SkillPopupPositionCalculator {

    public SkillPopupPosition calculate(
            double descriptionMaxX,
            double descriptionMinY,
            double cardMinX,
            double screenMaxX,
            double popupWidth,
            double gap,
            double minimumY
    ) {
        double x = descriptionMaxX + gap;
        double y = descriptionMinY - 80;

        if (x + popupWidth > screenMaxX) {
            x = cardMinX - popupWidth - gap;
        }

        if (y < minimumY) {
            y = minimumY;
        }

        return new SkillPopupPosition(x, y);
    }
}












