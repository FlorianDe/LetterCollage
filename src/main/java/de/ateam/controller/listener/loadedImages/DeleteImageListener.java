package main.java.de.ateam.controller.listener.loadedImages;

import main.java.de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class DeleteImageListener implements ActionListener {
	protected ICollageController controller;
	BufferedImage clickedImage;

	public DeleteImageListener(ICollageController controller, BufferedImage clickedImage) {

		this.controller = controller;
		this.clickedImage = clickedImage;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.getImageLoaderModel().removeImage(this.clickedImage);
	}
}
