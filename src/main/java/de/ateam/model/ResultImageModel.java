package main.java.de.ateam.model;

import main.java.de.ateam.model.roi.RegionOfInterestImage;
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
    private RegionOfInterestImage endResultVisibleRoiImage;
    private RegionOfInterestImage actualVisibleRoiImage;
    public Rectangle viewRect;

    public ResultImageModel(){
        mouseMode = MouseMode.DRAG;
        try {
            this.endResultVisibleRoiImage = new RegionOfInterestImage(ImageIO.read(FileLoader.loadFile("img/resultImage.png")));
            this.actualVisibleRoiImage = endResultVisibleRoiImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.viewRect = new Rectangle();
        this.zoomFactor = 1;
    }


    public Dimension getRenderSize(){
        if(actualVisibleRoiImage!=null && actualVisibleRoiImage.getVisualImage() != null)
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

    public RegionOfInterestImage getEndResultRoiImage() {
        return endResultVisibleRoiImage;
    }
    public void setEndResultRoiImage(RegionOfInterestImage resultImage) {
        this.endResultVisibleRoiImage = resultImage;
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

    public RegionOfInterestImage getActualVisibleRoiImage() {
        return actualVisibleRoiImage;
    }

    public BufferedImage getActualVisibleImage() {
        return actualVisibleRoiImage.getVisualImage();
    }

    public void setActualVisibleRoiImage(RegionOfInterestImage actualVisibleRoiImage) {
        this.actualVisibleRoiImage = actualVisibleRoiImage;
        this.setChanged();
        this.notifyObservers(null);
    }
}
