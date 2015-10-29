package main.java;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class VideoCap extends JFrame {
	
	private static final String haarcascades_frontalface = "haarcascades/haarcascade_frontalface_default.xml";
	private static final String haarcascades_eye = "haarcascades/haarcascade_eye.xml";
	boolean faceDetectionOn = false;
	boolean eyeDetectionOn = true;

	private static final long serialVersionUID = -1421396949037115883L;

	public static void main(String args[]) {
		if(OpenCVUtils.loadLibrary(Core.NATIVE_LIBRARY_NAME)){
			VideoCap vc = new VideoCap();
			vc.openCVVidoReader(new MainFrame());
		}
	}

	public void openCVVidoReader(MainFrame mf) {
		VideoCapture camera = new VideoCapture(0);
		MatOfByte mof = new MatOfByte();
		String path = System.getProperty("user.home") + "/Desktop" + "/camera.jpg";
		Mat image = null;
		BufferedImage bufImage = null;

		if (!camera.isOpened()) {
			System.out.println("No Camera found...=>Error");
		} else {
			image = new Mat();
			
			
			int steps = 100;
			long startTime = System.currentTimeMillis();
			int i = 0;
			while (i++<steps) {
				if (camera.read(image)) {
					if (!image.empty()) {
						System.out.println("Captured Frame Width " + image.width() + " Height " + image.height());

						// Detect faces in the image.
						// MatOfRect is a special container class for Rect.
						if (faceDetectionOn) {
							String haarcascades_frontalfacePath = OSUtils.preparePathForOS(this.getClass().getClassLoader().getResource(haarcascades_frontalface).getPath());
							CascadeClassifier faceDetector = new CascadeClassifier(haarcascades_frontalfacePath);
							System.out.println(haarcascades_frontalfacePath);
							//CascadeClassifier eyeDetector = new CascadeClassifier(haarcascades_eye);
							if (!faceDetector.empty()) {
								MatOfRect faceDetections = new MatOfRect();
								faceDetector.detectMultiScale(image, faceDetections);
								System.out.println(String.format("Detected %s faces!", faceDetections.toArray().length));
								
								// Draw a bounding box around each face.
								for (Rect rect : faceDetections.toArray()) {
									Imgproc.rectangle(image, new Point(rect.x, rect.y),
											new Point(rect.x + rect.width, rect.y + rect.height),
											new Scalar(0, 255, 0));
								}
							} else {
								System.out.println("Facedetector is empty...cannot detect faces!");
							}
						}
						mf.getBuffView().setBufferedImage(OpenCVUtils.matToBufferedImage(image));
					
						Imgcodecs.imwrite(path, image);
						// Imgcodecs.imwrite(path, mof);
						// Highgui.imwrite("camera.jpg", frame);
						// mof.copyTo(frame);
					}
				}
			}

			long ms = (System.currentTimeMillis() - startTime);
	        System.out.println(ms+" ms for " + steps + " steps!");
	        System.out.println("Real FPS:" + (steps*1000.0)/ms);
		}
		camera.release();
	}
}