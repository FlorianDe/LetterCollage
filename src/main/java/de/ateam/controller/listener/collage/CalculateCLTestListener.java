package de.ateam.controller.listener.collage;

import de.ateam.controller.ICollageController;
import de.ateam.controller.roi.CalculationResult;
import de.ateam.controller.roi.RegionOfInterestCLCalculator;
import de.ateam.controller.roi.RegionOfInterestCalculator;
import de.ateam.exception.NoFontSelectedException;
import de.ateam.model.roi.RegionOfInterestImage;
import de.ateam.model.text.Letter;
import de.ateam.model.text.LetterCollection;
import de.ateam.utils.OpenCVUtils;
import org.jocl.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.HashMap;

import static org.jocl.CL.CL_DEVICE_TYPE_ALL;

public class CalculateCLTestListener implements ActionListener {
	protected ICollageController controller;

	public CalculateCLTestListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			ArrayList<Letter> letters = new ArrayList<>();
			try {
				for (Character c : this.controller.getRoiModel().getInputText().toCharArray()) {
					letters.add(this.controller.getRoiModel().getLetterCollection().getLetter(c));
				}
                RegionOfInterestCLCalculator roic = new RegionOfInterestCLCalculator(this.controller.getRoiModel().getLoadedImages(), letters, controller);
                roic.calculateIntersectionMatrix();



			} catch (NullPointerException npExce) {
				throw new NoFontSelectedException();
			}
		}
		catch(NoFontSelectedException nfsExce){
			this.controller.getRoiModel().notifyViewFromController();
		}
	}
}
