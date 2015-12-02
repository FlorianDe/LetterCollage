package main.java.de.ateam.controller.listener.resultImage;

import main.java.de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridOnOffListener implements ActionListener {
	protected ICollageController controller;

	public GridOnOffListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.getResultImageModel().setResolutionRasterVisible(!this.controller.getResultImageModel().isResolutionRasterVisible());
	}
}
