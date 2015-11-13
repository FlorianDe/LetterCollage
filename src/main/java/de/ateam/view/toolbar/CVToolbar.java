package main.java.de.ateam.view.toolbar;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.listener.resultImage.ZoomInListener;
import main.java.de.ateam.controller.listener.resultImage.ZoomOutListener;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;
import main.java.de.ateam.utils.FileLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVToolbar extends JToolBar implements CstmObserver {
    private JButton btnNew;
    private JButton btnOpen;
    private JButton btnZoomIn;
    private JButton btnZoomOut;

    ICollageController controller;
    public CVToolbar(ICollageController controller){
        this.controller = controller;
        this.createToolbar();
        this.controller.getResultImageModel().addObserver(this);
    }

    public void createToolbar(){
        this.btnNew = (JButton) createToolbarButton(new JButton(), "img/New.gif");
        this.add(this.btnNew);

        this.btnOpen = (JButton) createToolbarButton(new JButton(), "img/Open.gif");
        this.add(this.btnOpen);

        this.addSeparator();

        this.btnZoomIn = (JButton) createToolbarButton(new JButton(), "img/ZoomIn.gif");
        this.btnZoomIn.addActionListener(new ZoomInListener(this.controller));
        this.add(this.btnZoomIn);

        this.btnZoomOut = (JButton) createToolbarButton(new JButton(), "img/ZoomOut.gif");
        this.btnZoomOut.addActionListener(new ZoomOutListener(this.controller));
        this.add(this.btnZoomOut);
    }

    public AbstractButton createToolbarButton(AbstractButton btn, String filePath){
        try {
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(FileLoader.loadFile(filePath)));
            btn.setIcon(imageIcon);
        } catch (Exception e) {
            String str = filePath.replace("img/", "");
            btn.setText(str.substring(0, str.indexOf(".")).toUpperCase());
        }
        finally{
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        }
        return btn;
    }


    @Override
    public void update(CstmObservable arg0, Object arg1) {
        this.revalidate();
        this.repaint();
    }
}
