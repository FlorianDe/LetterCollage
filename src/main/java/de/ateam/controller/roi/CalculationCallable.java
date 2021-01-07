package de.ateam.controller.roi;

import de.ateam.controller.ICollageController;
import de.ateam.model.roi.RegionOfInterestImage;
import de.ateam.model.text.Letter;
import de.ateam.utils.OpenCVUtils;
import org.opencv.core.Mat;

import java.awt.*;
import java.util.concurrent.Callable;


/**
 * Created by Florian on 12.01.2016.
 */
public class CalculationCallable implements Callable<CalculationResultList> {
    private Mat mat_roiImage;
    private Mat mat_saliencyMap;
    private Mat mat_letter;
    Point roiCenter;
    private CalculationResultList calculationResultList;
    ICollageController controller;

    public CalculationCallable(ICollageController controller, int imgIndex, int letterIndex, RegionOfInterestImage roiImage, Letter letter){
        this.controller = controller;
        this.calculationResultList = new CalculationResultList(imgIndex, letterIndex);
        this.mat_roiImage = roiImage.getCalculationMask();
        //System.out.println("mat_roiImage Channels:"+this.mat_roiImage.channels());
        this.mat_saliencyMap = OpenCVUtils.bufferedImageToMat(roiImage.getSaliencyMap());
        //System.out.println("mat_saliencyMap Channels:"+this.mat_saliencyMap.channels());
        this.mat_letter = letter.getCalculationMask();
        //System.out.println("mat_letter Channels:"+this.mat_letter.channels());
        this.roiCenter = roiImage.getMiddlePoint();
    }

    @Override
    public CalculationResultList call() throws Exception {
        final double maxAspectRatio = Math.max((double)mat_letter.width() / (double)mat_roiImage.width(), (double)mat_letter.height() / (double)mat_roiImage.height());

        for (double step_scaleFactor = controller.getRoiModel().SCALE_START; step_scaleFactor <= controller.getRoiModel().getScaleEnd(); step_scaleFactor=step_scaleFactor+controller.getRoiModel().getScaleStepSize()) {
            //System.out.print(step_scaleFactor + ", ");
            int stepHeightMax = (int)(mat_roiImage.height()*maxAspectRatio*step_scaleFactor)-mat_letter.height();
            int stepWidthMax = (int)(mat_roiImage.width()*maxAspectRatio*step_scaleFactor)-mat_letter.width();
            for (int step_dy = 0; step_dy < stepHeightMax; step_dy+=step_scaleFactor) {
                for (int step_dx = 0; step_dx < stepWidthMax; step_dx+=step_scaleFactor) {
                    CalculationResult cr = RegionOfInterestCalculator.calculateIntersection(mat_roiImage, mat_saliencyMap, mat_letter, maxAspectRatio, step_scaleFactor, step_dx, step_dy, roiCenter);
                    if(cr.getIntersectAreaPercentage() > 0.0) {
                        calculationResultList.add(cr);
                    }
                }
            }
            controller.getResultImageModel().incrementtWorkerDone();
            //System.out.println("Percentage: " + (100*controller.getResultImageModel().incrementtWorkerDone())/(double)controller.getResultImageModel().getMaxWorker());
        }
        return calculationResultList;
    }
}
