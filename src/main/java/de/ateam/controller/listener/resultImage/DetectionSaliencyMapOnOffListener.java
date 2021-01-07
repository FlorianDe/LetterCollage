package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DetectionSaliencyMapOnOffListener implements ItemListener {
    protected ICollageController controller;

    public DetectionSaliencyMapOnOffListener(ICollageController controller) {
        this.controller = controller;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        this.controller.getResultImageModel().setSaliencyMapDetection(e.getStateChange() == ItemEvent.SELECTED);
    }
}
