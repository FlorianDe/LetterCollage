package main.java.de.ateam.controller.roi;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.exception.NoFontSelectedException;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.model.text.Letter;
import main.java.de.ateam.utils.OpenCVUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Florian on 17.11.2015.
 */
public class RegionOfInterestController {
    ICollageController controller;
    RegionOfInterestDetector roiDetector;
    RegionOfInterestCalculator roic;

    ArrayList<Letter> letters = new ArrayList<>();
    HashMap<Integer, Integer> imMapLetter = new HashMap<>();


    public RegionOfInterestCalculator getRoic() {
        return roic;
    }

    public RegionOfInterestDetector getRoiDetector() {
        return roiDetector;
    }

    public void setRoiDetector(RegionOfInterestDetector roiDetector) {
        this.roiDetector = roiDetector;
    }

    public RegionOfInterestController(ICollageController controller){
        this.controller = controller;
        this.roiDetector = new RegionOfInterestDetector(controller);
    }

    public void initProgressbar(){
        controller.getResultImageModel().getWorkerDone().set(0);
        controller.getResultImageModel().setMaxWorker(this.controller.getRoiModel().getLoadedImages().size()*this.controller.getRoiModel().getInputText().toCharArray().length*(int)((controller.getRoiModel().getScaleEnd() - controller.getRoiModel().SCALE_START)/controller.getRoiModel().getScaleStepSize()));
    }

    public void calculateROIsAutomatic(){
        for(RegionOfInterestImage roiImage : this.controller.getRoiModel().getRoiCollection().getRoiImages()){
            roiImage.resetAllROIs();

            if(controller.getResultImageModel().isSaliencyMapDetection()) {
                roiImage.setSaliencyMap(OpenCVUtils.matToBufferedImage(roiDetector.saliencyMapDetector(roiImage)));
            }
            if(controller.getResultImageModel().isEyeDetection()) {
                roiDetector.eyeRecognition(roiImage);
            }
            if(controller.getResultImageModel().isFaceDetection()) {
                roiDetector.faceRecognition(roiImage);
            }
            if(controller.getResultImageModel().isFullbodyDetection()) {
                roiDetector.fullbodyRecognition(roiImage);
            }
        }
    }

    public void calculateSolutionForCollage(){
        try{
            letters = new ArrayList<>();
            try {
                for (Character c : this.controller.getRoiModel().getInputText().toCharArray()) {
                    letters.add(this.controller.getRoiModel().getLetterCollection().getLetter(c));
                }
            }
            catch(NullPointerException npExce){
                throw new NoFontSelectedException();
            }
            for (Map.Entry<Character, Letter> entry : controller.getRoiModel().getLetterCollection().getLetterMap().entrySet()) {
                Letter letter = entry.getValue();
                letter.setOutlineResultMask(controller.getRoiModel().getLetterCollection().calculateResultBorders(letter.getSymbol(), controller.getResultImageModel().getFontOutlineColor(), controller.getResultImageModel().getFontOutlineThickness()));
            }

            this.roic = new RegionOfInterestCalculator(this.controller.getRoiModel().getLoadedImages(), letters, controller);
            this.roic.calculateIntersectionMatrixParallel();


            //RegionOfInterestCalculator.curCount.set(0);
            imMapLetter = new HashMap<>();

            double maxSum = 0;
            for(int i = 0; i < roic.getLetters().size(); i++) {
                double sum = 0;
                HashMap<Integer, Integer> mapLetter = new HashMap<>();
                for (int letterCounter = i; letterCounter < roic.getLetters().size()+i; letterCounter++){
                    CalculationResult calcRes = CalculationResult.getZero();
                    Integer tempNumber = 0;
                    for (int imgCounter = 0; imgCounter < roic.getRoiImages().size(); imgCounter++){
                        if(!mapLetter.containsValue(imgCounter) && imgCounter<roic.getLetters().size()) {
                            CalculationResult tempCalcRes = roic.getBestResultsForImageLeter(imgCounter, letterCounter % roic.getLetters().size());
                            if (calcRes.getWeightedPercentage() <= tempCalcRes.getWeightedPercentage()) {
                                sum += tempCalcRes.getWeightedPercentage();
                                calcRes = tempCalcRes;
                                tempNumber = imgCounter;
                            }
                        }
                    }
                    mapLetter.put(letterCounter % roic.getLetters().size(),tempNumber);
                }
                if(sum > maxSum) {
                    maxSum = sum;
                    imMapLetter = mapLetter;
                }
            }

            drawResult();

        }
        catch(NoFontSelectedException nfsExce){
            this.controller.getRoiModel().notifyViewFromController();
        }
    }


