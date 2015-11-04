package de.ateam.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class BufferedImageView extends JPanel{
	private BufferedImage actImage;
	
    public void initializeUI()
    {
        this.setDoubleBuffered(true);
        this.setVisible(true);
    }
	
	public void setBufferedImage(BufferedImage actImage){
		this.actImage = actImage;
		//this.revalidate();
		this.repaint();
	}
    
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2d.setRenderingHints(rh);
        
		if(this.actImage!=null){
			System.out.println("Repaint:" + this.actImage.toString());
			g.drawImage(this.actImage, 0, 0, null);
			//g.drawImage(this.actImage, 1, 1, this.actImage.getWidth(), this.actImage.getHeight(), null);
		}
	}
    
    @Override
    public Dimension getPreferredSize() {
    	return new Dimension(640,480);
    }
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(640,480);
	}
}
