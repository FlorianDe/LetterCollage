package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class GridOnOffListener implements ItemListener {
	protected ICollageController controller;

	public GridOnOffListener(ICollageController controller) {
		this.controller = controller;
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		this.controller.getResultImageModel().setResolutionRasterVisible(e.getStateChange()== ItemEvent.SELECTED);
	}
}
