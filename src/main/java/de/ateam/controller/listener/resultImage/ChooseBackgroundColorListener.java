package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseBackgroundColorListener implements ActionListener {
    protected ICollageController controller;

    public ChooseBackgroundColorListener(ICollageController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Color background = JColorChooser.showDialog(null, "Change Result Image Background Color", controller.getResultImageModel().getBackgroundColor());
        if (background != null) {
            controller.getResultImageModel().setBackgroundColor(background);
            if (controller.getRoiController().getRoic() != null) {
                controller.getRoiController().drawResult();
            }
        }
    }
}
