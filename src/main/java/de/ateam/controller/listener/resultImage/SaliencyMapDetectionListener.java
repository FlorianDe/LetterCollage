package main.java.de.ateam.controller.listener.resultImage;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.utils.OpenCVUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaliencyMapDetectionListener implements ActionListener {
	protected ICollageController controller;

	public SaliencyMapDetectionListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		(new Thread() {
			public void run() {
				controller.getResultImageModel().getActualVisibleRoiImage().setSaliencyMap(OpenCVUtils.matToBufferedImage(controller.getRoiController().getRoiDetector().saliencyMapDetector(controller.getResultImageModel().getActualVisibleRoiImage())));
				controller.getResultImageModel().getActualVisibleRoiImage().calculateCenterWeight();
				controller.getCollageView().getResultImagePanel().repaint();
			}}).start();
	}
}
