package main.java.de.ateam.controller.listener.collage;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.text.LetterCollection;
import main.java.de.ateam.model.text.LetterFactory;

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
			this.controller.getRoiModel().loadFont((String) e.getItem());

			// Todo Möglicher Weise noch umbrüche betrachten ?
			//this.controller.getRoiModel().getRoiCollection().addImage(this.controller.getRoiModel().getLetterCollection().drawBufFromString(this.controller.getRoiModel().getInputText()));
			//this.controller.getResultImageModel().setActualVisibleRoiImage(this.controller.getRoiModel().getRoiCollection().getLastAddedImage());
		}
	}
}
