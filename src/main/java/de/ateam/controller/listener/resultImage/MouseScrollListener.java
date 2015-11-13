package main.java.de.ateam.controller.listener.resultImage;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.ResultImageModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Florian on 13.11.2015.
 */
public class MouseScrollListener extends MouseAdapter {
    Point pPressed;

    ICollageController controller;
    public MouseScrollListener(ICollageController controller){
        this.controller  = controller;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pPressed = new Point(e.getPoint());

        //System.out.printf("[mousePressed] X:%s  Y:%s\n", e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.controller.getResultImageModel().setMouseMode(ResultImageModel.MouseMode.NORMAL);
        //System.out.printf("[mouseReleased] X:%s  Y:%s\n", e.getX(), e.getY());
    }



    @Override
    public void mouseDragged(MouseEvent e) {
        if (pPressed != null) {
            this.controller.getResultImageModel().setMouseMode(ResultImageModel.MouseMode.DRAG);
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, (JComponent)e.getSource());
            if (viewPort != null) {
                int deltaX = pPressed.x - e.getX();
                int deltaY = pPressed.y - e.getY();

                Rectangle view = viewPort.getViewRect();
                view.x += deltaX;
                view.y += deltaY;

                //map.scrollRectToVisible(view);
                this.controller.getResultImageModel().setViewRect(view);
            }
        }
        //System.out.printf("[mouseDragged] X:%s  Y:%s\n", e.getX(), e.getY());
    }
}
