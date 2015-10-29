package main.java;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;


public class MainFrame extends JFrame {
	private BufferedImageView buffView;
	
	public BufferedImageView getBuffView() {
		return buffView;
	}

	public void setBuffView(BufferedImageView buffView) {
		this.buffView = buffView;
	}

	public MainFrame(){
		initializeUI();
	}
	
	private void initializeUI() {
        this.setTitle("CAS");
        this.setMaximumSize(getMaximumSize());
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        
		this.buffView = new BufferedImageView();        
        this.add(this.buffView,  BorderLayout.CENTER);

		
        this.pack();
        this.setVisible(true);
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
