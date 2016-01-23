package main.java.de.ateam.view.menu;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.listener.resultImage.MouseModeSetListener;
import main.java.de.ateam.model.ResultImageModel;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenuView extends JMenu  implements CstmObserver {

    JMenuExtension jme;
    JMenuItem menuItemMouseModeZoomIn;
    JMenuItem menuItemMouseModeZoomOut;

    ICollageController controller;

    public CVMenuView(String name, ICollageController controller){
        super(name);
        this.controller = controller;
        this.controller.getResultImageModel().addObserver(this);
        this.jme = new JMenuExtension(ActionEvent.CTRL_MASK);
        this.jme.setInformationJM(this, "STRING_DESCRIPTION");


        this.menuItemMouseModeZoomIn = this.jme.createJMenuItem(new JMenuItem("ZoomIn"), 'I', "STRING_DESCRIPTION", this);
        this.menuItemMouseModeZoomIn.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.ZOOMIN));

        this.menuItemMouseModeZoomOut = this.jme.createJMenuItem(new JMenuItem("ZoomOut"), 'O', "STRING_DESCRIPTION", this);
        this.menuItemMouseModeZoomOut.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.ZOOMOUT));
    }

    @Override
    public void update(CstmObservable o, Object arg) {
        this.menuItemMouseModeZoomIn.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.ZOOMIN));
        this.menuItemMouseModeZoomOut.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.ZOOMOUT));
    }
}
