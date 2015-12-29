package main.java.de.ateam.controller.roi;

/**
 * Created by Florian on 22.11.2015.
 */
public class CalculationResult {
    private double scaleFactor;
    private double relScaleFactor;
    private double dX;
    private double dY;
    private double intersectAreaPercentage;

    public double getScaleFactor() {
        return scaleFactor;
    }
    public double getdX() {
        return dX;
    }
    public double getdY() {
        return dY;
    }
    public double getIntersectAreaPercentage() {
        return intersectAreaPercentage;
    }
    public double getRelScaleFactor() {
        return relScaleFactor;
    }
    public static CalculationResult zero;

    static{
        zero = new CalculationResult(1.0,1.0,0,0,0.0);
    }

    public CalculationResult(double scaleFactor, double relScaleFactor, double dX, double dY, double intersectAreaPercentage) {
        this.scaleFactor = scaleFactor;
        this.relScaleFactor = relScaleFactor;
        this.dX = dX;
        this.dY = dY;
        this.intersectAreaPercentage = intersectAreaPercentage;
    }

    public static CalculationResult getZero(){
        return zero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalculationResult that = (CalculationResult) o;

        if (Double.compare(that.scaleFactor, scaleFactor) != 0) return false;
        if (Double.compare(that.dX, dX) != 0) return false;
        if (Double.compare(that.dY, dY) != 0) return false;
        return Double.compare(that.intersectAreaPercentage, intersectAreaPercentage) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(scaleFactor);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(intersectAreaPercentage);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "CalculationResult{" +
                "scaleFactor=" + scaleFactor +
                ", relScaleFactor=" + relScaleFactor +
                ", dX=" + dX +
                ", dY=" + dY +
                ", intersectAreaPercentage=" + intersectAreaPercentage +
                '}';
    }
}
