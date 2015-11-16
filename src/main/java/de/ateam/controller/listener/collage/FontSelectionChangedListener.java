package main.java.de.ateam.controller.listener.collage;

import main.java.de.ateam.controller.ICollageController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class FontSelectionChangedListener implements ItemListener {
	protected ICollageController controller;

	public FontSelectionChangedListener(ICollageController controller) {
		this.controller = controller;
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED) {
			this.controller.getCollageModel().loadFont((String) e.getItem());
			String text = "Viktor stinkt nach pups!";
			this.controller.getImageLoaderModel().addImage(this.controller.getCollageModel().getLetterCollection().drawBufFromString(text));

		}
	}
}