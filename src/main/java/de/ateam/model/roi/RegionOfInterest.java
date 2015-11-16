package main.java.de.ateam.model.roi;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by vspadi on 16.11.15.
 */
public class RegionOfInterest {
    private BufferedImage image;
    private BufferedImage debug;
    private Graphics2D g2d;
    private ArrayList<Rectangle> areas;

    protected RegionOfInterest(BufferedImage image) {
        this.image = image;
        this.debug = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        this.g2d = this.debug.createGraphics();
        this.g2d.drawImage(this.image, 0,0,null);
        this.areas = new ArrayList<>();
        findRegions();
    }

    public void findRegions() {
        // hier regions finden augen und so was, gesicht, alles einschließen was erstmal so interessant sein könnte
        // da müsste man gucken welche params man so abfucken kann

        repaintDebugImage();
    }

    public void addRegion(Rectangle rect) {
        // hier hab ich mir gedacht könnte man noch von extern regions einfügen /klick drag area markieren?

        repaintDebugImage();
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
        if(areas.contains(rect))
            areas.remove(rect);
        else {

        }

        repaintDebugImage();
    }

    // weitere funktionen könnten hier stehen um bsp selektion zu erweitern
    public Rectangle findRegionBySimilarity(Point point) {
        // klickste aufn punkt sucht er von dort aus mit schwellenwert in der umgebung die ähnlichen pixel
        // und macht aus den bounds ne region / woher schwellenwert? entweder in parameter oder in ne klasse reinpacken

        repaintDebugImage();
        return null;
    }

    private void repaintDebugImage() {
        this.g2d.drawImage(this.image, 0,0, null);
        this.g2d.setColor(Color.RED);
        this.g2d.drawRect(10,10, 20,20);
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public BufferedImage getDebugImage() {
        return this.debug;
    }
}
