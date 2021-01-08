package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;
import de.ateam.model.text.Letter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ChooseBorderColorListener implements ActionListener {
    protected ICollageController controller;

    public ChooseBorderColorListener(ICollageController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Color borderColor = JColorChooser.showDialog(null, "Change Result Image Border Color", controller.getResultImageModel().getBackgroundColor());
        if (borderColor != null) {
            controller.getResultImageModel().setFontOutlineColor(borderColor);
            //Set<Character> chars = new HashSet(Arrays.asList(controller.getRoiModel().getInputText().toCharArray()));

            for (Map.Entry<Character, Letter> entry : controller.getRoiModel().getLetterCollection().getLetterMap().entrySet()) {
                Letter letter = entry.getValue();
                letter.setOutlineResultMask(controller.getRoiModel().getLetterCollection().calculateResultBorders(letter.getSymbol(), controller.getResultImageModel().getFontOutlineColor(), controller.getResultImageModel().getFontOutlineThickness()));
            }

            if (controller.getRoiController().getRoic() != null) {
                controller.getRoiController().drawResult();
            }
        }
    }
}
