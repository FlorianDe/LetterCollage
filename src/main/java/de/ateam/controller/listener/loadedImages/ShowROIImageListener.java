package main.java.de.ateam.controller.listener.loadedImages;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.roi.RegionOfInterest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ShowROIImageListener implements ActionListener {
	protected ICollageController controller;
	RegionOfInterest regionOfInterest;

	public ShowROIImageListener(ICollageController controller, BufferedImage image) {

		this.controller = controller;
		this.regionOfInterest = this.controller.getCollageModel().getROICollection().getImage(image);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!this.regionOfInterest.getDebugImage().equals(this.controller.getResultImageModel().getActualVisibleImage())) {
			this.controller.getResultImageModel().setZoomFactor(1.0);
			this.controller.getResultImageModel().setActualVisibleImage(this.regionOfInterest.getDebugImage());
		}
	}
}
