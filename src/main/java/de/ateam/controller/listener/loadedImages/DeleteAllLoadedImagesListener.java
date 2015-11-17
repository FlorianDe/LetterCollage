package main.java.de.ateam.controller.listener.loadedImages;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.roi.RegionOfInterestImage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteAllLoadedImagesListener implements ActionListener {
	protected ICollageController controller;

	public DeleteAllLoadedImagesListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.getRoiModel().getRoiCollection().removeAllImages();
	}
}
