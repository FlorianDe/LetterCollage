package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZoomInListener implements ActionListener {
    protected ICollageController controller;

    public ZoomInListener(ICollageController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.controller.getResultImageModel().setZoomFactor(this.controller.getResultImageModel().getZoomFactor() * 1.09);
    }
}
