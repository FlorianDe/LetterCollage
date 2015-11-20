package main.java.de.ateam.controller.roi;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.roi.RegionOfInterest;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.model.text.Letter;
import main.java.de.ateam.utils.OpenCVUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import java.awt.image.BufferedImage;

/**
 * Created by Florian on 20.11.2015.
 */
public class RegionOfInterestCalculator {

    ICollageController controller;
    public RegionOfInterestCalculator(ICollageController controller) {
        this.controller = controller;
    }

    public void calculateIntersection(RegionOfInterestImage roiImage, Letter letter, double scale, int dX, int dY){
        long start = System.currentTimeMillis();
        Mat roiImageMat = roiImage.getCalculationMask();
        Mat letterMat = letter.getCalculationMask();
        double minAspectRatio = Math.max(letterMat.width() / roiImageMat.width(), letterMat.height() / roiImageMat.height());
        Rect subMatRect = new Rect( dX, dY, letterMat.width(), letterMat.height());
        Mat scaledCroppedRoiImageMat = new Mat( roiImageMat.setTo(new Scalar(minAspectRatio * scale)), subMatRect);


        int countBefore = Core.countNonZero(scaledCroppedRoiImageMat);
        Mat xoredMat = Mat.zeros(roiImageMat.rows(), roiImageMat.cols(), roiImageMat.type());
        Core.bitwise_xor(scaledCroppedRoiImageMat, letterMat, xoredMat);
        int countAfter = Core.countNonZero(xoredMat);

        //this.controller.getResultImageModel().setActualVisibleRoiImage(new RegionOfInterestImage(roiImage.getCalculationMaskHelper()));
        this.controller.getResultImageModel().setActualVisibleRoiImage(new RegionOfInterestImage(OpenCVUtils.matToBufferedImage(roiImageMat)));
        System.out.println("Time to calculateIntersection " + (System.currentTimeMillis()-start) + " ms");
        System.out.printf("letterMat: [W:%s, H:%s, type:%s], scaledCroppedRoiImageMat: [W:%s, H:%s, type:%s]\n", letterMat.width(), letterMat.height(), letterMat.type(), scaledCroppedRoiImageMat.width(), scaledCroppedRoiImageMat.height(), scaledCroppedRoiImageMat.type());
        System.out.printf("Count [Before:%s, After:%s] Percentage intersected:%s\n", countBefore,countAfter,(100.0/countBefore)*countAfter);
    }
}
