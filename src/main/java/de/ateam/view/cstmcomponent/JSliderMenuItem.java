package main.java.de.ateam.view.cstmcomponent;

import main.java.de.ateam.controller.ICollageController;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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