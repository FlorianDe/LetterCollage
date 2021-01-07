package de.ateam.view;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {
    private BufferedImageView buffView;
    private BufferedImageView maskView;

    public BufferedImageView getMaskView() {
        return maskView;
    }

    public void setMaskView(BufferedImageView maskView) {
        this.maskView = maskView;
    }

    public BufferedImageView getBuffView() {
        return buffView;
    }

    public void setBuffView(BufferedImageView buffView) {
        this.buffView = buffView;
    }

    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {
        this.setTitle("CAS");
        this.setMaximumSize(getMaximumSize());
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);

        this.buffView = new BufferedImageView();
        this.add(this.buffView, BorderLayout.WEST);

        this.maskView = new BufferedImageView();
        this.add(this.maskView, BorderLayout.EAST);


        this.pack();
        this.setVisible(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1280, 480);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(1280, 480);
    }
}
