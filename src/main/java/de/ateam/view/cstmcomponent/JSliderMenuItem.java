package de.ateam.view.cstmcomponent;

import de.ateam.controller.ICollageController;

import javax.swing.*;

/**
 * Created by Florian on 23.01.2016.
 */
public class JSliderMenuItem extends JMenuItem {

    JLabel heading;
    JSlider slider;

    ICollageController controller;

    public JSliderMenuItem(String name, ICollageController controller, JMenu jmenu) {
        this.controller = controller;
        this.heading = new JLabel(name);

        this.slider = new JSlider();
        this.slider.setMajorTickSpacing(20);
        this.slider.setMinorTickSpacing(10);

        jmenu.add(heading);
        jmenu.add(slider);
    }
}