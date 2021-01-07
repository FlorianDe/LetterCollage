package de.ateam.controller.listener.resultImage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import de.ateam.controller.ICollageController;

public class ZoomInListener implements ActionListener {
	protected ICollageController controller;

	public ZoomInListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.getResultImageModel().setZoomFactor(this.controller.getResultImageModel().getZoomFactor()*1.09);
	}
}
