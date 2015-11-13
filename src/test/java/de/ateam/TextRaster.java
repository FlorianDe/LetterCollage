package test.java.de.ateam;

import main.java.de.ateam.utils.FontLoader;

import javax.swing.*;
import java.awt.*;



public class TextRaster extends JPanel{
	private JFrame frame;
	private JSlider slider;
	private JTextField textField;
	private JToolBar toolbar;
	
	public TextRaster() {
		super();
		this.frame = new JFrame("Textsampler");
		this.frame.setLayout(new BorderLayout());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	
		this.slider = new JSlider();
		this.textField = new JTextField();
		
		this.toolbar = new JToolBar();
		this.toolbar.add(slider);
		this.toolbar.add(textField);
		this.frame.add(toolbar, BorderLayout.PAGE_START);
		this.frame.add(this, BorderLayout.CENTER);
		
		this.frame.setSize(500, 500);
		this.setSize(500, 500);
		this.frame.setVisible(true);	
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2d.setRenderingHints(rh);
        
		Font font = FontLoader.getFont("GoodDog");
		font = font.deriveFont(90.0f);
		System.out.println(font.getSize());
		System.out.println(font);
		g.setFont(font);
		g.drawString("Hallo Flo!", 100, 100);
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TextRaster raster = new TextRaster();

	}

}
