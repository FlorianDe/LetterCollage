package main.java.de.ateam.model.roi;

import main.java.de.ateam.utils.CstmObservable;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by vspadi on 16.11.15.
 */
public class RegionOfInterestImage{

    private BufferedImage normalImage;
    private BufferedImage roiImage;
    private Graphics2D g2dRoiImage;
    private ArrayList<RegionOfInterest> rois;
    private boolean roiMode;

    public RegionOfInterestImage(BufferedImage normalImage) {
        this.roiMode = true;
        this.normalImage = normalImage;
        this.roiImage = new BufferedImage(normalImage.getWidth(), normalImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        this.g2dRoiImage = this.roiImage.createGraphics();
        this.g2dRoiImage.drawImage(this.normalImage, 0, 0, null);
        this.rois = new ArrayList<>();
        findRegions();
    }

    public void findRegions() {
        // hier regions finden augen und so was, gesicht, alles einschließen was erstmal so interessant sein könnte
        // da müsste man gucken welche params man so abfucken kann

        repaintRoiImage();
    }

    public void addRegionOfInterest(Rectangle rect, Color color) {
        // hier hab ich mir gedacht könnte man noch von extern regions einfügen /klick drag area markieren?
        rois.add(new RegionOfInterest(rect, color));
        repaintRoiImage();
    }

    public void addRegionOfInterest(Rectangle rect) {
        this.addRegionOfInterest(rect, RegionOfInterest.DEFAULT_COLOR);
    }

    public ArrayList<RegionOfInterest> getIntersectingRegionOfInterests(Point point) {
        ArrayList<RegionOfInterest> selectedRois = new ArrayList<>();
        for(RegionOfInterest roi : rois){
            if(roi.getShape().contains(point)) {
                selectedRois.add(roi);
            }
        }
        return selectedRois;
    }

    public ArrayList<RegionOfInterest> getIntersectingRegionOfInterests(Rectangle rect) {
        ArrayList<RegionOfInterest> selectedRois = new ArrayList<>();
        for(RegionOfInterest r : rois){
            if(r.getShape().intersects(rect)) {
                selectedRois.add(r);
            }
        }
        return selectedRois;
    }

    public synchronized void deleteRegionOfInterests(ArrayList<RegionOfInterest> rois){
        for(RegionOfInterest roi : rois){
            this.rois.remove(roi);
        }
        repaintRoiImage();
    }

    public  synchronized void deleteRegionOfInterest(RegionOfInterest roi) {
        // wenn ne referenz kommt, lösch sie sonst such alle regions auf intersection ab und lösch die
        if (rois.contains(roi)){
            rois.remove(roi);
        }
        repaintRoiImage();
    }

    // weitere funktionen könnten hier stehen um bsp selektion zu erweitern
    public Rectangle findRegionBySimilarity(Point point) {
        repaintRoiImage();
        return null;
    }

    //TODO SOLLTE ER EIGTL NICHT TUN, SELBER ZEICHNEN :/!
    public void repaintRoiImage() {
        int strokeThickness = 3;
        int fontConstHeight = 10;
        this.g2dRoiImage.drawImage(this.normalImage, 0, 0, null);
        this.g2dRoiImage.setStroke(new BasicStroke(strokeThickness));
        for(RegionOfInterest roi : rois){
            this.g2dRoiImage.setColor(roi.getColor());
            Rectangle2D r = roi.getShape().getBounds2D();
            this.g2dRoiImage.drawString(roi.getWeighting()+"",(int)r.getX()+strokeThickness,(int)r.getY()+strokeThickness+fontConstHeight);
            this.g2dRoiImage.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
        }
    }

    public BufferedImage getNormalImage() {
        return this.normalImage;
    }

    public BufferedImage getRoiImage() {
        return this.roiImage;
    }

    public boolean isRoiMode() {
        return roiMode;
    }

    public void setRoiMode(boolean roiMode) {
        this.roiMode = roiMode;
    }

    public BufferedImage getVisualImage(){
        if(isRoiMode())
            return getRoiImage();
        return getNormalImage();
    }
}
