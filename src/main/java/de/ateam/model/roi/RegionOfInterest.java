package main.java.de.ateam.model.roi;

import java.awt.*;

/**
 * Created by Florian on 17.11.2015.
 */
public class RegionOfInterest {
    private static final Color DEFAULT_COLOR = Color.RED;

    private Shape shape;
    private Color color;

    public RegionOfInterest(Shape roi){
        this(roi, DEFAULT_COLOR);
    }

    public RegionOfInterest(Shape shape, Color color){
        this.shape = shape;
        this.color = color;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof RegionOfInterest))
            return false;

        RegionOfInterest other = (RegionOfInterest)obj;
        return this.getShape().getBounds2D().equals(other.getShape().getBounds2D());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new RegionOfInterest(this.getShape(), this.getColor());
    }
}
