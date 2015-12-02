package main.java.de.ateam.controller.roi;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.model.text.Letter;
import main.java.de.ateam.model.text.LetterCollection;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;

/**
 * Created by Florian on 20.11.2015.
 */
public class RegionOfInterestCalculator {
    ArrayList<RegionOfInterestImage> roiImages;
    ArrayList<Letter> letters;

    Mat[] mat_roiImages;
    Mat[] mat_letters;
    ArrayList<CalculationResult>[][] calculations;

    ICollageController controller;

    public RegionOfInterestCalculator(ArrayList<RegionOfInterestImage> roiImages, ArrayList<Letter> letters, ICollageController controller) {
        this.roiImages = roiImages;
        this.letters = letters;
        this.controller = controller;

        //Retrieve all Mat objects!
        mat_roiImages = new Mat[roiImages.size()];
        for (int i = 0; i < roiImages.size(); i++) {
            mat_roiImages[i] = roiImages.get(i).getCalculationMask();
        }

        mat_letters = new Mat[letters.size()];
        for (int i = 0; i < letters.size(); i++) {
            mat_letters[i] = letters.get(i).getCalculationMask();
        }

        calculations = new ArrayList[mat_roiImages.length][mat_letters.length];
    }

    //TODO VERFAHREN EINBAUN FÜR BF!!! WAHRSCH EINFACH HOCHSKALIEREN UND IMMER UM PAAR PIXEL VERSCHIEBEN :)!
    public void calculateIntersectionMatrix(){
        long start = System.currentTimeMillis();
        int possibilities = 0;
        for (int roii_index = 0; roii_index < mat_roiImages.length; roii_index++) {
            for (int letter_index = 0; letter_index < mat_letters.length; letter_index++) {
                calculations[roii_index][letter_index] = new ArrayList<>();
                double maxAspectRatio = Math.max((double)mat_letters[letter_index].width() / (double)mat_roiImages[roii_index].width(), (double)mat_letters[letter_index].height() / (double)mat_roiImages[roii_index].height());
                for (double step_scaleFactor = 1.0; step_scaleFactor <= 4.0; step_scaleFactor=step_scaleFactor+1.0) {
                    int stepHeightMax = (int)(mat_roiImages[roii_index].height()*maxAspectRatio*step_scaleFactor)-mat_letters[letter_index].height();
                    for (int step_dy = 0; step_dy < stepHeightMax; step_dy+=step_scaleFactor) {
                        int stepWidthMax = (int)(mat_roiImages[roii_index].width()*maxAspectRatio*step_scaleFactor)-mat_letters[letter_index].width();
                        for (int step_dx = 0; step_dx < stepWidthMax; step_dx+=step_scaleFactor) {
                            calculations[roii_index][letter_index].add(calculateIntersection(mat_roiImages[roii_index], mat_letters[letter_index], maxAspectRatio, step_scaleFactor, step_dy, step_dx));
                            possibilities++;
                        }
                    }
                }
            }
        }
        System.out.println("[calculateIntersectionMatrix] Time:" + (System.currentTimeMillis() - start) + " ms   Possibilites:"+ possibilities);
    }


    //return the overlapped roi area in percentage/100
    public CalculationResult calculateIntersection(Mat mat_roiImage, Mat mat_letter, double maxAspectRatio, double scaleFactor, int dY, int dX){
        //long start = System.currentTimeMillis();
        //double maxAspectRatio = Math.max((double)mat_letter.width() / (double)mat_roiImage.width(), (double)mat_letter.height() / (double)mat_roiImage.height());
        Rect subMatRect = new Rect( dX, dY, mat_letter.width(), mat_letter.height());
        //System.out.println("[subMatRect] Y:" + subMatRect.y + "  X:" + subMatRect.x + "  H:" + subMatRect.height + "  H:" + subMatRect.height);
        Size newSize = new Size(mat_roiImage.width()*maxAspectRatio*scaleFactor, mat_roiImage.height()*maxAspectRatio * scaleFactor);
        //System.out.println("[newSize] W:" + newSize.width + "  H:" + newSize.height);
        Mat scaledCroppedRoiImageMat = new Mat();
        Imgproc.resize(mat_roiImage, scaledCroppedRoiImageMat, newSize);
        int countBefore = Core.countNonZero(scaledCroppedRoiImageMat);
        if(countBefore==0){
            return CalculationResult.getZero();
        }


        //System.out.println("[scaledCroppedRoiImageMat] W:" + scaledCroppedRoiImageMat.width() + "  H:" + scaledCroppedRoiImageMat.height());
        scaledCroppedRoiImageMat = new Mat(scaledCroppedRoiImageMat, subMatRect);
        Mat xoredMat = Mat.zeros(mat_roiImage.rows(), mat_roiImage.cols(), mat_roiImage.type());
        Core.bitwise_and(scaledCroppedRoiImageMat, mat_letter, xoredMat);
        int countAfter = Core.countNonZero(xoredMat);
        double percentage = 0.0;
        if(countAfter!=0 && countBefore!=0) {
            percentage = (double) countAfter / (double) countBefore;
        }


        //System.out.println("Time to calculateIntersection " + (System.currentTimeMillis() - start) + " ms");
        //System.out.printf("mat_letter: [W:%s, H:%s, type:%s], scaledCroppedRoiImageMat: [W:%s, H:%s, type:%s]\n", mat_letter.width(), mat_letter.height(), mat_letter.type(), scaledCroppedRoiImageMat.width(), scaledCroppedRoiImageMat.height(), scaledCroppedRoiImageMat.type());
        //System.out.printf("Count [Before:%s, After:%s] Percentage intersected:%s\n", countBefore, countAfter, (100.0 / countBefore) * countAfter);
        //this.controller.getResultImageModel().setActualVisibleRoiImage(new RegionOfInterestImage(OpenCVUtils.matToBufferedImage(xoredMat)));

        return new CalculationResult(scaleFactor,(maxAspectRatio*scaleFactor*((double)LetterCollection.LETTER_SIZE/(double)LetterCollection.SAMPLER_SIZE)), dX, dY, percentage);
    }

    public CalculationResult getBestResultsForImageLeter(int image, int letter){
        CalculationResult crRet = CalculationResult.getZero();
        for (CalculationResult cr : calculations[image][letter]){
            if(crRet.getIntersectAreaPercentage() < cr.getIntersectAreaPercentage()){
                crRet = cr;
            }
        }
        return crRet;
    }

    public Mat[] getMat_letters() {
        return mat_letters;
    }

    public Mat[] getMat_roiImages() {
        return mat_roiImages;
    }
}
