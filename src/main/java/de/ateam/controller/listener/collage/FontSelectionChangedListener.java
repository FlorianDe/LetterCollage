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
			// Todo Möglicher Weise noch umbrüche betrachten ?
			this.controller.getImageLoaderModel().addImage(this.controller.getCollageModel().getLetterCollection().drawBufFromString("Florian stinkt \nnach pups!\nUnd zwar zieml doll...\nJAAA"));
			this.controller.getResultImageModel().setActualVisibleImage(this.controller.getImageLoaderModel().getLastAddedImage());
		}
	}
}
