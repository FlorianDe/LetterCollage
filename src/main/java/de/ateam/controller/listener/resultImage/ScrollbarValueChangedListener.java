package main.java.de.ateam.controller.listener.resultImage;

import main.java.de.ateam.controller.ICollageController;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * Created by Florian on 17.11.2015.
 */
public class ScrollbarValueChangedListener implements AdjustmentListener {

    protected ICollageController controller;
    protected JViewport viewPort;

    public ScrollbarValueChangedListener(ICollageController controller, JViewport viewPort) {
        this.controller = controller;
        this.viewPort = viewPort;
    }


    public void adjustmentValueChanged(AdjustmentEvent e) {
        //Adjustable source = e.getAdjustable();
        if (e.getValueIsAdjusting()) {
            return;
        }
        this.controller.getResultImageModel().setViewRect(viewPort.getViewRect());



        /*
        int orient = source.getOrientation();
        if (orient == Adjustable.HORIZONTAL) {
            System.out.println("from horizontal scrollbar");
        } else {
            System.out.println("from vertical scrollbar");
        }
        int type = evt.getAdjustmentType();
        switch (type) {
            case AdjustmentEvent.UNIT_INCREMENT:
                System.out.println("Scrollbar was increased by one unit");
                break;
            case AdjustmentEvent.UNIT_DECREMENT:
                System.out.println("Scrollbar was decreased by one unit");
                break;
            case AdjustmentEvent.BLOCK_INCREMENT:
                System.out.println("Scrollbar was increased by one block");
                break;
            case AdjustmentEvent.BLOCK_DECREMENT:
                System.out.println("Scrollbar was decreased by one block");
                break;
            case AdjustmentEvent.TRACK:
                System.out.println("The knob on the scrollbar was dragged");
                break;
        }
        int value = evt.getValue();
        */
    }
}