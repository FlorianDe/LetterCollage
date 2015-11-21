package main.java.de.ateam.controller.listener.collage;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.roi.RegionOfInterestCalculator;
import main.java.de.ateam.model.roi.RegionOfInterest;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.model.text.Letter;
import main.java.de.ateam.model.text.LetterFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CalculateTestListener implements ActionListener {
	protected ICollageController controller;

	public CalculateTestListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Letter> letters = new ArrayList<>();
		for(Character c : this.controller.getRoiModel().getInputText().toCharArray()){
			letters.add(this.controller.getRoiModel().getLetterCollection().getLetter(c));
		}
		RegionOfInterestCalculator roic = new RegionOfInterestCalculator(this.controller.getRoiModel().getLoadedImages(), letters, controller);
		roic.calculateIntersectionMatrix();

		//this.controller.getRoiController().getRoiCalculator().calculateIntersection(roii, l, 1, 0, 0);
	}
}
