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

        //this.controller.getImageLoaderModel().addObserver(this);
        this.controller.getCollageModel().addObserver(this);
        refreshList();
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
    public void refreshList(){
        this.imageContainer.removeAll();
        /*
        for(BufferedImage buf : this.controller.getImageLoaderModel().getLoadedImages()){
            if(buf!=null) {
                this.imageContainer.add(new CVImageLoaderRow(this.controller, buf));
            }
        }
        */
        for(BufferedImage buf : this.controller.getCollageModel().getLoadedImages()){
            if(buf!=null) {
                this.imageContainer.add(new CVImageLoaderRow(this.controller, buf));
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
