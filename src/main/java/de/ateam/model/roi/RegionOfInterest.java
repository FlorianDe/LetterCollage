package main.java.de.ateam.model.roi;

import java.awt.*;

/**
 * Created by Florian on 17.11.2015.
 */
public class RegionOfInterest {
    public static final Color DEFAULT_COLOR = Color.RED;
    public static final Color FACEDETECTION_COLOR = Color.GREEN;
    public static final Color EYEDETECTION_COLOR = Color.BLUE;
    public static final Color SIMILARSELECTION_COLOR = Color.MAGENTA;

    private Shape shape;
    private Color color;
    private double weighting;

    public RegionOfInterest(Shape shape){
        this(shape, DEFAULT_COLOR);
    }
    public RegionOfInterest(Shape shape, Color color){
        this(shape, color, 1.0);
    }
    public RegionOfInterest(Shape shape, Color color, double weighting){
        this.shape = shape;
        this.color = color;
        this.weighting = weighting;
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

    public double getWeighting() {
        return weighting;
    }

    public void setWeighting(double weighting) {
        this.weighting = weighting;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new RegionOfInterest(this.getShape(), this.getColor());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegionOfInterest that = (RegionOfInterest) o;

        return !(shape != null ? !shape.equals(that.shape) : that.shape != null);
    }

    @Override
    public int hashCode() {
        return shape != null ? shape.hashCode() : 0;
    }
}
