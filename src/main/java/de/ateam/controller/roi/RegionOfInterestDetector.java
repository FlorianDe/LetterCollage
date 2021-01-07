package de.ateam.controller.roi;

import de.ateam.controller.ICollageController;
import de.ateam.model.roi.RegionOfInterest;
import de.ateam.model.roi.RegionOfInterestImage;
import de.ateam.utils.FileLoader;
import de.ateam.utils.OpenCVUtils;
import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.*;
import java.awt.Point;
import java.awt.image.DataBufferByte;

/**
 * Created by Florian on 20.11.2015.
 */
public class RegionOfInterestDetector {
    public static final int SIMILAR_SAMPLER_RADIUS = 10;
    public static final float SIMILAR_THRESHOLD = 0.85f;
    public static final double WEIGHTING_SALIENCY_PIXEL = 3.0;
    private static final double WEIGHTING_EYE = 3.0;
    private static final double WEIGHTING_FACE = 2.0;
    private static final double WEIGHTING_FULLBODY = 1.5;
    private static final String haarcascades_frontalfaceString = "opencv/haarcascades/haarcascade_frontalface_default.xml";
    private static final String haarcascades_eyeStringString = "opencv/haarcascades/haarcascade_eye.xml";
    private static final String haarcascades_fullbodyStringString = "opencv/haarcascades/haarcascade_fullbody.xml";

    private static final CascadeClassifier frontalfaceDetector;
    private static final CascadeClassifier eyeDetector;
    private static final CascadeClassifier fullbodyDetector;

    ICollageController controller;

    static{
        frontalfaceDetector = FileLoader.loadCascadeFile(haarcascades_frontalfaceString);
        eyeDetector = FileLoader.loadCascadeFile(haarcascades_eyeStringString);
        fullbodyDetector = FileLoader.loadCascadeFile(haarcascades_fullbodyStringString);
    }

    public RegionOfInterestDetector(ICollageController controller){
        this.controller = controller;
    }

    public void cascadeRegognitionHelper(RegionOfInterestImage roiImage, CascadeClassifier cascadeClassifier, Color regionOfInterestColor, double threshold){
        cascadeRegognitionHelper(roiImage, cascadeClassifier, regionOfInterestColor, threshold, 1.0);
    }

    public void cascadeRegognitionHelper(RegionOfInterestImage roiImage, CascadeClassifier cascadeClassifier, Color regionOfInterestColor, double threshold, double weighting){
        if (!frontalfaceDetector.empty()) {
           // (new Thread() {
            //    public void run() {


                    Mat image = OpenCVUtils.bufferedImageToMat(roiImage.getNormalImage());
                    MatOfRect detections = new MatOfRect();
                    MatOfInt reject_levels = new MatOfInt();
                    MatOfDouble level_weights = new MatOfDouble();

                    cascadeClassifier.detectMultiScale3(image, detections, reject_levels, level_weights, 1.2, 3, 1, new Size(30,30), new Size(image.width(),image.height()), true);
                    System.out.println(String.format("Detected %s region(s)!", detections.toArray().length));

                    // Draw a bounding box around each detection.
                    Rect[] detectionArray = detections.toArray();
                    double[] level_weightsArray = level_weights.toArray();
                    for (int i = 0; i < detectionArray.length; i++) {
                        System.out.println("Percentage: "+level_weightsArray[i]);
                        if(level_weightsArray[i]>threshold)
                            roiImage.addRegionOfInterest(new Rectangle(detectionArray[i].x, detectionArray[i].y, detectionArray[i].width, detectionArray[i].height), regionOfInterestColor, weighting);
                    }

                    /*
                    for (Rect rect : detections.toArray()) {

                        roiImage.addRegionOfInterest(new Rectangle(rect.x, rect.y, rect.width, rect.height), regionOfInterestColor, weighting);
                    }
                    */
                    controller.getRoiModel().getRoiCollection().roiImageUpdated(roiImage);
                //}
            //}).start();
        } else {
            System.out.println("Detector [" + cascadeClassifier.toString() + "] is empty...cannot detect faces!");
        }
    }
    public void fullbodyRecognition(RegionOfInterestImage roiImage){
        cascadeRegognitionHelper(roiImage, fullbodyDetector,RegionOfInterest.COLOR_FULLBODY, 1.0, WEIGHTING_FULLBODY);
    }
    public void faceRecognition(RegionOfInterestImage roiImage){
        cascadeRegognitionHelper(roiImage,frontalfaceDetector,RegionOfInterest.COLOR_FACEDETECTION, 3.8, WEIGHTING_FACE);
    }
    public void eyeRecognition(RegionOfInterestImage roiImage){
        cascadeRegognitionHelper(roiImage,eyeDetector,RegionOfInterest.COLOR_EYEDETECTION, 1.7, WEIGHTING_EYE);
    }

