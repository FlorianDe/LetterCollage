package de.ateam.controller.listener.collage;

import de.ateam.controller.ICollageController;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class SampleSizeChangedListener   implements ChangeListener {
	protected ICollageController controller;

	public SampleSizeChangedListener(ICollageController controller) {
		this.controller = controller;
	}


	@Override
	public void stateChanged(ChangeEvent e) {
		int px = this.controller.getRoiModel().getLetterCollection().getSAMPLER_SIZE();

		if(e.getSource() instanceof JSlider){
			JSlider source = (JSlider)e.getSource();
			px = (int)source.getValue();
		}
		else if(e.getSource() instanceof JTextField){
			JTextField source = (JTextField)e.getSource();
			px = Integer.parseInt(source.getText());
		}

		this.controller.getRoiModel().getLetterCollection().setSamplerSize(px);
	}
}
