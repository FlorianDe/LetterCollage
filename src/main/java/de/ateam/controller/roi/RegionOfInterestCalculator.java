package main.java.de.ateam.controller.roi;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.roi.RegionOfInterest;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.model.text.Letter;
import main.java.de.ateam.utils.OpenCVUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Florian on 20.11.2015.
 */
public class RegionOfInterestCalculator {
    ArrayList<RegionOfInterestImage> roiImages;
    ArrayList<Letter> letters;

    ArrayList<Mat> mat_roiImages;
    ArrayList<Mat> mat_letters;

    ICollageController controller;

    public RegionOfInterestCalculator(ArrayList<RegionOfInterestImage> roiImages, ArrayList<Letter> letters, ICollageController controller) {
        this.roiImages = roiImages;
        this.letters = letters;
        this.controller = controller;

        //Retrieve all Mat objects!
        mat_roiImages = new ArrayList<>();
        for(RegionOfInterestImage roii : roiImages){
            mat_roiImages.add(roii.getCalculationMask());
        }
        mat_letters = new ArrayList<>();
        for(Letter l : letters){
            mat_letters.add(l.getCalculationMask());
        }
    }

    public void calculateIntersectionMatrix(){
        long start = System.currentTimeMillis();
        for(Mat mat_roiImage : mat_roiImages){
            for(Mat mat_letter : mat_letters) {
                //TODO VERFAHREN EINBAUN FÜR BF!!! WAHRSCH EINFACH HOCHSKALIEREN UND IMMER UM PAAR PIXEL VERSCHIEBEN :)!
                System.out.print(calculateIntersection(mat_roiImage, mat_letter, 1.0, 0, 0)+", ");
            }
            System.out.println();
        }
        System.out.println("calculateIntersectionMatrix " + (System.currentTimeMillis() - start) + " ms");
    }


    //return the overlapped roi area in percentage/100
    public double calculateIntersection(Mat mat_roiImage, Mat mat_letter, double scale, int dX, int dY){
        //long start = System.currentTimeMillis();
        double minAspectRatio = Math.max((double)mat_letter.width() / (double)mat_roiImage.width(), (double)mat_letter.height() / (double)mat_roiImage.height());
        Rect subMatRect = new Rect( dX, dY, mat_letter.width(), mat_letter.height());
        //System.out.println("[subMatRect] Y:" + subMatRect.y + "  X:" + subMatRect.x + "  H:" + subMatRect.height + "  H:" + subMatRect.height);
        Size newSize = new Size(mat_roiImage.width()*minAspectRatio*scale, mat_roiImage.height()*minAspectRatio * scale);
        //System.out.println("[newSize] W:" + newSize.width + "  H:" + newSize.height);
        Mat scaledCroppedRoiImageMat = new Mat();
        Imgproc.resize(mat_roiImage, scaledCroppedRoiImageMat, newSize);
        //System.out.println("[scaledCroppedRoiImageMat] W:" + scaledCroppedRoiImageMat.width() + "  H:" + scaledCroppedRoiImageMat.height());
        scaledCroppedRoiImageMat = new Mat(scaledCroppedRoiImageMat, subMatRect);

        int countBefore = Core.countNonZero(scaledCroppedRoiImageMat);
        Mat xoredMat = Mat.zeros(mat_roiImage.rows(), mat_roiImage.cols(), mat_roiImage.type());
        Core.bitwise_and(scaledCroppedRoiImageMat, mat_letter, xoredMat);
        int countAfter = Core.countNonZero(xoredMat);


        //System.out.println("Time to calculateIntersection " + (System.currentTimeMillis() - start) + " ms");
        //System.out.printf("mat_letter: [W:%s, H:%s, type:%s], scaledCroppedRoiImageMat: [W:%s, H:%s, type:%s]\n", mat_letter.width(), mat_letter.height(), mat_letter.type(), scaledCroppedRoiImageMat.width(), scaledCroppedRoiImageMat.height(), scaledCroppedRoiImageMat.type());
        //System.out.printf("Count [Before:%s, After:%s] Percentage intersected:%s\n", countBefore, countAfter, (100.0 / countBefore) * countAfter);
        //this.controller.getResultImageModel().setActualVisibleRoiImage(new RegionOfInterestImage(OpenCVUtils.matToBufferedImage(xoredMat)));

        return (double)countAfter/(double)countBefore;
    }
}
