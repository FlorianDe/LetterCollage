package main.java.de.ateam.model.roi;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by vspadi on 16.11.15.
 */
public class RegionOfInterestImage {
    private BufferedImage normalImage;
    private BufferedImage roiImage;
    private Graphics2D g2dRoiImage;
    private ArrayList<Rectangle> rois;
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

    public void addRegion(Rectangle rect) {
        // hier hab ich mir gedacht könnte man noch von extern regions einfügen /klick drag area markieren?

        repaintRoiImage();
    }

    public ArrayList<Rectangle> getRegion(Point point) {
        // die funktion soll auf einen punkt der man ihr gibt alle regions zurückgeben,
        // damit man ggf ne referenz löschen kann um eine Region zu löschen

        return null;
    }

    public ArrayList<Rectangle> getRegion(Rectangle rect) {
        // hier soll man mit einer region referenzen auf intersecting regions bekommen,
        // bsp: markieren und alle markierten löschen oder so ODER sogar sowas wie "Besondere wichtigkeit"
        // verschieben oder was weiß ich

        return null;
    }

    public void deleteRegion(Rectangle rect) {
        // wenn ne referenz kommt, lösch sie sonst such alle regions auf intersection ab und lösch die
        if(rois.contains(rect))
            rois.remove(rect);
        else {
            //TODO WHAT?
        }

        repaintRoiImage();
    }

    // weitere funktionen könnten hier stehen um bsp selektion zu erweitern
    public Rectangle findRegionBySimilarity(Point point) {
        // klickste aufn punkt sucht er von dort aus mit schwellenwert in der umgebung die ähnlichen pixel
        // und macht aus den bounds ne region / woher schwellenwert? entweder in parameter oder in ne klasse reinpacken

        repaintRoiImage();
        return null;
    }

    private void repaintRoiImage() {
        this.g2dRoiImage.drawImage(this.normalImage, 0, 0, null);
        this.g2dRoiImage.setColor(Color.RED);
        this.g2dRoiImage.drawRect(10, 10, 20, 20);
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
