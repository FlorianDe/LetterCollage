package main.java.de.ateam.controller.listener.resultImage;

import main.java.de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FaceDetectionListener implements ActionListener {
	protected ICollageController controller;

	public FaceDetectionListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.getRoiController().getRoiDetector().faceRecognition(this.controller.getResultImageModel().getActualVisibleRoiImage());
	}
}
