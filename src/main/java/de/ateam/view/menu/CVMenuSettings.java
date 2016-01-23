package main.java.de.ateam.view.menu;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.controller.listener.resultImage.MouseModeSetListener;
import main.java.de.ateam.model.ResultImageModel;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.CstmObserver;
import main.java.de.ateam.view.cstmcomponent.JSliderMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Florian on 13.11.2015.
 */
public class CVMenuSettings extends JMenu  implements CstmObserver {

    JMenuExtension jme;
    JMenuItem menuItemSettings;

    ICollageController controller;

    public CVMenuSettings(String name, ICollageController controller){
        super(name);
        this.controller = controller;
        this.controller.getResultImageModel().addObserver(this);
        this.jme = new JMenuExtension(ActionEvent.CTRL_MASK);
        this.jme.setInformationJM(this, "STRING_DESCRIPTION");


        this.menuItemSettings = this.jme.createJMenuItem(new JMenuItem("Settings"), 'I', "STRING_DESCRIPTION", this);
        this.menuItemSettings.addActionListener(new MouseModeSetListener(controller, ResultImageModel.MouseMode.ZOOMIN));

        //JSliderMenuItem jsmi = new JSliderMenuItem("Scale end", controller, this);
        JLabel heading = new JLabel(name);

        JSlider slider = new JSlider();
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(10);

        this.addSeparator();
        this.add(heading);
        this.add(slider);

        this.addSeparator();

        this.add(menuItemSettings);


    }

    @Override
    public void update(CstmObservable o, Object arg) {}
}
