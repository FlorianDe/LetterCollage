package main.java.de.ateam.model;

import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.FileLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Florian on 13.11.2015.
 */
public class ResultImageModel extends CstmObservable {
    public enum MouseMode{
        DRAG, ZOOMIN, ZOOMOUT, NORMAL;
    }
    private MouseMode mouseMode;
    private double zoomFactor;
    private BufferedImage resultImage;
    private BufferedImage actualVisibleImage;
    public Rectangle viewRect;

    public ResultImageModel(){
        mouseMode = MouseMode.DRAG;
        try {
            this.resultImage = ImageIO.read(FileLoader.loadFile("img/resultImage.png"));
            this.actualVisibleImage = resultImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.viewRect = new Rectangle();
        this.zoomFactor = 1;
    }


    public Dimension getRenderSize(){
        if(resultImage!=null)
            return new Dimension((int)(this.getActualVisibleImage().getWidth() * this.getZoomFactor()),
                                (int)(this.getActualVisibleImage().getHeight() * this.getZoomFactor()));
        return new Dimension(0,0);
    }

    public double getZoomFactor() {
        return zoomFactor;
    }
    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        this.setChanged();
        this.notifyObservers(null);
    }
    public MouseMode getMouseMode() {
        return mouseMode;
    }
    public void setMouseMode(MouseMode mouseMode) {
        this.mouseMode = mouseMode;
        this.setChanged();
        this.notifyObservers(null);
    }
    public BufferedImage getResultImage() {
        return resultImage;
    }
    public void setResultImage(BufferedImage resultImage) {
        this.resultImage = resultImage;
        this.setChanged();
        this.notifyObservers(null);
    }
    public Rectangle getViewRect() {
        return viewRect;
    }
    public void setViewRect(Rectangle viewRect) {
        this.viewRect = viewRect;
        this.setChanged();
        this.notifyObservers(null);
    }

    public BufferedImage getActualVisibleImage() {
        return actualVisibleImage;
    }

    public void setActualVisibleImage(BufferedImage actualVisibleImage) {
        this.actualVisibleImage = actualVisibleImage;
        this.setChanged();
        this.notifyObservers(null);
    }
}
