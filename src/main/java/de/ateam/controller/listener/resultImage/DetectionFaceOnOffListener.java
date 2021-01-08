package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DetectionFaceOnOffListener implements ItemListener {
    protected ICollageController controller;

    public DetectionFaceOnOffListener(ICollageController controller) {
        this.controller = controller;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        this.controller.getResultImageModel().setFaceDetection(e.getStateChange() == ItemEvent.SELECTED);
    }
}
