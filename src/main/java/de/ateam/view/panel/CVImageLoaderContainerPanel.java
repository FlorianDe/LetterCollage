package main.java.de.ateam.view.panel;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVImageLoaderContainerPanel extends JPanel implements CstmObserver{

    ICollageController controller;
    public CVImageLoaderFileChooser imageLoaderFileChooser;
    public JPanel roiImageContainer;

    public CVImageLoaderContainerPanel(ICollageController controller){
        this.controller = controller;
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(new BorderLayout());

        this.imageLoaderFileChooser = new CVImageLoaderFileChooser(controller);

        this.roiImageContainer = new JPanel();
        this.roiImageContainer.setLayout(new BoxLayout(this.roiImageContainer, BoxLayout.Y_AXIS));
        this.roiImageContainer.setOpaque(false);

        this.add(this.imageLoaderFileChooser, BorderLayout.NORTH);
        this.add(this.roiImageContainer, BorderLayout.CENTER);

        //this.controller.getImageLoaderModel().addObserver(this);
        this.controller.getRoiModel().addObserver(this);
        this.controller.getRoiModel().getRoiCollection().addObserver(this);



        update(null, null);
    }

    public void setStyle(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = getBackground();
        Color color2 = color1.darker().darker();
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(
                0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setStyle(g);
    }



    //K�nnte man evtl ersetzen durch "JList binding" habe ich aber knoch nicht gemacht und sind ja keine 5000Bilder :D!
    public synchronized void refreshList(){
        this.roiImageContainer.removeAll();
        /*
        for(BufferedImage buf : this.controller.getImageLoaderModel().getLoadedImages()){
            if(buf!=null) {
                this.roiImageContainer.add(new CVRoiImageLoaderRow(this.controller, buf));
            }
        }
        */
        for(RegionOfInterestImage roiImg : this.controller.getRoiModel().getLoadedImages()){
            if(roiImg!=null ) {
                this.roiImageContainer.add(new CVRoiImageLoaderRow(this.controller, roiImg));
            }
        }
    }

    @Override
    public void update(CstmObservable o, Object arg) {
        this.refreshList();
        this.revalidate();
        this.repaint();
    }
}
