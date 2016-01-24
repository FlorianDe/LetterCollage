package main.java.de.ateam.model.roi;

import main.java.de.ateam.controller.roi.RegionOfInterestDetector;
import main.java.de.ateam.utils.OpenCVUtils;
import main.java.de.ateam.utils.ShapeUtils;
import org.opencv.core.Mat;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by vspadi on 16.11.15.
 */
public class RegionOfInterestImage{

    private BufferedImage normalImage;
    private BufferedImage roiImage;
    private BufferedImage saliencyMap;
    private Graphics2D g2dRoiImage;
    private ArrayList<RegionOfInterest> rois;
    private boolean roiMode;
    private double zoomFactor;
    private Point middlePoint;

    public RegionOfInterestImage(BufferedImage normalImage) {
        this.roiMode = true;
        this.normalImage = normalImage;
        this.saliencyMap = new BufferedImage(normalImage.getWidth(), normalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        this.roiImage = new BufferedImage(normalImage.getWidth(), normalImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        this.g2dRoiImage = this.roiImage.createGraphics();
        this.g2dRoiImage.drawImage(this.normalImage, 0, 0, null);
        this.rois = new ArrayList<>();
        this.zoomFactor = 1.0;
    }

    public void addRegionOfInterest(Shape shape, Color color, double weighting) {
        // hier hab ich mir gedacht könnte man noch von extern regions einfügen /klick drag area markieren?
        rois.add(new RegionOfInterest(shape, color, weighting));
        calculateCenterWeight();
        repaintRoiImage();
    }
    public void addRegionOfInterest(Shape shape, Color color) {
        addRegionOfInterest(shape, color, 1.0);
    }
    public void addRegionOfInterest(Shape shape) {
        this.addRegionOfInterest(shape, RegionOfInterest.COLOR_DEFAULT);
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
        calculateCenterWeight();
        repaintRoiImage();
    }

    public synchronized void deleteRegionOfInterest(RegionOfInterest roi) {
        if (rois.contains(roi)){
            rois.remove(roi);
        }
        calculateCenterWeight();
        repaintRoiImage();
    }

    // weitere funktionen könnten hier stehen um bsp selektion zu erweitern
    public Rectangle findRegionBySimilarity(Point point) {
        calculateCenterWeight();
        repaintRoiImage();
        return null;
    }

    //TODO SOLLTE ER EIGTL NICHT TUN, SELBER ZEICHNEN, ABER BESSER FÜR PERFORMANCE!
    public void repaintRoiImage() {
        int strokeThickness = 3;
        int fontConstHeight = 10;
        this.g2dRoiImage.drawImage(this.normalImage, 0, 0, null);
        this.g2dRoiImage.setStroke(new BasicStroke(strokeThickness));
        for(RegionOfInterest roi : rois){
            this.g2dRoiImage.setColor(roi.getColor());
            Rectangle2D r = roi.getShape().getBounds2D();
            this.g2dRoiImage.drawString(roi.getWeighting()+"",(int)r.getX()+strokeThickness,(int)r.getY()+strokeThickness+fontConstHeight);
            this.g2dRoiImage.draw(roi.getShape());

            ShapeUtils.setTransparency(g2dRoiImage, 0.25f);
            this.g2dRoiImage.fill(roi.getShape());
            ShapeUtils.setTransparency(g2dRoiImage, 1f);
        }
        if(middlePoint != null) {
            this.g2dRoiImage.setColor(Color.GREEN);
            this.g2dRoiImage.draw(ShapeUtils.getEllipseFromCenter(this.middlePoint.x, this.middlePoint.y, 10,10));
        }
    }

    public void calculateCenterWeight() {
        BufferedImage mask = getCalculationMaskHelper();

        int xMax = 0;
        int xMin = -1;
        int yMax = 0;
        int yMin = -1;
        double xAvg = 0;
        double yAvg = 0;
        double cnt = 0;
        for(int y = 0; y < mask.getHeight(); y++) {
            for(int x = 0; x < mask.getWidth(); x++) {
                if((mask.getRGB(x,y)) == -1) {
                    double wAvg = 1;
                    ArrayList<RegionOfInterest> rois = this.getIntersectingRegionOfInterests(new Point(x,y));
                    if(rois.size() > 0) {
                        for(RegionOfInterest roi: rois) {
                            wAvg += roi.getWeighting();
                        }
                        wAvg /= rois.size();
                    }

                    if(x > xMax) xMax = x;
                    if(xMin == -1) xMin = x;
                    if(y > yMax) yMax = y;
                    if(yMin == -1) yMin = y;
                    xAvg += x*wAvg;
                    yAvg += y*wAvg;
                    cnt+=wAvg;
                }
                if((this.getSaliencyMap().getRGB(x,y)) == -1) {
                    if(x > xMax) xMax = x;
                    if(xMin == -1) xMin = x;
                    if(y > yMax) yMax = y;
                    if(yMin == -1) yMin = y;
                    xAvg += x* RegionOfInterestDetector.WEIGHTING_SALIENCY_PIXEL;
                    yAvg += y* RegionOfInterestDetector.WEIGHTING_SALIENCY_PIXEL;
                    cnt+=RegionOfInterestDetector.WEIGHTING_SALIENCY_PIXEL;
                }
            }
        }
        if(cnt != 0) {
            xAvg /= cnt;
            yAvg /= cnt;
            this.middlePoint = new Point((int)xAvg, (int)yAvg);
        } else {
            this.middlePoint = null;
        }
    }

    public BufferedImage getCalculationMaskHelper() {
        BufferedImage roiMask = new BufferedImage(roiImage.getWidth(),roiImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = roiMask.createGraphics();
        g2d.setColor(Color.WHITE);
        for(RegionOfInterest roi : rois){
            g2d.fill(roi.getShape());
        }
        g2d.dispose();
        return roiMask;
    }

    public Mat getCalculationMask() {
        //TODO HIER IEIN DUMMER FEHLER BEI DER KONVERTIERUNG?!
        BufferedImage roiMask = getCalculationMaskHelper();
        Mat mat = OpenCVUtils.bufferedImageToMat(roiMask);

        //Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        return mat;
    }

    public ArrayList<RegionOfInterest> getRois() {
        return rois;
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

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public Point getMiddlePoint() {
        return this.middlePoint;
    }

    public BufferedImage getSaliencyMap() {
        return saliencyMap;
    }

    public void setSaliencyMap(BufferedImage saliencyMap) {
        this.saliencyMap = saliencyMap;
        calculateCenterWeight();
        repaintRoiImage();
    }
}
