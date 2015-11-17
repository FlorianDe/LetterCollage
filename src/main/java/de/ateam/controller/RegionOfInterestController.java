package main.java.de.ateam.controller;

import main.java.de.ateam.model.roi.RegionOfInterest;
import main.java.de.ateam.model.roi.RegionOfInterestImage;
import main.java.de.ateam.utils.OSUtils;
import main.java.de.ateam.utils.OpenCVUtils;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.*;
import java.awt.image.BufferedImage;
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


    private CascadeClassifier loadCascadeFile(String file){
        ClassLoader cl = this.getClass().getClassLoader();
        URL url = cl.getResource(file);
        if(url != null) {
            String cascadesFilePath = OSUtils.preparePathForOS(url.getPath());
            CascadeClassifier detector = new CascadeClassifier(cascadesFilePath);
            return detector;
        } else {
            System.out.println("Cannot find: "+file.toString());
        }
        return null;
    }
}
