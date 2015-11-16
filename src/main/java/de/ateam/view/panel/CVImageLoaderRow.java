package main.java.de.ateam.view.panel;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.listener.loadedImages.DeleteImageListener;
import main.java.de.ateam.controller.listener.loadedImages.ShowImageListener;
import main.java.de.ateam.controller.listener.loadedImages.ShowROIImageListener;
import main.java.de.ateam.model.roi.RegionOfInterest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVImageLoaderRow extends JPanel {

    int BTN_HEIGHT = 50;
    int BTN_WIDTH = 50;
    ICollageController controller;
    BufferedImage img;

    public CVImageLoaderRow(ICollageController controller, BufferedImage img){
        this.controller = controller;
        this.img = img;

        this.setLayout(new GridLayout(1, 2));
        this.createRow(img);
    }


    public void createRow( BufferedImage img){
        Image newimg = img.getScaledInstance( BTN_WIDTH, BTN_HEIGHT,  java.awt.Image.SCALE_SMOOTH ) ;
        JButton btnImage = new JButton(new ImageIcon(newimg));
        btnImage.setSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        btnImage.setOpaque(false);
        btnImage.setBackground(Color.WHITE);

        btnImage.addActionListener(new ShowROIImageListener(this.controller, img));
        //btnImage.addActionListener(new ShowImageListener(this.controller, img));

        this.add(btnImage);

        

        JButton btnDelete = new JButton("X");
        btnDelete.setSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        btnDelete.setOpaque(false);
        btnDelete.setBackground(Color.WHITE);
        btnDelete.addActionListener(new DeleteImageListener(this.controller, img));

        this.add(btnDelete);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int)super.getPreferredSize().getWidth(),BTN_HEIGHT);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension((int)super.getPreferredSize().getWidth(),BTN_HEIGHT);
    }


}