    public void drawResult(){
        int width = 0;
        for (int i = 0; i <letters.size(); i++) {
            width+=letters.get(i).getResultMask().getWidth();
        }
        BufferedImage bi_finalImage = new BufferedImage(width, this.controller.getRoiModel().getLetterCollection().getLETTER_SIZE(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d_finalImage = bi_finalImage.createGraphics();
        g2d_finalImage.setColor(controller.getResultImageModel().getBackgroundColor());

        //g2d_finalImage.setColor(Color.WHITE);
        g2d_finalImage.fillRect(0, 0, bi_finalImage.getWidth(), bi_finalImage.getHeight());
        int xOffset = 0;
        for (int i = 0; i < letters.size(); i++) {
            BufferedImage letterImgBuf = letters.get(i).getResultMask();
            BufferedImage letterImgBufCopy = new BufferedImage(letterImgBuf.getWidth(), letterImgBuf.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2dLetterImgBufCopy = letterImgBufCopy.createGraphics();
            CalculationResult tempCalcRes = roic.getBestResultsForImageLeter(imMapLetter.get(i), i);
            System.out.println(tempCalcRes);
            BufferedImage bufImg = this.controller.getRoiModel().getLoadedImages().get(imMapLetter.get(i)).getNormalImage();
            double maxAspectRatio = Math.max((double) letterImgBuf.getWidth() / (double) bufImg.getWidth(), (double) letterImgBuf.getHeight() / (double) bufImg.getHeight());
				/*
				g2dLetterImgBufCopy.drawImage(bufImg,
						-(int) ((double) tempCalcRes.getdX() * ((double)LetterCollection.LETTER_SIZE/LetterCollection.SAMPLER_SIZE)),
						-(int) ((double) tempCalcRes.getdY() * ((double)LetterCollection.LETTER_SIZE/LetterCollection.SAMPLER_SIZE)),
						(int) (bufImg.getWidth() * (maxAspectRatio * tempCalcRes.getScaleFactor())),
						(int) (bufImg.getHeight() * (maxAspectRatio * tempCalcRes.getScaleFactor())),
						null);
				*/
            double newWidth = bufImg.getWidth() * (maxAspectRatio * tempCalcRes.getScaleFactor());
            double newHeight = bufImg.getHeight() * (maxAspectRatio * tempCalcRes.getScaleFactor());

            g2dLetterImgBufCopy.drawImage(bufImg,
                    -(int) (tempCalcRes.getdX() * newWidth),
                    -(int) (tempCalcRes.getdY() * newHeight),
                    (int)newWidth,
                    (int)newHeight,
                    null);
            byte[] alphaPixels = ((DataBufferByte)letterImgBuf.getRaster().getDataBuffer()).getData();
            byte[] colorPixels = ((DataBufferByte)letterImgBufCopy.getRaster().getDataBuffer()).getData();

            for (int j = 0; j < colorPixels.length; j=j+4) {
                colorPixels[j] = alphaPixels[j/4];
            }
            g2d_finalImage.drawImage(letterImgBufCopy, xOffset, 0, null);
            g2d_finalImage.drawImage(letters.get(i).getOutlineResultMask(), xOffset, 0, null);
            xOffset+=letterImgBuf.getWidth();
        }

        RegionOfInterestImage roiImageNew = new RegionOfInterestImage(bi_finalImage);
        roiImageNew.setZoomFactor(this.controller.getResultImageModel().getEndResultRoiImage().getZoomFactor());
        this.controller.getResultImageModel().setEndResultRoiImage(roiImageNew);
        this.controller.getResultImageModel().setActualVisibleRoiImage(this.controller.getResultImageModel().getEndResultVisibleRoiImage());
        System.out.println(imMapLetter.toString());
    }

}