    public Mat saliencyMapDetector(RegionOfInterestImage roiImage){
        SaliencyMapDetector smd = new SaliencyMapDetector();
        return smd.calculate(OpenCVUtils.bufferedImageToMat(roiImage.getNormalImage()),0.42);
    }

    public void similarDetection(RegionOfInterestImage roiImage, Point p) {
        long startTime = System.currentTimeMillis();
        // get Buffer
        byte[] pixels = ((DataBufferByte) roiImage.getNormalImage().getRaster().getDataBuffer()).getData();
        int width = roiImage.getNormalImage().getWidth();
        int height = roiImage.getNormalImage().getHeight();
        boolean[][] isChecked = new boolean[width][height];
        boolean pass = false;
        int samples = 0;

        float[] average = new float[3];


        // create Sample
        int xS = p.x + SIMILAR_SAMPLER_RADIUS;
        int yS = p.y + SIMILAR_SAMPLER_RADIUS;

        for(int x = p.x - SIMILAR_SAMPLER_RADIUS+1; x < xS; x++) {
            for(int y = p.y - SIMILAR_SAMPLER_RADIUS+1; y < yS; y++) {
                if(x >= 0 && y >= 0 && x < width && y < height){
                    float[] hsv = OpenCVUtils.getHSVPixel(pixels, x, y, width);
                    average[0] += hsv[0];
                    average[1] += hsv[1];
                    average[2] += hsv[2];
                    samples++;
                }
            }
        }
        if(samples != 0) {
            // durchschnitt
            average[0] /= samples;
            average[1] /= samples;
            average[2] /= samples;

            int lMax = p.x;
            int rMax = p.x;
            float difA = 1;
            float difB = 1;
            // kreiself*cker
            while(!pass) {
                if(lMax > 1 && difA > SIMILAR_THRESHOLD) {
                    difA = OpenCVUtils.hsvSimilarity(average, OpenCVUtils.getHSVPixel(pixels, lMax--, p.y, width));
                } else {
                    difA = 0;
                }
                if(rMax < width && difB > SIMILAR_THRESHOLD) {
                    difB = OpenCVUtils.hsvSimilarity(average, OpenCVUtils.getHSVPixel(pixels, rMax++, p.y, width));
                } else{
                    difB = 0;
                }
                if(difA < SIMILAR_THRESHOLD && difB < SIMILAR_THRESHOLD) {
                    pass = true;
                }
            }
            int yMin = p.y;
            int yMax = p.y;
            int tMax = p.y;
            int bMin = p.y;
            int tSam = 0;
            int bSam = 0;
            pass = false;
            samples = 0;
            difA = 1;
            difB = 1;
            for(int x = lMax; x < rMax; x++) {
                // top pass
                pass = false;
                while(!pass) {
                    if(tMax > 1 && difA > SIMILAR_THRESHOLD/2) {
                        difA = OpenCVUtils.hsvSimilarity(average, OpenCVUtils.getHSVPixel(pixels, x , tMax--, width));
                        if(tMax < yMin)
                            yMin = tMax;
                    } else {
                        difA = 0;
                    }
                    if(bMin < height && difB > SIMILAR_THRESHOLD/2) {
                        difB = OpenCVUtils.hsvSimilarity(average, OpenCVUtils.getHSVPixel(pixels, x, bMin++, width));
                        if(bMin > yMax)
                            yMax = bMin;
                    } else{
                        difB = 0;
                    }
                    if(difA < SIMILAR_THRESHOLD && difB < SIMILAR_THRESHOLD) {
                        pass = true;
                        tMax = p.y;
                        bMin = p.y;
                    }
                }
            }
            System.out.printf("yMax %d  yMin %d\n", yMax, yMin);
            roiImage.addRegionOfInterest(new Rectangle(lMax, yMin, rMax - lMax, yMax - yMin),RegionOfInterest.COLOR_SIMILARSELECTION);


            roiImage.repaintRoiImage();
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.printf("Calculationtime for magic wand: %d\n",elapsedTime);
        }
    }


}
