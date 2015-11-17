package main.java.de.ateam.controller.listener.loadedImages;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.roi.RegionOfInterestImage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ShowRoiImageListener implements ActionListener {
	protected ICollageController controller;
	RegionOfInterestImage roiImage;

	public ShowRoiImageListener(ICollageController controller, RegionOfInterestImage roiImage) {
		this.controller = controller;
		this.roiImage = roiImage;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!this.roiImage.getVisualImage().equals(this.controller.getResultImageModel().getActualVisibleImage())) {
			this.controller.getResultImageModel().setZoomFactor(1.0);
			this.controller.getResultImageModel().setActualVisibleRoiImage(this.roiImage);
		}
	}
}
