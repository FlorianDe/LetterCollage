package main.java.de.ateam.view.cstmcomponent;

import main.java.de.ateam.controller.ICollageController;

import java.awt.FlowLayout;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;


public class JSliderLabelPanel extends JPanel{

	private static final long serialVersionUID = -8302774182641615679L;
	private final double scale;

	ICollageController controller;
	
	JSlider valueSlider;
	JLabel valueLabel;
		
	
	public JSliderLabelPanel(ICollageController controller, int min, int max, int value, double scale, String unit){
		super();
		this.controller = controller;
		this.scale = scale;

		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setOpaque(false);
		this.valueSlider = new JSlider(JSlider.HORIZONTAL, min, max, value);
		this.valueSlider.setMinorTickSpacing((max-min)/20);
		this.valueSlider.setMajorTickSpacing((max-min)/10);
		this.valueSlider.setPaintTicks(true);
		this.valueSlider.setPaintLabels(true);
		Dictionary<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put(min, new JLabel((min*scale)+unit));
		labelTable.put(max, new JLabel((max*scale)+unit));
		this.valueSlider.setLabelTable(labelTable);
		this.valueSlider.setOpaque(false);
		this.valueLabel = new JLabel();
		
		this.add(this.valueSlider);
		this.add(this.valueLabel);

		this.valueSlider.addChangeListener(e -> {this.valueLabel.setText((valueSlider.getValue()*scale)+unit);});
		this.valueLabel.setText((value*scale)+unit);
	}

	public double getValue(){
		return valueSlider.getValue()*this.scale;
	}

	public void setValue(double value){
		valueSlider.setValue((int)(value/this.scale));
	}
}
