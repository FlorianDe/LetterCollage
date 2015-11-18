package main.java.de.ateam.controller;

import main.java.de.ateam.model.roi.RegionOfInterest;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.utils.OSUtils;
import main.java.de.ateam.utils.OpenCVUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.*;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.net.URL;

/**
 * Created by Florian on 17.11.2015.
 */
public class RegionOfInterestController {
    private static final String haarcascades_frontalfaceString = "opencv/haarcascades/haarcascade_frontalface_default.xml";
    private static final String haarcascades_eyeStringString = "opencv/haarcascades/haarcascade_eye.xml";

    private CascadeClassifier frontalfaceDetector;
    private CascadeClassifier eyeDetector;

    ICollageController controller;

    public RegionOfInterestController(ICollageController controller){
        this.controller = controller;

        this.frontalfaceDetector = loadCascadeFile(haarcascades_frontalfaceString);
        this.eyeDetector = loadCascadeFile(haarcascades_eyeStringString);
    }

    public void faceRecognition(RegionOfInterestImage roiImage){
        if (!frontalfaceDetector.empty()) {
            MatOfRect faceDetections = new MatOfRect();
            Mat image = OpenCVUtils.bufferedImageToMat(roiImage.getNormalImage());

            frontalfaceDetector.detectMultiScale(image, faceDetections);
            System.out.println(String.format("Detected %s face(s)!", faceDetections.toArray().length));

            // Draw a bounding box around each face.
            for (Rect rect : faceDetections.toArray()) {
                roiImage.addRegionOfInterest(new Rectangle(rect.x, rect.y, rect.width, rect.height), RegionOfInterest.FACEDETECTION_COLOR);
            }
            this.controller.getRoiModel().getRoiCollection().roiImageUpdated(roiImage);
        } else {
            System.out.println("Facedetector is empty...cannot detect faces!");
        }
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
        int xS = p.x + RegionOfInterest.SIMILAR_SAMPLER_RADIUS;
        int yS = p.y + RegionOfInterest.SIMILAR_SAMPLER_RADIUS;

        for(int x = p.x - RegionOfInterest.SIMILAR_SAMPLER_RADIUS+1; x < xS; x++) {
            for(int y = p.y - RegionOfInterest.SIMILAR_SAMPLER_RADIUS+1; y < yS; y++) {
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
                if(lMax > 1 && difA > RegionOfInterest.SIMILAR_THRESHOLD) {
                    difA = OpenCVUtils.hsvSimilarity(average, OpenCVUtils.getHSVPixel(pixels, lMax--, p.y, width));
                } else {
                    difA = 0;
                }
                if(rMax < width && difB > RegionOfInterest.SIMILAR_THRESHOLD) {
                    difB = OpenCVUtils.hsvSimilarity(average, OpenCVUtils.getHSVPixel(pixels, rMax++, p.y, width));
                } else{
                    difB = 0;
                }
                if(difA < RegionOfInterest.SIMILAR_THRESHOLD && difB < RegionOfInterest.SIMILAR_THRESHOLD) {
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
                    if(tMax > 1 && difA > RegionOfInterest.SIMILAR_THRESHOLD/2) {
                        difA = OpenCVUtils.hsvSimilarity(average, OpenCVUtils.getHSVPixel(pixels, x , tMax--, width));
                        if(tMax < yMin)
                            yMin = tMax;
                    } else {
                        difA = 0;
                    }
                    if(bMin < height && difB > RegionOfInterest.SIMILAR_THRESHOLD/2) {
                        difB = OpenCVUtils.hsvSimilarity(average, OpenCVUtils.getHSVPixel(pixels, x, bMin++, width));
                        if(bMin > yMax)
                            yMax = bMin;
                    } else{
                        difB = 0;
                    }
                    if(difA < RegionOfInterest.SIMILAR_THRESHOLD && difB < RegionOfInterest.SIMILAR_THRESHOLD) {
                        pass = true;
                        tMax = p.y;
                        bMin = p.y;
                    }
                }
            }
            System.out.printf("yMax %d  yMin %d\n", yMax, yMin);
            roiImage.addRegionOfInterest(new Rectangle(lMax, yMin, rMax - lMax, yMax - yMin));


            roiImage.repaintRoiImage();
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.printf("Calculationtime for magic wand: %d\n",elapsedTime);
        }
    }

    private CascadeClassifier loadCascadeFile(String file){
        ClassLoader cl = this.getClass().getClassLoader();
        URL url = cl.getResource(file);
        if(url != null) {
            String cascadesFilePath = OSUtils.preparePathForOS(url.getPath());
            CascadeClassifier detector = new CascadeClassifier(cascadesFilePath);
            System.out.println(detector);
            return detector;
        } else {
            System.out.println("Cannot find: "+file.toString());
        }
        return null;
    }
}
