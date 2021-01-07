package de.ateam;

import de.ateam.utils.OpenCVUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;

public class Benchmarks {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Benchmarks bm = new Benchmarks();
        Mat mat = bm.retrieveMat();
        long startTime, ms;
        int steps = 100;
        BufferedImage bf;

        startTime = System.currentTimeMillis();
        for (int i = 0; i < steps; i++) {
            bf = OpenCVUtils.matToBufferedImage(mat);
        }
        ms = (System.currentTimeMillis() - startTime);
        System.out.println(ms + " ms for " + steps + " steps!");
        System.out.println("Test1 FPS:" + (steps * 1000.0) / ms);

    }

    public Benchmarks() {

    }


    public Mat retrieveMat() {
        VideoCapture camera = new VideoCapture();
        Mat image = null;

        if (!camera.isOpened()) {
            System.out.println("No Camera found...=>Error");
        } else {
            image = new Mat();

            if (camera.read(image)) {
                if (!image.empty()) {
                    camera.release();
                    return image;
                }
            }
        }
        camera.release();
        return null;
    }
}
