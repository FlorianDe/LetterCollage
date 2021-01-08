package de.ateam.controller.listener.collage;

import de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculateAutomaticListener implements ActionListener {
    protected ICollageController controller;

    public CalculateAutomaticListener(ICollageController controller) {

        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread() {
            public void run() {
                controller.getRoiController().initProgressbar();
                controller.getRoiController().calculateROIsAutomatic();
                controller.getRoiController().calculateSolutionForCollage();
            }
        }.start();
    }
}
