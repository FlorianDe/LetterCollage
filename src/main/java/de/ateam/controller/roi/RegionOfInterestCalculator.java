package main.java.de.ateam.controller.roi;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.model.text.Letter;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Florian on 20.11.2015.
 */
public class RegionOfInterestCalculator {
    ArrayList<RegionOfInterestImage> roiImages;
    ArrayList<Letter> letters;

    /*
    Mat[] mat_roiImages;
    Mat[] mat_letters;
    */
    ArrayList<CalculationResult>[][] calculations;

    ICollageController controller;

    public RegionOfInterestCalculator(ArrayList<RegionOfInterestImage> roiImages, ArrayList<Letter> letters, ICollageController controller) {
        this.roiImages = roiImages;
        this.letters = letters;
        this.controller = controller;

        calculations = new ArrayList[roiImages.size()][letters.size()];
    }

    //TODO VERFAHREN EINBAUN FÜR BF!!! WAHRSCH EINFACH HOCHSKALIEREN UND IMMER UM PAAR PIXEL VERSCHIEBEN :)!
    public void calculateIntersectionMatrixParallel(){
        long start = System.currentTimeMillis();
        int possibilities = 0;

        ExecutorService executor = Executors.newFixedThreadPool(Math.min(Runtime.getRuntime().availableProcessors() + 1, roiImages.size()*letters.size()));
        List<Future<CalculationResultList>> futures = new ArrayList<>();
        for (int roii_index = 0; roii_index < roiImages.size(); roii_index++) {
            for (int letter_index = 0; letter_index < letters.size(); letter_index++) {
                Callable<CalculationResultList> callable = new CalculationCallable(this.controller, roii_index,letter_index, roiImages.get(roii_index), letters.get(letter_index));
                Future<CalculationResultList> future = executor.submit(callable);
                futures.add(future);
            }
        }
        for(Future<CalculationResultList> future : futures){
            try {
                CalculationResultList crl = future.get();
                crl.add(CalculationResult.getZero());
                calculations[crl.getImgIndex()][crl.getLetterIndex()] = crl;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.MINUTES);
            if (!executor.isTerminated()) {
                throw new InterruptedException("Timeout!");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(new Throwable().getStackTrace()[0].getClassName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("[calculateIntersectionMatrix] Time:" + (System.currentTimeMillis() - start) + " ms   Possibilites:"+ possibilities);
    }

    /*
    public void calculateIntersectionMatrixSequential(){
        long start = System.currentTimeMillis();
        int possibilities = 0;
        for (int roii_index = 0; roii_index < mat_roiImages.length; roii_index++) {
            for (int letter_index = 0; letter_index < mat_letters.length; letter_index++) {
                calculations[roii_index][letter_index] = new ArrayList<>();
                double maxAspectRatio = Math.max((double)mat_letters[letter_index].width() / (double)mat_roiImages[roii_index].width(), (double)mat_letters[letter_index].height() / (double)mat_roiImages[roii_index].height());
                //System.out.printf("\nImg:%s, Letter:%s, Scale:", roii_index, letter_index);
                for (double step_scaleFactor = 1.0; step_scaleFactor <= 4.0; step_scaleFactor=step_scaleFactor+0.25) {
                    //System.out.print(step_scaleFactor + ", ");
                    int stepHeightMax = (int)(mat_roiImages[roii_index].height()*maxAspectRatio*step_scaleFactor)-mat_letters[letter_index].height();
                    for (int step_dy = 0; step_dy < stepHeightMax; step_dy+=step_scaleFactor) {
                        int stepWidthMax = (int)(mat_roiImages[roii_index].width()*maxAspectRatio*step_scaleFactor)-mat_letters[letter_index].width();
                        for (int step_dx = 0; step_dx < stepWidthMax; step_dx+=step_scaleFactor) {
                            calculations[roii_index][letter_index].add(calculateIntersection(mat_roiImages[roii_index], mat_letters[letter_index], maxAspectRatio, step_scaleFactor, step_dx, step_dy));
                            possibilities++;
                        }
                    }
                }
            }
        }
        System.out.println("[calculateIntersectionMatrix] Time:" + (System.currentTimeMillis() - start) + " ms   Possibilites:"+ possibilities);
    }
    */


    //return the overlapped roi area in percentage/100
    public static CalculationResult calculateIntersection(Mat mat_roiImage, Mat mat_saliencyMap, Mat mat_letter, double maxAspectRatio, double scaleFactor, int dX, int dY, Point roiCenter){
        //long start = System.currentTimeMillis();

        if(roiCenter==null){
            return CalculationResult.getZero();
        }

        Rect subMatRect = new Rect( dX, dY, mat_letter.width(), mat_letter.height());
        Point actCenter = new Point((int)(dX+(mat_letter.width()/maxAspectRatio/scaleFactor/2)), (int)(dY+(mat_letter.height()/maxAspectRatio/scaleFactor / 2)));
        int dH = (int)((roiCenter.getY()>mat_roiImage.height()/2)?roiCenter.getY():(mat_roiImage.height()-roiCenter.getY()));
        int dW = (int)((roiCenter.getX()>mat_roiImage.width()/2)?roiCenter.getX():(mat_roiImage.width()-roiCenter.getX()));
        int maxDistance = (int)Math.sqrt(Math.pow(dW,2)+Math.pow(dH,2));
        int actDistance = (int)Math.sqrt(Math.pow(Math.abs(actCenter.getX()-roiCenter.getX()),2)+Math.pow(Math.abs(actCenter.getY()-roiCenter.getY()),2));

        Size newSize = new Size(mat_roiImage.width()*maxAspectRatio*scaleFactor, mat_roiImage.height()*maxAspectRatio * scaleFactor);
        Mat scaledCroppedRoiImageMat = new Mat();
        Imgproc.resize(mat_roiImage, scaledCroppedRoiImageMat, newSize);

        Mat scaledCroppedSaliencyMap = new Mat();
        Imgproc.resize(mat_saliencyMap, scaledCroppedSaliencyMap, newSize);


        int countBefore = Core.countNonZero(scaledCroppedRoiImageMat)+Core.countNonZero(scaledCroppedSaliencyMap);
        if(countBefore==0){
            return CalculationResult.getZero();
        }


        scaledCroppedRoiImageMat = new Mat(scaledCroppedRoiImageMat, subMatRect);
        Mat xoredRoiImageMat = Mat.zeros(mat_roiImage.rows(), mat_roiImage.cols(), mat_roiImage.type());
        Core.bitwise_and(scaledCroppedRoiImageMat, mat_letter, xoredRoiImageMat);


        scaledCroppedSaliencyMap = new Mat(scaledCroppedSaliencyMap, subMatRect);
        Mat xoredSaliencyMapMat = Mat.zeros(mat_saliencyMap.rows(), mat_saliencyMap.cols(), mat_saliencyMap.type());
        Core.bitwise_and(scaledCroppedSaliencyMap, mat_letter, xoredSaliencyMapMat);


        int countAfter = Core.countNonZero(xoredRoiImageMat)+Core.countNonZero(xoredSaliencyMapMat);
        double percentage = 0.0;
        if(countAfter!=0 && countBefore!=0) {
            percentage = (double) countAfter / (double) countBefore;
        }


        //System.out.println("Time to calculateIntersection " + (System.currentTimeMillis() - start) + " ms");
        //System.out.printf("mat_letter: [W:%s, H:%s, type:%s], scaledCroppedRoiImageMat: [W:%s, H:%s, type:%s]\n", mat_letter.width(), mat_letter.height(), mat_letter.type(), scaledCroppedRoiImageMat.width(), scaledCroppedRoiImageMat.height(), scaledCroppedRoiImageMat.type());
        //System.out.printf("Count [Before:%s, After:%s] Percentage intersected:%s\n", countBefore, countAfter, (100.0 / countBefore) * countAfter);
        //this.controller.getResultImageModel().setActualVisibleRoiImage(new RegionOfInterestImage(OpenCVUtils.matToBufferedImage(xoredMat)));
        //return new CalculationResult(scaleFactor,(maxAspectRatio*scaleFactor*((double)this.controller.getRoiModel().getLetterCollection().getLETTER_SIZE()/(double)this.controller.getRoiModel().getLetterCollection().getSAMPLER_SIZE())), dX/newSize.width, dY/newSize.height, percentage);
        return new CalculationResult(scaleFactor, dX/newSize.width, dY/newSize.height, percentage, 1.0-(double)actDistance/maxDistance);
    }

    public CalculationResult getBestResultsForImageLeter(int image, int letter){
        CalculationResult crRet = CalculationResult.getZero();
        for (CalculationResult cr : calculations[image][letter]){
            if(crRet.getWeightedPercentage() < cr.getWeightedPercentage()){
                crRet = cr;
            }
        }
        return crRet;
    }

    public ArrayList<CalculationResult>[][] getCalculationResults() {
        return this.calculations;
    }

    public ArrayList<Letter> getLetters() {
        return letters;
    }

    public ArrayList<RegionOfInterestImage> getRoiImages() {
        return roiImages;
    }
}
