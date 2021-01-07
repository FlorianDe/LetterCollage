package de.ateam.utils;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by Florian on 12.01.2016.
 */
public class ShapeUtils {

    public static final AlphaComposite TRANSPARENCY_25 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f);
    public static final AlphaComposite TRANSPARENCY_100 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);

    public static Ellipse2D getEllipseFromCenter(double x, double y, double width, double height) {
        double newX = x - width / 2.0;
        double newY = y - height / 2.0;
        Ellipse2D ellipse = new Ellipse2D.Double(newX, newY, width, height);
        return ellipse;
    }

    public static void setTransparency(Graphics2D g2d, float alpha) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }
}
