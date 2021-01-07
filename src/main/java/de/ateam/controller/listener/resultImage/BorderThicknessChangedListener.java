package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;
import de.ateam.model.text.Letter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Map;

public class BorderThicknessChangedListener implements ChangeListener {
    protected ICollageController controller;

    public BorderThicknessChangedListener(ICollageController controller) {
        this.controller = controller;
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            controller.getResultImageModel().setFontOutlineThickness((int) source.getValue());
            for (Map.Entry<Character, Letter> entry : controller.getRoiModel().getLetterCollection().getLetterMap().entrySet()) {
                Letter letter = entry.getValue();
                letter.setOutlineResultMask(controller.getRoiModel().getLetterCollection().calculateResultBorders(letter.getSymbol(), controller.getResultImageModel().getFontOutlineColor(), controller.getResultImageModel().getFontOutlineThickness()));
            }
            controller.getRoiController().drawResult();
        }
    }
}
