package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;

import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Created by Florian on 13.11.2015.
 */
public class MouseWheelZoomListener implements MouseWheelListener {

    protected ICollageController controller;

    public MouseWheelZoomListener(ICollageController controller) {
        this.controller = controller;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
            int notches = e.getWheelRotation();

            if (notches < 0) {
                this.controller.getResultImageModel().setZoomFactor(this.controller.getResultImageModel().getZoomFactor() * 1.05);
            } else {
                this.controller.getResultImageModel().setZoomFactor(this.controller.getResultImageModel().getZoomFactor() * 0.95);
            }
        }
    }
}
