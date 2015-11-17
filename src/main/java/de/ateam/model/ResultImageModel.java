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
        DRAG, ZOOMIN, ZOOMOUT, DEFAULT, PAINT, ERASE, SETWEIGHT, SIMILAR_SELECT;
    }
    private MouseMode mouseMode;
    private double zoomFactor;
    private RegionOfInterestImage endResultVisibleRoiImage;
    private RegionOfInterestImage actualVisibleRoiImage;
    private Rectangle viewRect;
    private Rectangle actualDrawnRoi;
    private Color actualDrawColor;


    public ResultImageModel(){
        mouseMode = MouseMode.SETWEIGHT;
        try {
            this.endResultVisibleRoiImage = new RegionOfInterestImage(ImageIO.read(FileLoader.loadFile("img/resultImage.png")));
            this.actualVisibleRoiImage = endResultVisibleRoiImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.viewRect = new Rectangle();
        this.zoomFactor = 1;
        this.actualDrawnRoi = null;
        this.actualDrawColor = Color.RED;
    }


    public Rectangle getRealCoordinates(Rectangle rect){
        Rectangle rTemp = new Rectangle();
        rTemp.setRect((rect.getX() / zoomFactor), (rect.getY() / zoomFactor), (rect.getWidth() / zoomFactor), (rect.getHeight() / zoomFactor));
        return rTemp;
    }
    public Point getRealCoordinates(Point p){
        return new Point((int)(p.getX()/zoomFactor), (int)(p.getY()/zoomFactor));
    }

    public double getStrokeThickness(){
        return 1.0/zoomFactor;
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

    public Rectangle getActualDrawnRoi() {
        return actualDrawnRoi;
    }

    public void setActualDrawnRoi(Rectangle actualDrawnRoi) {
        this.actualDrawnRoi = actualDrawnRoi;
        this.setChanged();
        this.notifyObservers(null);
    }

    public RegionOfInterestImage getEndResultVisibleRoiImage() {
        return endResultVisibleRoiImage;
    }
    public void setEndResultVisibleRoiImage(RegionOfInterestImage endResultVisibleRoiImage) {
        this.endResultVisibleRoiImage = endResultVisibleRoiImage;
    }

    public Color getActualDrawColor() {
        return actualDrawColor;
    }

    public void setActualDrawColor(Color actualDrawColor) {
        this.actualDrawColor = actualDrawColor;
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
