package main.java.de.ateam.controller.listener.loadedImages;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.roi.RegionOfInterestImage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class DeleteRoiImageListener implements ActionListener {
	protected ICollageController controller;
	RegionOfInterestImage clickedRoiImage;

	public DeleteRoiImageListener(ICollageController controller, RegionOfInterestImage clickedRoiImage) {
		this.controller = controller;
		this.clickedRoiImage = clickedRoiImage;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.getRoiModel().getRoiCollection().removeImage(this.clickedRoiImage);
	}
}
