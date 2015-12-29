package main.java.de.ateam.view.cstmcomponent;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.listener.collage.SampleSizeChangedListener;
import main.java.de.ateam.model.text.LetterCollection;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;

import java.awt.FlowLayout;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;


public class CASJSliderPanel extends JPanel implements CstmObserver {

	private static final long serialVersionUID = -8302774182641615679L;

	ICollageController controller;
	
	JSlider speedSlider;
	JLabel speedLabel;
		
	
	public CASJSliderPanel(ICollageController controller){
		super();
		this.controller = controller;
		this.controller.getRoiModel().getLetterCollection().addObserver(this);
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setOpaque(false);
		this.speedSlider = new JSlider(JSlider.HORIZONTAL, LetterCollection.SAMPLER_MIN, LetterCollection.SAMPLER_MAX, this.controller.getRoiModel().getLetterCollection().getSAMPLER_SIZE());
		this.speedSlider.setMinorTickSpacing(LetterCollection.SAMPLER_MIN);
		this.speedSlider.setMajorTickSpacing(25);
		this.speedSlider.setPaintTicks(true);
		this.speedSlider.setPaintLabels(true);
		Dictionary<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put(LetterCollection.SAMPLER_MIN, new JLabel(((double)LetterCollection.SAMPLER_MIN)+"px"));
		labelTable.put(LetterCollection.SAMPLER_MAX, new JLabel(((double)LetterCollection.SAMPLER_MAX)+"px"));
		this.speedSlider.setLabelTable(labelTable);
		this.speedSlider.setOpaque(false);
		this.speedSlider.addChangeListener(new SampleSizeChangedListener(controller));
		
		this.speedLabel = new JLabel();
		this.speedLabel.setText(this.controller.getRoiModel().getLetterCollection().getSAMPLER_SIZE()+"px");
		
		
		this.add(this.speedSlider);
		this.add(this.speedLabel);
	}


	@Override
	public void update(CstmObservable arg0, Object arg1) {
		//this.speedLabel.setText(controller.getSimulationModel().getDelay()+"ms [~" + new DecimalFormat("0.00").format((1000.0/controller.getSimulationModel().getDelay())) + " fps]");
		this.speedLabel.setText(this.controller.getRoiModel().getLetterCollection().getSAMPLER_SIZE()+"px");
	}
}
