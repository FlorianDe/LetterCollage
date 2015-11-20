package main.java.de.ateam.controller.listener.collage;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.roi.RegionOfInterest;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.model.text.Letter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculateTestListener implements ActionListener {
	protected ICollageController controller;

	public CalculateTestListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		RegionOfInterestImage roii = this.controller.getResultImageModel().getActualVisibleRoiImage();
		Letter l = this.controller.getRoiModel().getLetterCollection().getLetter('A');

		this.controller.getRoiController().getRoiCalculator().calculateIntersection(roii, l, 1, 0, 0);
	}
}
