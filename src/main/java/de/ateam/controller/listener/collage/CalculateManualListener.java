package main.java.de.ateam.controller.listener.collage;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.roi.CalculationResult;
import main.java.de.ateam.controller.roi.RegionOfInterestCalculator;
import main.java.de.ateam.exception.NoFontSelectedException;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.model.text.Letter;
import main.java.de.ateam.utils.OpenCVUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalculateManualListener implements ActionListener {
	protected ICollageController controller;

	public CalculateManualListener(ICollageController controller) {

		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new Thread()
		{
			public void run(){
				controller.getRoiController().initProgressbar();
				controller.getRoiController().calculateSolutionForCollage();
			}
		}.start();
	}
}
