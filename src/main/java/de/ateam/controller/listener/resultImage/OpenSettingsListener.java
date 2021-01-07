package de.ateam.controller.listener.resultImage;

import de.ateam.controller.ICollageController;
import de.ateam.controller.roi.RegionOfInterestCalculator;
import de.ateam.view.dialog.SettingsDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenSettingsListener implements ActionListener {
	protected ICollageController controller;

	public OpenSettingsListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SettingsDialog sd = new SettingsDialog(this.controller);

		sd.setOnOKListener(okEvent -> {
			applyOrOkBtnClicked(sd);
			sd.closeAction();
		});
		sd.setOnApplyListener(applyEvent -> {
			applyOrOkBtnClicked(sd);
		});

		sd.setVisible(true);
	}

	public void applyOrOkBtnClicked(SettingsDialog sd){
		controller.getRoiModel().getLetterCollection().setSamplerSize((int)sd.getSliderLetterSampleSize().getValue());
		controller.getRoiModel().setScaleEnd(sd.getSliderScaleEnd().getValue());
		controller.getRoiModel().setScaleStepSize(sd.getSliderScaleSteps().getValue());

		controller.getRoiController().drawResult();
	}
}
