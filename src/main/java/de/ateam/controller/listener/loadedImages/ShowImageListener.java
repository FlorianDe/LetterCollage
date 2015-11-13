package main.java.de.ateam.controller.listener.loadedImages;

import main.java.de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ShowImageListener implements ActionListener {
	protected ICollageController controller;
	BufferedImage clickedImage;

	public ShowImageListener(ICollageController controller, BufferedImage clickedImage) {

		this.controller = controller;
		this.clickedImage = clickedImage;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.getResultImageModel().setZoomFactor(1.0);
		this.controller.getResultImageModel().setActualVisibleImage(this.clickedImage);
	}
}
