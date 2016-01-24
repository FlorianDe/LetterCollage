package main.java.de.ateam.controller.listener.resultImage;

import main.java.de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SaliencyOverlayOnOffListener implements ItemListener {
	protected ICollageController controller;

	public SaliencyOverlayOnOffListener(ICollageController controller) {
		this.controller = controller;
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		this.controller.getResultImageModel().setSaliencyMapOverlay(e.getStateChange()== ItemEvent.SELECTED);
	}
}
