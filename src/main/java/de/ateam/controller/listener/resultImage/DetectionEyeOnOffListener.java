package main.java.de.ateam.controller.listener.resultImage;

import main.java.de.ateam.controller.ICollageController;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DetectionEyeOnOffListener implements ItemListener {
	protected ICollageController controller;

	public DetectionEyeOnOffListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		this.controller.getResultImageModel().setEyeDetection(e.getStateChange()== ItemEvent.SELECTED);
	}
}
