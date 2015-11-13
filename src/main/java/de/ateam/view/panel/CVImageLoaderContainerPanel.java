package main.java.de.ateam.view.panel;

import main.java.de.ateam.controller.ICollageController;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVImageLoaderContainerPanel extends JPanel {

    ICollageController controller;
    public CVImageLoaderFileChooser imageLoaderFileChooser;
    public JPanel imageContainer;

    public CVImageLoaderContainerPanel(ICollageController controller){
        this.controller = controller;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.imageLoaderFileChooser = new CVImageLoaderFileChooser(controller);

        this.imageContainer = new JPanel();
        this.imageContainer.setLayout(new BoxLayout(this.imageContainer, BoxLayout.Y_AXIS));
        this.imageContainer.setOpaque(false);

        this.add(this.imageLoaderFileChooser);
        this.add(this.imageContainer);

        refreshList();
    }



    public void refreshList(){
        for(BufferedImage buf : this.controller.getImageLoaderModel().getLoadedImages()){
            this.imageContainer.add(new CVImageLoaderRow(this.controller, buf));
        }
    }
}
