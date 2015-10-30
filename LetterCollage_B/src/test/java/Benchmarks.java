package test.java;

import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import main.java.utils.OpenCVUtils;

public class Benchmarks {
	
	public static void main(String[] args){
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
        System.out.println(ms+" ms for " + steps + " steps!");
        System.out.println("Test1 FPS:" + (steps*1000.0)/ms);
        
	}
	
	public Benchmarks(){
		
	}
	
	
	public Mat retrieveMat(){
		VideoCapture camera = new VideoCapture(0);
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
