package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DetectionFullbodyOnOffListener implements ItemListener {
	protected ICollageController controller;

	public DetectionFullbodyOnOffListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		this.controller.getResultImageModel().setFullbodyDetection(e.getStateChange()== ItemEvent.SELECTED);
	}
}
