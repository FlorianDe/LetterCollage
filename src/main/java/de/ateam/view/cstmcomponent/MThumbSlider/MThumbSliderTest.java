package de.ateam.view.cstmcomponent.MThumbSlider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @version 1.0 9/3/99
 */
public class MThumbSliderTest extends JFrame {
    public MThumbSliderTest() {
        super("MThumbSlider Example");

        JSlider slider = new JSlider();
        slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider));

        int n = 2;
        MThumbSlider mSlider = new MThumbSlider(n);
        mSlider.setValueAt(25, 0);
        mSlider.setValueAt(75, 1);
        mSlider.setUI(new BasicMThumbSliderUI());

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(slider);
        getContentPane().add(mSlider);
    }

    public static void main(String args[]) {
        try {
            UIManager
                    .setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (Exception ex) {
            System.err.println("Error loading L&F: " + ex);
        }

        MThumbSliderTest f = new MThumbSliderTest();
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setSize(300, 100);
        f.show();
    }
}
