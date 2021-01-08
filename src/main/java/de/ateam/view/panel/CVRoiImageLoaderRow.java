package de.ateam.view.panel;

import de.ateam.controller.ICollageController;
import de.ateam.controller.listener.loadedImages.DeleteRoiImageListener;
import de.ateam.controller.listener.loadedImages.ShowRoiImageListener;
import de.ateam.model.roi.RegionOfInterestImage;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVRoiImageLoaderRow extends JPanel {

    int BTN_HEIGHT = 50;
    int BTN_WIDTH = 50;
    ICollageController controller;
    RegionOfInterestImage roiImage;

    public CVRoiImageLoaderRow(ICollageController controller, RegionOfInterestImage roiImage) {
        this.controller = controller;
        this.roiImage = roiImage;

        this.setLayout(new GridLayout(1, 2));
        this.createRow(roiImage);
    }


    public void createRow(RegionOfInterestImage roiImage) {
        Image newimg = roiImage.getVisualImage().getScaledInstance(BTN_WIDTH, BTN_HEIGHT, java.awt.Image.SCALE_SMOOTH);
        JButton btnImage = new JButton(new ImageIcon(newimg));
        btnImage.setSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        btnImage.setOpaque(false);
        btnImage.setBackground(Color.WHITE);

        btnImage.addActionListener(new ShowRoiImageListener(this.controller, roiImage));
        //btnImage.addActionListener(new ShowImageListener(this.controller, img));

        this.add(btnImage);


        JButton btnDelete = new JButton("X");
        btnDelete.setSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        btnDelete.setOpaque(false);
        btnDelete.setBackground(Color.WHITE);
        btnDelete.addActionListener(new DeleteRoiImageListener(this.controller, roiImage));

        this.add(btnDelete);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) super.getPreferredSize().getWidth(), BTN_HEIGHT);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension((int) super.getPreferredSize().getWidth(), BTN_HEIGHT);
    }


}
