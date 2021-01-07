package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EyeDetectionListener implements ActionListener {
	protected ICollageController controller;

	public EyeDetectionListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.getRoiController().getRoiDetector().eyeRecognition(this.controller.getResultImageModel().getActualVisibleRoiImage());
	}
}
