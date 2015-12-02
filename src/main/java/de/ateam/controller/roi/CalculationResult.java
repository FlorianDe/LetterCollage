package main.java.de.ateam.controller.roi;

/**
 * Created by Florian on 22.11.2015.
 */
public class CalculationResult {
    private double scaleFactor;
    private double relScaleFactor;
    private int dX;
    private int dY;
    private double intersectAreaPercentage;

    public double getScaleFactor() {
        return scaleFactor;
    }
    public int getdX() {
        return dX;
    }
    public int getdY() {
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

    public CalculationResult(double scaleFactor, double relScaleFactor, int dX, int dY, double intersectAreaPercentage) {
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
        if (dX != that.dX) return false;
        if (dY != that.dY) return false;
        return Double.compare(that.intersectAreaPercentage, intersectAreaPercentage) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(scaleFactor);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + dX;
        result = 31 * result + dY;
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
