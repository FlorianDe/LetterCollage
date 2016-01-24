package main.java.de.ateam.model;

import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.utils.CstmObservable;
import main.java.de.ateam.utils.FileLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Florian on 13.11.2015.
 */
public class ResultImageModel extends CstmObservable {
    public enum MouseMode{
        DRAG, ZOOMIN, ZOOMOUT, DEFAULT, PAINT, ERASE, SETWEIGHT, SIMILAR_SELECT, POLYGONPAINT;
    }
    private MouseMode mouseMode;
    private RegionOfInterestImage endResultVisibleRoiImage;
    private RegionOfInterestImage actualVisibleRoiImage;
    private boolean resolutionRasterVisible;
    private boolean saliencyMapOverlay;
    private Rectangle viewRect;
    private Shape actualDrawnRoi;
    private ArrayList<Point> polygon;
    private int polygonSnapRadius = 10;
    private Color actualDrawColor;
    private int margin;
    private int maxWorker;
    private volatile AtomicInteger workerDone;

    public ResultImageModel(){
        mouseMode = MouseMode.SETWEIGHT;
        try {
            this.endResultVisibleRoiImage = new RegionOfInterestImage(ImageIO.read(FileLoader.loadFile("img/resultImage.png")));
            this.actualVisibleRoiImage = endResultVisibleRoiImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.viewRect = new Rectangle();
        this.actualDrawnRoi = null;
        this.actualDrawColor = Color.RED;
        this.margin = 0;
        this.polygon = new ArrayList<>();
        this.workerDone = new AtomicInteger(0);
    }

    public void clearPolygon() {
        this.polygon.clear();
        this.setActualDrawnRoi(null);
    }
    public void addPointToPolygon(Point p){
        this.polygon.add(p);
        Polygon drawPoly = new Polygon();
        for (Point point : polygon) {
            drawPoly.addPoint((int)point.getX(),(int)point.getY());
        }
        this.setActualDrawnRoi(drawPoly);
    }
    public ArrayList<Point> getPolygon() {
        return polygon;
    }

    public int getPolygonSnapRadius() {
        return polygonSnapRadius;
    }

    public void setPolygonSnapRadius(int polygonSnapRadius) {
        this.polygonSnapRadius = polygonSnapRadius;
        this.setChanged();
        this.notifyObservers(null);
    }

    public Rectangle getRealCoordinates(Rectangle rect){
        Rectangle rTemp = new Rectangle();
        double zF = getZoomFactor();
        rTemp.setRect((rect.getX()/zF), (rect.getY()/zF), (rect.getWidth()/zF), (rect.getHeight()/zF));
        return rTemp;
    }
    public Point getRealCoordinates(Point p){
        return new Point((int)(p.getX()/getZoomFactor()), (int)(p.getY()/getZoomFactor()));
    }

    public double getStrokeThickness(){
        return 1.0/getZoomFactor();
    }

    public Dimension getRenderSize(){
        if(actualVisibleRoiImage!=null && actualVisibleRoiImage.getVisualImage() != null)
            return new Dimension((int)(this.getActualVisibleImage().getWidth() * this.getZoomFactor()),
                                (int)(this.getActualVisibleImage().getHeight() * this.getZoomFactor()));
        return new Dimension(0,0);
    }

    public double getZoomFactor() {
        return this.actualVisibleRoiImage.getZoomFactor();
    }
    public void setZoomFactor(double zoomFactor) {
        this.actualVisibleRoiImage.setZoomFactor(zoomFactor);
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

    public Shape getActualDrawnRoi() {
        return actualDrawnRoi;
    }

    public void setActualDrawnRoi(Shape actualDrawnRoi) {
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

    public boolean isResolutionRasterVisible() {
        return resolutionRasterVisible;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
        this.setChanged();
        this.notifyObservers(null);
    }

    public AtomicInteger getWorkerDone() {
        return workerDone;
    }

    public void resetWorkerDone() {
        this.workerDone.set(0);
        this.setChanged();
        this.notifyObservers(null);
    }
    public int incrementtWorkerDone() {
        int workerDone = this.workerDone.incrementAndGet();
        this.setChanged();
        this.notifyObservers(null);
        return workerDone;
    }

    public int getMaxWorker() {
        return maxWorker;
    }

    public void setMaxWorker(int maxWorker) {
        this.maxWorker = maxWorker;
        this.setChanged();
        this.notifyObservers(null);
    }

    public void setResolutionRasterVisible(boolean resolutionRasterVisible) {
        this.resolutionRasterVisible = resolutionRasterVisible;
        this.setChanged();
        this.notifyObservers(null);
    }

    public boolean isSaliencyMapOverlay() {
        return saliencyMapOverlay;
    }

    public void setSaliencyMapOverlay(boolean saliencyMapOverlay) {
        this.saliencyMapOverlay = saliencyMapOverlay;
        this.setChanged();
        this.notifyObservers(null);
    }
}
