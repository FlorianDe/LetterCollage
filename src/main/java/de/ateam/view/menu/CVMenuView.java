package de.ateam.view.menu;

import de.ateam.controller.ICollageController;
import de.ateam.controller.listener.resultImage.GridOnOffListener;
import de.ateam.controller.listener.resultImage.SaliencyOverlayOnOffListener;
import de.ateam.controller.listener.resultImage.ZoomInListener;
import de.ateam.controller.listener.resultImage.ZoomOutListener;
import de.ateam.model.ResultImageModel;
import de.ateam.utils.CstmObservable;
import de.ateam.utils.CstmObserver;
import de.ateam.view.cstmcomponent.StayOpenCheckBoxMenuItemUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenuView extends JMenu implements CstmObserver {

    JMenuExtension jme;
    JMenuItem menuItemMouseModeZoomIn;
    JMenuItem menuItemMouseModeZoomOut;
    JCheckBoxMenuItem menuItemRasterOnOff;
    JCheckBoxMenuItem menuItemSaliencyOverlayOnOff;

    ICollageController controller;

    public CVMenuView(String name, ICollageController controller) {
        super(name);
        this.controller = controller;
        this.controller.getResultImageModel().addObserver(this);
        this.jme = new JMenuExtension(ActionEvent.CTRL_MASK);
        this.jme.setInformationJM(this, "STRING_DESCRIPTION");


        this.menuItemRasterOnOff = new JCheckBoxMenuItem("Raster On/Off");
        this.menuItemRasterOnOff.addItemListener(new GridOnOffListener(controller));
        menuItemRasterOnOff.setUI(new StayOpenCheckBoxMenuItemUI());
        this.add(menuItemRasterOnOff);

        this.menuItemSaliencyOverlayOnOff = new JCheckBoxMenuItem("Saliency Overlay On/Off");
        this.menuItemSaliencyOverlayOnOff.addItemListener(new SaliencyOverlayOnOffListener(controller));
        menuItemSaliencyOverlayOnOff.setUI(new StayOpenCheckBoxMenuItemUI());
        this.add(menuItemSaliencyOverlayOnOff);

        this.addSeparator();

        this.menuItemMouseModeZoomIn = this.jme.createJMenuItem(new JMenuItem("Zoom in"), 'O', "STRING_DESCRIPTION", this);
        this.menuItemMouseModeZoomIn.addActionListener(new ZoomInListener(controller));

        this.menuItemMouseModeZoomOut = this.jme.createJMenuItem(new JMenuItem("Zoom out"), 'O', "STRING_DESCRIPTION", this);
        this.menuItemMouseModeZoomOut.addActionListener(new ZoomOutListener(controller));


    }

    @Override
    public void update(CstmObservable o, Object arg) {
        this.menuItemMouseModeZoomIn.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.ZOOMIN));
        this.menuItemMouseModeZoomOut.setEnabled(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.ZOOMOUT));
    }
}
