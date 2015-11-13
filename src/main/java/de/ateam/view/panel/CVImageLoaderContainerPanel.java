package main.java.de.ateam.view.panel;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVImageLoaderContainerPanel extends JPanel implements CstmObserver{

    ICollageController controller;
    public CVImageLoaderFileChooser imageLoaderFileChooser;
    public JPanel imageContainer;

    public CVImageLoaderContainerPanel(ICollageController controller){
        this.controller = controller;
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(new BorderLayout());

        this.imageLoaderFileChooser = new CVImageLoaderFileChooser(controller);

        this.imageContainer = new JPanel();
        this.imageContainer.setLayout(new BoxLayout(this.imageContainer, BoxLayout.Y_AXIS));
        this.imageContainer.setOpaque(false);

        this.add(this.imageLoaderFileChooser, BorderLayout.NORTH);
        this.add(this.imageContainer, BorderLayout.CENTER);

        this.controller.getImageLoaderModel().addObserver(this);

        refreshList();
    }



    //Könnte man evtl ersetzen durch "JList binding" habe ich aber knoch nicht gemacht und sind ja keine 5000Bilder :D!
    public void refreshList(){
        this.imageContainer.removeAll();
        for(BufferedImage buf : this.controller.getImageLoaderModel().getLoadedImages()){
            this.imageContainer.add(new CVImageLoaderRow(this.controller, buf));
        }
    }

    @Override
    public void update(CstmObservable o, Object arg) {
        this.refreshList();
        this.revalidate();
        this.repaint();
    }
}
