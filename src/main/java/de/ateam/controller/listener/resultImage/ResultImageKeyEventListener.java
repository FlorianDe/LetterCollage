package main.java.de.ateam.controller.listener.resultImage;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.ResultImageModel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Florian on 17.11.2015.
 */
public class ResultImageKeyEventListener  implements KeyListener {
    //ResultImageModel.MouseMode lastMouseMode;

    ICollageController controller;
    public ResultImageKeyEventListener(ICollageController controller){
        this.controller  = controller;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //this.lastMouseMode = this.controller.getResultImageModel().getMouseMode();
        if(e.getKeyCode() == KeyEvent.VK_E){
            this.controller.getResultImageModel().setMouseMode(ResultImageModel.MouseMode.ERASE);
        }
        else if(e.getKeyCode() == KeyEvent.VK_B){
            this.controller.getResultImageModel().setMouseMode(ResultImageModel.MouseMode.PAINT);
        }
        else if(e.getKeyCode() == KeyEvent.VK_O){
            this.controller.getResultImageModel().setMouseMode(ResultImageModel.MouseMode.ZOOMIN);
        }
        else if(e.getKeyCode() == KeyEvent.VK_I){
            this.controller.getResultImageModel().setMouseMode(ResultImageModel.MouseMode.ZOOMOUT);
        }
        else if(e.getKeyCode() == KeyEvent.VK_D){
            this.controller.getResultImageModel().setMouseMode(ResultImageModel.MouseMode.DEFAULT);
        }
        //System.out.printf("[keyPressed] getExtendedKeyCode:%s, getModifiersEx:%s\n", e.getExtendedKeyCode(), e.getModifiersEx());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //this.controller.getResultImageModel().setMouseMode(lastMouseMode);
        //System.out.printf("[keyReleased] getExtendedKeyCode:%s, getModifiersEx:%s\n", e.getExtendedKeyCode(), e.getModifiersEx());
    }



    @Override
    public void keyTyped(KeyEvent e) {

        //System.out.printf("[keyTyped] getExtendedKeyCode:%s, getModifiersEx:%s, getKeyCode:%s\n", e.getExtendedKeyCode(), e.getModifiersEx(),e.getKeyCode());
    }
}
