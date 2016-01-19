package main.java.de.ateam.controller.roi;

/**
 * Created by Florian on 22.11.2015.
 */
public class CalculationResult implements Comparable{
    private double scaleFactor;
    private double dX;
    private double dY;
    private double intersectAreaPercentage;
    private double weight;
    private double weightedPercentage;

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
    public double getWeight() { return weight; }
    public static CalculationResult zero;

    static{
        zero = new CalculationResult(1.0,0,0,0.0,0.0);
    }

    public CalculationResult(double scaleFactor, double dX, double dY, double intersectAreaPercentage, double weight) {
        this.scaleFactor = scaleFactor;
        this.dX = dX;
        this.dY = dY;
        this.intersectAreaPercentage = intersectAreaPercentage;
        this.weightedPercentage = intersectAreaPercentage+(0.2*weight);
        this.weight = weight;
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
                ", dX=" + dX +
                ", dY=" + dY +
                ", intersectAreaPercentage=" + intersectAreaPercentage +
                ", weight=" + weight +
                ", weightedPercentage=" + weightedPercentage +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        CalculationResult c = (CalculationResult)o;
        if(c.weightedPercentage > ((CalculationResult) o).weightedPercentage) {
            return -1;
        } else if(c.weightedPercentage < ((CalculationResult) o).weightedPercentage) {
            return 1;
        } else {
            return 0;
        }
    }

    public double getWeightedPercentage() {
        return weightedPercentage;
    }
}
