package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FullbodyDetectionListener implements ActionListener {
	protected ICollageController controller;

	public FullbodyDetectionListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.getRoiController().getRoiDetector().fullbodyRecognition(this.controller.getResultImageModel().getActualVisibleRoiImage());
	}
}
